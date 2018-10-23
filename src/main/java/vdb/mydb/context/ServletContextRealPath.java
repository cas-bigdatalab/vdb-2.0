package vdb.mydb.context;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class ServletContextRealPath implements ExtendedProperty,
		ServletContextAware
{
	private ServletContext _servletContext;

	String _path = "";

	public String getString()
	{
		String realPath = _servletContext.getRealPath(_path);
		try
		{
			return new File(realPath).getCanonicalPath();
		}
		catch (IOException e)
		{
			return realPath;
		}
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public void setPath(String path)
	{
		_path = path;
	}
}
