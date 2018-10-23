package vdb.mydb.rpc.simplex;

public interface SimpleField
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

	public String getType();

	public void setType(String type);

	public String getEntityName();

	public void setEntityName(String entityName);
}
