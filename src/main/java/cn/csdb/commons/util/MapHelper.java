/*
 * 创建日期 2005-4-26
 */
package cn.csdb.commons.util;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletRequest;

/**
 * Map操作类，提供更多的getter/setter。
 * 
 * @author bluejoe
 */
public class MapHelper
{
	private Map _map;

	public MapHelper(Map map)
	{
		_map = map;
	}

	public Object get(String key)
	{
		return _map.get(key);
	}

	public boolean getBitBoolean(String key)
	{
		Object value = get(key);

		if (value == null)
			return false;

		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue();

		return getInteger(key, 0) != 0;
	}

	public boolean getBoolean(String key)
	{
		Object value = get(key);

		if (value == null)
			return false;

		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue();

		boolean bValue = Boolean.valueOf(value.toString()).booleanValue();
		return bValue;
	}

	public double getDouble(String key)
	{
		return getDouble(key, 0);
	}

	public double getDouble(String key, float defaultValue)
	{
		try
		{
			Object value = get(key);

			if (value == null)
				return defaultValue;

			if (value instanceof Double)
				return ((Double) value).doubleValue();

			double dValue = Double.parseDouble(value.toString());
			return dValue;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	public boolean getFlag(String key, int bitMask)
	{
		int flag = getInteger(key, 0);
		return (flag & bitMask) != 0;
	}

	/**
	 * 获取float字段值，发生错误时返回0
	 * 
	 * @param key
	 */
	public float getFloat(String key)
	{
		return getFloat(key, 0);
	}

	/**
	 * 获取float字段值
	 * 
	 * @param key
	 * @param defaultValue
	 */
	public float getFloat(String key, float defaultValue)
	{
		try
		{
			Object value = get(key);

			if (value == null)
				return defaultValue;

			if (value instanceof Float)
				return ((Float) value).floatValue();

			float fValue = Float.parseFloat(value.toString());
			return fValue;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 获取整型字段值，发生错误时返回0
	 * 
	 * @param key
	 */
	public int getInteger(String key)
	{
		return getInteger(key, 0);
	}

	/**
	 * 获取整型字段值
	 * 
	 * @param key
	 * @param defaultValue
	 *            发生错误时返回的值
	 */
	public int getInteger(String key, int defaultValue)
	{
		try
		{
			Object value = get(key);

			if (value == null)
				return defaultValue;

			if (value instanceof Integer)
				return ((Integer) value).intValue();

			int iValue = Integer.parseInt(value.toString());
			return iValue;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	public long getLong(String key)
	{
		return getLong(key, 0);
	}

	public long getLong(String key, long defaultValue)
	{
		try
		{
			Object value = get(key);

			if (value == null)
				return defaultValue;

			if (value instanceof Long)
				return ((Long) value).longValue();

			long lValue = Long.parseLong(value.toString());
			return lValue;
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * 获取字符串类型的字段值，如果为值为空，返回空白字符串
	 * 
	 * @param key
	 */
	public String getNotNullString(String key)
	{
		return getString(key, "");
	}

	/**
	 * 获取字段值，返回字符串内容
	 * 
	 * @param key
	 */
	public String getString(String key)
	{
		if (key == null)
		{
			return null;
		}

		Object value = get(key);
		return value == null ? null : value.toString();
	}

	/**
	 * 获取字符串类型的字段值
	 * 
	 * @param key
	 * @param defaultValue
	 *            如果为值为空，返回的值
	 */
	public String getString(String key, String defaultValue)
	{
		String value = getString(key);

		if (value == null)
		{
			return defaultValue;
		}

		return value;
	}

	/**
	 * 设置一个标志字段的属性值
	 * 
	 * @param key
	 *            字段名
	 * @param op
	 *            操作属性
	 * @param add
	 *            是否增加/删除
	 */
	public void modifyFlag(String key, int bitMask, boolean add)
	{
		int flag = getInteger(key, 0);
		if (add)
		{
			flag |= bitMask;
		}
		else
		{
			flag &= (~bitMask);
		}

		_map.put(key, "" + flag);
	}

	public void set(Object key, Object value)
	{
		_map.put(key, value);
	}

	public void setBitBoolean(String key, boolean value)
	{
		_map.put(key, value ? "1" : "0");
	}

	public void setBoolean(String key, boolean value)
	{
		_map.put(key, new Boolean(value));
	}

	public void setDouble(String key, double value)
	{
		_map.put(key, new Double(value));
	}

	/**
	 * 设置float值
	 * 
	 * @param key
	 * @param value
	 */
	public void setFloat(String key, float value)
	{
		_map.put(key, new Float(value));
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setInteger(String key, int value)
	{
		_map.put(key, new Integer(value));
	}

	/**
	 * @param key
	 * @param value
	 */
	public void setLong(String key, long value)
	{
		_map.put(key, new Long(value));
	}

	public void setRequestParameter(String key, ServletRequest request)
	{
		String value = request.getParameter(key);
		_map.put(key, value);
	}

	public void setRequestParameters(ServletRequest request)
	{
		Enumeration keys = request.getParameterNames();
		while (keys.hasMoreElements())
		{
			String key = (String) keys.nextElement();
			String value = request.getParameter(key);
			_map.put(key, value);
		}
	}

	public Map getMap()
	{
		return _map;
	}
}