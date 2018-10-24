/*
 * 创建日期 2005-1-10
 */
package cn.csdb.commons.orm;

import java.io.File;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author bluejoe
 */
public class XmlMapperServlet extends HttpServlet
{
	private static XmlMapper _mapper;

	public static BeanMapper getMapper()
	{
		if (_mapper == null)
			throw new RuntimeException(
					"mapper is null! pls make sure that `XmlMapperServlet` has been loaded.");

		return _mapper;
	}

	/*
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void init() throws ServletException
	{
		String xmlPath = getInitParameter("xml-path");

		if (xmlPath == null)
		{
			System.out.println("parameter `xml-path` is empty!");
			return;
		}

		try
		{
			File f = new File(xmlPath);
			// relative path
			if (!f.isAbsolute())
			{
				f = new File(getServletContext().getRealPath("/"), xmlPath);
			}

			xmlPath = f.getCanonicalPath();

			if (!f.exists())
			{
				throw new ServletException(MessageFormat
						.format("can not find xml file `{0}`",
								new Object[] { xmlPath }));
			}
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}

		System.out.println(MessageFormat.format(
				"building mapper from `{0}` ...", new Object[] { xmlPath }));

		try
		{
			_mapper = new XmlMapper();
			_mapper.load(xmlPath);

			System.out.println(MessageFormat.format(
					"XmlMapper built successfully!", new Object[] {}));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
