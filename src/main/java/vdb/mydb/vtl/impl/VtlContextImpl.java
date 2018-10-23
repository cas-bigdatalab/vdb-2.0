package vdb.mydb.vtl.impl;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.tools.view.ViewToolContext;

import vdb.mydb.vtl.VspContext;
import cn.csdb.commons.util.StringKeyMap;

public class VtlContextImpl implements VspContext
{
	public class Param
	{
		private VtlContextImpl _context;

		private Map<String, String> _map = new StringKeyMap<String>();

		public Param(VtlContextImpl context)
		{
			_context = context;
		}

		public String get(String name)
		{
			if (_map.containsKey(name))
				return _map.get(name);

			return _context.getParameter(name);
		}

		public void set(String name, String value)
		{
			_map.put(name, value);
		}
	}

	private ViewToolContext _context;

	public VtlContextImpl(ViewToolContext context)
	{
		_context = context;
		_context.put("param", getParam());
	}

	public boolean containsKey(Object key)
	{
		return _context.containsKey(key);
	}

	public Object get(String key)
	{
		return _context.get(key);
	}

	public Object[] getKeys()
	{
		return _context.getKeys();
	}

	public Param getParam()
	{
		return new Param(this);
	}

	public String getParameter(String name)
	{
		return _context.getRequest().getParameter(name);
	}

	public HttpServletRequest getRequest()
	{
		return _context.getRequest();
	}

	public String getRequestURI()
	{
		return _context.getRequest().getRequestURI();
	}

	public HttpServletResponse getResponse()
	{
		return _context.getResponse();
	}

	public ServletContext getServletContext()
	{
		return _context.getServletContext();
	}

	public Object put(String key, Object value)
	{
		return _context.put(key, value);
	}

	public Object remove(Object key)
	{
		return _context.remove(key);
	}
}
