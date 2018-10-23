/*
 * 2005-10-5
 */
package cn.csdb.commons.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.csdb.commons.jsp.Pageable;

/*
 * @author bluejoe
 */
public class ListUtils
{
	public static <K, V, O> ListMap<K, V> list2ListMap(List<O> list,
			MapEntry<K, V, O> entryGetter)
	{
		ListMap<K, V> listMap = new ListMap<K, V>();
		for (O o : list)
		{
			listMap.add(entryGetter.getKey(o), entryGetter.getValue(o));
		}

		return listMap;
	}

	/**
	 * @param list
	 * @param object
	 * @return
	 */
	public static <K, V, O> Map<K, V> list2Map(List<O> list,
			MapEntry<K, V, O> entryGetter)
	{
		Map<K, V> map = new HashMap<K, V>();
		for (O o : list)
		{
			map.put(entryGetter.getKey(o), entryGetter.getValue(o));
		}

		return map;
	}

	public interface MapEntry<K, V, O>
	{
		public K getKey(O o);

		public V getValue(O o);
	}

	public static void addInOrder(List listInOrder, Object o,
			Comparator comparator)
	{
		int which = 0;

		for (int i = 0; i < listInOrder.size(); i++)
		{
			Object o2 = listInOrder.get(i);
			// if o < o2
			if (comparator.compare(o, o2) < 0)
			{
				break;
			}

			which++;
		}

		listInOrder.add(which, o);
	}

	public static <T> T lookup(Collection<T> list, Matcher<T> matcher)
	{
		return MatcherUtils.lookup(list.iterator(), matcher);
	}

	public static <T> List<T> subList(List<T> src, Matcher<T> matcher)
	{
		List<T> srs = new ArrayList<T>();
		for (T o : src)
		{
			if (matcher.matches(o))
			{
				srs.add(o);
			}
		}

		return srs;
	}

	public static <T> Pageable<T> makePageable(List<T> list)
	{
		final List<T> finalList = list;

		return new Pageable<T>()
		{

			public List<T> list(int beginning, int size) throws Exception
			{
				return finalList.subList(beginning, beginning + size);
			}

			public int size() throws Exception
			{
				return finalList.size();
			}
		};
	}
}
