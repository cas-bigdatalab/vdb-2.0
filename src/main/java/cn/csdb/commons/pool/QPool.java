/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

import java.nio.channels.ClosedByInterruptException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Level;

import cn.csdb.commons.util.ObjectProxy;

/*
 * Pool接口的一个实现，用以完成普通的pooling机制。
 * 
 * @author bluejoe
 */
public class QPool implements Pool
{
	/**
	 * 所有对象Map，键为Object，值为PooledObject
	 */
	private Map _allObjects = new HashMap();

	private long _connectTimeout;

	private static long _seq = 0;

	private ObjectOwner _holder;

	/**
	 * 可用对象栈，每一项为PooledObject
	 */
	private Stack _idleObjects = new Stack();

	/**
	 * 队列等待时间上限
	 */
	private long _maxWait;

	/**
	 * 对象池名字
	 */
	private String _name;

	private PoolProperties _properties;

	private long _requests = 0;

	private long _tryTimes;

	/**
	 * 最大连接数
	 */
	private int _upperSize;

	private long _waitChip;

	/**
	 * 初始化方法
	 * 
	 * @param creator
	 *            设置对象的creator
	 * @throws Exception
	 */
	public QPool(ObjectOwner holder) throws Exception
	{
		_holder = holder;
	}

	/**
	 * 销毁对象池
	 */
	public void destroy() throws Exception
	{
		Iterator it = _allObjects.entrySet().iterator();
		while (it.hasNext())
		{
			PooledObject po = (PooledObject) ((Map.Entry) it.next()).getValue();
			try
			{
				_holder.destroyObject(po.getObject());
			}
			catch (Exception e)
			{
			}
		}

		_idleObjects.clear();
		_allObjects.clear();
	}

	protected void finalize() throws Throwable
	{
		destroy();
		super.finalize();
	}

	/**
	 * 返还对象
	 * 
	 * @param o
	 */
	public void freeObject(Object o) throws Exception
	{
		if (o == null)
		{
			return;
		}

		synchronized (this)
		{
			PooledObject po = (PooledObject) _allObjects.get(o);

			// 发现来自其它来源或者已标识删除的对象
			if (po == null)
			{
				return;
			}

			_idleObjects.push(po);
			po.toIdle();
		}

		PoolManager.getInstance().getLogger().log(
				Level.ALL,
				MessageFormat.format("[{0}]<--[{1}], idle: {2}, total: {3}",
						new Object[] { _name, o.toString(),
								new Integer(_idleObjects.size()),
								new Integer(_allObjects.size()) }));
	}

	public int getIdleCount()
	{
		return _idleObjects.size();
	}

	/**
	 * 获取空闲对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object getObject() throws Throwable
	{
		Object o = null;

		_requests++;
		if (_requests > 1000000L)
		{
			_requests = 0;
		}

		long requestID = _requests;
		long i = 0;

		while (true)
		{
			// 已取出对象
			if (o != null)
			{
				// 不可用，则删除之
				if (!_holder.isObjectAvailable(o))
				{
					PoolManager
							.getInstance()
							.getLogger()
							.log(
									Level.ALL,
									MessageFormat
											.format(
													"[{0}][{1,number,000000}]: found closed object and then removed it",
													new Object[] { _name,
															new Long(requestID) }));

					// 删除该对象
					destroyObject(o);
					o = null;
				}
				// 可用，结束循环
				else
				{
					break;
				}
			}

			// 有空闲对象
			synchronized (this)
			{
				if (!_idleObjects.isEmpty())
				{
					// 取出该对象
					PooledObject po = (PooledObject) _idleObjects.pop();
					po.toActive();
					o = po.getObject();

					// 该对象是辛辛苦苦等到的
					if (i > 0)
					{
						PoolManager
								.getInstance()
								.getLogger()
								.log(
										Level.ALL,
										MessageFormat
												.format(
														"[{0}][{1,number,000000}]: got available object after {2} tries ({3}ms).",
														new Object[] {
																_name,
																new Long(
																		requestID),
																new Long(i),
																new Long(
																		i
																				* _waitChip) }));
					}
				}
				// 无空闲对象
				else
				{
					// 还可以自增长
					if (_allObjects.size() < _upperSize)
					{
						createObject();
						continue;
					}
					// 不可增长，除了等待，别无他法
					else
					{
						// 开始等待
						if (i == 0)
						{
							PoolManager
									.getInstance()
									.getLogger()
									.log(
											Level.ALL,
											MessageFormat
													.format(
															"[{0}][{1,number,000000}]: no more object available, waiting...",
															new Object[] {
																	_name,
																	new Long(
																			requestID) }));
						}

						// 等待一段时间看看
						wait(_waitChip);
						i++;

						// 等待超时
						if (_maxWait > 0 && i >= _tryTimes)
						{
							String msg = MessageFormat
									.format(
											"[{0}][{1,number,000000}]: unable to retrieve available object in limited time ({2}ms)!",
											new Object[] { _name,
													new Long(requestID),
													new Long(_maxWait) });

							PoolManager.getInstance().getLogger().error(msg);
							throw new Exception(msg);
						}
					}
				}
			}
		}

		PoolManager
				.getInstance()
				.getLogger()
				.log(
						Level.ALL,
						MessageFormat
								.format(
										"[{0}][{1,number,000000}]-->[{2}], idle: {3}, total: {4}",
										new Object[] {
												_name,
												new Long(requestID),
												o.toString(),
												new Integer(_idleObjects.size()),
												new Integer(_allObjects.size()) }));

		return o;
	}

	public int getPooledCount()
	{
		return _allObjects.size();
	}

	public PoolProperties getProperties()
	{
		return _properties;
	}

	/**
	 * 增长，创建新的对象
	 * 
	 * @return
	 * @throws Exception
	 */
	private Object createObject() throws Throwable
	{
		// 超过最大数目
		if (_allObjects.size() >= _upperSize)
			return null;

		Object o;

		try
		{
			o = createObjectInTime();
		}
		catch (Exception e)
		{
			String msg = MessageFormat.format(
					"[{0}]: failed to create new object due to {1}",
					new Object[] { _name, e.getMessage() });

			PoolManager.getInstance().getLogger().error(msg);
			throw new Exception(msg, e);
		}

		PooledObject po = new PooledObject(o, _properties);

		_idleObjects.push(po);
		_allObjects.put(o, po);

		PoolManager.getInstance().getLogger().log(
				Level.ALL,
				MessageFormat.format("[{0}]<-+[{1}], idle: {2}, total: {3}",
						new Object[] { _name, o.toString(),
								new Integer(_idleObjects.size()),
								new Integer(_allObjects.size()) }));

		return null;
	}

