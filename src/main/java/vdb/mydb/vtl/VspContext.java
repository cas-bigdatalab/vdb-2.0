package vdb.mydb.vtl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.context.Context;

public interface VspContext extends Context
{
	public String getParameter(String name);

	public HttpServletRequest getRequest();

	public String getRequestURI();

	public HttpServletResponse getResponse();

	public ServletContext getServletContext();
}