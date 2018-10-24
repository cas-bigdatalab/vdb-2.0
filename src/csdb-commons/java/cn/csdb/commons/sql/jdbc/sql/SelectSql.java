package cn.csdb.commons.sql.jdbc.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;

/**
 * @author bluejoe
 */
public class SelectSql implements QuerySql
{
	private String _orderBy;

	private String _queryFields;

	private String _tableNames;

	private StringSql _whereFilter;

	public SelectSql()
	{
		reset();
	}

	private StringSql toSqlText(JdbcSource sqlSource) throws SQLException
	{
		StringSql st = new StringSql();

		String sql = MessageFormat.format("select {0} from {1}", new Object[] {
				_queryFields == null ? "*" : _queryFields,
				_tableNames == null ? "@this" : _tableNames });

		if (_whereFilter != null)
		{
			String wf = _whereFilter.getString();
			if (wf != null && wf.length() > 0)
			{
				sql += " where " + wf;
				st.addParameters(_whereFilter);
			}
		}

		if (_orderBy != null)
		{
			sql += " order by " + _orderBy;
		}

		st.setString(sql);
		return st;
	}

	public SelectSql reset()
	{
		_orderBy = null;
		_queryFields = null;
		_tableNames = null;
		_whereFilter = null;

		return this;
	}

	public SelectSql setField(String... fields)
	{
		_queryFields = null;

		if (fields != null)
		{
			for (int i = 0; i < fields.length; i++)
			{
				if (_queryFields != null)
				{
					_queryFields += ",";
				}
				else
				{
					_queryFields = "";
				}

				_queryFields += fields[i];
			}
		}

		return this;
	}

	public SelectSql setFilter(StringSql filter)
	{
		_whereFilter = filter;
		return this;
	}

	public SelectSql setOrderBy(String ob)
	{
		_orderBy = ob;
		return this;
	}

	public SelectSql setTableName(String... tableNames)
	{
		String s = null;
		for (String name : tableNames)
		{
			if (s != null)
				s += ",";
			else
				s = "";

			s += name;
		}

		_tableNames = s;
		return this;
	}

	public void executeQuery(JdbcSource sqlSource, int beginning, int size,
			Connection conn, ResultSetHandler handler) throws SQLException
	{
		toSqlText(sqlSource).executeQuery(sqlSource, beginning, size, conn,
				handler);
	}

	public QuerySql getSqlForCount(JdbcSource sqlSource) throws SQLException
	{
		return toSqlText(sqlSource).getSqlForCount(sqlSource);
	}
}
