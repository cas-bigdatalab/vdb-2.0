package vdb.metacat.fs;

public class UnknownCatalogObjectType extends Exception
{
	public UnknownCatalogObjectType(String typeName)
	{
		super();
		_typeName = typeName;
	}

	private String _typeName;

	public String getMessage()
	{
		return String.format("don't know how to create instance of type `%s`",
				_typeName);
	}

}
