package vdb.metacat.fs;

import vdb.metacat.CatalogObject;
import vdb.metacat.PlainObject;
import vdb.metacat.meta.MetaData;
import cn.csdb.commons.util.StringKeyMap;

public class CatalogObjectImpl implements CatalogObject, PlainObject
{
	protected MetaData _meta = new MetaData(new StringKeyMap<String>());

	/**
	 * almighty getter method!
	 * 
	 * @param name
	 * @return
	 */
	public String get(String name)
	{
		return _meta.get(name);
	}

	public String getDescription()
	{
		return get("description");
	}

	public String getId()
	{
		return _meta.getId();
	}

	public String getLastModified()
	{
		return _meta.getLastModified();
	}

	public String getName()
	{
		return get("name");
	}

	public String getTitle()
	{
		return get("title");
	}

	public String getUri()
	{
		return getName();
	}

	public MetaData meta()
	{
		return _meta;
	}

	public void set(String name, String value)
	{
		_meta.set(name, value);
	}

	public void setName(String name)
	{
		_meta.set("name", name);
	}

	public void setTitle(String title)
	{
		_meta.set("title", title);
	}

	public void setUri(String uri)
	{
		_meta.set("uri", uri);
	}

	@Override
	public String toString()
	{
		return _meta.toString();
	}
}
