package vdb.mydb.jsp.action.editor;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Field;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanImpl;
import vdb.mydb.typelib.type.VdbRefType;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoGetReferenceTitle extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{

		String fieldurl = request.getParameter("field");
		String key = request.getParameter("key");

		try
		{
			vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
			Field field = tool.getField(fieldurl);

			List<AnyBean> anyBeans = ((VdbRefType) field.getTypeDriver())
					.getOptions();

			for (AnyBean ab : anyBeans)
			{
				String temp = ((AnyBeanImpl) ab).getItemId().toString();
				String id = temp.substring(temp.indexOf("/") + 1);
				if (id.equalsIgnoreCase(key))
				{
					request.setAttribute("data", ab.getTitle().getTitle());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
