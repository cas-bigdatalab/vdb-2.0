package vdb.mydb.jsp.ip;

import java.util.regex.Pattern;

public class IpAddress implements Address
{

	private Pattern _pattern;

	public void setIp(String ip)
	{
		_pattern = Pattern.compile(ip);
	}

	public boolean matches(String target)
	{
		return _pattern.matcher(target).matches();
	}

}