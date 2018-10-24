/*
 * Created on 2007-3-23
 */
package cn.csdb.commons.util;

import java.util.Collection;

public class CollectionUtils
{
	public static <T> void copy(T[] array, Collection<T> list)
	{
		for (T o : array)
		{
			list.add(o);
		}
	}
}
