package vdb.mydb.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class CustomResourceLoader implements ResourceLoader,
		ServletContextAware
{
	private ServletContext _servletContext;

	PageLocator _pageLocator;

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public InputStream getResourceStream(String arg0)
	{
		if (_pageLocator == null)
			return null;

		String realPath = _pageLocator.getRealPath(arg0);
		if (realPath == null)
			return null;

		try
		{
			return new FileInputStream(new File(_servletContext
					.getRealPath(realPath)));
		}
		catch (FileNotFoundException e)
		{
			return null;
		}
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
