package vdb.mydb.typelib;

import java.io.Serializable;

import vdb.mydb.bean.AnyBean;

public interface JdbcRowReader
{
	Serializable get(String columnName);

	Serializable getId();

	AnyBean getBean();
}
