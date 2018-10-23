package vdb.mydb.web;

import java.io.InputStream;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.view.WebappResourceLoader;

public class VspResourceLoader extends WebappResourceLoader
{
	static ResourceLoader _innerResourceLoader = null;

	public InputStream getResourceStream(String arg0)
			throws ResourceNotFoundException
	{
		if (_innerResourceLoader != null)
		{
			InputStream is = _innerResourceLoader.getResourceStream(arg0);
			if (is != null)
				return is;
		}

		return super.getResourceStream(arg0);
	}

	public static ResourceLoader getInnerResourceLoader()
	{
		return _innerResourceLoader;
	}

	public static void setInnerResourceLoader(ResourceLoader resourceLoader)
	{
		_innerResourceLoader = resourceLoader;
	}
}
