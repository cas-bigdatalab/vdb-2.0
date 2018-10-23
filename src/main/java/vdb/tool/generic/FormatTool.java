package vdb.tool.generic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.regex.Pattern;

import cn.csdb.commons.util.TimeUtils;

public class FormatTool
{
	public String formatBytes(Number n, String seperator)
	{
		String format = "%s%s%s";
		double o = n.doubleValue();

		String suffix[] = { "B", "KB", "MB", "GB", "TB", "PB" };
		for (String s : suffix)
		{
			if (o < 1024)
				return String.format(format, new DecimalFormat("###.##")
						.format(o), seperator, s);
			o /= 1024;
		}

		return "";
	}

	public String formatBytes(Number n)
	{
		return formatBytes(n, "");
	}

	public String format(String pattern, Object o)
	{
		if (o == null)
			return null;

		if (pattern == null || pattern.length() == 0)
			pattern = "%s";

		return String.format(pattern, o);
	}

	public String trimToLimit(String str, int length)
	{
		String string = "";
		if (str != null)
		{
			string = str;
		}
		if (string.length() > length)
		{
			string = string.substring(0, length);
		}
		return string;
	}

	public String formatSubStr(String str, int length, String replace)
	{
		return formatSubString(str, length, replace);
	}

	public String replaceFirst(String str, String regex, String replace)
	{
		if (str.indexOf(regex) > 0)
			return str.substring(0, str.indexOf(regex)) + replace
					+ str.substring(str.indexOf(regex) + regex.length());
		return str;
	}

	public String formatSubString(String str, int length, String replace)
	{
		String string = "";
		if (str != null)
		{
			string = str;
		}
		if (string.length() > length)
		{
			string = string.substring(0, length);
		}
		if (replace != null)
		{
			string = string + replace;
		}
		return string;
	}

	public String formatDate(Date o)
	{
		return formatDate(o, null);
	}

	public String formatDate(Date o, String pattern)
	{
		if (pattern == null || pattern.length() == 0)
			pattern = "yyyy-MM-dd";

		try
		{
			return TimeUtils.getTimeString(o, pattern);
		}
		catch (Exception e)
		{
			return "";
		}
	}

	public String encodeUrl(String url) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(url, "utf-8");
	}

	public String encode2(String url, String encoding)
			throws UnsupportedEncodingException
	{
		return URLEncoder.encode(URLEncoder.encode(url, encoding), encoding);
	}

	public String encodeUrl(String url, String encoding)
			throws UnsupportedEncodingException
	{
		return URLEncoder.encode(url, encoding);
	}

	public String decodeUrl(String url) throws UnsupportedEncodingException
	{
		return URLDecoder.decode(url, "utf-8");
	}

	public String decodeUrl(String url, String encoding)
			throws UnsupportedEncodingException
	{
		return URLDecoder.decode(url, encoding);
	}

	public String formatHTMLText(String o)
	{
		if (o == null)
			return "";

		String regs[][] = {
				{ "<\\s*script([^>]*)>([\\s\\S]*?)</\\s*script\\s*>", "" },
				{ "<\\s*style([^>]*)>([\\s\\S]*?)</\\s*style\\s*>", "" },
				{ "<[^>]*>", "" }, { "&nbsp;", " " }, { "&lt;", "<" },
				{ "&gt;", ">" }, { "&amp;", "&" }, { "[ \\t]+", " " },
				{ "\\n\\s*\\r", "" } };
		for (String[] sreg : regs)
		{
			Pattern par = Pattern.compile(sreg[0], Pattern.CASE_INSENSITIVE);
			o = par.matcher(o).replaceAll(sreg[1]);
		}
		return o;
	}

	public int parseInt(String str)
	{
		int i = 0;
		try
		{
			i = Integer.parseInt(str);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return i;
	}

}
