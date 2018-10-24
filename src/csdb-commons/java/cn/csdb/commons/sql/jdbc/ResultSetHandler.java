package cn.csdb.commons.sql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bluejoe
 */
public interface ResultSetHandler
{
	public void afterQuery(ResultSet rs) throws SQLException;
}
