package cn.csdb.commons.sql.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.text.MessageFormat;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Level;

import cn.csdb.commons.pool.ObjectOwner;
import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.pool.PoolProperties;

public class ConnectionHolder implements ObjectOwner
{
	private long _maxWait;

	/**
	 * JDBC驱动程序
	 */
	private PoolProperties _pp;

	private Driver _jdbcDriver;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.sql.dbcpx.PoolListener#createObject(cn.csdb.commons.sql.dbcpx.ObjectPool)
	 */
	public Object createObject() throws Exception
	{
		String jdbcURL = _pp.getProperty("url");
		if (jdbcURL == null)
		{
			String msg = MessageFormat.format("[{0}]: JDBC url is empty!",
					new Object[] { _pp.getName() });
			PoolManager.getInstance().getLogger().error(msg);
			throw new Exception(msg);
		}

		Properties p = new Properties();
		p.putAll(_pp.getPropertiesMap());

		if (_maxWait > 0)
		{
			DriverManager.setLoginTimeout((int) _maxWait / 1000);
		}

		//WARN: 不要使用DriverManager.getConnection，会造成同步等待
		Connection con = _jdbcDriver.connect(jdbcURL.trim(), p);
		return con;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.sql.dbcpx.PoolListener#removeObject(cn.csdb.commons.sql.dbcpx.ObjectPool,
	 *      java.lang.Object)
	 */
	public void destroyObject(Object o) throws Exception
	{
		((Connection) o).close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.sql.dbcpx.PoolListener#init(cn.csdb.commons.sql.dbcpx.ObjectPool,
	 *      cn.csdb.commons.sql.dbcpx.PoolParameters)
	 */
	public void init(PoolProperties pp) throws Exception
	{
		_pp = pp;

		// 获取数据库类型
		String driverName = _pp.getProperty("driver");
		_jdbcDriver = (Driver) Class.forName(driverName.trim()).newInstance();
		if (_jdbcDriver == null)
		{
			String msg = MessageFormat.format(
					"[{0}]: JDBC driver name was empty", new Object[] { _pp
							.getName() });
			PoolManager.getInstance().getLogger().error(msg);
			throw new Exception(msg);
		}

		try
		{
			PoolManager
					.getInstance()
					.getLogger()
					.log(
							Level.ALL,
							MessageFormat
									.format(
											"[{0}]: JDBC driver `{1}` registered successfully.",
											new Object[] { _pp.getName(),
													driverName }));
		}
		catch (Exception e)
		{
			String msg = MessageFormat.format(
					"[{0}]: failed to register JDBC driver `{1}`: {2}",
					new Object[] { _pp.getName(), driverName, e.getMessage() });
			PoolManager.getInstance().getLogger().error(msg);
			throw new Exception(msg);
		}

		try
		{
			_maxWait = Long.parseLong(pp.getProperty(PoolProperties.MAX_WAIT));
		}
		catch (NumberFormatException e)
		{
			_maxWait = 2000;
		}

		// 注册
		try
		{
			Context env = (Context) new InitialContext()
					.lookup("java:comp/env");
			if (env != null)
			{
				try
				{
					env.createSubcontext("pool");
				}
				catch (Throwable e)
				{
				}
			}

			env.bind("jdbc/" + _pp.getName(), this);
		}
		catch (Throwable e)
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.sql.dbcpx.PoolListener#isObjectAvailable(java.lang.Object)
	 */
	public boolean isObjectAvailable(Object o) throws Exception
	{
		return !((Connection) o).isClosed();
	}
}