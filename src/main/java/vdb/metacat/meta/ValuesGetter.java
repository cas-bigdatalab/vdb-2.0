package vdb.metacat.meta;

public interface ValuesGetter<K, V>
{
	public boolean contains(K key);

	public V get(K key);
}
