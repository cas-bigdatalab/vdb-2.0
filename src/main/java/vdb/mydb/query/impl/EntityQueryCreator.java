package vdb.mydb.query.impl;

import vdb.metacat.Entity;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.QueryCreator;

public class EntityQueryCreator implements QueryCreator
{
	Entity _entity;

	public EntityQueryCreator(Entity entity)
	{
		super();
		_entity = entity;
	}

	public AnyQuery createQuery() throws Exception
	{
		AnyQuery query = new AnyBeanDao(_entity).createQuery();
		return query;
	}
}
