/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.pool;

/**
 * 对象的处理类，负责生成、销毁对象。
 * 
 * @author bluejoe
 */
public interface ObjectOwner
{
	/**
	 * 创建一个新的对象
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object createObject() throws Exception;

	/**
	 * 销毁指定的对象
	 * 
	 * @param o
	 * @throws Exception
	 */
	public void destroyObject(Object o) throws Exception;

	/**
	 * 初始化
	 * 
	 * @param pp
	 * @throws Exception
	 */
	public void init(PoolProperties pp) throws Exception;

	/**
	 * 判断指定对象是否有效
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public boolean isObjectAvailable(Object o) throws Exception;
}
