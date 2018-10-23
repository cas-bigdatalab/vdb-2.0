package vdb.mydb.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil
{
	public static String getRequestPath(HttpServletRequest request)
	{
		String path = (String) request
				.getAttribute("javax.servlet.include.servlet_path");
		String info = (String) request
				.getAttribute("javax.servlet.include.path_info");
		if (path == null)
		{
			path = request.getServletPath();
			info = request.getPathInfo();
		}

		if (info != null)
		{
			path += info;
		}

		return path;
	}
}
