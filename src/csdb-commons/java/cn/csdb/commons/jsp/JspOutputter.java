/*
 * Created on 2003-6-16
 * 
 */
package cn.csdb.commons.jsp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 该类用以实现JSP页面输出的重定向。
 * 
 * @author bluejoe
 */
public class JspOutputter
{
	private String url;

	private ServletContext application;

	private HttpServletRequest request;

	private HttpServletResponse response;

	public JspOutputter(String url, ServletContext application,
			HttpServletRequest request, HttpServletResponse response)
	{
		this.url = url;
		this.application = application;
		this.request = request;
		this.response = response;
	}

	public void serialize(OutputStream os) throws Exception
	{
		HttpServletResponse hsr;

		try
		{
			hsr = new WrappedResponse(this.response, os);
			application.getRequestDispatcher(url).forward(request, hsr);
		}
		finally
		{
			hsr = null;

			if (os != null)
			{
				os.close();
			}
		}
	}

	/**
	 * 存储为指定文件
	 * 
	 * @param path
	 * @return
	 */
	public File toFile(String path)
	{
		File file = new File(path);
		FileOutputStream fos;
		try
		{
			fos = new FileOutputStream(file);
			serialize(fos);

			return file;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			serialize(baos);
			return baos.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
}
