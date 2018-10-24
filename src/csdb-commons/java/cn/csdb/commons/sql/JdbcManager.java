/*
 * Created on 2005-8-21
 */
package cn.csdb.commons.sql;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.jdbc.impl.JdbcSourceImpl;

/*
 * JdbcManager is a manager which manages DataSource resources and provides
 * SQLSource wrappers. <p> in general, u may define a DataSource in JNDI
 * context, eg. in resin, u may define a `<resource-ref>` tag in the `web.xml`.
 * <p> DataSource may be defined as a DBPool in your `pool.xml`, too. in fact,
 * `DBPool` implements `DataSource` interface, so every DBPool is a DataSource.
 * <p> every DataSource should have its own name, e.g `jdbc/db` in `web.xml`, or
 * `test1` in `pool.xml`. just call JdbcManager.lookup(dataSourceName) to
 * retrieve it. <p> SQLSource is a wrapper of DataSource, u may call
 * JdbcManager.getJdbcSource(dataSource) to retrieve a JdbcSource of given
 * DataSource. <p>
 * 
 * @author bluejoe
 */
public class JdbcManager
{
	private static JdbcManager _instance;

	private Map<DataSource, JdbcSource> _jdbcSources = new HashMap<DataSource, JdbcSource>();

	private JdbcManager()
	{
	}

	/**
	 * 获取唯一实例。
	 * 
	 * @return
	 */
	static public JdbcManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new JdbcManager();
		}

		return _instance;
	}

	/**
	 * 根据数据源获取JdbcSource对象。
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	public JdbcSource getJdbcSource(DataSource dataSource) throws Exception
	{
		if (dataSource == null)
			return null;

		if (_jdbcSources.containsKey(dataSource))
		{
			return (JdbcSource) _jdbcSources.get(dataSource);
		}

		JdbcSourceImpl JdbcSource = new JdbcSourceImpl(dataSource);
		_jdbcSources.put(dataSource, JdbcSource);

		return JdbcSource;
	}

	/**
	 * 根据连接池名字获取JdbcSource对象。
	 * 
	 * dataSourceName为DBPool或者servlet环境中连接池的名字
	 */
	public JdbcSource getJdbcSource(String dataSourceName) throws Exception
	{
		DataSource ds = lookupDataSource(dataSourceName);
		if (ds == null)
			throw new DataSourceNotFound(dataSourceName);

		return getJdbcSource(ds);
	}

	public boolean ifExists(String dataSourceName) throws Exception
	{
		return lookupDataSource(dataSourceName) != null;
	}

	/**
	 * 根据连接池名字获取数据源
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public DataSource lookupDataSource(String dataSourceName)
	{
		// 先从pool里面获取
		DataSource ds = (DataSource) PoolManager.getInstance().getPool(
				dataSourceName);

		if (ds == null)
		{
			// 从context获取？
			try
			{
				Context env = (Context) new InitialContext()
						.lookup("java:comp/env");
				ds = (DataSource) env.lookup(dataSourceName);
			}
			catch (Exception e)
			{
			}
		}

		return ds;
	}

	/**
	 * 标志某个数据源的过期，这将引起JdbcSource重新构造JdbcSource对象。
	 * 
	 * @param dataSource
	 */
	public void invalidate(DataSource dataSource)
	{
		if (dataSource != null)
			_jdbcSources.remove(dataSource);
	}

	/**
	 * 标志某个数据源的过期
	 * 
	 * @param dataSourceName
	 */
	public void invalidate(String dataSourceName)
	{
		invalidate(lookupDataSource(dataSourceName));
	}
}
