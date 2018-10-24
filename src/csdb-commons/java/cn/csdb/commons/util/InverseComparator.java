/*
 * 创建日期 2005-11-15
 */
package cn.csdb.commons.util;

import java.util.Comparator;

/**
 * @author bluejoe
 */
public class InverseComparator implements Comparator
{
	private Comparator _comparator;

	public InverseComparator(Comparator comparator)
	{
		_comparator = comparator;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2)
	{
		return _comparator.compare(o2, o1);
	}
}
