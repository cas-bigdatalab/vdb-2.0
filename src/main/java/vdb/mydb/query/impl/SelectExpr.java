package vdb.mydb.query.impl;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.Entity;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.query.JdbcExpr;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class SelectExpr implements JdbcExpr
{
	private Entity _entity;

	private List<String> _joinedTables = new ArrayList<String>();

	public SelectExpr(AnyQueryImpl query, Entity entity)
	{
		_entity = entity;
	}

	public void addTable(String tableName)
	{
		assert (tableName != null);
		_joinedTables.add(tableName);
	}

	public boolean isCrossQuery()
	{
		return !_joinedTables.isEmpty();
	}

	@Override
	public String toString()
	{
		try
		{
			return toStringSql().getString();
		}
		catch (Exception e)
		{
			return super.toString();
		}
	}

	public StringSql toStringSql() throws Exception
	{
		VdbDataSet dsex = ((VdbDataSet) _entity.getDataSet());

		if (!isCrossQuery())
			return new StringSql(String.format("select * from %s", dsex
					.quote(_entity.getTableName())));

		String sql = String.format("select %s.* from %s", dsex.quote(_entity
				.getTableName()), dsex.quote(_entity.getTableName()));

		for (String table : _joinedTables)
		{
			sql += ("," + dsex.quote(table));
		}

		return new StringSql(sql);
	}
}
