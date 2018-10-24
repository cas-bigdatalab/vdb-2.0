/*
 * 创建日期 2005-4-12
 */
package cn.csdb.commons.sql.catalog;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.StringKeyMap;

/**
 * @author bluejoe
 */
public class JdbcDatabase
{
	private int _databaseMajorVersion;

	private int _databaseMinorVersion;

	private String _databaseProductName;

	private String _databaseProductVersion;

	private JdbcCatalog _sqlCatalog;

	private JdbcSource _sqlSource;

	private ListMap<String, JdbcTable> _tables = new ListMap<String, JdbcTable>(
			new StringKeyMap<JdbcTable>());

	public JdbcDatabase(Connection conn, JdbcSource sqlSource,
			JdbcCatalog catalog) throws SQLException
	{
		_sqlSource = sqlSource;
		_sqlCatalog = catalog;

		DatabaseMetaData dm = conn.getMetaData();
		// 读取数据库信息
		try
		{
			_databaseMajorVersion = dm.getDatabaseMajorVersion();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_databaseMinorVersion = dm.getDatabaseMinorVersion();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_databaseProductName = dm.getDatabaseProductName();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_databaseProductVersion = dm.getDatabaseProductVersion();
		}
		catch (Throwable e)
		{
		}

		// 获取所有表
		List tps = _sqlSource.getSqlDialect().getTables(conn);
		// 处理每个表
		for (int i = 0; i < tps.size(); i++)
		{
			// 创建表对象
			try
			{
				JdbcTable table = new JdbcTable(_sqlSource, this, (Map) tps
						.get(i));
				_tables.add(table.getTableName(), table);

				table.setPrimaryKeyNames(_sqlSource.getSqlDialect()
						.getPrimaryKeys(conn, table.getTableName()));
			}
			catch (Throwable e)
			{
				continue;
			}
		}
	}

	public JdbcCatalog getCatalog()
	{
		return _sqlCatalog;
	}

	public int getDatabaseMajorVersion()
	{
		return this._databaseMajorVersion;
	}

	public int getDatabaseMinorVersion()
	{
		return this._databaseMinorVersion;
	}

	public String getDatabaseProductName()
	{
		return this._databaseProductName;
	}

	public String getDatabaseProductVersion()
	{
		return this._databaseProductVersion;
	}

	public JdbcTable getTable(String tableName)
	{
		if (tableName == null)
			return null;

		return _tables.map().get(tableName);
	}

	public List<JdbcTable> getTables()
	{
		return _tables.list();
	}
}