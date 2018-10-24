/*
 * 创建日期 2005-1-12
 */
package cn.csdb.commons.util;

/**
 * 该类用以描述具有属性的特性。
 * 
 * @author bluejoe
 */
public interface ELSupport
{
	/**
	 * 根据属性名获取相应类型的属性值。 如果不存在指定属性名，则返回null。 可以返回数值、时间、文本类型的属性值。
	 * 
	 * @param attributeName
	 */
	public Object get(String key);

	/**
	 * 设置指定的属性。 如果不支持该属性值的设置，则返回false。
	 * 
	 * @param propertyName
	 * @param propertyValue
	 */
	public Object set(Object key, Object value);
}
