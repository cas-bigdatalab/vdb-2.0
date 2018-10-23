package vdb.mydb.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.context.Context;

public class RequestContext2VtlContextAdapter implements Context
{
	Map<String, Object> _map = new HashMap<String, Object>();

	private HttpServletRequest _request;

	public RequestContext2VtlContextAdapter(HttpServletRequest request)
	{
		super();
		this._request = request;
	}

	public boolean containsKey(Object arg0)
	{
		return _request.getAttribute((String) arg0) != null;
	}

	public Object get(String arg0)
	{
		if (_map.containsKey(arg0))
			return _map.get(arg0);

		return _request.getAttribute(arg0);
	}

	public Object[] getKeys()
	{
		return null;
	}

	public Object put(String arg0, Object arg1)
	{
		_map.put(arg0, arg1);
		return null;
	}

	public Object remove(Object arg0)
	{
		return null;
	}
}
