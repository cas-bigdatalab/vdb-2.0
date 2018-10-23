/*
 * Created on 2005-12-11
 */
package cn.csdb.commons.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author bluejoe
 */
public class PropertyGetter
{
	private Method _method;

	private String _propertyName;

	public PropertyGetter(Method method)
	{
		_method = method;
	}

	public PropertyGetter(Method method, String propertyName)
	{
		_method = method;
		_propertyName = propertyName;
	}

	public Object doGet(Object o) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException
	{
		if (_propertyName != null)
			return _method.invoke(o, new Object[] { _propertyName });

		return _method.invoke(o, new Object[] {});
	}

	public Method getMethod()
	{
		return _method;
	}
}
