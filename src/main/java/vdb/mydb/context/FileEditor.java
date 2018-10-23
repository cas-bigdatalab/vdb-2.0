package vdb.mydb.context;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.web.context.ServletContextAware;

public class FileEditor extends PropertyEditorSupport implements
		ServletContextAware
{
	ServletContext _servletContext;

	@Override
	public void setAsText(String text) throws IllegalArgumentException
	{
		Logger.getLogger(this.getClass()).debug("setting file: " + text);

		File file = new File(text);
		if (!file.isAbsolute())
		{
			file = new File(_servletContext.getRealPath("/"), text);
		}

		try
		{
			setValue(file.getCanonicalFile());
		}
		catch (IOException e)
		{
			setValue(file);
		}
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}
}