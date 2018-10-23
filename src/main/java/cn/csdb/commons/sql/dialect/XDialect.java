/*
 * 创建日期 2005-9-8
 */
package cn.csdb.commons.sql.dialect;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import cn.csdb.commons.sql.types.JdbcObject;
import cn.csdb.commons.sql.types.ResultSetColumn;
import cn.csdb.commons.util.SqlUtils;
import cn.csdb.commons.util.StringUtils;
import cn.csdb.commons.util.TimeUtils;

/*
 * 简单的SQLAdapter
 * 
 * @author bluejoe
 */
public class XDialect implements SqlDialect
{
	public Statement createQueryStatement(Connection conn, boolean isFullQuery,
			boolean isReadOnly) throws SQLException
	{
		return conn.createStatement(isFullQuery ? ResultSet.TYPE_FORWARD_ONLY
				: ResultSet.TYPE_SCROLL_INSENSITIVE,
				isReadOnly ? ResultSet.CONCUR_READ_ONLY
						: ResultSet.CONCUR_UPDATABLE);
	}

	protected String escapeString(ResultSetColumn column, String s)
	{
		if (s == null)
			return "NULL";

		switch (column.getColumnType())
		{
			case Types.CLOB:
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
				return "'" + StringUtils.strictReplaceAll(s, "\'", "''") + "'";
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				return "'" + s + "'";
		}

		return s;
	}

	public List getColumns(Connection conn, String schemaName, String tableName)
			throws SQLException
	{
		List fields = new ArrayList();
		ResultSet columnsResultSet = null;
		try
		{
			columnsResultSet = conn.getMetaData().getColumns(null, schemaName,
					tableName, "%");
			while (columnsResultSet.next())
			{
				fields.add(SqlUtils.loadResultSetProperties(columnsResultSet));
			}
		}
		finally
		{
			if (columnsResultSet != null)
				columnsResultSet.close();
		}
		return fields;
	}

	/**
	 * 文本-->String 二进制-->byte [] 数值-->java.lang.Number 日期-->java.util.Date
	 */
	public Object getObject(ResultSet rs, int i) throws SQLException
	{
		Object o = rs.getObject(i);

		// blob
		if (o instanceof Blob)
		{
			Blob b = (Blob) o;
			return b.getBytes(1, (int) b.length());
		}

		// clob
		if (o instanceof Clob)
		{
			Clob b = (Clob) o;
			return b.getSubString(1, (int) b.length());
		}

		return o;
	}

	public List getPrimaryKeys(Connection conn, String tableName)
			throws SQLException
	{
		List pks = new Vector();
		ResultSet primaryKeysResultSet = null;

		try
		{
			primaryKeysResultSet = conn.getMetaData().getPrimaryKeys(null,
					getSchemaName(conn), tableName);
			while (primaryKeysResultSet.next())
			{
				pks.add(primaryKeysResultSet.getString("COLUMN_NAME"));
			}
		}
		finally
		{
			if (primaryKeysResultSet != null)
				primaryKeysResultSet.close();
		}

		return pks;
	}

	public String getSchemaName(Connection conn) throws SQLException
	{
		return null;
	}

	public String getString(ResultSet rs, int i) throws SQLException
	{
		Object o = rs.getObject(i);
		if (o == null)
			return null;

		// bytes
		if (o instanceof byte[])
		{
			return new String((byte[]) (o));
		}

		// clob
		if (o instanceof Clob)
		{
			Clob b = (Clob) o;
			return b.getSubString(1, (int) b.length());
		}

		// blob
		if (o instanceof Blob)
		{
			Blob b = (Blob) o;
			return new String(b.getBytes(1, (int) b.length()));
		}

		return o.toString();
	}

	public List getTables(Connection conn) throws SQLException
	{
		List tables = new Vector();
		ResultSet tablesResultSet = null;
		try
		{
			tablesResultSet = conn.getMetaData().getTables(null,
					getSchemaName(conn), "%", new String[] { "TABLE" });
			while (tablesResultSet.next())
			{
				// 创建表对象
				tables.add(SqlUtils.loadResultSetProperties(tablesResultSet));
			}
		}
		finally
		{
			if (tablesResultSet != null)
				tablesResultSet.close();
		}

		return tables;
	}

