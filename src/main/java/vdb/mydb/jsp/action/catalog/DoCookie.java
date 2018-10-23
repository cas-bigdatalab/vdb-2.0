package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import vdb.mydb.vtl.action.ServletActionProxy;

public class DoCookie extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String datasetNameCookie = "";
		Cookie cookies[] = ((HttpServletRequest) request).getCookies();

		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				Cookie cookie = cookies[i];
				if ("datasetName".equals(cookie.getName()))
				{
					datasetNameCookie = cookie.getValue();
				}
			}
		}
		request.setAttribute("datasetNameCookie", datasetNameCookie);
	}
}