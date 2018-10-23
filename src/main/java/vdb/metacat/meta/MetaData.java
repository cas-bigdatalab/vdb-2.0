package vdb.metacat.meta;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

import vdb.metacat.Catalog;
import vdb.metacat.CatalogObject;
import vdb.metacat.PlainObject;
import cn.csdb.commons.util.StringKeyMap;
import cn.csdb.commons.util.StringUtils;
import cn.csdb.commons.util.TimeUtils;

public class MetaData implements PlainObject, ValuesGetter<String, String>
{
	private ValuesGetter<String, String> _defaults = new BasicValuesGetter<String, String>();

	private Map<String, String> _map = new StringKeyMap<String>();

	public MetaData()
	{
	}

	public MetaData(Map<String, String> map)
	{
		_map = map;
	}

	public boolean contains(String key)
	{
		return _map.containsKey(key);
	}

	public String get(String name)
	{
		if (_map.containsKey(name))
			return _map.get(name);

		return _defaults == null ? null : _defaults.get(name);
	}

	public boolean getBoolean(String key)
	{
		try
		{
			return "true".equals(get(key).toString());
		}
		catch (Throwable e)
		{
			return false;
		}
	}

	public CatalogObject getCatalogObject(String name, Catalog catalog)
	{
		return catalog.fromUri(get(name));
	}

	public String getId()
	{
		String uuid = null;
		if (_map.containsKey("id"))
		{
			uuid = _map.get("id");
		}

		if (uuid == null || "".equals(uuid))
		{
			uuid = StringUtils.getGuid();
			_map.put("id", uuid);
		}

		return uuid;
	}

	public String getLastModified()
	{
		String lastModified = null;
		if (_map.containsKey("lastModified"))
		{
			lastModified = _map.get("lastModified");
		}

		if (lastModified == null || "".equals(lastModified))
		{
			lastModified = TimeUtils.getNowString();
			_map.put("lastModified", lastModified);
		}

		return lastModified;
	}

	public Number getNumber(String key)
	{
		try
		{
			return NumberFormat.getInstance().parse(get(key));
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	public Map<String, String> map()
	{
		return _map;
	}

	public void putAll(Map<String, String> map)
	{
		_map.putAll(map);
	}

	public void set(String name, String value)
	{
		_map.put(name, value);
	}

	public void setBoolean(String key, boolean value)
	{
		set(key, "" + value);
	}

	public void setDefaults(ValuesGetter<String, String> defaults)
	{
		_defaults = defaults;
	}

	public void setNumber(String key, Number value)
	{
		set(key, value == null ? null : value.toString());
	}

	@Override
	public String toString()
	{
		return _map.toString();
	}
}
