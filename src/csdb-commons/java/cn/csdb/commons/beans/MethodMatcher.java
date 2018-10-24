/*
 * Created on 2006-1-3
 */
package cn.csdb.commons.beans;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author bluejoe
 */
public class MethodMatcher
{
	private static Pattern _pattern = Pattern.compile("[^a-zA-Z0-9]");

	private String getNormalizedPropertyName(String propertyName)
	{
		return _pattern.matcher(propertyName).replaceAll("");
	}

	public boolean isGetter(String propertyName, Method method)
	{
		Class[] cs = method.getParameterTypes();
		if (cs.length == 0)
		{
			String npn = getNormalizedPropertyName(propertyName);
			String mn = method.getName();

			// isXXX
			if (mn.equalsIgnoreCase("is" + npn)
					&& method.getReturnType().equals(boolean.class))
			{
				return true;
			}

			// getXXX
			if (mn.equalsIgnoreCase("get" + npn))
			{
				return true;
			}
		}

		return false;
	}

	// Object get(String name)
	public boolean isMapGetter(Method method)
	{
		Class[] cs = method.getParameterTypes();
		if (cs.length == 1)
		{
			String mn = method.getName();

			if (mn.equalsIgnoreCase("get") && cs[0] == String.class
					&& method.getReturnType().equals(Object.class))
			{
				return true;
			}
		}

		return false;
	}

	// Object set(String name, Object value)
	public boolean isMapSetter(Method method)
	{
		Class[] cs = method.getParameterTypes();
		if (cs.length == 2)
		{
			String mn = method.getName();

			if ((mn.equalsIgnoreCase("set") || mn.equalsIgnoreCase("put"))
					&& cs[0] == String.class && cs[1] == Object.class)
			{
				return true;
			}
		}

		return false;
	}

	public boolean isSetter(String propertyName, Method method)
	{
		Class[] cs = method.getParameterTypes();
		Class rt = method.getReturnType();
		if (rt.equals(void.class) && cs.length == 1)
		{
			String npn = getNormalizedPropertyName(propertyName);
			String mn = method.getName();

			// setXXX
			if (mn.equalsIgnoreCase("set" + npn))
			{
				return true;
			}
		}

		return false;
	}
}
