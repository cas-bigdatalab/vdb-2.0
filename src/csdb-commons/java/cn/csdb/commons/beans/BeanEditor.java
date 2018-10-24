/*
 * Created on 2005-12-11
 */
package cn.csdb.commons.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.csdb.commons.util.StringKeyMap;

/**
 * @author bluejoe
 */
public class BeanEditor<T>
{
	private static Map<String, BeanEditor> _cachedEditors = new HashMap<String, BeanEditor>();

	private Class _beanClass;

	private Map _getters;

	private Method[] _methods;

	private MethodMatcher _methodMatcher;

	private Map _setters;

	private BeanEditor(Class beanClass)
	{
		_methodMatcher = new MethodMatcher();
		_setters = new StringKeyMap();
		_getters = new StringKeyMap();

		_beanClass = beanClass;
		_methods = _beanClass.getMethods();
	}

	public static BeanEditor getBeanEditor(Class clazz)
	{
		String key = clazz.getName();
		BeanEditor be = (BeanEditor) _cachedEditors.get(key);
		if (be == null)
		{
			be = new BeanEditor(clazz);
			_cachedEditors.put(key, be);
		}

		return be;
	}

	private void assertClass(Object o)
	{
		if (!_beanClass.isAssignableFrom(o.getClass()))
			throw new RuntimeException(
					MessageFormat
							.format(
									"mismatched bean class `{0}`, class `{1}` required",
									new Object[] { o.getClass().getName(),
											_beanClass.getName() }));
	}

	public void doBatchGet(Object o, Map<String, T> map, List<String> names)
			throws Exception
	{
		assertClass(o);

		for (String name : names)
		{
			T value = doGet(o, name);
			map.put(name, value);
		}
	}

	/**
	 * @param o
	 * @param properties
	 * @throws Exception
	 */
	public void doBatchSet(Object o, Map<String, T> properties)
			throws Exception
	{
		assertClass(o);

		// iterate all fields
		Iterator<Entry<String, T>> it = properties.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<String, T> me = (Entry<String, T>) it.next();
			String key = (String) me.getKey();
			T value = me.getValue();
			doSet(o, key, value);
		}
	}

	public T doGet(Object o, String propertyName) throws Exception
	{
		assertClass(o);

		PropertyGetter ps = getGetter(propertyName);
		if (ps != null)
		{
			try
			{
				return (T) ps.doGet(o);
			}
			catch (Exception e)
			{
				throw new RuntimeException(MessageFormat.format(
						"failed to get property `{0}` of `{1}`", new Object[] {
								propertyName, _beanClass.getName() }), e);
			}
		}

		return null;
	}

	public void doSet(Object o, String propertyName, T propertyValue)
			throws Exception
	{
		assertClass(o);

		PropertySetter propertySetter = getSetter(propertyName);
		if (propertySetter != null)
		{
			try
			{
				propertySetter.doSet(o, propertyValue);
			}
			catch (Exception e)
			{
				throw new RuntimeException(MessageFormat.format(
						"failed to set property `{0}` of `{1}`", new Object[] {
								propertyName, _beanClass.getName() }), e);
			}
		}
	}

	public PropertyGetter getGetter(String propertyName)
	{
		PropertyGetter propertyGetter = null;
		PropertyGetter mapPropertyGetter = null;

		if (!_getters.containsKey(propertyName))
		{
			for (int i = 0; i < _methods.length; i++)
			{
				Method method = _methods[i];

				if (Modifier.isPublic(method.getModifiers())
						&& Modifier.isPublic(method.getDeclaringClass()
								.getModifiers()))
				{
					if (_methodMatcher.isGetter(propertyName, method))
					{
						propertyGetter = new PropertyGetter(method);
						break;
					}

					// get(...)
					if (_methodMatcher.isMapGetter(method))
					{
						mapPropertyGetter = new PropertyGetter(method,
								propertyName);
					}
				}
			}

			// TODO
			if (propertyGetter == null)
			{
				propertyGetter = mapPropertyGetter;
			}

			_getters.put(propertyName, propertyGetter);
		}
		else
		{
			propertyGetter = (PropertyGetter) _getters.get(propertyName);
		}

		return propertyGetter;
	}

	public PropertySetter getSetter(String propertyName)
	{
		PropertySetter propertySetter = null;
		PropertySetter mapPropertySetter = null;

		if (!_setters.containsKey(propertyName))
		{
			for (int i = 0; i < _methods.length; i++)
			{
				Method method = _methods[i];

				if (Modifier.isPublic(method.getModifiers())
						&& Modifier.isPublic(method.getDeclaringClass()
								.getModifiers())
						&& _methodMatcher.isSetter(propertyName, method))
				{
					propertySetter = new PropertySetter(method);
					break;
				}

				// put(...) or set(...)
				if (_methodMatcher.isMapSetter(method))
				{
					mapPropertySetter = new PropertySetter(method, propertyName);
				}
			}

			if (propertySetter == null)
			{
				// TODO
				propertySetter = mapPropertySetter;
			}

			_setters.put(propertyName, propertySetter);
		}
		else
		{
			propertySetter = (PropertySetter) _setters.get(propertyName);
		}

		return propertySetter;
	}
}
