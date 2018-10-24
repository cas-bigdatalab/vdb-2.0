/*
 * 创建日期 2005-10-5
 */
package cn.csdb.commons.util;

/**
 * @author bluejoe
 */
public interface Matcher<T>
{
	public boolean matches(T toMatch);
}
