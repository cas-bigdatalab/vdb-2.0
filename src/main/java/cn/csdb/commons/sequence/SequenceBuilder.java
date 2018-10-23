/*
 * 创建日期 2005-1-5
 */
package cn.csdb.commons.sequence;

import java.io.IOException;

/**
 * 用以创建Sequence的接口。
 * 
 * @author wkc,bluejoe
 */
public interface SequenceBuilder
{
	static public final int ACTION_INSERT = 0;

	static public final int ACTION_UPDATE = 1;

	static public final int ACTION_DELETE = 2;

	/**
	 * 添加一个sequence。
	 * 
	 * @param sequence
	 */
	public abstract void addSequence(Sequence sequence);

	/**
	 * 设置一个sequence。
	 * 
	 * @param sequence
	 */
	public abstract void setSequence(Sequence sequence);

	/**
	 * 删除一个sequence。
	 * 
	 * @param sequence
	 */
	public abstract void removeSequence(Sequence sequence);

	/**
	 * 清除所有的Sequence
	 */
	public abstract void removeAll();

	/**
	 * 获取一个sequence
	 * 
	 * @param name
	 */
	public abstract Sequence getSequence(String sequenceName);

	/**
	 * 完成sequence集合的序列化
	 * 
	 * @throws IOException
	 */
	public void serialize() throws IOException;
}