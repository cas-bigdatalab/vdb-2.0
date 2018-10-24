/*
 * 创建日期 2005-4-26
 */
package cn.csdb.commons.util;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 对象操作类，主要完成数据转换。
 * 
 * @author bluejoe
 */
public class ObjectHelper
{
	private Object _object;

	public ObjectHelper(Object object)
	{
		_object = object;
	}

	public boolean evalBoolean()
	{
		return evalBoolean(false);
	}

	public char evalCharacter(char defaultValue)
	{
		if (_object == null || _object.equals(""))
			return defaultValue;

		if (_object instanceof Character)
			return ((Character) _object).charValue();

		if (_object instanceof byte[])
			return (char) ((byte[]) _object)[0];

		if (_object instanceof String)
		{
			String so = (String) _object;
			return so.charAt(0);
		}

		return defaultValue;
	}

	public boolean evalBoolean(boolean defaultValue)
	{
		if (_object == null || _object.equals(""))
			return defaultValue;

		if (_object instanceof Boolean)
			return ((Boolean) _object).booleanValue();

		if (_object instanceof String)
		{
			String so = (String) _object;
			return defaultValue ? !("false".equalsIgnoreCase(so)) : "true"
					.equalsIgnoreCase(so);
		}

		return defaultValue;
	}

	public double evalDouble()
	{
		if (_object == null)
			return 0.0D;
		if (_object instanceof Number)
		{
			double dValue = ((Number) _object).doubleValue();
			if (Double.isNaN(dValue))
				return 0.0D;
			else
				return dValue;
		}
		if (_object.equals(""))
			return 0.0D;
		if (_object instanceof String)
		{
			double dValue = Double.parseDouble((String) _object);
			if (Double.isNaN(dValue))
				return 0.0D;
			else
				return dValue;
		}
		else
		{
			return 0.0D;
		}
	}

	public int evalInteger()
	{
		if (_object == null)
			return 0;
		if (_object instanceof Number)
			return ((Number) _object).intValue();
		if (_object.equals(""))
			return 0;
		if (_object instanceof String)
		{
			return Integer.parseInt((String) _object);
		}
		else
		{
			return 0;
		}
	}

	public long evalLong()
	{
		if (_object == null)
			return 0L;
		if (_object instanceof Number)
			return ((Number) _object).longValue();
		if (_object.equals(""))
			return 0L;
		if (_object instanceof String)
		{
			return Long.parseLong((String) _object);
		}
		else
		{
			return 0L;
		}
	}

	public Object evalObject()
	{
		return _object;
	}

	public String evalString()
	{
		if (_object == null)
			return null;

		if (_object instanceof byte[])
			return new String((byte[]) _object);

		return _object.toString();
	}

	public long evalPeriod() throws Exception
	{
		String value;

		if (_object == null)
			return 0;
		else if (_object instanceof Number)
			return ((Number) _object).longValue();
		else if (_object.equals(""))
			return 0;
		else if (_object instanceof String)
			value = (String) _object;
		else
		{
			return 0;
		}

		long sign = 1;
		long period = 0;

		int i = 0;
		int length = value.length();

		if (length > 0 && value.charAt(i) == '-')
		{
			sign = -1;
			i++;
		}

		while (i < length)
		{
			long delta = 0;
			char ch;

			for (; i < length && (ch = value.charAt(i)) >= '0' && ch <= '9'; i++)
				delta = 10 * delta + ch - '0';

			// XXX: need to change this
			if (length <= i)
				period += 1000 * delta;
			else
			{
				switch (value.charAt(i++))
				{
					case 's':
					case 'S':
						period += 1000 * delta;
						break;

					case 'm':
						period += 60000L * delta;
						break;

					case 'h':
					case 'H':
						period += 60L * 60000L * delta;
						break;

					case 'd':
					case 'D':
						period += 24L * 3600L * 1000L * delta;
						break;

					case 'w':
					case 'W':
						period += 7L * 24L * 3600L * 1000L * delta;
						break;

					case 'M':
						period += 30L * 24L * 3600L * 1000L * delta;
						break;

					case 'y':
					case 'Y':
						period += 365L * 24L * 3600L * 1000L * delta;
						break;

					default:
						throw new Exception(MessageFormat.format(
								"unknown character `{0}' in period",
								new Object[] { value }));
				}
			}
		}

		return sign * period;
	}

	public Date evalDate()
	{
		if (_object == null)
		{
			return null;
		}

		if (_object instanceof Date)
		{
			return (Date) _object;
		}

		String s = _object.toString().trim();
		if (s.length() == 0)
		{
			return null;
		}

		String[] formats = new String[] { s.indexOf(":") < 0 ? "yyyy-MM-dd"
				: "yyyy-MM-dd HH:mm:ss" };

		for (int m = 0; m < formats.length; m++)
		{
			try
			{
				return new SimpleDateFormat(formats[m]).parse(s);
			}
			catch (Exception e)
			{
				continue;
			}
		}

		return null;
	}
}
