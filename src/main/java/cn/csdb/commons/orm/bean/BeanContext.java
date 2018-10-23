/*
 * Created on 2006-1-26
 */
package cn.csdb.commons.orm.bean;

import java.util.Map;

import cn.csdb.commons.orm.Persistor;

/**
 * @author bluejoe
 */
public interface BeanContext
{
	public Map getSqlMap();

	public Persistor getPersistor();
}
