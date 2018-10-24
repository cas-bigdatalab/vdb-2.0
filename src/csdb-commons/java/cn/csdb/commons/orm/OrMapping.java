/*
 * Created on 2007-6-5
 */
package cn.csdb.commons.orm;

import java.io.Serializable;
import java.util.Map;

import javax.sql.DataSource;

import cn.csdb.commons.sql.jdbc.sql.StringSql;

public interface OrMapping
{
	public void bean2Map(Object bean, Map<String, Serializable> map,
			boolean onInsert) throws Exception;

	public Object map2Bean(Map<String, Serializable> map) throws Exception;

	public StringSql getIdFilter(Serializable id);

	public Serializable getPrimaryKeyValue(Object bean) throws Exception;

	public DataSource getDataSource() throws Exception;

	public String getTableName();

	public void setGeneratedKey(Object bean, String key, Serializable value)
			throws Exception;
}
