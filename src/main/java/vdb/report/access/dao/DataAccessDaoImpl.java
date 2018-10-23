package vdb.report.access.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.DailyRecord;
import vdb.report.access.vo.EntityAccessRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.access.vo.OperationAccessRecord;
import vdb.report.util.DateUtil;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DataAccessDaoImpl implements DataAccessDao
{

	private JdbcSource _jdbcsource;

	private JdbcSource _logJdbcSource;

	private String _datasetId;

	public DataAccessDaoImpl(String dsuri)
	{
		DataSet ds = (DataSet) VdbManager.getEngine().getCatalog().fromUri(
				dsuri);
		try
		{
			_datasetId = ds.getId();
			_jdbcsource = JdbcSourceManager.getInstance().getReportsJdbcSource(
					ds);
			_logJdbcSource = JdbcSourceManager.getInstance().getLogsJdbcSource(
					ds);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获得指定实体的关键词统计指标（前50条）
	 * 
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<KeyWordsStats> getKeywordStatsOfEntity(String entityId)
	{
		try
		{
			final List<KeyWordsStats> list = new ArrayList<KeyWordsStats>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"SELECT DISTINCT KEY_WORD,ENTITY,OP_COUNTS FROM KEY_WORDS_STATS WHERE ENTITY='"
											+ entityId
											+ "' order by OP_COUNTS DESC"),1,50,
							new ResultSetHandler()
							{
								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									while (rs.next())
									{
										KeyWordsStats record = new KeyWordsStats();
										record.setKeyword(rs.getString(1));
										record.setCounts(rs.getInt(3));
										Entity vdbEntity = (Entity) VdbManager
												.getEngine().getCatalog()
												.fromId(rs.getString(2));
										record.setEntity(vdbEntity.getTitle());
										list.add(record);
									}
								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得指定实体的操作类型统计
	 * 
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<OperationAccessRecord> getOperationStatsOfEntity(String entityId)
	{
		try
		{
			final List<OperationAccessRecord> list = new ArrayList<OperationAccessRecord>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"select sum(OP_COUNTS),operation from dailystats where ENTITY='"
											+ entityId
											+ "' group by operation order by sum(OP_COUNTS) DESC"),
							new ResultSetHandler()
							{

								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									while (rs.next())
									{
										OperationAccessRecord record = new OperationAccessRecord();
										record.setCount(rs.getInt(1));
										record.setOperation(rs.getString(2));
										list.add(record);
									}
								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得指定实体的IP统计指标
	 * 
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<IPRecord> getIPStatsOfEntity(String entityId)
	{
		try
		{
			final List<IPRecord> list = new ArrayList<IPRecord>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"SELECT DISTINCT IP,AREA,sum(OP_COUNTS) FROM dailystats WHERE ENTITY='"
											+ entityId
											+ "' group by IP,AREA order by sum(OP_COUNTS) DESC"),1,50,
							new ResultSetHandler()
							{
								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									while (rs.next())
									{
										list.add(new IPRecord(rs.getString(1),
												rs.getString(2), rs.getInt(3)));
									}
								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集的关键词统计指标
	 * 
	 * @return
	 */
	public List<KeyWordsStats> getKeywordStats()
	{
		try
		{
			final List<KeyWordsStats> list = new ArrayList<KeyWordsStats>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"SELECT DISTINCT KEY_WORD,ENTITY,OP_COUNTS FROM KEY_WORDS_STATS WHERE DATASET ='"
											+ _datasetId
											+ "' order by OP_COUNTS DESC"),1,50,
							new ResultSetHandler()
							{
								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									while (rs.next())
									{
										KeyWordsStats record = new KeyWordsStats();
										record.setKeyword(rs.getString(1));
										record.setCounts(rs.getInt(3));
										Entity vdbEntity = (Entity) VdbManager
												.getEngine().getCatalog()
												.fromId(rs.getString(2));
										record.setEntity(vdbEntity.getTitle());
										list.add(record);
									}

								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集的操作统计指标
	 * 
	 * @return
	 */
	public List<OperationAccessRecord> getOperationStats()
	{
		try
		{
			final List<OperationAccessRecord> list = new ArrayList<OperationAccessRecord>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"select sum(OP_COUNTS),operation from dailystats where DATASET='"
											+ _datasetId
											+ "' group by operation order by sum(OP_COUNTS) DESC"),
							new ResultSetHandler()
							{

								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									while (rs.next())
									{
										OperationAccessRecord record = new OperationAccessRecord();
										record.setCount(rs.getInt(1));
										record.setOperation(rs.getString(2));
										list.add(record);
									}
								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集的IP统计指标
	 * 
	 * @return
	 */
	public List<IPRecord> getIPStats()
	{
		try
		{
			final List<IPRecord> list = new ArrayList<IPRecord>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"SELECT DISTINCT IP,AREA,sum(OP_COUNTS) FROM dailystats where DATASET ='"
											+ _datasetId
											+ "' group by IP,AREA order by sum(OP_COUNTS) DESC"),1,50,
							new ResultSetHandler()
							{

								public void afterQuery(ResultSet rs)
										throws SQLException
								{

									while (rs.next())
									{
										list.add(new IPRecord(rs.getString(1),
												rs.getString(2), rs.getInt(3)));
									}
								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集内实体被访统计指标的情况
	 * 
	 * @return
	 */
	public List<EntityAccessRecord> getEntityCounts()
	{
		try
		{
			final List<EntityAccessRecord> list = new ArrayList<EntityAccessRecord>();
			_jdbcsource
					.executeQuery(
							new StringSql(
									"select  entity,sum(OP_COUNTS),max(OP_DATE) from dailystats where DATASET ='"
											+ _datasetId
											+ "' group by entity order by sum(OP_COUNTS) desc"),
							new ResultSetHandler()
							{

								public void afterQuery(ResultSet rs)
										throws SQLException
								{
									while (rs.next())
									{
										EntityAccessRecord record = new EntityAccessRecord();
										record.setId(rs.getString(1));
										record.setCounts(rs.getInt(2));
										record.setDate(rs.getString(3));
										Entity vdbEntity = (Entity) VdbManager
												.getEngine().getCatalog()
												.fromId(record.getId());
										if (vdbEntity == null)
										{
											record.setTitle("错误的ID或实体被删除");
										}
										else
										{
											record.setTitle(vdbEntity
													.getTitle());
										}
										list.add(record);
									}
								}
							});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集指定操作发生的区域统计指标（即统计指定操作出现在各个区域的次数）
	 * 
	 * @param operation
	 *            指定操作[共五种操作：增加、删除、修改、查看、查询]
	 * @return
	 */
	public List<AreaCountRecord> getOperationSpecArea(String operation)
	{
		String sql = "";
		if (operation.equals("all"))
		{
			sql = "select AREA,sum(OP_COUNTS) from DAILYSTATS where DATASET='"
					+ _datasetId
					+ "' group by AREA order by sum(OP_COUNTS) DESC";
		}
		else if (operation.equals("show"))
		{
			sql = "select AREA,sum(OP_COUNTS) from DAILYSTATS where DATASET='"
					+ _datasetId
					+ "' AND OPERATION!='update' AND OPERATION!='insert' AND OPERATION!='delete' AND OPERATION!='query' group by AREA order by sum(OP_COUNTS) DESC";
		}
		else
		{
			sql = "select AREA,sum(OP_COUNTS) from DAILYSTATS where DATASET='"
					+ _datasetId + "' AND OPERATION='" + operation
					+ "'group by AREA order by sum(OP_COUNTS) DESC";
		}
		final String jdbcSql = sql;
		try
		{
			final List<AreaCountRecord> list = new ArrayList<AreaCountRecord>();
			_jdbcsource.executeQuery(new StringSql(jdbcSql),
					new ResultSetHandler()
					{
						public void afterQuery(ResultSet rs)
								throws SQLException
						{
							int i = 0;
							while (rs.next() && i < 20)
							{
								list.add(new AreaCountRecord(rs.getString(1),
										rs.getInt(2)));
								i++;
							}
							if (i == 20)
							{
								AreaCountRecord other = new AreaCountRecord(
										"其他地区", 0);
								while (rs.next())
								{
									other.setCount(other.getCount()
											+ rs.getInt(2));
								}
								list.add(other);
							}
						}
					});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集指定实体指定操作发生的区域统计指标（即统计指定实体指定操作出现在各个区域的次数）
	 * 
	 * @param operation
	 *            操作类型[共五种操作：增加、删除、修改、查看、查询]
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<AreaCountRecord> getOperationSpecArea(String operation,
			String entityId)
	{
		String sql = "";
		if (operation.equals("all"))
		{
			sql = "select AREA,sum(OP_COUNTS) from DAILYSTATS where DATASET='"
					+ _datasetId + "' AND ENTITY='" + entityId
					+ "' group by AREA order by sum(OP_COUNTS) DESC";
		}
		else if (operation.equals("show"))
		{
			sql = "select AREA,sum(OP_COUNTS) from DAILYSTATS where DATASET='"
					+ _datasetId
					+ "' AND OPERATION!='update'  AND OPERATION!='insert' AND OPERATION!='query' AND OPERATION!='delete' AND ENTITY='"
					+ entityId + "' group by AREA order by sum(OP_COUNTS) DESC";
		}
		else
		{
			sql = "select AREA,sum(OP_COUNTS) from DAILYSTATS where DATASET='"
					+ _datasetId + "' AND OPERATION='" + operation
					+ "'AND ENTITY='" + entityId
					+ "' group by AREA order by sum(OP_COUNTS) DESC";
		}
		final String jdbcSql = sql;
		try
		{
			final List<AreaCountRecord> list = new ArrayList<AreaCountRecord>();
			_jdbcsource.executeQuery(new StringSql(jdbcSql),
					new ResultSetHandler()
					{
						public void afterQuery(ResultSet rs)
								throws SQLException
						{
							int i = 0;
							while (rs.next() && i < 20)
							{
								list.add(new AreaCountRecord(rs.getString(1),
										rs.getInt(2)));
								i++;
							}
							if (i == 20)
							{
								AreaCountRecord other = new AreaCountRecord(
										"其他地区", 0);
								while (rs.next())
								{
									other.setCount(other.getCount()
											+ rs.getInt(2));
								}
								list.add(other);
							}
						}
					});
			return list;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集指定操作在指定时间范围内发生的次数
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param operation
	 *            操作类型[共五种操作：增加、删除、修改、查看、查询]
	 * @return
	 */
	public List<DailyRecord> getOperationSpecPeriod(String startDate,
			String endDate, String operation)
	{
		try
		{
			String sql = "";
			if (operation.equals("all"))
			{
				sql = "select OP_DATE,sum(OP_COUNTS) from dailystats where DATASET ='"
						+ _datasetId
						+ "' AND OP_DATE>='"
						+ startDate
						+ "' AND OP_DATE<='"
						+ endDate
						+ "' group by OP_DATE order by sum(OP_COUNTS) ASC";
			}
			else if (operation.equals("show"))
			{
				sql = "select OP_DATE,sum(OP_COUNTS) from dailystats where DATASET ='"
						+ _datasetId
						+ "' AND OP_DATE>='"
						+ startDate
						+ "' AND OP_DATE<='"
						+ endDate
						+ "' AND OPERATION!='update'  AND OPERATION!='insert' AND OPERATION!='query' AND OPERATION!='delete' group by OP_DATE order by sum(OP_COUNTS) ASC";
			}
			else
			{
				sql = "select OP_DATE,sum(OP_COUNTS) from dailystats where DATASET ='"
						+ _datasetId
						+ "' AND OP_DATE>='"
						+ startDate
						+ "' AND OP_DATE<='"
						+ endDate
						+ "' AND OPERATION='"
						+ operation
						+ "'group by OP_DATE order by sum(OP_COUNTS) ASC";
			}
			final String jdbcSql = sql;
			// 从表中获得相应的统计记录
			final List<DailyRecord> list = new ArrayList<DailyRecord>();
			_jdbcsource.executeQuery(new StringSql(jdbcSql),
					new ResultSetHandler()
					{
						public void afterQuery(ResultSet rs)
								throws SQLException
						{
							while (rs.next())
							{
								list.add(new DailyRecord(rs.getString(1)
										.substring(0, 10), rs.getInt(2)));
							}
						}
					});
			List<DailyRecord> rlist = new ArrayList<DailyRecord>();
			Calendar startCalendar = DateUtil.getDateAsCalendar(startDate);
			Calendar endCalendar = DateUtil.getDateAsCalendar(endDate);
			// 先给每天的访问次数赋0值
			while (startCalendar.before(endCalendar))
			{
				rlist.add(new DailyRecord(DateUtil
						.getDateAsString(startCalendar), 0));
				startCalendar.add(Calendar.DAY_OF_MONTH, +1);
			}
			rlist.add(new DailyRecord(DateUtil.getDateAsString(startCalendar),
					0));
			Iterator<DailyRecord> it = list.iterator();
			while (it.hasNext())
			{
				DailyRecord record = it.next();
				Iterator<DailyRecord> rit = rlist.iterator();
				while (rit.hasNext())
				{
					DailyRecord rRecord = rit.next();
					if (rRecord.getDate().equals(record.getDate()))
					{
						rRecord.setCount(record.getCount());
					}
				}
			}
			return rlist;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集指定实体指定操作在指定时间范围内发生的次数
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param entityId
	 *            实体ID
	 * @param operation
	 *            操作类型[共五种操作：增加、删除、修改、查看、查询]
	 * @return
	 */
	public List<DailyRecord> getOperationSpecPeriod(String startDate,
			String endDate, String entityId, String operation)
	{
		try
		{
			String sql = "";
			if (operation.equals("all"))
			{
				sql = "select OP_DATE,sum(OP_COUNTS) from dailystats where ENTITY='"
						+ entityId
						+ "' AND OP_DATE>='"
						+ startDate
						+ "' AND OP_DATE<='" + endDate + "' group by OP_DATE";
			}
			else if (operation.equals("show"))
			{
				sql = "select OP_DATE,sum(OP_COUNTS) from dailystats where ENTITY='"
						+ entityId
						+ "' AND OP_DATE>='"
						+ startDate
						+ "' AND OP_DATE<='"
						+ endDate
						+ "' AND OPERATION!='update' AND OPERATION!='query' AND OPERATION!='insert'  AND OPERATION!='delete' group by OP_DATE";
			}
			else
			{
				sql = "select OP_DATE,sum(OP_COUNTS) from dailystats where ENTITY='"
						+ entityId
						+ "' AND OP_DATE>='"
						+ startDate
						+ "' AND OP_DATE<='"
						+ endDate
						+ "' AND OPERATION='"
						+ operation + "'group by OP_DATE";
			}
			final String jdbcSql = sql;
			final List<DailyRecord> list = new ArrayList<DailyRecord>();
			_jdbcsource.executeQuery(new StringSql(jdbcSql),
					new ResultSetHandler()
					{
						public void afterQuery(ResultSet rs)
								throws SQLException
						{
							while (rs.next())
							{
								list.add(new DailyRecord(rs.getString(1)
										.substring(0, 10), rs.getInt(2)));
							}
						}
					});
			List<DailyRecord> rlist = new ArrayList<DailyRecord>();
			Calendar startCalendar = DateUtil.getDateAsCalendar(startDate);
			Calendar endCalendar = DateUtil.getDateAsCalendar(endDate);
			while (startCalendar.before(endCalendar))
			{
				rlist.add(new DailyRecord(DateUtil
						.getDateAsString(startCalendar), 0));
				startCalendar.add(Calendar.DAY_OF_MONTH, +1);
			}
			rlist.add(new DailyRecord(DateUtil.getDateAsString(startCalendar),
					0));
			Iterator<DailyRecord> it = list.iterator();
			while (it.hasNext())
			{
				DailyRecord record = (DailyRecord) it.next();
				Iterator<DailyRecord> rit = rlist.iterator();
				while (rit.hasNext())
				{
					DailyRecord rRecord = (DailyRecord) rit.next();
					if (rRecord.getDate().equals(record.getDate()))
					{
						rRecord.setCount(record.getCount());
					}
				}
			}
			return rlist;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得此数据集内指定操作在指定时间（小时）发生的次数
	 * 
	 * @param hour
	 *            指定时间（小时）
	 * @param operation
	 *            操作类型[共五种操作：增加、删除、修改、查看、查询]
	 * @return
	 */
	public int getOperationSpecToday(int hour, String operation)
	{
		Date hourStart = DateUtil.getHourStart(hour);
		Date hourEnd = DateUtil.getHourEnd(hour);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "";
		if (operation.equals("all"))
		{
			sql = "select count(*) from logrecords where DATASET='"
					+ _datasetId + "' AND OP_TIME>='" + df.format(hourStart)
					+ "' and OP_TIME <='" + df.format(hourEnd) + "'";
		}
		else if (operation.equals("show"))
		{
			sql = "select count(*) from logrecords where DATASET='"
					+ _datasetId
					+ "' and OP_TIME >='"
					+ df.format(hourStart)
					+ "' AND OP_TIME<='"
					+ df.format(hourEnd)
					+ "' AND OPERATION!='insert' AND OPERATION!='update' AND OPERATION!='delete' AND OPERATION!='query'";
		}
		else
		{
			sql = "select count(*) from logrecords where DATASET='"
					+ _datasetId + "' and OP_TIME >='" + df.format(hourStart)
					+ "' AND OP_TIME<='" + df.format(hourEnd)
					+ "' AND OPERATION='" + operation + "'";
		}
		int counts;
		try
		{
			counts = _logJdbcSource.countRecords(new StringSql(sql));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		return counts;
	}

	/**
	 * 获得此数据集内指定实体指定操作在指定时间（小时）发生的次数
	 * 
	 * @param hour
	 *            指定时间（小时）
	 * @param operation
	 *            操作类型[共五种操作：增加、删除、修改、查看、查询]
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public int getOperationSpecToday(int hour, String operation, String entityId)
	{
		Date hourStart = DateUtil.getHourStart(hour);
		Date hourEnd = DateUtil.getHourEnd(hour);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sql = "";
		if (operation.equals("all"))
		{
			sql = "select count(*) from logrecords where DATASET='"
					+ _datasetId + "' AND OP_TIME>='" + df.format(hourStart)
					+ "' and OP_TIME <='" + df.format(hourEnd)
					+ "' AND ENTITY='" + entityId + "'";
		}
		else if (operation.equals("show"))
		{
			sql = "select count(*) from logrecords where DATASET='"
					+ _datasetId
					+ "' and OP_TIME >='"
					+ df.format(hourStart)
					+ "' AND OP_TIME<='"
					+ df.format(hourEnd)
					+ "' AND OPERATION!='insert' AND OPERATION!='update' AND OPERATION!='query' AND OPERATION!='delete' AND ENTITY='"
					+ entityId + "'";
		}
		else
		{
			sql = "select count(*) from logrecords where DATASET='"
					+ _datasetId + "' and OP_TIME >='" + df.format(hourStart)
					+ "' AND OP_TIME<='" + df.format(hourEnd)
					+ "' AND OPERATION='" + operation + "' AND ENTITY='"
					+ entityId + "'";
		}
		int counts;
		try
		{
			counts = _logJdbcSource.countRecords(new StringSql(sql));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		return counts;
	}
}
