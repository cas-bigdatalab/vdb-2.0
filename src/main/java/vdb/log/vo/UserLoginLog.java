package vdb.log.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserLoginLog
{
	private long id;

	private String ip;

	private String op_time;

	private String operation;

	private String param1;

	private String param2;

	private String param3;

	private String user;

	public UserLoginLog()
	{
		super();
	}

	public UserLoginLog(String user, String ip, String operation,
			String param1, String param2, String param3)
	{
		super();
		this.user = user;
		this.ip = ip;
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.op_time = format.format(now);
		this.operation = operation;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
	}

	public long getId()
	{
		return id;
	}

	public String getIp()
	{
		return ip;
	}

	public String getOpTime()
	{
		return op_time;
	}

	public String getOperation()
	{
		return operation;
	}

	public String getParam1()
	{
		return param1;
	}

	public String getParam2()
	{
		return param2;
	}

	public String getParam3()
	{
		return param3;
	}

	public String getUser()
	{
		return user;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public void setOpTime(String op_time)
	{
		this.op_time = op_time;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public void setParam1(String param1)
	{
		this.param1 = param1;
	}

	public void setParam2(String param2)
	{
		this.param2 = param2;
	}

	public void setParam3(String param3)
	{
		this.param3 = param3;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

}
