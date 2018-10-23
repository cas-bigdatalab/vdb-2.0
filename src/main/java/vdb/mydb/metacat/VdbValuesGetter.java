package vdb.mydb.metacat;

import java.util.Map;

import vdb.metacat.meta.ValueGetter;

public class VdbValuesGetter
{
	private Map<String, ValueGetter> _map;

	public Map<String, ValueGetter> getMap()
	{
		return _map;
	}

	public void setMap(Map<String, ValueGetter> map)
	{
		_map = map;
	}
}
