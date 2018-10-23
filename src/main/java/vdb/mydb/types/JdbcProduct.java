package vdb.mydb.types;

import java.util.Map;

import cn.csdb.commons.util.StringKeyMap;

public class JdbcProduct
{
	private String _databaseNameLabel;

	private String _defaultPort;

	private String _driver;

	private String _name;

	private Map<String, String> _properties = new StringKeyMap<String>();

	private String _title;

	private String _url;

	public String getDatabaseNameLabel()
	{
		return _databaseNameLabel;
	}

	public String getDefaultPort()
	{
		return _defaultPort;
	}

	public String getDriver()
	{
		return _driver;
	}

	public String getName()
	{
		return _name;
	}

	public Map<String, String> getProperties()
	{
		return _properties;
	}

	public String getTitle()
	{
		return _title;
	}

	public String getUrl()
	{
		return _url;
	}

	public boolean requiresHostPort()
	{
		return _url != null && _url.indexOf("$host") > 0;
	}

	public void setDatabaseNameLabel(String databaseNameLabel)
	{
		_databaseNameLabel = databaseNameLabel;
	}

	public void setDefaultPort(String defaultPort)
	{
		_defaultPort = defaultPort;
	}

	public void setDriver(String driver)
	{
		_driver = driver;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setProperties(Map<String, String> properties)
	{
		_properties = properties;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public void setUrl(String url)
	{
		_url = url;
	}
}
