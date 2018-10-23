package vdb.metacat.fs;

import java.util.Map;

import vdb.metacat.Catalog;
import vdb.metacat.CatalogObject;
import cn.csdb.commons.util.StringKeyMap;

public class CatalogImpl implements Catalog
{
	private Map<String, CatalogObject> _idObjects = new StringKeyMap<CatalogObject>();

	private Map<String, CatalogObject> _uriObjects = new StringKeyMap<CatalogObject>();

	public void cacheIn(CatalogObject object)
	{
		_idObjects.put(object.getId(), object);
		_uriObjects.put(object.getUri(), object);
	}

	public void cacheOut(CatalogObject object)
	{
		_idObjects.remove(object.getId());
		_uriObjects.remove(object.getUri());
	}

	public void clear()
	{
		_idObjects.clear();
		_uriObjects.clear();
	}

	public <T extends CatalogObject> T fromId(String id)
	{
		return (T) _idObjects.get(id);
	}

	public <T extends CatalogObject> T fromUri(String uri)
	{
		return (T) _uriObjects.get(uri);
	}
}
