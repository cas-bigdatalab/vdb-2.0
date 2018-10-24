/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.sql.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.text.MessageFormat;
import java.util.logging.Logger;

import javax.sql.DataSource;

import cn.csdb.commons.pool.Pool;
import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.pool.PoolProperties;
import cn.csdb.commons.sql.DataSourceNotFound;
import cn.csdb.commons.sql.JdbcManager;

/**
 * ���������ӳص����á� ����ref-nameֵ����ָ�������õ����ӳ�������jndi�����е����ӳ�����
 * 
 * @author bluejoe
 */
public class AliasPool implements Pool, DataSource
{
	private PoolProperties _pp;

	private DataSource _dataSource;

	public void ping() throws Exception
	{
	}

	public void destroy() throws Exception
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.pool.Pool#getProperties()
	 */
	public PoolProperties getProperties()
	{
		return _pp;
	}

	public void init(PoolProperties pp) throws Exception
	{
		_pp = pp;
		String refName = pp.getProperty("ref-name");

		if (refName == null)
		{
			String msg = MessageFormat.format(
					"[{0}]: ref-name of DataSource was empty",
					new Object[] { pp.getName() });
			PoolManager.getInstance().getLogger().error(msg);
			throw new Exception(msg);
		}

		_dataSource = JdbcManager.getInstance().lookupDataSource(refName);
		if (_dataSource == null)
			throw new DataSourceNotFound(refName);

		JdbcManager.getInstance().getJdbcSource(this);
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

	public void setLoginTimeout(int arg0) throws SQLException
	{
		_dataSource.setLoginTimeout(arg0);
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException
	{
		_dataSource.setLogWriter(arg0);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}
