package vdb.mydb.bean;

import java.io.Serializable;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;

public class BeanRef implements Serializable
{
	private Entity _entity;

	Serializable _id;

	public BeanRef(String entityUri, Serializable id)
	{
		this((Entity) VdbManager.getInstance().getCatalog().fromUri(entityUri),
				id);
	}

	public BeanRef(Entity entity, Serializable id)
	{
		_entity = entity;
		_id = id;
	}

	public AnyBean getBean() throws Exception
	{
		if (_id == null)
			return null;

		return new AnyBeanDao(_entity).lookup(_id);
	}

	public Entity getEntity()
	{
		return _entity;
	}

	public Serializable getId()
	{
		return _id;
	}

	public void setId(Serializable id)
	{
		_id = id;
	}
}
