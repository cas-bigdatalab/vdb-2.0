package vdb.mydb.jsp.action.editor;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoComplexEditor extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		// String entityStr = request.getParameter("entity");
		String fieldStr = request.getParameter("fieldUrl");
		String data = request.getParameter("data");

		int count = 0;

		vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();

		Field field = tool.getField(fieldStr);

		Entity table = field.getEntity();

		VdbEntity te = (VdbEntity) table;
		AnyBeanDao dao = new AnyBeanDao(table);

		String id = request.getParameter("id");

		AnyBean bean = null;

		bean = dao.lookup(id);

		request.setAttribute("field", field);
		request.setAttribute("bean", bean);
		request.setAttribute("data", data);
	}
}
