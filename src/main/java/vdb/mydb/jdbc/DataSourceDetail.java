package vdb.mydb.jdbc;

import java.io.File;

public class DataSourceDetail
{
	private String _driverClassName;

	private String _firstTable;

	private String _password;

	private File _scriptFile;

	private String _url;

	private String _userName;

	public String getDriverClassName()
	{
		return _driverClassName;
	}

	public String getFirstTable()
	{
		return _firstTable;
	}

	public String getPassword()
	{
		return _password;
	}

	public File getScriptFile()
	{
		return _scriptFile;
	}

	public String getUrl()
	{
		return _url;
	}

	public String getUserName()
	{
		return _userName;
	}

	public void setDriverClassName(String driverClassName)
	{
		_driverClassName = driverClassName;
	}

	public void setFirstTable(String firstTable)
	{
		_firstTable = firstTable;
	}

	public void setPassword(String password)
	{
		_password = password;
	}

	public void setScriptFile(File scripts)
	{
		_scriptFile = scripts;
	}

	public void setUrl(String url)
	{
		_url = url;
	}

	public void setUserName(String username)
	{
		_userName = username;
	}
}
