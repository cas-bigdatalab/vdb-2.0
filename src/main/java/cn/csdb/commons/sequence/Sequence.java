/*
 * 创建日期 2003-11-12
 * 
 */
package cn.csdb.commons.sequence;

/**
 * 实现类似于Oracle Sequence功能。 <br>
 * 通过调用getNextValue()方法获取下一个序列值。
 * 
 * @see SequenceBuilder
 * 
 * @author wkc,bluejoe
 */
public class Sequence
{
	private long current;

	private long end;

	private long beginning;

	private long interval;

	private String sequenceName;

	private SequenceBuilder sb;

	/**
	 * 构造Sequence对象
	 * 
	 * @param name
	 * @param range
	 */
	public Sequence(SequenceBuilder sb, String name, long beginning,
			long interval)
	{
		this.sb = sb;
		this.sequenceName = name;
		this.beginning = (beginning / interval) * interval;
		this.interval = interval;

		this.current = beginning;
		this.end = beginning + interval;
	}

	/**
	 * 返回下一个序列值，调用之后系统将自动将该值设置为当前序列值 long 下一个序列值
	 */
	public long getNextValue()
	{
		current++;

		// 已开始下一区段
		if (current > beginning)
		{
			beginning += interval;
			// 记录
			try
			{
				sb.serialize();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		// 到达终点
		if (current == end)
		{
			end += interval;
		}

		return current;
	}

	/**
	 * 返回当前序列值 long 序列值
	 */
	public long getCurrentValue()
	{
		return current;
	}

	public String getName()
	{
		return sequenceName;
	}

	/**
	 */
	public long getBeginning()
	{
		return beginning;
	}

	/**
	 */
	public long getInterval()
	{
		return interval;
	}

	/**
	 * @param l
	 */
	public void setCurrentValue(long l)
	{
		current = l;
		beginning = (current / interval) * interval;
	}

}
