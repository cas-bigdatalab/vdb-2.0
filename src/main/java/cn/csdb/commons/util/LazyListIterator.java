/*
 * Created on 2008-1-31
 */
package cn.csdb.commons.util;

import java.util.Iterator;

public class LazyListIterator<T> implements Iterator<T>
{

	private LazyList<T> _list;

	private int _current;

	private int _size;

	public LazyListIterator(LazyList<T> list)
	{
		_list = list;
		_size = list.size();
		_current = -1;
	}

	public boolean hasNext()
	{
		return _current < _size - 1;
	}

	public T next()
	{
		_current++;
		return _list.get(_current);
	}

	public void remove()
	{
		throw new UnsupportedOperationException();
	}

}
