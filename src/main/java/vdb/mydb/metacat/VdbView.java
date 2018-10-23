package vdb.mydb.metacat;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.CatalogObject;
import vdb.metacat.Domain;
import vdb.metacat.View;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.metacat.fs.NullCatalogObjectException;

public class VdbView extends CatalogObjectImpl implements View
{
	private Domain _domain;

	List<CatalogObject> _items = new ArrayList<CatalogObject>();

	private CatalogObject _source;

	public VdbView()
	{

	}

	public void addItem(CatalogObject object)
	{
		if (object == null)
			throw new NullCatalogObjectException();

		_items.add(object);
	}

	public Domain getDomain()
	{
		return _domain;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbView getEx()
	{
		return this;
	}

	public CatalogObject[] getItems()
	{
		return _items.toArray(new CatalogObject[0]);
	}

	public <T> T[] getItems(T[] type)
	{
		return _items.toArray(type);
	}

	public CatalogObject getSource()
	{
		return _source;
	}

	public void removeItem(CatalogObject object)
	{
		if (object == null)
			throw new NullCatalogObjectException();

		_items.remove(object);
	}

	public void removeItems()
	{
		_items.clear();
	}

	public void setDomain(Domain domain)
	{
		_domain = domain;
	}

	public void setSource(CatalogObject source)
	{
		_source = source;
	}
}
