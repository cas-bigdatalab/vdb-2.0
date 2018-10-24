/*
 * Created on 2005-2-12
 */
package cn.csdb.commons.util;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * OptionTokenizer用以从指定字符串中解析出枚举值。
 * 
 * 如：字符串的内容为："男,女"，或者"0:男,1:女"。
 * 
 * @author Administrator
 */
public class MapEntries
{
	private String _delimiter1 = ",";

	private String _delimiter2 = ":";

	private String _valueSet;

	private Map<String, String> _options;

	public MapEntries(String valueSet)
	{
		_valueSet = valueSet;
		parse();
	}

	public MapEntries(String valueSet, String delimiter1, String delimiter2)
	{
		_valueSet = valueSet;
		_delimiter1 = delimiter1;
		_delimiter2 = delimiter2;
		parse();
	}

	/**
	 * e.g:s="0:男,1:女"
	 * 
	 * @param s
	 */
	public void parse()
	{
		_options = new StringKeyMap();

		if (_valueSet == null)
		{
			return;
		}

		StringTokenizer st = new StringTokenizer(_valueSet, _delimiter1);
		int i = 1;

		while (st.hasMoreTokens())
		{
			String is = st.nextToken().trim();

			// 如果is以''包含，则忽略''
			if (is.startsWith("'") && is.endsWith("'"))
			{
				is = is.substring(1, is.length() - 1);
			}

			String optionCode, option;
			StringTokenizer st2 = new StringTokenizer(is, _delimiter2);
			optionCode = st2.nextToken();
			if (st2.hasMoreTokens())
			{
				option = st2.nextToken();
			}
			else
			{
				option = optionCode;
				// optionCode = "" + i;
			}

			_options.put(optionCode, option);

			i++;
		}
	}

	public Map<String, String> getOptions()
	{
		return _options;
	}
}