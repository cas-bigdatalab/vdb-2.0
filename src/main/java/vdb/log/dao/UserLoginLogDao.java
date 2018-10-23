package vdb.log.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import vdb.log.vo.UserLoginLog;
import vdb.mydb.jdbc.JdbcSourceManager;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class UserLoginLogDao
{

	public void addLoginLog(UserLoginLog log) throws Exception
	{
		Map<String, String> map = new LinkedHashMap();
		map.put("USER_NAME", log.getUser());
		map.put("IP", log.getIp());
		map.put("OP_TIME", log.getOpTime());
		map.put("OPERATION", log.getOperation());
		map.put("PARAM1", log.getParam1());
		map.put("PARAM2", log.getParam2());
		map.put("PARAM3", log.getParam3());
		JdbcSource dataSource = getJdbcSource();
		dataSource.insertRecord("USERLOG", map);
	}

	public Pageable<UserLoginLog> getUserLoginLogs(String username)
	{
		try
		{
			String ssql = "select * from USERLOG " + " where USER_NAME = ? "
					+ " order by OP_TIME ";
			QuerySql sql = new StringSql(ssql, username);
			return createPageable(getJdbcSource(), sql);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private Pageable<UserLoginLog> createPageable(JdbcSource dataSource,
			QuerySql sql) throws Exception
	{
		return dataSource.createQuery(sql, new ResultSetReader<UserLoginLog>()
		{
			public UserLoginLog read(ResultSet rs, int row) throws SQLException
			{
				UserLoginLog log = new UserLoginLog();
				log.setUser(rs.getString("USER_NAME"));
				log.setIp(rs.getString("IP"));
				log.setOpTime(rs.getString("OP_TIME"));
				log.setOperation(rs.getString("OPERATION"));
				log.setParam1(rs.getString("PARAM1"));
				log.setParam2(rs.getString("PARAM2"));
				log.setParam3(rs.getString("PARAM3"));
				return log;
			}
		});
	}

	private JdbcSource getJdbcSource() throws Exception
	{
		return JdbcSourceManager.getInstance().getLogsJdbcSource();
	}
}
