package vdb.mydb.query.impl;

import java.io.Serializable;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.RelationKey;
import vdb.mydb.query.JdbcExpr;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class ManyToOneExpr implements JdbcExpr
{
	private Field _collectionField;

	private Entity _entity;

	private Serializable _pkValue;

	public ManyToOneExpr(Entity entity, Field collectionField,
			Serializable pkValue)
	{
		_entity = entity;
		_collectionField = collectionField;
		_pkValue = pkValue;
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
		RelationKey foreignKey = _collectionField.getRelationKey();

		String joinFilter = String.format("%s=?", foreignKey.getPeer()
				.getColumnName());

		return new StringSql(joinFilter, _pkValue);
	}
}
