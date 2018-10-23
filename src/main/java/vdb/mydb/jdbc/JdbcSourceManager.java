package vdb.mydb.jdbc;

import java.io.IOException;
import java.util.Map;

import org.apache.velocity.context.Context;

import vdb.metacat.DataSet;
import vdb.metacat.Repository;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.types.JdbcProduct;
import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.JdbcManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.pool.JdbcPool;
import cn.csdb.commons.util.StringKeyMap;

/*
 * 
 * @author bluejoe 2008-6-18
 */
public class JdbcSourceManager
{
	public interface DbcpPropertiesSetter
	{
		void setDriverName(String driverName);

		void setLoginTimeout(int seconds);

		void setPassword(String password);

		void setProperties(Map<String, String> properties);

		void setUrl(String url);

		void setUserName(String userName);
	}

	private static JdbcSourceManager _instance = new JdbcSourceManager();

	public static JdbcSourceManager getInstance()
	{
		return _instance;
	}

	private JdbcSourceManager()
	{
	}

	private void createDataSource(String dsName, DataSet ds) throws Exception
	{
		Context context = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		context.put("dsRoot", ((VdbDataSet) ds).getDataSetXml().getParentFile()
				.getCanonicalPath());
		context.put("dataset", ds);

		final Map<String, String> ps = new StringKeyMap<String>();
		ps.put("name", getString(dsName, context));
		ps.put("class", JdbcPool.class.getName());

		setJdbcProperties(ds, new DbcpPropertiesSetter()
		{
			public void setDriverName(String driverName)
			{
				ps.put("driver", driverName);
			}

			public void setLoginTimeout(int seconds)
			{
				ps.put("connect-timeout", "" + seconds * 1000);
			}

			public void setPassword(String password)
			{
				ps.put("password", password);
			}

			public void setProperties(Map<String, String> properties)
			{
				ps.putAll(properties);
			}

			public void setUrl(String url)
			{
				ps.put("url", url);
			}

			public void setUserName(String userName)
			{
				ps.put("user", userName);
			}
		});

		PoolManager.getInstance().addPool(ps);
	}

	private void createJdbcSource(String dsName, DataSourceDetail dsd,
			Context ctx) throws Exception
	{
		final Map<String, String> ps = new StringKeyMap<String>();

		ps.put("name", dsName);
		ps.put("class", JdbcPool.class.getName());
		ps.put("user", VdbManager.getEngine().getVelocityEngine().evaluate(
				dsd.getUserName(), ctx));
		ps.put("password", VdbManager.getEngine().getVelocityEngine().evaluate(
				dsd.getPassword(), ctx));
		ps.put("driver", VdbManager.getEngine().getVelocityEngine().evaluate(
				dsd.getDriverClassName(), ctx));
		ps.put("url", VdbManager.getEngine().getVelocityEngine().evaluate(
				dsd.getUrl(), ctx));

		PoolManager.getInstance().addPool(ps);
	}

	public Context createRepositoryContext(DataSet ds) throws IOException
	{
		Context ctx = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		ctx.put("dataset", ds);
		ctx.put("dsRoot", ((VdbDataSet) ds).getDataSetXml().getParentFile()
				.getCanonicalPath());
		return ctx;
	}

	private String getDataSourceName(DataSet ds)
	{
		return "dataset." + ds.getId();
	}

	/**
	 * @deprecated
	 * @return
	 * @throws Exception
	 */
	public JdbcSource getExtJdbcSource() throws Exception
	{
		return getLogsJdbcSource();
	}

