/*
 * Created on 2008-1-31
 */
package cn.csdb.commons.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LazyList<T> implements Serializable, Iterable<T>, List<T>
{
	private int _cacheBeginning;

	private List<T> _cache;

	private int _cacheSize;

	private int _size;

	private LazyListListener<T> _listener;

	public LazyList(int cacheSize, LazyListListener<T> listener)
			throws Exception
	{
		_cacheBeginning = -1;
		_size = -1;
		_cacheSize = (cacheSize < 0 ? 0 : cacheSize);
		_listener = listener;

		// 不使用cache
		if (_cacheSize == 0)
		{
			_cacheBeginning = 0;
			_cache = new ArrayList<T>();
			_cache.addAll(_listener.offer());
			_size = _cache.size();
		}
	}

	public void readMore(int i) throws Exception
	{
		_cacheBeginning = i;
		_cache = new ArrayList<T>();

		// 使用cache
		if (_cacheSize > 0)
		{
			_cache.addAll(_listener.offer(i, _cacheSize));
		}
	}

	public T get(int i)
	{
		if (_cacheSize > 0
				&& (_cacheBeginning < 0 || i >= _cacheBeginning + _cacheSize || i < _cacheBeginning))
		{
			try
			{
				readMore((i / _cacheSize) * _cacheSize);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		return _cache.get(i - _cacheBeginning);

	}

	public Iterator<T> iterator()
	{
		return new LazyListIterator<T>(this);
	}

	public int size()
	{
		if (_size == -1)
		{
			try
			{
				_size = _listener.count();
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}

		return _size;
	}

	public boolean isEmpty()
	{
		return size() <= 0;
	}

	public boolean contains(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	public Object[] toArray()
	{
		throw new UnsupportedOperationException();
	}

	public Object[] toArray(Object[] arg0)
	{
		throw new UnsupportedOperationException();
	}

	public boolean add(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection arg0)
	{
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection arg0)
	{
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int arg0, Collection arg1)
	{
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection arg0)
	{
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection arg0)
	{
		throw new UnsupportedOperationException();
	}

	public void clear()
	{
		throw new UnsupportedOperationException();
	}

	public T set(int arg0, Object arg1)
	{
		throw new UnsupportedOperationException();
	}

	public void add(int arg0, Object arg1)
	{
		throw new UnsupportedOperationException();
	}

	public T remove(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	public int indexOf(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	public int lastIndexOf(Object arg0)
	{
		throw new UnsupportedOperationException();
	}

	public ListIterator listIterator()
	{
		throw new UnsupportedOperationException();
	}

	public ListIterator listIterator(int arg0)
	{
		throw new UnsupportedOperationException();
	}

	public List subList(int arg0, int arg1)
	{
		throw new UnsupportedOperationException();
	}
}
