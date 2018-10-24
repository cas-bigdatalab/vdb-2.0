/*
 * Created on 2006-6-20
 */
package cn.csdb.commons.sql.catalog;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.dialect.SqlDialect;

public class JdbcCatalog
{
	private JdbcDatabase _database;

	private String _catalogName;

	private int _driverMajorVersion;

	private int _driverMinorVersion;

	private String _driverName;

	private String _driverVersion;

	private String _identifierQuoteString;

	private int _jdbcMajorVersion;

	private int _jdbcMinorVersion;

	private String _url;

	private String _userName;

	private JdbcSource _sqlSource;

	private SqlDialect _sqlDialect;

	public JdbcCatalog(Connection conn, JdbcSource sqlSource,
			SqlDialect sqlDialect) throws SQLException
	{
		_sqlSource = sqlSource;
		_sqlDialect = sqlDialect;

		DatabaseMetaData databaseMetaData = conn.getMetaData();

		// 读取属性
		// oracle驱动会抛出error
		try
		{
			_userName = databaseMetaData.getUserName();
		}
		catch (Throwable e)
		{
		}
		try
		{
			_url = databaseMetaData.getURL();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_catalogName = conn.getCatalog();
		}
		catch (Throwable e)
		{
		}

		PoolManager.getInstance().getLogger().debug(
				"extracting catalog from `" + _url + "` ...");

		try
		{
			// _identifierQuoteString = dm.getIdentifierQuoteString();
			_identifierQuoteString = _sqlDialect
					.getIdentifierQuoteString(databaseMetaData);
		}
		catch (Throwable e)
		{
		}

		try
		{
			_driverVersion = databaseMetaData.getDriverVersion();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_driverMajorVersion = databaseMetaData.getDriverMajorVersion();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_driverMinorVersion = databaseMetaData.getDriverMinorVersion();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_jdbcMajorVersion = databaseMetaData.getJDBCMajorVersion();
		}
		catch (Throwable e)
		{
		}

		try
		{
			_jdbcMinorVersion = databaseMetaData.getJDBCMinorVersion();
		}
		catch (Throwable e)
		{
		}

		_database = new JdbcDatabase(conn, _sqlSource, this);
	}

	public String getCatalogName()
	{
		return _catalogName;
	}

	public JdbcDatabase getDatabase()
	{
		return _database;
	}

	public int getDriverMajorVersion()
	{
		return _driverMajorVersion;
	}

	public int getDriverMinorVersion()
	{
		return _driverMinorVersion;
	}

	public String getDriverName()
	{
		return _driverName;
	}

	public String getDriverVersion()
	{
		return _driverVersion;
	}

	public String getIdentifierQuoteString()
	{
		return _identifierQuoteString;
	}

	public int getJdbcMajorVersion()
	{
		return _jdbcMajorVersion;
	}

	public int getJdbcMinorVersion()
	{
		return _jdbcMinorVersion;
	}

	public JdbcSource getJdbcSource()
	{
		return _sqlSource;
	}

	public String getURL()
	{
		return _url;
	}

	public String getUserName()
	{
		return _userName;
	}
}