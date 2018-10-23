/*
 * Created on 2006-1-19
 */
package cn.csdb.commons.sql.jdbc.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.util.SqlUtils;

/**
 * @author bluejoe
 */
public class DeleteSql implements Sql
{
	private String _tableName;

	private StringSql _whereFilter;

	public DeleteSql(String tableName, StringSql whereFilter)
	{
		_tableName = tableName;
		_whereFilter = whereFilter;
	}

	private StringSql toSqlText(JdbcSource sqlSource) throws SQLException
	{
		String tableName = SqlUtils.getNotNullTable(sqlSource, _tableName)
				.getTableName();

		StringSql st = new StringSql();
		String sql = MessageFormat.format("delete from {0}",
				new Object[] { sqlSource.getQuotedIdentifier(tableName) });

		if (_whereFilter != null)
		{
			String wf = _whereFilter.getString();
			if (wf != null && wf.length() > 0)
			{
				sql += " where " + wf;
				st.addParameters(_whereFilter);
			}
		}

		st.setString(sql);
		return st;
	}

	public void executeUpdate(JdbcSource sqlSource, Connection conn,
			StatementHandler handler) throws SQLException
	{
		toSqlText(sqlSource).executeUpdate(sqlSource, conn, handler);
	}
}
