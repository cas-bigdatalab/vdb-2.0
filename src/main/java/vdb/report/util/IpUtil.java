package vdb.report.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IpUtil
{
	// byte buffer
	private static ByteArrayOutputStream baos = new ByteArrayOutputStream();

	// Log
	private static Log log = LogFactory.getLog(IpUtil.class);

	// string buffer
	private static StringBuilder sb = new StringBuilder();

	public static int getInt(String s, int faultValue)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException e)
		{
			return faultValue;
		}
	}

	public static byte[] getIpByteArrayFromString(String ip)
	{
		byte[] ret = new byte[4];
		if (ip.equals("localhost"))
			ip = "127.0.0.1";
		StringTokenizer st = new StringTokenizer(ip, ".");
		try
		{
			ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return ret;
	}

	public static String getIpStringFromBytes(byte[] ip)
	{
		sb.delete(0, sb.length());
		sb.append(ip[0] & 0xFF);
		sb.append('.');
		sb.append(ip[1] & 0xFF);
		sb.append('.');
		sb.append(ip[2] & 0xFF);
		sb.append('.');
		sb.append(ip[3] & 0xFF);
		return sb.toString();
	}

	public static String getString(byte[] b)
	{
		return getString(b, "GBK");
	}

	public static String getString(byte[] b, int offset, int len)
	{
		return getString(b, offset, len, "GBK");
	}

	public static String getString(byte[] b, int offset, int len,
			String encoding)
	{
		try
		{
			return new String(b, offset, len, encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			return new String(b, offset, len);
		}
	}

	public static String getString(byte[] b, String encoding)
	{
		try
		{
			return new String(b, encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			return new String(b);
		}
	}

	public static String getString(ByteBuffer buf)
	{
		baos.reset();
		while (buf.hasRemaining())
		{
			baos.write(buf.get());
		}
		return getString(baos.toByteArray());
	}

	public static String getString(ByteBuffer buf, byte delimit)
	{
		baos.reset();
		while (buf.hasRemaining())
		{
			byte b = buf.get();
			if (b == delimit)
				return getString(baos.toByteArray());
			else
				baos.write(b);
		}
		return getString(baos.toByteArray());
	}

	public static String getString(ByteBuffer buf, byte delimit, int maxLen)
	{
		baos.reset();
		while (buf.hasRemaining() && maxLen-- > 0)
		{
			byte b = buf.get();
			if (b == delimit)
				break;
			else
				baos.write(b);
		}
		while (buf.hasRemaining() && maxLen-- > 0)
			buf.get();
		return getString(baos.toByteArray());
	}

	public static String getString(ByteBuffer buf, int len)
	{
		baos.reset();
		while (buf.hasRemaining() && len-- > 0)
		{
			baos.write(buf.get());
		}
		return getString(baos.toByteArray());
	}

	public static String getString(String s, String srcEncoding,
			String destEncoding)
	{
		try
		{
			return new String(s.getBytes(srcEncoding), destEncoding);
		}
		catch (UnsupportedEncodingException e)
		{
			return s;
		}
	}

	public static boolean isIpEquals(byte[] ip1, byte[] ip2)
	{
		return (ip1[0] == ip2[0] && ip1[1] == ip2[1] && ip1[2] == ip2[2] && ip1[3] == ip2[3]);
	}
}