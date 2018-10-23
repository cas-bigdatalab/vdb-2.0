package vdb.report.resstat.filestats.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.filestat.impl.RepositoryManager;
import vdb.mydb.filestat.tool.FilesTool;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.repo.FileRepository;
import vdb.report.resstat.filestats.vo.FileIndicator;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;
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
public class FileStatsJob implements Job
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
	public void accumulateFileIndicator(String dsuri)
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

		// 如果定时任务还在执行，则退出
		if (!flag)
		{
			logger.debug("Job is running,please waiting...");
			return;
		}

		// 开始执行定时任务，将变量置为false
		flag = false;

		// 得到当前时间
		String date = DateUtil.getTodayAsString();
		List<RepositoryIndicator> riList = new ArrayList<RepositoryIndicator>();
		RepositoryIndicator ri;
		Map<String, FileIndicator> map;

		FileStatsUtil filesStatsTool = new FileStatsUtil();
		FilesTool tool = new FilesTool();
		RepositoryManager rm = tool.getRepositoryManager(ds);
		if (rm != null)
		{
			for (FileRepository repository : rm.getRepositories())
			{
				ri = new RepositoryIndicator();
				ri.setDsid(ds.getId());
				ri.setDatasetUri(ds.getUri());
				ri.setRepositoryName(repository.getName());
				ri.setLastStatsTime(date);
				// 设置初始的最大文件大小和最小文件大小均为0
				ri.setMaxFileBytes(0);
				ri.setMinFileBytes(0);
				ri.setDirectoryNum(0);
				ri.setMaxDirDepth(0);

				map = new HashMap<String, FileIndicator>();
				ri.setFileIndicatorMap(map);
				filesStatsTool.doFileStats(ds, repository, ri);

				// 将其添加到list列表中
				riList.add(ri);
			}
		}

		// 删除表中当天的统计记录
		deleteStats(date, ds);

		for (RepositoryIndicator indicator : riList)
		{
			// 将文件统计指标写入数据库中
			insertDataIntoDb(indicator, ds);
		}

		// 定时任务执行完毕，将变量置为true
		flag = true;
	}

	/**
	 * 将定时任务统计得到的指标数据插入到数据库中
	 * 
	 * @param ri
	 *            文件统计指标
	 * @param ds
	 *            数据集
	 */
	@SuppressWarnings("unchecked")
	private void insertDataIntoDb(RepositoryIndicator ri, DataSet ds)
	{
		try
		{
			JdbcSource jt = (JdbcSource) JdbcSourceManager.getInstance()
					.getReportsJdbcSource(ds);
			String sql = "insert into VDB_DQ_REPOSITORY (DSID,DSURI,REPOSITORYNAME,DIRECTORYNUM,"
					+ "MAXFILEBYTES,MINFILEBYTES,MAXDIRDEPTH,LASTSTATSTIME) values (?,?,?,?,?,?,?,?)";
			jt.executeUpdate(new StringSql(sql, ri.getDsid(), ri
					.getDatasetUri(), ri.getRepositoryName(), ri
					.getDirectoryNum(), ri.getMaxFileBytes(), ri
					.getMinFileBytes(), ri.getMaxDirDepth(), ri
					.getLastStatsTime()));

			Integer rid;
			sql = "select max(RID) from VDB_DQ_REPOSITORY";
			rid = jt.queryForObject(new StringSql(sql),
					new ResultSetReader<Integer>()
					{
						public Integer read(ResultSet rs, int row)
								throws SQLException
						{
							return rs.getInt(1);
						}

					});

			Collection<FileIndicator> collection = ri.getFileIndicatorMap()
					.values();
			if (collection != null && collection.size() != 0)
			{
				List<FileIndicator> fileIndicatorList = new ArrayList<FileIndicator>(
						collection);
				for (FileIndicator fi : fileIndicatorList)
				{
					sql = "insert into VDB_DQ_FILE (RID,CONTENTTYPE,FILEEXTENSION,BYTES,ITEMS,LASTSTATSTIME) "
							+ "values (?,?,?,?,?,?)";
					jt.executeUpdate(new StringSql(sql, rid.intValue(), fi
							.getContentType(), fi.getFileExtension(), fi
							.getBytes(), fi.getItems(), ri.getLastStatsTime()));
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
					"DELETE FROM VDB_DQ_REPOSITORY where LASTSTATSTIME='"
							+ date + "'"));
			jt.executeUpdate(new StringSql(
					"DELETE FROM VDB_DQ_FILE where LASTSTATSTIME='" + date
							+ "'"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
