package vdb.report.util;

//FIXME: IpEntry
public class IPEntry
{
	private String _area;

	private String _beginIp;

	private String _country;

	private String _endIp;

	public IPEntry()
	{
		_beginIp = _endIp = _country = _area = "";
	}

	public String getArea()
	{
		return _area;
	}

	public void setArea(String area)
	{
		_area = area;
	}

	public String getBeginIp()
	{
		return _beginIp;
	}

	public void setBeginIp(String beginIp)
	{
		_beginIp = beginIp;
	}

	public String getCountry()
	{
		return _country;
	}

	public void setCountry(String country)
	{
		_country = country;
	}

	public String getEndIp()
	{
		return _endIp;
	}

	public void setEndIp(String endIp)
	{
		_endIp = endIp;
	}

}
