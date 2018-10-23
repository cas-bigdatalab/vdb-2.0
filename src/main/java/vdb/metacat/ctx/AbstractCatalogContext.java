package vdb.metacat.ctx;

import java.util.Map;
import java.util.Map.Entry;

import vdb.metacat.CatalogObject;
import vdb.metacat.fs.UnknownCatalogObjectType;
import cn.csdb.commons.util.StringKeyMap;

public abstract class AbstractCatalogContext implements CatalogContext
{
	private Map<String, Class<?>> _classMap = new StringKeyMap<Class<?>>();

	public <T extends CatalogObject> T create(String typeName) throws Exception
	{
		Class<?> clazz = _classMap.get(typeName);
		if (clazz == null)
			throw new UnknownCatalogObjectType(typeName);

		return (T) clazz.newInstance();
	}

	public void registerClass(String typeName, Class<?> clazz)
	{
		_classMap.put(typeName, clazz);
	}

	public void setMap(Map<String, String> map) throws ClassNotFoundException
	{
		for (Entry entry : map.entrySet())
		{
			registerClass((String) entry.getKey(), Class.forName((String) entry
					.getValue()));
		}
	}
}
