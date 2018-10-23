package vdb.mydb.security.impl;

import java.util.List;

import vdb.mydb.security.VdbRole;

public class BasicRole implements VdbRole
{
	private String name;

	private String title;

	private String resourceClassType;

	private List<String> operations;

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

	public List<String> getOperations()
	{
		return operations;
	}

	public void setOperations(List<String> operations)
	{
		this.operations = operations;
	}

	public String getResourceClassType()
	{
		return resourceClassType;
	}

	public void setResourceClassType(String resourceClassType)
	{
		this.resourceClassType = resourceClassType;
	}

}
