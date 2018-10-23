package vdb.mydb.rpc.simplex;

import java.io.Serializable;

public class SimpleDataSetImpl implements SimpleDataSet, Serializable
{

	private String uri;

	private String id;

	private String description;

	private String name;

	private String Title;

	private String lastModified;

	private String DataBaseType;

	private boolean isConnected;

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
		return Title;
	}

	public void setTitle(String title)
	{
		Title = title;
	}

	public String getLastModified()
	{
		return lastModified;
	}

	public void setLastModified(String lastModified)
	{
		this.lastModified = lastModified;
	}

	public String getDataBaseType()
	{
		return DataBaseType;
	}

	public void setDataBaseType(String dataBaseType)
	{
		DataBaseType = dataBaseType;
	}

	public boolean isConnected()
	{
		return isConnected;
	}

	public void setConnected(boolean isConnected)
	{
		this.isConnected = isConnected;
	}
}
