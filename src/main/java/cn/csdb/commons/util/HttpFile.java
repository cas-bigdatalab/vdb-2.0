/*
 * Created on 2008-2-27
 */
package cn.csdb.commons.util;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpFile
{
	private InputStream _stream;

	private long _length;

	public HttpFile(InputStream is, long length)
	{
		_stream = is;
		_length = length;
	}

	public long download(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		long position = 0;
		response.setHeader("Accept-Ranges", "bytes");
		if (request.getHeader("Range") != null)
		{
			response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
			String sclient = request.getHeader("Range");
			sclient = sclient.substring("bytes=".length());
			if (sclient.charAt(sclient.length() - 1) == '-')
				sclient = sclient.substring(0, sclient.length() - 1);
			position = Long.parseLong(sclient);
		}

		response.setHeader("Content-Length", new Long(_length - position)
				.toString());

		if (position != 0)
		{
			response.setHeader("Content-Range", "bytes "
					+ new Long(position).toString() + "-"
					+ new Long(_length - 1).toString() + "/"
					+ new Long(_length).toString());
		}

		long k = 0;
		int ibuffer = 65536;
		byte[] bytes = new byte[ibuffer];

		try
		{
			if (position != 0)
				_stream.skip(position);
			while (k < _length)
			{
				int bs = _stream.read(bytes, 0, ibuffer);
				if (bs <= 0)
					break;

				response.getOutputStream().write(bytes, 0, bs);
				response.getOutputStream().flush();
				k += bs;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return position;
	}
}
