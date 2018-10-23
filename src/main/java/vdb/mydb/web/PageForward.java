package vdb.mydb.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

import vdb.mydb.jsp.RequestHandler;

public class PageForward implements RequestHandler, ServletContextAware
{
	Pattern _pattern;

	private ServletContext _servletContext;

	private PageLocator _pageLocator;

	public boolean handle(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException
	{
		// DO NOT reenter this handle() method!
		if (request.getAttribute(this.getClass().getName()) != null)
			return false;

		String uri = ((HttpServletRequest) request).getRequestURI();
		Matcher matcher = _pattern.matcher(uri);
		if (!matcher.matches())
			return false;

		String target = _pageLocator.getRealPath(uri);
		if (target == null)
		{
			return false;
		}

		Logger.getLogger(this.getClass()).debug(uri + ": " + target);
		request.setAttribute(this.getClass().getName(), this);
		_servletContext.getRequestDispatcher(target).forward(request, response);

		return true;
	}

	public void setPattern(String pattern)
	{
		_pattern = Pattern.compile(pattern);
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public PageLocator getPageLocator()
	{
		return _pageLocator;
	}

	public void setPageLocator(PageLocator pageLocator)
	{
		_pageLocator = pageLocator;
	}
}
