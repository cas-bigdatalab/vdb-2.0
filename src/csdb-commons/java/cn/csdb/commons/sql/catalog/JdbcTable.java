/*
 * 创建日期 2005-4-12
 */
package cn.csdb.commons.sql.catalog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ConnectionHandler;
import cn.csdb.commons.sql.types.ResultSetColumn;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.MapHelper;
import cn.csdb.commons.util.StringKeyMap;

/**
 * @author bluejoe
 */
public class JdbcTable
{
	private JdbcDatabase _database;

	private ListMap<String, JdbcColumn> _columns;

	private List _primaryKeyNames;

	private Map _properties = new StringKeyMap();

	private JdbcSource _sqlSource;

	public JdbcTable(JdbcSource sqlSource, JdbcDatabase database, Map properties)
	{
		_sqlSource = sqlSource;
		_database = database;
		_properties = properties;
	}

	public JdbcDatabase getDatabase()
	{
		return _database;
	}

	public JdbcColumn getColumn(String fieldName)
	{
		if (fieldName == null)
			return null;

		loadColumns();
		return _columns.map().get(fieldName);
	}

	public List<JdbcColumn> getColumns()
	{
		loadColumns();
		return _columns.list();
	}

	public void loadColumns()
	{
		if (_columns == null)
		{
			final ListMap<String, JdbcColumn> columns = new ListMap<String, JdbcColumn>(
					new StringKeyMap<JdbcColumn>());

			class FieldsExtractor implements ConnectionHandler
			{
				private JdbcTable _ti;

				FieldsExtractor(JdbcTable ti)
				{
					_ti = ti;
				}

				public void handle(Connection conn) throws SQLException
				{
					// 获取列
					Map<String, Object> namedColumns = new StringKeyMap<Object>();
					String sql = "select * from "
							+ _sqlSource
									.getQuotedIdentifier(_ti.getTableName())
							+ " where 1=0";

					Statement statement = null;
					ResultSet resultSet = null;
					try
					{
						PoolManager.getInstance().getLogger().debug(
								"extracting fields of `" + _ti.getTableName()
										+ "` ...");

						statement = conn.createStatement();
						resultSet = statement.executeQuery(sql);
						ResultSetMetaData rsmd = resultSet.getMetaData();

						for (int i = 1; i <= rsmd.getColumnCount(); i++)
						{
							ResultSetColumn column = new ResultSetColumn(rsmd,
									i);
							namedColumns.put(column.getColumnName(), column);
						}
					}
					finally
					{
						if (resultSet != null)
							resultSet.close();
						if (statement != null)
							statement.close();
					}

					// 获取该表的所有字段
					List fps = _sqlSource.getSqlDialect().getColumns(conn,
							getSchemaName(), getShortTableName());
					// 处理每个字段
					for (int i = 0; i < fps.size(); i++)
					{
						// 创建表对象
						Map m = (Map) fps.get(i);
						JdbcColumn column = new JdbcColumn(_sqlSource, _ti, m,
								(ResultSetColumn) namedColumns.get(m
										.get("COLUMN_NAME")));

						columns.add(column.getColumnName(), column);
					}
				}
			}

			try
			{
				_sqlSource.handle(new FieldsExtractor(this));
				_columns = columns;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(String.format(
						"failed to load columns of table `%s`", this
								.getTableName()), e);
			}
		}
	}

	public String getPrimaryKeyName()
	{
		if (_primaryKeyNames == null || _primaryKeyNames.size() == 0)
			return null;

		return (String) _primaryKeyNames.get(0);
	}

	public List getPrimaryKeyNames()
	{
		return _primaryKeyNames;
	}

	public String getTableName()
	{
		return new MapHelper(_properties).getString("TABLE_NAME");
	}

	public String getSchemaName()
	{
		return new MapHelper(_properties).getString("TABLE_SCHEM");
	}

	public String getFullTableName()
	{
		return getTableName();
	}

	public String getShortTableName()
	{
		if (_properties.containsKey("SHORT_TABLE_NAME"))
			return new MapHelper(_properties).getString("SHORT_TABLE_NAME");

		return getTableName();
	}

	public String getTableType()
	{
		return new MapHelper(_properties).getString("TABLE_TYPE");
	}

	public void setPrimaryKeyNames(List primaryKeyNames)
	{
		_primaryKeyNames = primaryKeyNames;
	}

	public String toString()
	{
		return getTableName();
	}
}