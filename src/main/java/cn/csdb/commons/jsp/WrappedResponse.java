package cn.csdb.commons.jsp;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 对HttpServletResponse的包装类，完成response信息的重定向。
 * 
 * @author bluejoe
 */
public class WrappedResponse extends HttpServletResponseWrapper
{
	private ServletOutputStream sos;

	/**
	 * @param response
	 *            待输出的response
	 * @param os
	 *            输出目的流
	 */
	public WrappedResponse(HttpServletResponse response, OutputStream os)
	{
		super(response);
		sos = new WrappedOutputStream(os);
	}

	public ServletOutputStream getOutputStream() throws IOException
	{
		return sos;
	}
}

/**
 * @author bluejoe
 */
class WrappedOutputStream extends ServletOutputStream
{
	private OutputStream os;

	public WrappedOutputStream(OutputStream os)
	{
		this.os = os;
	}

	public void write(int arg0) throws IOException
	{
		os.write(arg0);
	}

	public void close() throws IOException
	{
		os.close();
	}
}
