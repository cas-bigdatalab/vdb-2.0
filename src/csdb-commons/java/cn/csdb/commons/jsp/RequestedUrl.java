/*
 * 创建日期 2005-9-29
 */
package cn.csdb.commons.jsp;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bluejoe
 */
public class RequestedUrl
{
	public class RequestedURLDecoder
	{
		private RequestedUrl _uri;

		public RequestedURLDecoder(RequestedUrl requestedURI)
		{
			this._uri = requestedURI;
		}

		public String get(String encoding) throws UnsupportedEncodingException
		{
			return URLDecoder.decode(_uri.getString(), encoding);
		}
	}

	public class RequestedURLEncoder
	{
		private RequestedUrl _uri;

		public RequestedURLEncoder(RequestedUrl requestedURI)
		{
			this._uri = requestedURI;
		}

		public String get(String encoding) throws UnsupportedEncodingException
		{
			return URLEncoder.encode(_uri.getString(), encoding);
		}
	}

	private String _uri;

	public RequestedUrl(HttpServletRequest request)
	{
		_uri = request.getRequestURI();
		String qs = request.getQueryString();
		if (qs != null)
			_uri += "?" + qs;
	}

	public RequestedUrl(String uri)
	{
		_uri = uri;
	}

	public RequestedURLDecoder getDecoder()
	{
		return new RequestedURLDecoder(this);
	}

	public RequestedURLEncoder getEncoder()
	{
		return new RequestedURLEncoder(this);
	}

	/**
	 * @return
	 */
	public String getString()
	{
		return _uri;
	}

	public String toString()
	{
		return _uri;
	}
}
