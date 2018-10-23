/*
 * Created on 2007-6-5
 */
package cn.csdb.commons.orm;

import javax.sql.DataSource;


public class SimpleBeanMapping implements BeanMapping
{
	private Class _beanClass;

	private DataSource _dataSource;

	private String _tableName;

	private String _primaryKeyName;

	public SimpleBeanMapping(Class beanClass, DataSource dataSource,
			String tableName, String primaryKeyName)
	{
		_beanClass = beanClass;
		_dataSource = dataSource;
		_tableName = tableName;
		_primaryKeyName = primaryKeyName;
	}

	public Object createBean() throws Exception
	{
		return _beanClass.newInstance();
	}

	public Class getBeanClass()
	{
		return _beanClass;
	}

	public String getPrimaryKey()
	{
		return _primaryKeyName;
	}

	public DataSource getDataSource() throws Exception
	{
		return _dataSource;
	}

	public String getTableName()
	{
		return _tableName;
	}
}
