package vdb.mydb.jsp.action.editor;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.mydb.vtl.action.ServletActionProxy;

public class ValidateIdExisted extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String entityUri = request.getParameter("entityUri");
		String idValue = request.getParameter("idValue");

		vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
		Entity entity = tool.getEntity(entityUri);
		vdb.mydb.bean.AnyBeanDao dao = new vdb.mydb.bean.AnyBeanDao(entity);
		String result = "0";
		if (dao.lookup(idValue) != null)
			result = "1";

		request.setAttribute("result", result);
	}
}