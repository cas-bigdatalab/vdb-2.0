package cn.csdb.commons.sql.jdbc.sql;

import java.sql.Connection;
import java.sql.SQLException;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.StatementHandler;

public interface Sql
{
	public void executeUpdate(JdbcSource sqlSource, Connection conn,
			StatementHandler handler) throws SQLException;
}
