/*
 * 创建日期 2005-10-5
 */
package cn.csdb.commons.util;

import java.util.Iterator;

/**
 * @author bluejoe
 */
public class MatcherUtils
{
	public static <T> T lookup(Iterator<T> it, Matcher<T> matcher)
	{
		while (it.hasNext())
		{
			T o = it.next();
			if (matcher.matches(o))
			{
				return o;
			}
		}

		return null;
	}
}
