/*
 * Created on 2005-4-2
 */
package cn.csdb.commons.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 忽略键名大小写的HashMap。 键名应该为String，否则将会调用toString转换成String。 不允许null键名。
 * 
 * @author bluejoe
 */
public class StringKeyMap<T> extends LinkedHashMap<String, T>
{
	public StringKeyMap()
	{
		super();
	}

	public StringKeyMap(Map map)
	{
		super(map);
	}

	public boolean containsKey(Object key)
	{
		if (key == null)
			return false;

		return lookupEntry(key.toString()) != null;
	}

	public T get(Object key)
	{
		if (key == null)
			return null;

		Entry<String, T> me = lookupEntry(key.toString());
		if (me == null)
			return null;

		return me.getValue();
	}

	private Entry<String, T> lookupEntry(String key)
	{
		// 逐个查找
		Iterator it = entrySet().iterator();
		while (it.hasNext())
		{
			Entry me = (Entry) it.next();
			Object k = me.getKey();
			if (k == key
					|| (k instanceof String && key.equalsIgnoreCase((String) k)))
			{
				return me;
			}
		}

		return null;
	}

	public T put(String key, T value)
	{
		if (key == null)
			return null;

		String sk = key.toString();
		Entry me = lookupEntry(sk);
		if (me != null)
		{
			remove(me.getKey());
		}

		return super.put(sk, value);
	}

	public T remove(String key)
	{
		if (key == null)
			return null;

		String sk = key.toString();
		Entry me = lookupEntry(sk);
		if (me != null)
		{
			return super.remove(me.getKey());
		}

		return null;
	}
}
