package vdb.mydb.util;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import cn.csdb.commons.util.StringKeyMap;

public class DelegatingServletContext implements ServletContext
{
	ServletContext _servletContext;

	Map<String, String> _customParameters = new StringKeyMap<String>();

	public DelegatingServletContext(ServletContext servletContext)
	{
		super();
		_servletContext = servletContext;
	}

	public void addInitParameter(String arg0, String arg1)
	{
		_customParameters.put(arg0, arg1);
	}

	public Object getAttribute(String arg0)
	{
		return _servletContext.getAttribute(arg0);
	}

	public Enumeration getAttributeNames()
	{
		return _servletContext.getAttributeNames();
	}

	public ServletContext getContext(String arg0)
	{
		return _servletContext.getContext(arg0);
	}

	public String getInitParameter(String arg0)
	{
		if (_customParameters.containsKey(arg0))
			return _customParameters.get(arg0);

		return _servletContext.getInitParameter(arg0);
	}

	public Enumeration getInitParameterNames()
	{
		return _servletContext.getInitParameterNames();
	}

	public int getMajorVersion()
	{
		return _servletContext.getMajorVersion();
	}

	public String getMimeType(String arg0)
	{
		return _servletContext.getMimeType(arg0);
	}

	public int getMinorVersion()
	{
		return _servletContext.getMinorVersion();
	}

	public RequestDispatcher getNamedDispatcher(String arg0)
	{
		return _servletContext.getNamedDispatcher(arg0);
	}

	public String getRealPath(String arg0)
	{
		return _servletContext.getRealPath(arg0);
	}

	public RequestDispatcher getRequestDispatcher(String arg0)
	{
		return _servletContext.getRequestDispatcher(arg0);
	}

	public URL getResource(String arg0) throws MalformedURLException
	{
		return _servletContext.getResource(arg0);
	}

	public InputStream getResourceAsStream(String arg0)
	{
		return _servletContext.getResourceAsStream(arg0);
	}

	public Set getResourcePaths(String arg0)
	{
		return _servletContext.getResourcePaths(arg0);
	}

	public String getServerInfo()
	{
		return _servletContext.getServerInfo();
	}

	public Servlet getServlet(String arg0) throws ServletException
	{
		return _servletContext.getServlet(arg0);
	}

	public String getServletContextName()
	{
		return _servletContext.getServletContextName();
	}

	public Enumeration getServletNames()
	{
		return _servletContext.getServletNames();
	}

	public Enumeration getServlets()
	{
		return _servletContext.getServlets();
	}

	public void log(Exception arg0, String arg1)
	{
		_servletContext.log(arg0, arg1);
	}

	public void log(String arg0, Throwable arg1)
	{
		_servletContext.log(arg0, arg1);
	}

	public void log(String arg0)
	{
		_servletContext.log(arg0);
	}

	public void removeAttribute(String arg0)
	{
		_servletContext.removeAttribute(arg0);
	}

	public void setAttribute(String arg0, Object arg1)
	{
		_servletContext.setAttribute(arg0, arg1);
	}

	public String getContextPath()
	{
		return null;
	}
}
