package vdb.mydb.query.impl;

import java.io.Serializable;
import java.util.Map;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.query.QueryFromSql;
import vdb.mydb.query.VarQuery;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.StringKeyMap;

public class QueryFromSqlImpl implements QueryFromSql
{
	private Entity _entity;

	private StringSql _sql;

	private Map<String, Serializable> _variables = new StringKeyMap<Serializable>();

	public QueryFromSqlImpl(Entity entity)
	{
		super();
		_entity = entity;
	}

	public VarQuery bindVariable(String name, Serializable value)
	{
		_variables.put(name, value);
		return this;
	}

	public Entity getEntity()
	{
		return _entity;
	}

	public StringSql getSql()
	{
		return _sql;
	}

	public QueryFromSql setSql(StringSql sql)
	{
		_sql = sql;
		return this;
	}

	public StringSql toStringSql() throws Exception
	{
		return _sql.mergeVariables(_variables);
	}

	public void orderBy(Field field, String orderAsc)
	{
		String sqlTemplate = _sql.getString();
		sqlTemplate = sqlTemplate + " order by " + field.getColumnName() + " "
				+ orderAsc;
		_sql.setString(sqlTemplate);
	}

}
