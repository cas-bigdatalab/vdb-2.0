package vdb.mydb.rpc;

import cn.csdb.grid.rpc.RPCClient;

public class VdbEngineServerConnector
{
	String _urlPattern;

	public <T> T getService(Class clazz) throws Exception
	{
		return new RPCClient<T>(String.format(_urlPattern, clazz.getName()))
				.getService(clazz);
	}

	public void setServerUrl(String baseUrl)
	{
		_urlPattern = baseUrl + "/%s";
	}

	public void setUrlPattern(String urlPattern)
	{
		_urlPattern = urlPattern;
	}
}
