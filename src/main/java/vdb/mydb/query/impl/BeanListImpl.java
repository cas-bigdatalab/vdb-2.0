package vdb.mydb.query.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vdb.metacat.Entity;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.BeanList;

public class BeanListImpl implements BeanList
{
	private List<AnyBean> _beans = new ArrayList<AnyBean>();

	private Entity _entity;

	public BeanListImpl(Entity entity)
	{
		_entity = entity;
	}

	public void add(AnyBean bean)
	{
		if (bean != null)
			_beans.add(bean);
	}

	public void add(Serializable beanId) throws Exception
	{
		_beans.add(new AnyBeanDao(_entity).lookup(beanId));
	}

	public Entity getEntity()
	{
		return _entity;
	}

	public List list(int beginning, int size) throws Exception
	{
		int s1 = beginning + size;
		int s2 = size();
		return _beans.subList(beginning, s1 > s2 ? s2 : s1);
	}

	public int size() throws Exception
	{
		return _beans.size();
	}

}
