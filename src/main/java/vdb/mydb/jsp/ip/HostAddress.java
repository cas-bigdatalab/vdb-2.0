package vdb.mydb.jsp.ip;

import java.net.InetAddress;

import org.apache.log4j.Logger;

public class HostAddress implements Address
{
	String _ip;

	public void setHost(String host)
	{
		try
		{
			_ip = InetAddress.getByName(host).getHostAddress();
		}
		catch (Exception e)
		{
			Logger.getLogger(this.getClass()).warn(
					String.format("unable to resolve host name: %s", host), e);
			_ip = host;
		}
	}

	public boolean matches(String target)
	{
		if (_ip == null)
			return false;

		return _ip.equals(target);
	}
}