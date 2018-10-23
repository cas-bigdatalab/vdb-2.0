package vdb.mydb.query.impl;

import java.io.Serializable;

import vdb.metacat.Field;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.QueryCreator;

public class CollectionQueryCreator implements QueryCreator
{
	Field _field;

	private Serializable _parentBeanId;

	public CollectionQueryCreator(Field field, Serializable parentBeanId)
	{
		super();
		_field = field;
		_parentBeanId = parentBeanId;
	}

	public AnyQuery createQuery() throws Exception
	{
		if (_field.isCollection())
		{
			AnyQuery query = new AnyBeanDao(_field.getRelationKey().getTarget())
					.createQuery();
			query.where(query.join(_field, _parentBeanId));

			return query;
		}

		return null;
	}
}
