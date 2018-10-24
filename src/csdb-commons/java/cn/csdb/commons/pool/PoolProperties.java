/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

import java.util.Map;

import cn.csdb.commons.util.StringKeyMap;

/*
 * 对象池的设置属性。
 * 
 * @author bluejoe
 */
public class PoolProperties
{
	/**
	 * 每个对象的最大活动时间
	 */
	public static final String MAX_ACTIVE_TIME = "max-active-time";

	/**
	 * 每个对象的最大空闲时间
	 */
	public static final String MAX_IDLE_TIME = "max-idle-time";

	/**
	 * 获取对象时的最大等待时间
	 */
	public static final String MAX_WAIT = "max-wait";

	/**
	 * PoolPinger的工作间隔时间
	 */
	public static final String MONITOR_INTERVAL = "ping-interval";

	/**
	 * 对象的最大允许数目
	 */
	public static final String UPPER_SIZE = "upper-size";

	/**
	 * 获取对象需要等待时，每次休息的时间
	 */
	public static final String WAIT_CHIP = "wait-chip";

	public static final String CONNECT_TIMEOUT = "connect-timeout";

	/**
	 * 对象池的参数
	 */
	private Map<String, String> _map = new StringKeyMap<String>();

	public PoolProperties(Map<String, String> pp)
	{
		_map.putAll(pp);
	}

	/**
	 * 获取对象池的名字
	 * 
	 * @return
	 */
	public String getName()
	{
		return getProperty("name");
	}

	/**
	 * 获取所有的参数
	 * 
	 * @return
	 */
	public Map getPropertiesMap()
	{
		return _map;
	}

	/**
	 * 获取指定名字的参数值
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name)
	{
		return _map.get(name);
	}
}
