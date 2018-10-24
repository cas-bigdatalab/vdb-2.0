/*
 * Created on 2006-1-19
 */
package cn.csdb.commons.sql.jdbc.sql;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.catalog.JdbcTable;
import cn.csdb.commons.sql.jdbc.NoColumnsException;
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.util.SqlUtils;

/*
 * 2006-10-25: 修改时不更新自增长字段
 * 
 * @author bluejoe
 */
public class UpdateSql implements Sql
{
	private Map _record;

	private String _tableName;

	private StringSql _whereFilter;

	public UpdateSql(String tableName, Map record, String whereFilter)
	{
		_tableName = tableName;
		_record = record;
		_whereFilter = new StringSql(whereFilter);
	}

	public UpdateSql(String tableName, Map record, StringSql whereFilter)
	{
		_tableName = tableName;
		_record = record;
		_whereFilter = whereFilter;
	}

	private StringSql toSqlText(JdbcSource sqlSource) throws SQLException
	{
		JdbcTable sqlTable = SqlUtils.getNotNullTable(sqlSource, _tableName);

		String tableName = sqlTable.getTableName();
		Map record = SqlUtils.formatColumns(sqlTable, _record, false, false,
				false);

		if (record.isEmpty())
			throw new NoColumnsException();

		String columnNameValueSet = null;
		Iterator it = record.entrySet().iterator();

		StringSql st = new StringSql();
		while (it.hasNext())
		{
			Entry me = (Entry) it.next();
			String name = (String) me.getKey();
			Object o = me.getValue();
			name = sqlSource.getQuotedIdentifier(name);

			if (columnNameValueSet == null)
			{
				columnNameValueSet = name;
			}
			else
			{
				columnNameValueSet += "," + name;
			}

			columnNameValueSet += "=?";
			st.addParameter((Serializable) o);
		}

		String sql = MessageFormat.format("update {0} set {1}", new Object[] {
				sqlSource.getQuotedIdentifier(tableName), columnNameValueSet });

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
