package vdb.mydb.rpc.simplex;

import java.io.Serializable;

public class SimpleEntityImpl implements SimpleEntity, Serializable
{

	private String uri;

	private String id;

	private String description;

	private String name;

	private String identifier;

	private String tableName;

	private String title;

	private String titleField;

	private String dataSetName;

	public String getUri()
	{
		return uri;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitleField()
	{
		return titleField;
	}

	public void setTitleField(String titleField)
	{
		this.titleField = titleField;
	}

	public String getIdentifier()
	{
		return identifier;
	}

	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public String getDataSetName()
	{
		return dataSetName;
	}

	public void setDataSetName(String dataSetName)
	{
		this.dataSetName = dataSetName;
	}
}
