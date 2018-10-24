/*
 * Created on 2007-6-5
 */
package cn.csdb.commons.orm;

public interface BeanMapper
{
	public BeanMapping getMapping(String mappingName);

	public BeanMapping getMapping(Class beanClass);
}
