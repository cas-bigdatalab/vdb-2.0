package vdb.log.vo;

public class DataModifications
{
	private String entity;

	private String id;

	private String operation;

	public DataModifications()
	{
		super();
	}

	public DataModifications(String entityUri, String id, String operation)
	{
		super();
		this.entity = entityUri;
		this.id = id;
		this.operation = operation;
	}

	public String getEntity()
	{
		return entity;
	}

	public void setEntity(String entityUri)
	{
		this.entity = entityUri;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}
}
