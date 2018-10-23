/*
 * Created on 2007-6-5
 */
package cn.csdb.commons.orm;

import javax.sql.DataSource;

/**
 * 使用bean的getter/setter机制进行映射
 * 
 * @author bluejoe
 *
 */
public interface BeanMapping
{
	public Object createBean() throws Exception;

	public Class getBeanClass();

	public String getPrimaryKey();

	public DataSource getDataSource() throws Exception;

	public String getTableName();
}
