package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.vtl.action.ServletActionProxy;

public class DoGetTypeNameAction extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		DoGetTypeName typeName = new DoGetTypeName();
		request.setAttribute("typeName", typeName);
	}
}
