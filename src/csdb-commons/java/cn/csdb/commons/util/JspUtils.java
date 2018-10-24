/*
 * 创建日期 2005-10-17
 */
package cn.csdb.commons.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author bluejoe
 */
public class JspUtils
{
	public static String getFullQueryString(HttpServletRequest request)
	{
		return getFullQueryString(request, null);
	}

	public static String getFullQueryString(HttpServletRequest request,
			String except)
	{
		String qs = null;
		Enumeration i = request.getParameterNames();

		while (i.hasMoreElements())
		{
			String pn = (String) i.nextElement();
			if (except != null && except.equals(pn))
				continue;

			String[] pv = request.getParameterValues(pn);
			for (int m = 0; m < pv.length; m++)
			{
				if (qs == null)
				{
					qs = "";
				}
				else
				{
					qs += "&";
				}

				qs += pn + "=" + pv[m];
			}
		}

		return qs;
	}

	public static String getFullURI(HttpServletRequest request)
	{
		return getFullURI(request, getFullQueryString(request));
	}

	public static String getFullURI(HttpServletRequest request,
			String quertString)
	{
		// TODO: 可能是forward？
		return getFullURI(request.getRequestURI(), quertString);
	}

	public static String getFullURI(String uri, String quertString)
	{
		return uri + (quertString == null ? "" : "?" + quertString);
	}

	/**
	 * 输出request内容
	 * 
	 * @param request
	 */
	public static String listParameters(HttpServletRequest request)
	{
		String out = "";
		Enumeration e = request.getParameterNames();

		while (e.hasMoreElements())
		{
			String name = (String) e.nextElement();
			String[] values = request.getParameterValues(name);

			out += name + ":";
			for (int i = 0; i < values.length; i++)
			{
				out += "[" + values[i] + "] ";
			}

			out += "\r\n";
		}

		return out;
	}
}
