package vdb.metacat.fs;

import vdb.metacat.DataSet;
import vdb.metacat.Repository;

public class RepositoryImpl extends CatalogObjectImpl implements Repository
{
	private DataSet _dataSet;

	public String getDatabaseName()
	{
		return get("databaseName");
	}

	public DataSet getDataSet()
	{
		return _dataSet;
	}

	public String getHost()
	{
		return get("host");
	}

	public int getLoginTimeout()
	{
		return _meta.getNumber("loginTimeout").intValue();
	}

	public String getPort()
	{
		return get("port");
	}

	public String getProductName()
	{
		return get("productName");
	}

	public String getProductVersion()
	{
		return get("productVersion");
	}

	public String getUserName()
	{
		return get("userName");
	}

	public String getUserPass()
	{
		return get("userPass");
	}

	public void setDataSet(DataSet dataSet)
	{
		_dataSet = dataSet;
	}

	public void setHost(String host)
	{
		set("host", host);
	}

	public void setLoginTimeout(int loginTimeout)
	{
		_meta.setNumber("loginTimeout", loginTimeout);
	}

	public void setPort(String port)
	{
		set("port", port);
	}

	public void setProductName(String productName)
	{
		set("productName", productName);
	}

	public void setProductVersion(String productVersion)
	{
		set("productVersion", productVersion);
	}

	public void setUserName(String userName)
	{
		set("userName", userName);
	}

	public void setUserPass(String userPass)
	{
		set("userPass", userPass);
	}

	public void setLocalFilePath(String localFilePath)
	{
		set("localFilePath", localFilePath);
	}

	public String getLocalFilePath()
	{
		return get("localFilePath");
	}
}
