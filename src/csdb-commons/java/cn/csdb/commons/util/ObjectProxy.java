/*
 * Created on 2006-6-23
 */
package cn.csdb.commons.util;

/**
 * 主要用于final object的情况。
 * 
 * @author bluejoe
 */
public class ObjectProxy<T>
{
	private T _innerObject;

	public T getObject()
	{
		return _innerObject;
	}

	public ObjectProxy(T o)
	{
		_innerObject = o;
	}

	public ObjectProxy()
	{
		_innerObject = null;
	}

	public void setObject(T o)
	{
		_innerObject = o;
	}
}
