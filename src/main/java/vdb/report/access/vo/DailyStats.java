package vdb.report.access.vo;

import java.util.Date;

/**
 * 日常统计指标
 * 
 * @author 苏贤明
 */
public class DailyStats
{

	/** 访问区域 */
	private String _area;

	/** 访问国家 */
	private String _country;

	/** 访问的数据集 */
	private String _dateset;

	/** 访问的实体 */
	private String _entity;

	/** 访问的IP */
	private String _ip;

	/** 访问的次数 */
	private int _counts;

	/** 统计截止日期 */
	private Date _date;

	/** 访问的操作 */
	private String _operation;

	/** 访问的用户名 */
	private String _username;

	public String getArea()
	{
		return _area;
	}

	public String getCountry()
	{
		return _country;
	}

	public String getDateset()
	{
		return _dateset;
	}

	public String getEntity()
	{
		return _entity;
	}

	public String getIp()
	{
		return _ip;
	}

	public int getCounts()
	{
		return _counts;
	}

	public Date getDate()
	{
		return _date;
	}

	public String getOperation()
	{
		return _operation;
	}

	public String getUsername()
	{
		return _username;
	}

	public void setArea(String area)
	{
		_area = area;
	}

	public void setCountry(String country)
	{
		_country = country;
	}

	public void setDateset(String datesetId)
	{
		_dateset = datesetId;
	}

	public void setEntity(String entityId)
	{
		_entity = entityId;
	}

	public void setIp(String ip)
	{
		_ip = ip;
	}

	public void setCounts(int counts)
	{
		_counts = counts;
	}

	public void setDate(Date date)
	{
		_date = date;
	}

	public void setOperation(String operation)
	{
		_operation = operation;
	}

	public void setUsername(String username)
	{
		_username = username;
	}

}