	private void setNumber(PreparedStatement st, int i, Object o,
			ResultSetColumn column) throws SQLException
	{
		// 数值类型，跳过
		if (o instanceof Number)
		{
			st.setObject(i, o);
			return;
		}

		if (o instanceof Boolean)
		{
			st.setObject(i, ((Boolean) o).booleanValue() ? "1" : "0");
			return;
		}

		int sqlType = column.getColumnType();

		String s = o.toString().trim();

		// 空文本认为是null
		if (s.length() == 0)
		{
			st.setNull(i, sqlType);
			return;
		}

		try
		{
			Number number = NumberFormat.getInstance().parse(s);
			st.setObject(i, number);
			return;
		}
		catch (ParseException e)
		{
			throw new SQLException("wrong number `" + s + "` for column `"
					+ column.getColumnName() + "`, `"
					+ column.getColumnTypeName() + "` expected.");
		}
	}

	private void setTime(PreparedStatement st, int i, Object o,
			ResultSetColumn column) throws SQLException
	{
		int sqlType = column.getColumnType();

		switch (sqlType)
		{
			case Types.DATE:
				if (o instanceof java.sql.Date)
				{
					st.setDate(i, (java.sql.Date) o);
					return;
				}
			case Types.TIME:
				if (o instanceof Time)
				{
					st.setTime(i, (Time) o);
					return;
				}
			case Types.TIMESTAMP:
				if (o instanceof Timestamp)
				{
					st.setTimestamp(i, (Timestamp) o);
					return;
				}
		}

		long time = -1;

		if (o instanceof java.sql.Date)
		{
			time = ((java.sql.Date) o).getTime();
		}

		else if (o instanceof Date)
		{
			time = ((Date) o).getTime();
		}

		else if (o instanceof Calendar)
		{
			time = ((Calendar) o).getTimeInMillis();
		}

		else
		{
			String s = o.toString().trim();
			if (s.length() == 0)
			{
				st.setNull(i, sqlType);
				return;
			}

			String[] formats = new String[] { s.indexOf(":") < 0 ? "yyyy-MM-dd"
					: "yyyy-MM-dd HH:mm:ss" };

			for (int m = 0; m < formats.length; m++)
			{
				try
				{
					time = new SimpleDateFormat(formats[m]).parse(s).getTime();
				}
				catch (Exception e)
				{
					continue;
				}
			}
		}

		if (time != -1)
		{
			switch (sqlType)
			{
				case Types.DATE:
					st.setDate(i, new java.sql.Date(time));
					return;
				case Types.TIME:
					st.setTime(i, new java.sql.Time(time));
					return;
				case Types.TIMESTAMP:
					st.setTimestamp(i, new Timestamp(time));
					return;
			}
		}

		throw new SQLException("wrong date `" + o + "` for column `"
				+ column.getColumnName() + "`, `" + column.getColumnTypeName()
				+ "` expected.");
	}

	public PreparedStatement prepareQueryStatement(Connection conn, String sql,
			boolean isFullQuery, boolean isReadOnly) throws SQLException
	{
		return conn.prepareStatement(sql,
				isFullQuery ? ResultSet.TYPE_FORWARD_ONLY
						: ResultSet.TYPE_SCROLL_INSENSITIVE,
				isReadOnly ? ResultSet.CONCUR_READ_ONLY
						: ResultSet.CONCUR_UPDATABLE);
	}

	/**
	 * get native SQL for block query
	 */
	public String sqlForBlockQuery(String sql, int beginning, int size)
	{
		return null;
	}

	/**
	 * get native SQL to retrieve record count
	 */
	public String sqlForCount(String sql)
	{
		Pattern pattern = Pattern.compile(
				"(^select\\s+)(.+?)(\\s+from\\s+)(.+?)(order\\s+by.+)?$",
				Pattern.CASE_INSENSITIVE);
		// sql = pattern.matcher(sql).replaceFirst("$1count($2)$3$4");
		sql = pattern.matcher(sql).replaceFirst("$1count(*)$3$4");
		return sql;
	}

	public void updateObject(PreparedStatement st, int i, Object o)
			throws SQLException
	{
		// 是JdbcObject类型
		if (o instanceof JdbcObject)
		{
			JdbcObject so = (JdbcObject) o;
			try
			{
				updateJdbcObject(st, i, so.getObject(), so.getColumn());
				return;
			}
			catch (Exception e)
			{
				SQLException e2 = new SQLException("failed to update column `"
						+ so.getColumn().getColumnName() + "` due to: "
						+ e.getMessage());
				e2.initCause(e);
				throw e2;
			}
		}

		// 不是SQLObject类型
		updateUnknownObject(st, i, o);
	}

