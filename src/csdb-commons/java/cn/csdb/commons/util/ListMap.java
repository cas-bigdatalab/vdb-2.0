/*
 * Created on 2008-1-15
 */
package cn.csdb.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * a ListMap is both a List and a Map
 * 
 * @author bluejoe
 */
public class ListMap<T1, T2> implements Serializable
{
	private List<T2> _list;

	private Map<T1, T2> _map;

	public ListMap()
	{
		this(new HashMap<T1, T2>());
	}

	public ListMap(List<T2> list, Map<T1, T2> map)
	{
		_map = map;
		_list = list;
	}

	public ListMap(Map<T1, T2> map)
	{
		this(new ArrayList<T2>(), map);
	}

	/**
	 * add a new entry, if the entry which has the same key exists, then replace
	 * it
	 * 
	 * @param key
	 * @param value
	 */
	public void add(T1 key, T2 value)
	{
		if (_map.containsKey(key))
		{
			T2 o = _map.get(key);
			_map.put(key, value);
			int i = _list.indexOf(o);
			if (i >= 0)
			{
				_list.set(i, value);
				return;
			}
		}

		_map.put(key, value);
		_list.add(value);
	}

	public void clear()
	{
		_list.clear();
		_map.clear();
	}

	public void insertBefore(T1 key, T2 value, T2 position)
	{
		_list.add(_list.indexOf(position), value);
		_map.put(key, value);
	}

	public List<T2> list()
	{
		return _list;
	}

	public void list(List<T2> list, Matcher<T2> matcher)
	{
		for (T2 t : _list)
		{
			if (matcher.matches(t))
				list.add(t);
		}
	}

	public Map<T1, T2> map()
	{
		return _map;
	}

	public T2 remove(T1 key)
	{
		T2 value = _map.remove(key);
		if (value != null)
		{
			_list.remove(value);
		}

		return value;
	}

	@Override
	public String toString()
	{
		return _map.toString();
	}
}
