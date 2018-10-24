package cn.csdb.commons.sql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bluejoe
 */
public class ResultSetPeeker implements ResultSetHandler
{
	private boolean _empty;

	/**
	 * @return
	 */
	public boolean isEmpty()
	{
		return _empty;
	}

	public void afterQuery(ResultSet rs) throws SQLException
	{
		_empty = !rs.next();
	}
}
