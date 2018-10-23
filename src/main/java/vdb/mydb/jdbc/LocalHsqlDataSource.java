package vdb.mydb.jdbc;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class LocalHsqlDataSource implements DataSource, InitializingBean,
		DisposableBean
{
	private DataSource _dataSource;

	private File _dbPath;

	public void afterPropertiesSet() throws Exception
	{
		Properties ps = new Properties();
		ps.put("driverClassName", "org.hsqldb.jdbcDriver");

		ps.put("url", "jdbc:hsqldb:" + _dbPath.getCanonicalPath()
				+ ";shutdown=true");
		ps.put("username", "sa");
		ps.put("password", "");
		_dataSource = BasicDataSourceFactory.createDataSource(ps);
	}

	public void destroy() throws Exception
	{
		_dataSource = null;
	}

	public Connection getConnection() throws SQLException
	{
		return _dataSource.getConnection();
	}

	public Connection getConnection(String arg0, String arg1)
			throws SQLException
	{
		return _dataSource.getConnection(arg0, arg1);
	}

	public int getLoginTimeout() throws SQLException
	{
		return _dataSource.getLoginTimeout();
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException
	{
		return _dataSource.getLogWriter();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return false;
	}

	public void setDatabasePath(File dbPath) throws Exception
	{
		_dbPath = dbPath;
	}

	public void setLoginTimeout(int arg0) throws SQLException
	{
		_dataSource.setLoginTimeout(arg0);
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException
	{
		_dataSource.setLogWriter(arg0);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		return null;
	}
}
