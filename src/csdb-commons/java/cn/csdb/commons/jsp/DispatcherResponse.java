package cn.csdb.commons.jsp;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author bluejoe
 */
public class DispatcherResponse extends HttpServletResponseWrapper
{
	private boolean _allows404 = false;

	private int _errorCode = -1;

	private Throwable _exception;

	private HttpServletRequest _request;

	/**
	 * @param arg0
	 */
	public DispatcherResponse(HttpServletRequest req, HttpServletResponse res,
			String uri, boolean allows404)
	{
		super(res);
		_request = req;
		_allows404 = allows404;
	}

	/**
	 * @return
	 */
	public int getErrorCode()
	{
		return _errorCode;
	}

	/**
	 * @return
	 */
	public Throwable getException()
	{
		return _exception;
	}

	/*
	 * £¨·Ç Javadoc£©
	 * 
	 * @see javax.servlet.http.HttpServletResponse#sendError(int)
	 */
	public void sendError(int arg0) throws IOException
	{
		_errorCode = arg0;
		_exception = (Throwable) _request
				.getAttribute("javax.servlet.jsp.jspException");
		if (arg0 != 404 || _allows404)
		{
			super.sendError(arg0);
		}
	}
}