	private Object createObjectInTime() throws Exception, InterruptedException,
			Throwable, TimeoutException
	{
		final ObjectProxy<Object> op = new ObjectProxy<Object>();
		final ObjectProxy<Throwable> tp = new ObjectProxy<Throwable>();
		class Lock
		{

		}

		final Object lock = new Lock();

		final ObjectOwner finalHolder = _holder;
		Thread threadObjectCreation = null;

		threadObjectCreation = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					//创建对象出错！
					//该函数是个阻塞函数
					op.setObject(finalHolder.createObject());
				}
				catch (ClosedByInterruptException e)
				{
					System.err.println(e.getMessage());
				}
				catch (InterruptedException e)
				{
					System.err.println(e.getMessage());
				}
				catch (Throwable e)
				{
					e.printStackTrace();
					tp.setObject(e);
				}

				//解锁
				synchronized (lock)
				{
					lock.notify();
				}
			}
		}, "create-object-thread-" + _seq);

		_seq++;

		threadObjectCreation.start();
		synchronized (lock)
		{
			lock.wait(_connectTimeout);
		}

		Throwable t = tp.getObject();
		//创建对象出错！
		if (t != null)
		{
			throw t;
		}

		//估计是超时
		Object o = op.getObject();
		if (o == null)
		{
			try
			{
				if (threadObjectCreation != null
						&& threadObjectCreation.isAlive())
					threadObjectCreation.interrupt();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			throw new TimeoutException(_connectTimeout);
		}

		return o;
	}

	/**
	 * 初始化本对象池
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public void init(PoolProperties pp) throws Exception
	{
		_properties = pp;
		_holder.init(pp);
		_name = pp.getName();

		try
		{
			_maxWait = Long.parseLong(pp.getProperty(PoolProperties.MAX_WAIT));
		}
		catch (NumberFormatException e)
		{
			_maxWait = 2000;
		}

		try
		{
			_upperSize = Integer.parseInt(pp
					.getProperty(PoolProperties.UPPER_SIZE));
		}
		catch (NumberFormatException e)
		{
			_upperSize = 20;
		}

		try
		{
			_waitChip = Integer.parseInt(pp
					.getProperty(PoolProperties.WAIT_CHIP));
		}
		catch (NumberFormatException e)
		{
			_waitChip = 100;
		}

		try
		{
			_connectTimeout = Long.parseLong(pp
					.getProperty(PoolProperties.CONNECT_TIMEOUT));
		}
		catch (Exception e)
		{
			_connectTimeout = 60000;
		}

		if (_waitChip == 0)
			_waitChip = 100;

		_tryTimes = _maxWait / _waitChip;
	}

	public void ping() throws Exception
	{
		List toBeRemoved = new Vector();
		Iterator it = _allObjects.entrySet().iterator();
		while (it.hasNext())
		{
			PooledObject po = (PooledObject) ((Map.Entry) it.next()).getValue();
			// 强制夺回长时间未返还的对象，并输出记录点
			Object o = po.getObject();
			if (po.isTooActive())
			{
				toBeRemoved.add(o);
				PoolManager
						.getInstance()
						.getLogger()
						.log(
								Level.ALL,
								MessageFormat
										.format(
												"[{0}]: found object [{1}] which was in use for too long time\r\n{2}",
												new Object[] { _name, o,
														po.getStackPoint() }));
			}

			// 强制关闭长时间未使用的对象
			if (po.isTooIdle())
			{
				toBeRemoved.add(o);
				PoolManager
						.getInstance()
						.getLogger()
						.log(
								Level.ALL,
								MessageFormat
										.format(
												"[{0}]: found object [{1}] which was on idle for too long time",
												new Object[] { _name, o }));
			}
		}

		for (int i = 0; i < toBeRemoved.size(); i++)
		{
			destroyObject(toBeRemoved.get(i));
		}
	}

	/**
	 * 删除指定对象
	 * 
	 * @param o
	 */
	private void destroyObject(Object o) throws Exception
	{
		_holder.destroyObject(o);

		synchronized (this)
		{
			PooledObject po = (PooledObject) _allObjects.get(o);
			_idleObjects.remove(po);
			_allObjects.remove(o);
		}

		PoolManager.getInstance().getLogger().log(
				Level.ALL,
				MessageFormat.format("[{0}]x->[{1}], idle: {2}, total: {3}",
						new Object[] { _name, o.toString(),
								new Integer(_idleObjects.size()),
								new Integer(_allObjects.size()) }));
	}

	public long getConnectTimeout()
	{
		return _connectTimeout;
	}

	public void setConnectTimeout(long connectTimeout)
	{
		_connectTimeout = connectTimeout;
	}
}