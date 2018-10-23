package vdb.mydb.rpc.simplex;

public interface SimpleEntity
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

	public String getTitleField();

	public void setTitleField(String titleField);

	public String getIdentifier();

	public void setIdentifier(String identifier);

	public String getTableName();

	public void setTableName(String tableName);

	public String getDataSetName();

	public void setDataSetName(String dataSetName);
}
