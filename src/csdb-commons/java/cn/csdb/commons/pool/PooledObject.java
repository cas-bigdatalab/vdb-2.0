package cn.csdb.commons.pool;

/*
 * 用以描述被池化的对象。 每个原始对象将会被连接池包装成PooledObject。
 * 
 * @author bluejoe
 */
class PooledObject
{
	/**
	 * 是否正在处于活动状态
	 */
	private boolean _active;

	/**
	 * 允许的最大活动时间
	 */
	private long _maxActiveTime;

	/**
	 * 允许的最大空闲时间
	 */
	private long _maxIdleTime;

	/**
	 * 原始对象
	 */
	private Object _rawObject;

	/**
	 * 调用栈点
	 */
	private Throwable _stackPoint;

	/**
	 * 调用时间点
	 */
	private long _timePoint;

	/**
	 * 创建一个池化对象
	 * 
	 * @param o
	 * @param properties
	 */
	public PooledObject(Object o, PoolProperties properties)
	{
		_rawObject = o;

		try
		{
			_maxActiveTime = Long.parseLong(properties
					.getProperty(PoolProperties.MAX_ACTIVE_TIME));
		}
		catch (Exception e)
		{
			_maxActiveTime = 300000;
		}

		try
		{
			_maxIdleTime = Long.parseLong(properties
					.getProperty(PoolProperties.MAX_IDLE_TIME));
		}
		catch (Exception e)
		{
			_maxIdleTime = 300000;
		}

		toIdle();
	}

	/**
	 * 获取开始空闲/活动的时间
	 * 
	 * @return
	 */
	private long getElapse()
	{
		return System.currentTimeMillis() - _timePoint;
	}

	/**
	 * 获取原始对象
	 * 
	 * @return
	 */
	public Object getObject()
	{
		return _rawObject;
	}

	/**
	 * 获取记录点的信息，用于打印。
	 * 
	 * @return
	 */
	public String getStackPoint()
	{
		String sp = "";
		if (_stackPoint != null)
		{
			StackTraceElement[] stes = _stackPoint.getStackTrace();
			if (stes.length >= 3)
			{
				for (int i = 3; i < stes.length; i++)
				{
					sp += "\tat " + stes[i].toString() + "\r\n";
				}
			}
		}

		return sp;
	}

	/**
	 * 该对象是否正在被使用？
	 * 
	 * @return
	 */
	public boolean isActive()
	{
		return _active;
	}

	/**
	 * 该对象是否正在空闲？
	 * 
	 * @return
	 */
	public boolean isIdle()
	{
		return !_active;
	}

	/**
	 * 该对象是否长时间被使用？
	 * 
	 * @return
	 */
	public boolean isTooActive()
	{
		return _active && getElapse() > _maxActiveTime;
	}

	/**
	 * 该对象是否长时间处于空闲状态？
	 * 
	 * @return
	 */
	public boolean isTooIdle()
	{
		return !_active && getElapse() > _maxIdleTime;
	}

	/**
	 * 设置活动状态
	 */
	public void toActive()
	{
		_active = true;
		_timePoint = System.currentTimeMillis();

		_stackPoint = new Throwable();
	}

	/**
	 * 设置空闲状态
	 */
	public void toIdle()
	{
		_active = false;
		_timePoint = System.currentTimeMillis();
		_stackPoint = null;
	}
}
