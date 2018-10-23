package vdb.log.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import vdb.log.vo.DataAccessLog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.util.DateUtil;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DataAccessLogDao
{
	public void insertLog(DataAccessLog log) throws Exception
	{
		Map map = new LinkedHashMap();
		map.put("USER_NAME", log.getUser());
		map.put("IP", log.getIp());
		map.put("OP_TIME", new Date());
		map.put("DATASET", log.getDataset());
		map.put("ENTITY", log.getEntity());
		map.put("OPERATION", log.getOperation());
		map.put("PARAM1", log.getParam1());
		map.put("PARAM2", log.getParam2());
		map.put("PARAM3", log.getParam3());
		DataSet ds = VdbManager.getEngine().getCatalog().fromId(
				log.getDataset());
		JdbcSource dataSource = getJdbcSource(ds);
		dataSource.insertRecord("LOGRECORDS", map);
	}

	public Pageable<DataAccessLog> getDataVisitationLogs(Entity entity,
			Serializable beanId)
	{
		try
		{
			String entityId = entity.getId();
			JdbcSource jdbcSource = getJdbcSource(entity.getDataSet());
			String ssql = "select * from LOGRECORDS " + " where PARAM2 = ? "
					+ " and ENTITY =? " + " and OPERATION = 'showBean' "
					+ " order by OP_TIME ";
			QuerySql sql = new StringSql(ssql, beanId, entityId);
			return createPageable(jdbcSource, sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<DataAccessLog> getDataAccessLogByDateAndDS(DataSet ds,Date date){
		Date dateStart = DateUtil.getDateStart(date);
		Date dateEnd = DateUtil.getDateEnd(date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try{
			String dsid = ds.getId();
			JdbcSource jdbcSource = getJdbcSource(ds);
			StringSql sql = new StringSql("select * from LOGRECORDS " + " where DATASET =? " +
					" and OP_TIME>=? and OP_TIME<=? ");
			sql.setParameter(1,dsid);
			sql.setParameter(2, df.format(dateStart));
			sql.setParameter(3, df.format(dateEnd));
			
			final List<DataAccessLog> logList = new ArrayList<DataAccessLog>();
			jdbcSource.executeQuery(sql, new ResultSetHandler()
			{
				public void afterQuery(ResultSet rs) throws SQLException
				{
					DataAccessLog record = null;
					while (rs.next())
					{
						record = new DataAccessLog();
						record.setId(rs.getLong("ID"));
						record.setUser(rs.getString("USER_NAME"));
						record.setIp(rs.getString("IP"));
						record.setOpTime(rs.getString("OP_TIME"));
						record.setDataset(rs.getString("DATASET"));
						record.setEntity(rs.getString("ENTITY"));
						record.setOperation(rs.getString("OPERATION"));
						record.setParam1(rs.getString("PARAM1"));
						record.setParam2(rs.getString("PARAM2"));
						record.setParam3(rs.getString("PARAM3"));
						logList.add(record);
					}
				}
			});
			
			return logList;
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Pageable<DataAccessLog> getDataModificationLogs(Entity entity,
			Serializable beanId)
	{
		try
		{
			String entityId = entity.getId();
			JdbcSource jdbcSource = getJdbcSource(entity.getDataSet());
			String ssql = "select * from LOGRECORDS " + " where PARAM2 = ? "
					+ " and ENTITY =? " + " and (OPERATION = 'update' "
					+ " or OPERATION = 'insert' "
					+ " or OPERATION = 'delete') " + " order by OP_TIME ";
			QuerySql sql = new StringSql(ssql, beanId, entityId);
			return createPageable(jdbcSource, sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Pageable<DataAccessLog> getUserAccessLogsInDataset(DataSet dataset,
			String username)
	{
		try
		{
			JdbcSource jdbcSource = getJdbcSource(dataset);
			String ssql = "select * from LOGRECORDS " + " where DATASET = ? "
					+ " and USER_NAME =? " + " order by OP_TIME ";
			QuerySql sql = new StringSql(ssql, dataset.getId(), username);
			return createPageable(jdbcSource, sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private JdbcSource getJdbcSource(DataSet dataset) throws Exception
	{
		return JdbcSourceManager.getInstance().getLogsJdbcSource(dataset);
	}

	private Pageable<DataAccessLog> createPageable(JdbcSource dataSource,
			QuerySql sql) throws Exception
	{
		return dataSource.createQuery(sql, new ResultSetReader<DataAccessLog>()
		{
			public DataAccessLog read(ResultSet rs, int row)
					throws SQLException
			{
				DataAccessLog log = new DataAccessLog();
				log.setUser(rs.getString("USER_NAME"));
				log.setIp(rs.getString("IP"));
				log.setOpTime(rs.getString("OP_TIME"));
				log.setDataset(rs.getString("DATASET"));
				log.setEntity(rs.getString("ENTITY"));
				log.setOperation(rs.getString("OPERATION"));
				log.setParam1(rs.getString("PARAM1"));
				log.setParam2(rs.getString("PARAM2"));
				log.setParam3(rs.getString("PARAM3"));
				return log;
			}
		});
	}
}