	protected void updateJdbcObject(PreparedStatement st, int i, Object o,
			ResultSetColumn column) throws SQLException
	{
		int sqlType = column.getColumnType();

		if (o == null)
		{
			st.setNull(i, sqlType);
			return;
		}

		switch (sqlType)
		{
			// 时间类型
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				setTime(st, i, o, column);
				return;
				// 数值类型
			case Types.BIGINT:
			case Types.BIT:
			case Types.BOOLEAN:
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.INTEGER:
			case Types.NUMERIC:
			case Types.REAL:
			case Types.SMALLINT:
			case Types.TINYINT:
				setNumber(st, i, o, column);
				return;
				// 二进制类型
			case Types.BINARY:
			case Types.BLOB:
			case Types.LONGVARBINARY:
			case Types.VARBINARY:
				// byte []
				if (o instanceof byte[])
				{
					st.setBytes(i, (byte[]) o);
					return;
				}

				// string
				if (o instanceof String)
				{
					st.setBytes(i, ((String) o).getBytes());
					return;
				}

				// inputstream
				if (o instanceof InputStream)
				{
					try
					{
						InputStream is = (InputStream) o;
						st.setBinaryStream(i, is, is.available());
					}
					catch (Exception e)
					{
						if (e instanceof SQLException)
							throw (SQLException) e;

						throw new SQLException(e.getMessage());
					}

					return;
				}

				// file
				if (o instanceof File)
				{
					try
					{
						File o2 = (File) o;
						InputStream is = new FileInputStream(o2);
						byte[] bytes = new byte[(int) o2.length()];
						is.read(bytes);
						is.close();
						st.setBytes(i, bytes);
					}
					catch (Exception e)
					{
						if (e instanceof SQLException)
							throw (SQLException) e;

						throw new SQLException(e.getMessage());
					}

					return;
				}

				// 文本类型
			case Types.CLOB:
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
				// byte []
				if (o instanceof byte[])
				{
					st.setString(i, new String((byte[]) o));
					return;
				}

				// string
				if (o instanceof String)
				{
					st.setString(i, (String) o);
					return;
				}

				// inputstream
				if (o instanceof InputStream)
				{
					try
					{
						InputStream is = (InputStream) o;
						st.setAsciiStream(i, is, is.available());
					}
					catch (Exception e)
					{
						if (e instanceof SQLException)
							throw (SQLException) e;

						throw new SQLException(e.getMessage());
					}

					return;
				}

				// Date
				if (o instanceof Date)
				{
					st.setString(i, TimeUtils.getTimeString((Date) o));
					return;
				}

				// file
				if (o instanceof File)
				{
					try
					{
						File o2 = (File) o;
						InputStream is = new FileInputStream(o2);
						byte[] bytes = new byte[(int) o2.length()];
						is.read(bytes);
						is.close();
						st.setString(i, new String(bytes));
					}
					catch (Exception e)
					{
						if (e instanceof SQLException)
							throw (SQLException) e;

						throw new SQLException(e.getMessage());
					}

					return;
				}

				st.setString(i, o.toString());
				return;
		}

		st.setObject(i, o);
	}

	protected void updateUnknownObject(PreparedStatement st, int i, Object o)
			throws SQLException
	{
		st.setObject(i, o);
	}

	public void updateObject(ResultSet rs, int i, Object o) throws SQLException
	{
		updateObject(new ObjectUpdater(rs), i, o);
	}

	public String updateString(Object o) throws SQLException
	{
		if (o == null)
			return "NULL";

		if (o instanceof JdbcObject)
		{
			JdbcObject so = (JdbcObject) o;
			ResultSetColumn column = so.getColumn();
			StringUpdater su = new StringUpdater();
			try
			{
				updateJdbcObject(su, -1, so.getObject(), column);
			}
			catch (Exception e)
			{
				SQLException e2 = new SQLException("failed to update column `"
						+ so.getColumn().getColumnName() + "` due to: "
						+ e.getMessage());
				e2.initCause(e);
				throw e2;
			}

			return escapeString(column, su.getString());
		}

		return "" + o;
	}

	public String getIdentifierQuoteString(DatabaseMetaData databaseMetaData)
			throws SQLException
	{
		return databaseMetaData.getIdentifierQuoteString();
	}
}