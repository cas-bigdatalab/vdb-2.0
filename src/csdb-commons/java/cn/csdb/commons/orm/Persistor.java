package cn.csdb.commons.orm;

import java.io.Serializable;

import cn.csdb.commons.sql.JdbcSource;

public interface Persistor
{
	Query createQuery() throws Exception;

	int delete(Object bean) throws Exception;

	int insert(Object bean) throws Exception;

	Object lookup(Serializable id) throws Exception;

	int update(Object bean) throws Exception;

	JdbcSource getJdbcSource();

}