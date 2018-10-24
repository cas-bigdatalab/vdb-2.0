/*
 * 创建日期 2005-7-27
 */
package cn.csdb.commons.pool;

import java.util.List;
import java.util.Vector;

/**
 * 对象池的监控线程类，PoolPinger由PoolManager自动启动，它将依次调用各对象池的onPing()方法
 * 
 * @author bluejoe
 */
public class PoolPinger extends Thread
{
	private PoolManager _pm;

	private boolean _stopped;

	public PoolPinger(PoolManager pm)
	{
		_pm = pm;
	}

	public void stopRunning()
	{
		_stopped = true;
	}

	public void run()
	{
		while (!_stopped)
		{
			try
			{
				Thread.sleep(_pm.getPingInterval());
			}
			catch (InterruptedException e)
			{
			}

			List pools = new Vector(_pm.getPools().values());
			for (int i = 0; i < pools.size(); i++)
			{
				Pool pool = (Pool) pools.get(i);
				try
				{
					pool.ping();
				}
				catch (Exception e)
				{
				}
			}
		}
	}
}