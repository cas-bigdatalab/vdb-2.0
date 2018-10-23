package vdb.mydb.typelib;

import java.io.Serializable;

import vdb.mydb.bean.AnyBean;

public interface JdbcRowWriter
{
	void set(String columnName, Serializable value);

	Serializable getId();

	AnyBean getBean();
}
