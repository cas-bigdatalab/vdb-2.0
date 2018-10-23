package vdb.mydb.rpc.simplex;

public interface SimpleDataSet
{

	public void setUri(String uri);

	public void setId(String id);

	public void setDescription(String description);

	public void setName(String name);

	public String getUri();

	public String getId();

	public String getDescription();

	public String getName();

	public String getTitle();

	public void setTitle(String title);

	public String getLastModified();

	public void setLastModified(String lastModified);

	public String getDataBaseType();

	public void setDataBaseType(String dataBaseType);

	public boolean isConnected();

	public void setConnected(boolean isConnected);

}
