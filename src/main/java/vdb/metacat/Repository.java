package vdb.metacat;

public interface Repository extends CatalogObject
{
	public String getDatabaseName();

	DataSet getDataSet();

	public String getHost();

	public int getLoginTimeout();

	public String getPort();

	public String getProductName();

	public String getProductVersion();

	public String getUserName();

	public String getUserPass();

	void setDataSet(DataSet dataSet);

	void setHost(String host);

	public String getLocalFilePath();
}
