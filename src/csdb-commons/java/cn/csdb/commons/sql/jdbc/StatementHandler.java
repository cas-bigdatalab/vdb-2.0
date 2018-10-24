package cn.csdb.commons.sql.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementHandler
{
	void afterExecute(PreparedStatement st) throws SQLException;
}
