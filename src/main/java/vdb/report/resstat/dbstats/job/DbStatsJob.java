package vdb.report.resstat.dbstats.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Relation;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.metacat.VdbDataSet;
import vdb.report.resstat.dbstats.cloud.DataSetCloud;
import vdb.report.resstat.dbstats.strategy.StatisticStrategy;
import vdb.report.resstat.dbstats.strategy.StatisticStrategyManager;
import vdb.report.resstat.dbstats.strategy.impl.StupidStatisticStrategy;
import vdb.report.resstat.dbstats.vo.DbIndicator;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.resstat.dbstats.vo.FieldIndicator;
import vdb.report.resstat.util.DateUtil;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/**
 * 数据质量统计分析的定时任务
 * 
 * @author 苏贤明
 * 
 */
public class DbStatsJob implements Job
{
	private static boolean flag = true;

	private Logger logger = Logger.getLogger(this.getClass());

	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		execute();
	}

	public void execute()
	{
		logger.debug("Database Statistics Job begin... ");
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			execute(ds.getUri());
		}
		logger.debug("Database Statistics Job end... ");
	}

	/**
	 * 用于VDB后台的立即抽取功能
	 * 
	 * @param dsuri
	 *            数据集URI
	 */
	public void accumulateDbIndicator(String dsuri)
	{
		execute(dsuri);
	}

	private void execute(String uri)
	{
		DataSet ds = VdbManager.getEngine().getCatalog().fromUri(uri);
		if (ds == null)
		{
			logger.error("Dataset is null......");
			return;
		}

		VdbDataSet vdbDs = (VdbDataSet) ds;
		if (vdbDs.getJdbcSource() == null)
		{
			logger.error("Database Connection Error......");
			return;
		}

		// 如果定时任务还在执行，则退出
		if (!flag)
		{
			logger.debug("Job is running,please waiting...");
			return;
		}

		// 开始执行定时任务，将变量置为false
		flag = false;

		Entity[] entities = ds.getEntities();
		Relation[] relationes = ds.getRelations();
		// 得到当前时间
		String date = DateUtil.getTodayAsString();

		DbIndicator dbIndicator = new DbIndicator();
		dbIndicator.setLastStatsTime(date);
		dbIndicator.setDsid(ds.getId());
		dbIndicator.setEntityNum(entities.length);
		dbIndicator
				.setRelationNum((relationes == null) ? 0 : relationes.length);
		if (dbIndicator.getEntityNum() > 1)
			dbIndicator
					.setRelationRate((dbIndicator.getRelationNum() == 0) ? 0
							: 2
									* dbIndicator.getRelationNum()
									/ (double) (dbIndicator.getEntityNum() * (dbIndicator
											.getEntityNum() - 1)));
		else
			dbIndicator.setRelationRate(0);

		// 初始字段个数和文本字段个数都为0
		dbIndicator.setFieldNum(0);
		dbIndicator.setTextFieldNum(0);
		dbIndicator.setBytes(0);
		dbIndicator.setRecordNum(0);

		// 得到统计策略
		StatisticStrategyManager ssm = VdbManager.getApplicationContext()
				.getStatisticStrategyManager();
		StatisticStrategy ss = null;
		if (ssm != null)
		{
			ss = ssm.getStatisticStrategy();
		}
		if (ss == null)
		{
			ss = new StupidStatisticStrategy();
		}

		// 数据集所有字段大小（记录数*字段数）和空字段大小
		long allFieldsSize = 0;
		long numberOfNullData = 0;

		List<EntityIndicator> entityIndicatorList = new ArrayList<EntityIndicator>();
		EntityIndicator entityIndicator;
		int numberOfFields = 0;

		for (Entity entity : ds.getEntities())
		{
			if (entity == null)
				continue;

			entityIndicator = ss.getEntityIndicator(entity, date);

			// 如果返回的实体指标为空，则继续
			if (entityIndicator == null)
			{
				continue;
			}

			// 计算字段与文本字段个数
			numberOfFields = entity.getFields().length;
			dbIndicator.setFieldNum(dbIndicator.getFieldNum() + numberOfFields);
			for (int i = 0; i < numberOfFields; i++)
			{
				Field vf = entity.getFields()[i];
				if (vf.getType().getName().equals("String"))
				{
					dbIndicator
							.setTextFieldNum(dbIndicator.getTextFieldNum() + 1);
				}
			}

			// 累加记录数与数据量
			long recordNum = entityIndicator.getRecordNum();
			dbIndicator.setBytes(dbIndicator.getBytes()
					+ entityIndicator.getBytes());
			dbIndicator.setRecordNum(dbIndicator.getRecordNum() + recordNum);

			long entityCellSize = recordNum * numberOfFields;
			allFieldsSize += entityCellSize;
			numberOfNullData = (long) (entityCellSize * entityIndicator
					.getIntegrityRate());
			entityIndicatorList.add(entityIndicator);
		}

		dbIndicator.setEntityIndicatorList(entityIndicatorList);
		dbIndicator
				.setIntegrityRate((allFieldsSize == 0) ? 0
						: ((allFieldsSize - numberOfNullData) / (double) allFieldsSize));

		// 删除表中当天的统计记录
		deleteStats(date, ds);

		// 将数据集统计指标、实体统计指标、字段统计指标写入数据库中
		insertDataIntoDb(dbIndicator, ds);

		if (ds != null && dbIndicator != null)
		{
			DataSetCloud dsc = new DataSetCloud(ds, dbIndicator);
			dsc.generateCloud();
		}

		// 定时任务执行完毕，将变量置为true
		flag = true;
	}

	/**
	 * 将定时任务统计得到的指标数据插入到数据库中
	 * 
	 * @param dbIndicator
	 *            数据集统计指标信息
	 * @param ds
	 *            数据集
	 */
	@SuppressWarnings("unchecked")
	private void insertDataIntoDb(DbIndicator dbIndicator, DataSet ds)
	{
		if (dbIndicator == null)
			return;

		try
		{
			JdbcSource jt = (JdbcSource) JdbcSourceManager.getInstance()
					.getReportsJdbcSource(ds);
			String sql = "insert into VDB_DQ_DATASET (DSID,BYTES,RECORDNUM,ENTITYNUM,RELATIONNUM,"
					+ "RELATIONRATE,FIELDNUM,TEXTFIELDNUM,INTEGRITYRATE,LASTSTATSTIME) values (?,?,?,?,?,?,?,?,?,?)";
			jt
					.executeUpdate(new StringSql(sql, dbIndicator.getDsid(),
							dbIndicator.getBytes(), dbIndicator.getRecordNum(),
							dbIndicator.getEntityNum(), dbIndicator
									.getRelationNum(), dbIndicator
									.getRelationRate(), dbIndicator
									.getFieldNum(), dbIndicator
									.getTextFieldNum(), dbIndicator
									.getIntegrityRate(), dbIndicator
									.getLastStatsTime()));

			Integer dqid, dtid;
			sql = "select max(DQID) from VDB_DQ_DATASET";
			dqid = jt.queryForObject(new StringSql(sql),
					new ResultSetReader<Integer>()
					{

						public Integer read(ResultSet rs, int row)
								throws SQLException
						{
							return rs.getInt(1);
						}

					});

			for (EntityIndicator ei : dbIndicator.getEntityIndicatorList())
			{
				if (ei == null)
					continue;

				sql = "insert into VDB_DQ_TABLE (DQID,ENTITYID,ENTITYURI,TITLE,TABLENAME,BYTES,RECORDNUM,INTEGRITYRATE,LASTSTATSTIME) "
						+ "values (?,?,?,?,?,?,?,?,?)";
				jt.executeUpdate(new StringSql(sql, dqid.intValue(),
						ei.getId(), ei.getUri(), ei.getTitle(), ei
								.getTableName(), ei.getBytes(), ei
								.getRecordNum(), ei.getIntegrityRate(), ei
								.getLastStatsTime()));

				sql = "select max(DTID) from VDB_DQ_TABLE";
				dtid = jt.queryForObject(new StringSql(sql),
						new ResultSetReader<Integer>()
						{

							public Integer read(ResultSet rs, int row)
									throws SQLException
							{
								return rs.getInt(1);
							}

						});

				for (FieldIndicator fi : ei.getFieldIndicatorList())
				{
					sql = "insert into VDB_DQ_FIELD (DTID,FIELDID,NAME,TITLE,INTEGRITYRATE,LASTSTATSTIME) "
							+ "values (?,?,?,?,?,?)";
					jt.executeUpdate(new StringSql(sql, dtid.intValue(), fi
							.getId(), fi.getName(), fi.getTitle(), fi
							.getIntegrityRate(), fi.getLastStatsTime()));
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定日期的统计记录
	 * 
	 * @param date
	 */
	private void deleteStats(String date, DataSet ds)
	{
		try
		{
			JdbcSource jt = (JdbcSource) JdbcSourceManager.getInstance()
					.getReportsJdbcSource(ds);
			jt.executeUpdate(new StringSql(
					"DELETE FROM VDB_DQ_DATASET where LASTSTATSTIME='" + date
							+ "'"));
			jt.executeUpdate(new StringSql(
					"DELETE FROM VDB_DQ_TABLE where LASTSTATSTIME='" + date
							+ "'"));
			jt.executeUpdate(new StringSql(
					"DELETE FROM VDB_DQ_FIELD where LASTSTATSTIME='" + date
							+ "'"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
