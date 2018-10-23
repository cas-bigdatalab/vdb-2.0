package vdb.mydb.jsp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.mydb.context.RequestContext2VtlContextAdapter;
import vdb.mydb.context.VspContextInjections;

public class RequestContextInjection implements Filter
{
	VspContextInjections _contextInjections;

	public void destroy()
	{
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws ServletException, IOException
	{
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		RequestContext2VtlContextAdapter ctx = new RequestContext2VtlContextAdapter(
				request);
		_contextInjections.inject(ctx);
		arg2.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

	public void setContextInjections(VspContextInjections contextInjections)
	{
		_contextInjections = contextInjections;
	}
}
