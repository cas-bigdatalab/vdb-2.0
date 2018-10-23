package vdb.mydb.engine;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import vdb.mydb.files.FileManager;
import vdb.mydb.index.VdbIndexer;
import vdb.mydb.jdbc.DataSourceDetail;
import vdb.mydb.job.VdbJobScheduler;
import vdb.mydb.module.ModuleManager;
import vdb.mydb.module.VdbModule;
import vdb.mydb.security.VdbSecurityManager;
import vdb.mydb.security.jdbc.JdbcUserDetailsService;
import vdb.mydb.theme.ThemeManager;
import vdb.mydb.types.IdGenerators;
import vdb.mydb.types.JdbcProducts;
import vdb.mydb.types.TypeManager;
import vdb.mydb.types.ViewTypeManager;
import vdb.mydb.vtl.VtlEngine;
import vdb.report.resstat.dbstats.strategy.StatisticStrategyManager;
import vdb.service.httpservice.HttpServiceVerbManager;

public class VdbApplicationContext implements ApplicationContextAware
{
	private ApplicationContext _applicationContext;

	public DataSource getAuthDataSource()
	{
		return (DataSource) _applicationContext.getBean("authSource");
	}

	public <T> T getBean(String name)
	{
		return (T) _applicationContext.getBean(name);
	}

	/**
	 * for VTL
	 */
	public <T> T get(String name)
	{
		return (T) _applicationContext.getBean(name);
	}

	public DataSourceDetail getDataSetFilesSourceDetail()
	{
		return getBean("dataSetFilesDataSourceDetail");
	}

	public DataSourceDetail getDataSetLogsSourceDetail()
	{
		return getBean("dataSetLogsDataSourceDetail");
	}

	public DataSourceDetail getDataSetReportsSourceDetail()
	{
		return getBean("dataSetReportDataSourceDetail");
	}

	public FileManager getFileManager()
	{
		return getBean("fileManager");
	}

	public HttpServiceVerbManager getHttpServiceVerbManager()
	{
		return getBean("httpServiceVerbManager");
	}

	public IdGenerators getIdGenerators()
	{
		return getBean("idGenerators");
	}

	public VdbIndexer getIndexer()
	{
		return getBean("indexer");
	}

	public JdbcProducts getJdbcProducts()
	{
		return getBean("jdbcProducts");
	}

	public VdbJobScheduler getJobScheduler()
	{
		return getBean("jobScheduler");
	}

	public DataSourceDetail getLogsSourceDetail()
	{
		return getBean("domainLogsDataSourceDetail");
	}

	public ModuleManager getModuleManager()
	{
		return getBean("moduleManager");
	}

	public List<VdbModule> getModules()
	{
		return getModuleManager().getModules();
	}

	public VdbSecurityManager getSecurityManager()
	{
		return getBean("securityManager");
	}

	public ThemeManager getThemeManager()
	{
		return getBean("themeManager");
	}

	public TypeManager getTypeManager()
	{
		return getBean("typeManager");
	}

	public DataSource getUserDataSource()
	{
		return getBean("userSource");
	}

	public JdbcUserDetailsService getJdbcUserDetailsService()
	{
		return getBean("userDetailsService");
	}

	public VtlEngine getVelocityEngine()
	{
		return getBean("velocityEngine");
	}

	public ViewTypeManager getViewTypeManager()
	{
		return getBean("viewTypeManager");
	}

	public StatisticStrategyManager getStatisticStrategyManager()
	{
		return getBean("statisticStrategyManager");
	}
	
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		_applicationContext = arg0;
	}

	public ApplicationContext getApplicationContext()
	{
		return _applicationContext;
	}
}