	/**
	 * retrieve JdbcSource for certain data set, according to its repository
	 * info.
	 */
	public JdbcSource getJdbcSource(DataSet ds)
	{
		try
		{
			String dsName = getDataSourceName(ds);
			if (JdbcManager.getInstance().ifExists(dsName))
				return JdbcManager.getInstance().getJdbcSource(dsName);

			createDataSource(dsName, ds);
			return JdbcManager.getInstance().getJdbcSource(dsName);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * retrieve extended JdbcSource for certain data set, which enables file
	 * management & accounting.
	 */
	public synchronized JdbcSource getJdbcSource(String dsName, DataSourceDetail dsd,
			Context ctx) throws Exception
	{
		// not built yet
		if (!JdbcManager.getInstance().ifExists(dsName))
		{
			createJdbcSource(dsName, dsd, ctx);
		}

		JdbcSource js = JdbcManager.getInstance().getJdbcSource(dsName);
		// is a new database?
		if (js.getJdbcCatalog().getDatabase().getTable(dsd.getFirstTable()) == null)
		{
			new ScriptExecutor(js, dsd.getScriptFile()).execute();

			JdbcManager.getInstance().invalidate(dsName);
			js = JdbcManager.getInstance().getJdbcSource(dsName);
		}

		return JdbcManager.getInstance().getJdbcSource(dsName);
	}

	public JdbcSource getLogsJdbcSource() throws Exception
	{
		Context ctx = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		return getJdbcSource("vdb.domain", VdbManager.getEngine()
				.getLogsSourceDetail(), ctx);
	}

	public JdbcSource getDsReportJdbcSource() throws Exception
	{
		Context ctx = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		return getJdbcSource("vdb.report", VdbManager.getEngine()
				.getDataSetReportsSourceDetail(), ctx);
	}

	public JdbcSource getFilesJdbcSource(DataSet ds) throws Exception
	{
		Context ctx = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		ctx.put("dataset", ds);
		return getJdbcSource("dataset." + ds.getId() + ".files", VdbManager
				.getEngine().getDataSetFilesSourceDetail(), ctx);
	}

	public JdbcSource getLogsJdbcSource(DataSet ds) throws Exception
	{
		Context ctx = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		ctx.put("dataset", ds);
		return getJdbcSource("dataset." + ds.getId() + ".logs", VdbManager
				.getEngine().getDataSetLogsSourceDetail(), ctx);
	}

	public JdbcSource getReportsJdbcSource(DataSet ds) throws Exception
	{
		Context ctx = VdbManager.getEngine().getVelocityEngine()
				.createContext();
		ctx.put("dataset", ds);
		return getJdbcSource("dataset." + ds.getId() + ".reports", VdbManager
				.getEngine().getDataSetReportsSourceDetail(), ctx);
	}

	private String getString(String src, Context ctx) throws Exception
	{
		return VdbManager.getEngine().getVelocityEngine().evaluate(src, ctx);
	}

	private String getUrl(JdbcProduct jp, String type, String host,
			String port, String sid, String userName, String password)
	{
		try
		{
			Context ctx = VdbManager.getEngine().getVelocityEngine()
					.createContext();
			ctx.put("host", host);
			ctx.put("port", port);
			ctx.put("databaseName", sid);
			return VdbManager.getEngine().getVelocityEngine().evaluate(
					jp.getUrl(), ctx);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void setJdbcProperties(DataSet ds, DbcpPropertiesSetter setter)
			throws Exception
	{
		Repository rep = ds.getRepository();
		Context ctx = createRepositoryContext(ds);
		setJdbcProperties(rep.getProductName(), rep.getHost(), rep.getPort(),
				rep.getDatabaseName(), rep.getUserName(), rep.getUserPass(),
				rep.getLoginTimeout(), setter, ctx);
	}

	private void setJdbcProperties(String type, String host, String port,
			String sid, String userName, String password, int loginTimeout,
			DbcpPropertiesSetter setter, Context ctx) throws Exception
	{
		JdbcProduct jp = VdbManager.getEngine().getJdbcProducts().getProduct(
				type);

		String url = getUrl(jp, type, host, port, getString(sid, ctx),
				userName, password);
		String driver = jp.getDriver();

		setter.setUrl(url);
		setter.setDriverName(driver);
		setter.setUserName(userName);
		setter.setPassword(password);
		setter.setLoginTimeout(loginTimeout);
		setter.setProperties(jp.getProperties());
	}

	public void unregisterJdbcSource(DataSet ds)
	{
		String dataSourceName = getDataSourceName(ds);
		try
		{
			PoolManager.getInstance().removePool(dataSourceName);
			JdbcManager.getInstance().invalidate(dataSourceName);
		}
		catch (Throwable e)
		{
		}
	}
}
