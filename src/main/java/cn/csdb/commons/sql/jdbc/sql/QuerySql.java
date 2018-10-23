package cn.csdb.commons.sql.jdbc.sql;

import java.sql.Connection;
import java.sql.SQLException;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;

public interface QuerySql
{
	public void executeQuery(JdbcSource sqlSource, int beginning, int size,
			Connection conn, ResultSetHandler handler) throws SQLException;

	QuerySql getSqlForCount(JdbcSource sqlSource) throws SQLException;
}