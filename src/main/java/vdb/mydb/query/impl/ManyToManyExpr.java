package vdb.mydb.query.impl;

import java.io.Serializable;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.query.JdbcExpr;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class ManyToManyExpr implements JdbcExpr
{
	private Field _collectionField;

	private Entity _entity;

	private Serializable _pkValue;

	public ManyToManyExpr(Entity entity, Field collectionField,
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
		VdbDataSet dsex = ((VdbDataSet) _entity.getDataSet());
		RelationKey foreignKey = _collectionField.getRelationKey();
		Relation rel = foreignKey.getRelation();

		String joinFilter = String.format("%s.%s=%s.%s and %s.%s=?", dsex
				.quote(rel.getAssocTableName()), dsex.quote(foreignKey
				.getColumnName()), dsex.quote(foreignKey.getTarget()
				.getTableName()), dsex.quote(foreignKey.getTarget()
				.getIdentifier().getField().getColumnName()), dsex.quote(rel
				.getAssocTableName()), dsex.quote(foreignKey.getPeer()
				.getColumnName()));

		return new StringSql(joinFilter, _pkValue);
	}
}
