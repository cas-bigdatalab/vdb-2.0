/*
 * Created on 2008-2-1
 */
package cn.csdb.commons.util;

import java.util.List;

public interface LazyListListener<T>
{
	public List<T> offer(int beginning, int cacheSize) throws Exception;

	public List<T> offer() throws Exception;

	public int count() throws Exception;
}
