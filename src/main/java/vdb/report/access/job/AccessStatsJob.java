package vdb.report.access.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.report.access.vo.DailyStats;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.util.DateUtil;
import vdb.report.util.IPSeeker;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/**
 * 数据访问统计定时任务
 * 
 * @author 苏贤明
 */

public class AccessStatsJob implements Job
{
	/** 定时任务的执行方法 */
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		execute();
	}

	public void execute()
	{
		Logger logger = Logger.getLogger("vdb.report");
		logger.debug("Data Access Statistics Job begin... ");

		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			try
			{
				executeStatistic(ds);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		logger.debug("Data Access Statistics Job end... ");
	}

	/**
	 * 用于VDB后台的立即抽取功能
	 * 
	 * @param dsuri
	 *            数据集URI
	 * @throws Exception
	 */
	public void executeStatistic(String dsuri) throws Exception
	{
		DataSet ds = VdbManager.getEngine().getCatalog().fromUri(dsuri);
		executeStatistic(ds);
	}

	/**
	 * 执行统计
	 * 
	 * @param ds
	 *            数据集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void executeStatistic(DataSet ds) throws Exception
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		JdbcSource logJdbcSource = JdbcSourceManager.getInstance()
				.getLogsJdbcSource(ds);
		JdbcSource reportJdbcSource = JdbcSourceManager.getInstance()
				.getReportsJdbcSource(ds);

		final Calendar byWhichDay = Calendar.getInstance();// 得到当前日历
		byWhichDay.clear();

		Calendar flag = Calendar.getInstance();
		flag.clear();

		reportJdbcSource
				.executeQuery(
						new StringSql(
								"SELECT DISTINCT OP_DATE FROM  DAILYSTATS order by OP_DATE DESC"),1,1,
						new ResultSetHandler()
						{
							public void afterQuery(ResultSet rs)
									throws SQLException
							{
								if (rs.next())
								{
									byWhichDay.setTime(rs.getDate(1));
									//byWhichDay.add(Calendar.DAY_OF_MONTH, 1);//如果今天执行多次立即抽取
								}
							}
						});

		if (byWhichDay.getTime().compareTo(flag.getTime()) == 0)
		{
			// 给byWhichDay赋值为LOGRECORDS里面的最早记录时间
			logJdbcSource
					.executeQuery(
							new StringSql(
									"SELECT DISTINCT OP_TIME FROM LOGRECORDS order by OP_TIME ASC"),1,1,
							new ResultSetHandler()
							{
								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									if (rs.next())
									{
										byWhichDay.setTime(rs.getDate(1));
									}
								}
							});
		}
		
		//如果LOGRECORDS和DAILYSTATS表中都没有数据，则程序不再往下执行
		if (byWhichDay.getTime().compareTo(flag.getTime()) == 0){
			return ;
		}

		// 每天00：10分开始执行定时任务，这里构造一个日历对象。
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.SECOND, 59);
		today.set(Calendar.MINUTE, 59);
		
		//today.add(Calendar.DAY_OF_MONTH, -1);//时间为前一天的23:59:59
		while (byWhichDay.before(today))
		{
			final Date curday = byWhichDay.getTime();
			Date dateStart = DateUtil.getDateStart(curday);
			Date dateEnd = DateUtil.getDateEnd(curday);
			// 如果当天已经执行过统计，则删除统计记录
			String sql = "delete from DAILYSTATS where OP_DATE>='"
					+ df.format(dateStart) + "' AND OP_DATE<='"
					+ df.format(dateEnd) + "'";
			reportJdbcSource.executeUpdate(new StringSql(sql));

			// 插入新的日常访问统计
			final List<DailyStats> dailyRecords = new ArrayList<DailyStats>();
			StringSql stringSql = getDailyStatsSQL(curday);

			logJdbcSource.executeQuery(stringSql, new ResultSetHandler()
			{
				public void afterQuery(ResultSet rs) throws SQLException
				{
					while (rs.next())
					{
						DailyStats record = new DailyStats();
						record.setUsername(rs.getString("user_name"));
						record.setIp(rs.getString("ip"));
						record.setDate(curday);
						record.setCounts(rs.getInt(3));
						record.setDateset(rs.getString("dataset"));
						record.setEntity(rs.getString("entity"));
						record.setOperation(rs.getString("operation"));
						String area = IPSeeker.getInstance().getArea(
								record.getIp());
						if (!area.equals("CZ88.NET"))
							record.setArea(area);
						else
							record.setArea("未知");
						record.setCountry(IPSeeker.getInstance().getCountry(
								record.getIp()));
						dailyRecords.add(record);
					}
				}
			});
			for (DailyStats record : dailyRecords)
			{
				Map<String, Object> map = new LinkedHashMap();
				map.put("USER_NAME", record.getUsername());
				map.put("IP", record.getIp());
				map.put("OP_DATE", record.getDate());
				map.put("OPERATION", record.getOperation());
				map.put("OP_COUNTS", record.getCounts());
				map.put("DATASET", record.getDateset());
				map.put("ENTITY", record.getEntity());
				map.put("COUNTRY", record.getCountry());
				map.put("AREA", record.getArea());
				reportJdbcSource.insertRecord("DAILYSTATS", map);
			}

			/**
			 * 插入新的关键词统计
			 */
			StringSql keywordSql = getKeyWordStatsSQL(curday);
			final List<KeyWordsStats> keywordRecords = new ArrayList<KeyWordsStats>();
			logJdbcSource.executeQuery(keywordSql, new ResultSetHandler()
			{
				public void afterQuery(ResultSet rs) throws SQLException
				{
					while (rs.next())
					{
						KeyWordsStats record = new KeyWordsStats();
						record.setKeyword(rs.getString("PARAM2"));
						record.setCounts(rs.getInt(2));
						record.setDataset(rs.getString(3));
						record.setEntity(rs.getString(4));
						record.setByDate(curday);
						keywordRecords.add(record);
					}
				}
			});

			for (KeyWordsStats record : keywordRecords)
			{
				keywordSql = new StringSql(
						"select * from key_words_stats where ENTITY=? AND KEY_WORD=?");
				keywordSql.setParameter(1, record.getEntity());
				keywordSql.setParameter(2, record.getKeyword());
				final KeyWordsStats existed = new KeyWordsStats();
				reportJdbcSource.executeQuery(new StringSql(keywordSql),
						new ResultSetHandler()
						{
							public void afterQuery(ResultSet rs)
									throws SQLException
							{
								if (rs.next())
								{
									existed.setByDate(rs.getDate("BY_DATE"));
								}
							}
						});

				if (existed.getByDate() != null)
				{
					StringSql updateSql = new StringSql(
							"update key_words_stats "
									+ "set OP_COUNTS=OP_COUNTS+?,BY_DATE=? where ENTITY=? AND KEY_WORD=?");
					updateSql.setParameter(1, record.getCounts());
					updateSql.setParameter(2, record.getByDate());
					updateSql.setParameter(3, record.getEntity());
					updateSql.setParameter(4, record.getKeyword());
					reportJdbcSource.executeUpdate(updateSql);
				}

				else if (existed.getByDate() == null)
				{
					Map<String, Object> map = new LinkedHashMap();
					map.put("KEY_WORD", record.getKeyword());
					map.put("DATASET", record.getDataset());
					map.put("ENTITY", record.getEntity());
					map.put("OP_COUNTS", record.getCounts());
					map.put("BY_DATE", record.getByDate());
					reportJdbcSource.insertRecord("key_words_stats", map);
				}
			}

			byWhichDay.add(Calendar.DAY_OF_MONTH, 1);
		}

		// 删除一个月以前的所有记录
		// today.add(Calendar.DAY_OF_MONTH, -30);
		// 如果日志记录已经统计过，而且已经写入DailyStats表和key_words_stats表，则从LOGRECORDS中删除相应的记录
		// logJdbcSource.executeUpdate(new StringSql("delete from logrecords
		// where OP_TIME<='" + df.format(today.getTime()) + "'"));
	}

	/**
	 * 获得指定日期访问统计的SQL语句
	 * 
	 * @param today
	 *            日期
	 * @return
	 */
	private StringSql getDailyStatsSQL(Date today)
	{
		// today的时间为昨天的23：59：59，则DateUtil.getDateStart(today))为昨天的00：00：00,DateUtil.getDateEnd(today))为昨天的23：59：59
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringSql dailyLogSQL = new StringSql(
				"select user_name,ip,count(id),dataset,entity,operation from logrecords where OP_TIME>=? AND OP_TIME<=? group by operation,user_name,ip,dataset,entity");
		dailyLogSQL.setParameter(1, df.format(DateUtil.getDateStart(today)));
		dailyLogSQL.setParameter(2, df.format(DateUtil.getDateEnd(today)));
		return dailyLogSQL;
	}

	/**
	 * 获得指定日期关键词统计的SQL语句
	 * 
	 * @param today
	 *            日期
	 * @return
	 */
	private StringSql getKeyWordStatsSQL(Date today)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringSql keyWordSQL = new StringSql(
				"select PARAM2,COUNT(*) OP_COUNTS,"
						+ "DATASET,ENTITY from logrecords where OP_TIME>=? AND OP_TIME<=? AND OPERATION=?"
						+ " AND  PARAM2!='' group by PARAM2,DATASET,ENTITY");
		keyWordSQL.setParameter(1, df.format(DateUtil.getDateStart(today)));
		keyWordSQL.setParameter(2, df.format(DateUtil.getDateEnd(today)));
		keyWordSQL.setParameter(3, "query");
		return keyWordSQL;
	}
}