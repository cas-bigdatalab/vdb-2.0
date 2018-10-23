package vdb.mydb.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.context.Context;
import org.springframework.context.ApplicationContext;

public class ApplicationContext2VtlContextAdapter implements Context
{
	Map<String, Object> _map = new HashMap<String, Object>();

	private ApplicationContext _applicationContext;

	public ApplicationContext2VtlContextAdapter(
			ApplicationContext applicationContext)
	{
		super();
		this._applicationContext = applicationContext;
	}

	public boolean containsKey(Object arg0)
	{
		return _applicationContext.containsBean((String) arg0);
	}

	public Object get(String arg0)
	{
		if (_map.containsKey(arg0))
			return _map.get(arg0);

		return _applicationContext.getBean(arg0);
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
