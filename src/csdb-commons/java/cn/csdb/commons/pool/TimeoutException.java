package cn.csdb.commons.pool;

public class TimeoutException extends Exception
{
	private long _timeout;

	public TimeoutException(long ms)
	{
		_timeout = ms;
	}

	public long getTimeout()
	{
		return _timeout;
	}

	@Override
	public String getMessage()
	{
		return String.format("time is out: %dms", _timeout);
	}
}
