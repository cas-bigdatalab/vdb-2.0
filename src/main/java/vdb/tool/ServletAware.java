package vdb.tool;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;

public interface ServletAware
{
	public void setServletContext(ServletContext servletContext);

	public void setRequest(ServletRequest request);
}
