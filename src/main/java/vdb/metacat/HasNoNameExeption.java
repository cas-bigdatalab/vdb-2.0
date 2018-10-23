package vdb.metacat;

public class HasNoNameExeption extends RuntimeException
{
	private CatalogObject _src;

	public HasNoNameExeption(CatalogObject src)
	{
		_src = src;
	}

	@Override
	public String getMessage()
	{
		return String.format("no name assigned to %s", _src);
	}
}
