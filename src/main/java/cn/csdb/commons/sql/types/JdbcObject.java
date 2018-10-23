/*
 * Created on 2006-8-22
 */
package cn.csdb.commons.sql.types;

import java.io.Serializable;

import cn.csdb.commons.sql.catalog.JdbcColumn;

public class JdbcObject implements Serializable
{
	private ResultSetColumn _column;

	private Serializable _value;

	public JdbcObject(Serializable value, ResultSetColumn column)
	{
		this._column = column;
		this._value = value;
	}

	public JdbcObject(Serializable value, JdbcColumn field)
	{
		this._column = field.getColumn();
		this._value = value;
	}

	public ResultSetColumn getColumn()
	{
		return _column;
	}

	public Serializable getObject()
	{
		if (_value == null)
			return null;

		if (_value instanceof JdbcObject)
			return ((JdbcObject) _value).getObject();

		return _value;
	}

	public String toString()
	{
		if (_value == null)
			return null;

		return _value.toString();
	}
}
