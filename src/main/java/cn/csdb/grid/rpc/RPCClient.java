package cn.csdb.grid.rpc;

import com.caucho.hessian.client.HessianProxyFactory;

public class RPCClient<T>
{
	String _url;

	public RPCClient(String url)
	{
		this._url = url;
	}

	@SuppressWarnings("unchecked")
	public T getService() throws Exception
	{
		HessianProxyFactory factory = new HessianProxyFactory();
		return (T) factory.create(_url);
	}

	public T getService(Class clazz) throws Exception
	{
		HessianProxyFactory factory = new HessianProxyFactory();
		return (T) factory.create(clazz, _url);
	}
}
