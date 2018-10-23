package vdb.report.access.vo;

//FIXME: IpRecord
public class IPRecord
{
	private String area;

	private int count;

	private String ip;

	public IPRecord(String ip, String area, int count)
	{
		super();
		this.ip = ip;
		this.area = area;
		this.count = count;
	}

	public String getArea()
	{
		return area;
	}

	public int getCount()
	{
		return count;
	}

	public String getIp()
	{
		return ip;
	}

	public void setArea(String area)
	{
		this.area = area;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

}
