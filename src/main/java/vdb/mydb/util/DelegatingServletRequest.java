package vdb.mydb.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class DelegatingServletRequest extends HttpServletRequestWrapper
		implements HttpServletRequest
{
	private String _encoding;

	public DelegatingServletRequest(HttpServletRequest request, String encoding)
	{
		super(request);
		_encoding = encoding;

		try
		{
			request.setCharacterEncoding(encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			// e.printStackTrace();
		}
	}

	public String getRequestURI()
	{
		String requestURI = super.getRequestURI();
		try
		{
			return URLDecoder.decode(requestURI, _encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			return requestURI;
		}
	}
}
