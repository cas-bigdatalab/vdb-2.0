package vdb.mydb.query;

import java.io.Serializable;

import vdb.metacat.Entity;
import vdb.mydb.bean.AnyBean;
import cn.csdb.commons.jsp.Pageable;

public interface BeanList extends Pageable
{
	public void add(AnyBean bean);

	public void add(Serializable beanId) throws Exception;

	public Entity getEntity();
}
