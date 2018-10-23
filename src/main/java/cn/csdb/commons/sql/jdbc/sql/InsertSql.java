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
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.util.SqlUtils;

/*
 * 2006-10-25: 增加时不添加NULL字段和自增长字段
 * 
 * @author bluejoe
 */
public class InsertSql implements Sql
{
	private Map _record;

	private String _tableName;

	public InsertSql(String tableName, Map record)
	{
		_tableName = tableName;
		_record = record;
	}

	private StringSql toSqlText(JdbcSource sqlSource) throws SQLException
	{
		JdbcTable sqlTable = SqlUtils.getNotNullTable(sqlSource, _tableName);

		String tableName = sqlTable.getTableName();
		Map record = SqlUtils.formatColumns(sqlTable, _record, true, false,
				false);

		String columnNames = null;
		String columnValues = null;

		StringSql st = new StringSql();
		Iterator it = record.entrySet().iterator();

		while (it.hasNext())
		{
			Entry me = (Entry) it.next();
			String name = (String) me.getKey();
			Object o = me.getValue();
			name = sqlSource.getQuotedIdentifier(name);

			if (columnNames == null)
			{
				columnNames = name;
				columnValues = "?";
			}
			else
			{
				columnNames += "," + name;
				columnValues += ",?";
			}

			st.addParameter((Serializable) o);
		}

		String sql = MessageFormat.format("insert into {0} ({1}) values ({2})",
				new Object[] { sqlSource.getQuotedIdentifier(tableName),
						columnNames, columnValues });

		st.setString(sql);
		return st;
	}

	public void executeUpdate(JdbcSource sqlSource, Connection conn,
			StatementHandler handler) throws SQLException
	{
		toSqlText(sqlSource).executeUpdate(sqlSource, conn, handler);
	}
}
