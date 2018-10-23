package vdb.mydb.metacat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import vdb.metacat.meta.BasicValuesGetter;
import vdb.metacat.meta.ValueGetter;
import vdb.metacat.meta.ValuesGetter;

public class Defaults
{
	Map<String, ValuesGetter<String, String>> _map;

	public ValuesGetter<String, String> getDefaultValues(String className)
	{
		return _map.get(className);
	}

	public Map<String, ValuesGetter<String, String>> getMap()
	{
		return _map;
	}

	public void setMap(Map<String, Map<String, String>> map)
	{
		_map = new HashMap<String, ValuesGetter<String, String>>();

		for (Entry me : map.entrySet())
		{
			Map<String, ?> defaults = (Map<String, ?>) me.getValue();
			BasicValuesGetter<String, String> newDefaults = new BasicValuesGetter<String, String>();

			_map.put((String) me.getKey(), newDefaults);

			for (Entry me2 : defaults.entrySet())
			{
				String key = (String) me2.getKey();
				Object value2 = me2.getValue();
				if (value2 instanceof ValueGetter)
				{
					newDefaults.set(key, (ValueGetter) value2);
				}
				else
				{
					newDefaults.set(key, (String) value2);
				}
			}
		}
	}

}
