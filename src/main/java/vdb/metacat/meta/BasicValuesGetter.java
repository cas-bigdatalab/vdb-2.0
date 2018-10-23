package vdb.metacat.meta;

import java.util.HashMap;
import java.util.Map;

public class BasicValuesGetter<K, V> implements ValuesGetter<K, V>
{
	Map<K, ValueGetter<V>> _map = new HashMap<K, ValueGetter<V>>();

	public boolean contains(K key)
	{
		return _map.containsKey(key);
	}

	public V get(K key)
	{
		if (!contains(key))
			return null;

		ValueGetter<V> vg = _map.get(key);
		if (vg == null)
			return null;

		return vg.get();
	}

	public void set(K key, V value)
	{
		_map.put(key, new BasicValueGetter<V>(value));
	}

	public void set(K key, ValueGetter<V> value)
	{
		_map.put(key, value);
	}
}
