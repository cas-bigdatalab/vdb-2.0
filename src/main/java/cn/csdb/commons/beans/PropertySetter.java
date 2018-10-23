/*
 * Created on 2005-12-11
 */
package cn.csdb.commons.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.csdb.commons.util.ObjectHelper;

/**
 * @author bluejoe
 */
public class PropertySetter
{
	private Method _method;

	private String _propertyName;

	public PropertySetter(Method method)
	{
		_method = method;
	}

	public PropertySetter(Method method, String propertyName)
	{
		_method = method;
		_propertyName = propertyName;
	}

	public void doSet(Object o, Object propertyValue)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException
	{
		if (_propertyName != null)
		{
			_method.invoke(o, new Object[] { _propertyName, propertyValue });
			return;
		}

		Class type = _method.getParameterTypes()[0];
		String typeName = type.getName();
		ObjectHelper vh = new ObjectHelper(propertyValue);

		if (typeName.equals("char") || type == Character.class)
		{
			_method.invoke(o, new Object[] { new Character(vh
					.evalCharacter('\0')) });
			return;
		}

		if (typeName.equals("boolean") || typeName.equals("java.lang.Boolean"))
		{
			_method.invoke(o,
					new Object[] { new Boolean(vh.evalBoolean(false)) });
			return;
		}

		if (typeName.equals("int") || typeName.equals("java.lang.Integer"))
		{
			_method.invoke(o, new Object[] { new Integer(vh.evalInteger()) });
			return;
		}

		if (typeName.equals("long") || typeName.equals("java.lang.Long"))
		{
			_method.invoke(o, new Object[] { new Long(vh.evalLong()) });
			return;
		}

		if (typeName.equals("double") || typeName.equals("java.lang.Double"))
		{
			_method.invoke(o, new Object[] { new Double(vh.evalDouble()) });
			return;
		}

		if (String.class.isAssignableFrom(type))
		{
			_method.invoke(o, new Object[] { vh.evalString() });
			return;
		}

		_method.invoke(o, new Object[] { vh.evalObject() });
	}

	public Method getMethod()
	{
		return _method;
	}
}
