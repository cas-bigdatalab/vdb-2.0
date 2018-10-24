/*
 * Created on 2005-7-29
 */
package cn.csdb.commons.pool;

/**
 * Pool用以描述对象池的接口。 配置在pool.xml里面的pool都必须实现Pool接口。
 * 
 * @see PoolManager
 * @author bluejoe
 */
public interface Pool
{
	/**
	 * 提供给检测线程使用
	 * 
	 * @throws Exception
	 */
	public abstract void ping() throws Exception;

	/**
	 * 清空对象池，销毁所有对象
	 */
	public abstract void destroy() throws Exception;

	/**
	 * 获取池的属性
	 * 
	 * @return
	 */
	public abstract PoolProperties getProperties();

	/**
	 * 初始化该对象池
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public abstract void init(PoolProperties pp) throws Exception;
}