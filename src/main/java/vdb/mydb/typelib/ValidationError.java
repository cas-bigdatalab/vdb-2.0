package vdb.mydb.typelib;

import vdb.metacat.CatalogObject;

public class ValidationError extends Exception
{
	private CatalogObject _item;

	public ValidationError(CatalogObject item, String error)
	{
		super(error);
		_item = item;
	}

	public CatalogObject getSource()
	{
		return _item;
	}
}
