/*
 * 创建日期 2005-12-5
 */
package cn.csdb.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author bluejoe
 */
public class ClassUtils
{
	public static Object invokeDeclaredMethod(Object o, String methodName,
			Object[] parameters) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException
	{
		return invokeMethod(o, methodName, parameters, true);
	}

	public static Object invokeMethod(Object o, String methodName,
			Object[] parameters) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException
	{
		return invokeMethod(o, methodName, parameters, false);
	}

	private static Object invokeMethod(Object o, String methodName,
			Object[] parameters, boolean declared) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException
	{
		Class[] parameterTypes;
		if (parameters == null)
		{
			parameterTypes = new Class[0];
		}
		else
		{
			parameterTypes = new Class[parameters.length];
			for (int i = 0; i < parameters.length; i++)
			{
				parameterTypes[i] = parameters[i].getClass();
			}
		}

		Method m = declared ? o.getClass().getDeclaredMethod(methodName,
				parameterTypes) : o.getClass().getMethod(methodName,
				parameterTypes);
		m.setAccessible(true);
		return m.invoke(o, parameters);
	}

	public static Object newDeclaredInstance(Class theClass, Object[] parameters)
			throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		return newInstance(theClass, parameters, true);
	}

	public static Object newInstance(Class theClass, Object[] parameters)
			throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		return newInstance(theClass, parameters, false);
	}

	public static Object newInstance(Class theClass, Object[] parameters,
			boolean declared) throws IllegalArgumentException,
			SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException
	{
		Class[] parameterTypes;
		if (parameters == null)
		{
			parameterTypes = new Class[0];
		}
		else
		{
			parameterTypes = new Class[parameters.length];
			for (int i = 0; i < parameters.length; i++)
			{
				parameterTypes[i] = parameters[i].getClass();
			}
		}

		Constructor c = declared ? theClass
				.getDeclaredConstructor(parameterTypes) : theClass
				.getConstructor(parameterTypes);
		c.setAccessible(true);

		return c.newInstance(parameters);
	}
}
