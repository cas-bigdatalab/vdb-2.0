/*
 * Created on 2006-1-28
 */
package cn.csdb.commons.orm.impl;

import java.util.Map;

import cn.csdb.commons.orm.Persistor;
import cn.csdb.commons.orm.bean.BeanContext;

/**
 * @author bluejoe
 */
public class BeanContextImpl implements BeanContext
{
	private Persistor _persistor;

	private Map _record;

	public BeanContextImpl(Persistor persistor, Map record)
	{
		_persistor = persistor;
		_record = record;
	}

	public Persistor getPersistor()
	{
		return _persistor;
	}

	public Map getSqlMap()
	{
		return _record;
	}
}
