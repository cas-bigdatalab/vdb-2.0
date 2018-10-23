package vdb.mydb.engine;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.ServletContext;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.ctx.CatalogContext;
import vdb.metacat.fs.CatalogImpl;
import vdb.metacat.fs.CatalogManager;
import vdb.mydb.ExternalLoader;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.Defaults;
import vdb.mydb.metacat.VdbCatalogContext;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.theme.Theme;
import vdb.tool.webpub.PublisherTool;
import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.JdbcManager;
import cn.csdb.commons.sql.JdbcSource;

public class VdbEngine extends VdbApplicationContext implements
		ServletContextAware, InitializingBean
{
	VdbEngineServer _engineServer;

	CacheManager _cacheManager;

	File _cacheXml;

	private Catalog _catalog;

	private CatalogContext _catalogContext;

	private CatalogManager _catalogManager;

	private VdbDomain _domain;

	Defaults _metaDefaults;

	File _modelsDir;

	File _poolXml;

	String _productName;

	String _productVersion;

	String _responseEncoding = "utf-8";

	ServletContext _servletContext;

	Calendar _startupTime;

	boolean _usingDaoCache;

	File _vdbRootDir;

	private Locale _locale;

	public VdbEngine()
	{
		VdbManager.setEngine(this);
	}

	public void afterPropertiesSet() throws Exception
	{
		setStartupTime(Calendar.getInstance());
		_servletContext.setAttribute(Locale.class.getName(), _locale);

		// pool
		PoolManager.getInstance().loadFromXML(_poolXml.getCanonicalPath());

		// ehcache
		_cacheManager = new CacheManager(_cacheXml.getCanonicalPath());

		// catalog
		initCatalog(_modelsDir, _metaDefaults);

		// engine server
		if (_engineServer != null)
			_engineServer.start();
	}

	public File getApplicationRoot()
	{
		return new File(getRealPath("/"));
	}

	public JdbcSource getAuthJdbcSource() throws Exception
	{
		return JdbcManager.getInstance().getJdbcSource(getAuthDataSource());
	}

	public synchronized Cache getCache(String cacheName) throws CacheException
	{
		Cache cache;
		if (!_cacheManager.cacheExists(cacheName))
		{
			cache = new Cache(cacheName, 100, false, false, 600, 300);
			_cacheManager.addCache(cache);
		}
		else
		{
			cache = _cacheManager.getCache(cacheName);
		}

		return cache;
	}

	public <T extends Serializable> T getCachedObject(String cacheName,
			Serializable key, ExternalLoader loader) throws CacheException
	{
		Cache cache = getCache(cacheName);
		Element e = cache.get(key);
		if (e == null)
		{
			e = new Element(key, loader.load(key));
			cache.put(e);
		}

		return (T) e.getValue();
	}

	public Catalog getCatalog()
	{
		return _catalog;
	}

	public CatalogContext getCatalogContext()
	{
		return _catalogContext;
	}

	public CatalogManager getCatalogManager()
	{
		return _catalogManager;
	}

	public File getCatalogRoot()
	{
		return _modelsDir;
	}

	public File getDataSetRoot(DataSet dataset)
	{
		return new File(_modelsDir, dataset.getUri());
	}

	public VdbDomain getDomain()
	{
		return _domain;
	}

	public String getProductName()
	{
		return _productName;
	}

	public String getProductVersion()
	{
		return _productVersion;
	}

	public String getRealPath(String path)
	{
		return _servletContext.getRealPath(path);
	}

	public String getResponseEncoding()
	{
		return _responseEncoding;
	}

	public String getRootPath()
	{
		return getRealPath("");
	}

	public ServletContext getServletContext()
	{
		return _servletContext;
	}

	public Calendar getStartupTime()
	{
		return _startupTime;
	}

	public Theme getTheme()
	{
		return getThemeManager().getTheme(_domain.getThemeName());
	}

	public JdbcSource getUserJdbcSource() throws Exception
	{
		return JdbcManager.getInstance().getJdbcSource(getUserDataSource());
	}

	public File getVdbRootDir()
	{
		return _vdbRootDir;
	}

	private void initCatalog(File catalogRoot, Defaults defaults)
			throws Exception
	{
		VdbCatalogContext context = new VdbCatalogContext(this);
		context.setDefaults(defaults);

		_catalog = new CatalogImpl();
		_catalogManager = new CatalogManager(catalogRoot, _catalog, context);
		_catalogContext = context;

		VdbDomain domain = (VdbDomain) _catalogManager.loadDomain();
		_domain = domain;
	}

	public boolean isUsingDaoCache()
	{
		return _usingDaoCache;
	}

	public void setCacheXml(File cacheXml)
	{
		_cacheXml = cacheXml;
	}

	public void setLocale(Locale locale)
	{
		_locale = locale;
	}

	public void setMetaDefaults(Defaults metaDefaults)
	{
		_metaDefaults = metaDefaults;
	}

	public void setModelsDir(File modelsDir)
	{
		_modelsDir = modelsDir;
	}

	public void setPoolXml(File poolXml)
	{
		_poolXml = poolXml;
	}

	public void setProductName(String productName)
	{
		_productName = productName;
	}

	public void setProductVersion(String productVersion)
	{
		_productVersion = productVersion;
	}

	public void setResponseEncoding(String responseEncoding)
	{
		_responseEncoding = responseEncoding;
	}

	public void setServletContext(ServletContext servletContext)
	{
		_servletContext = servletContext;
	}

	private void setStartupTime(Calendar startupTime)
	{
		_startupTime = startupTime;
	}

	public void setUsingDaoCache(boolean usingDaoCache)
	{
		_usingDaoCache = usingDaoCache;
	}

	public void setVdbRootDir(File vdbRootDir)
	{
		_vdbRootDir = vdbRootDir;
	}

	/**
	 * @deprecated
	 */
	public String getWebpub()
	{
		return new PublisherTool().getVdbPublisher().getWebpub();
	}

	public VdbEngineServer getEngineServer()
	{
		return _engineServer;
	}

	public void setEngineServer(VdbEngineServer engineServer)
	{
		_engineServer = engineServer;
	}
}
