package vdb.mydb.jsp.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.security.VdbGroup;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;

public class DoGetMenu extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		List<VdbGroup> groups = new AuthTool().getGroupList();
		StringBuilder html = new StringBuilder();
		html.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		html.append("<tree id=\"0\" >\n");
		for (int i = 0; i < groups.size(); i++)
		{
			html.append("<item text='"
					+ groups.get(i).get("GROUPNAME").toString() + "' id='"
					+ groups.get(i).get("GROUPCODE").toString() + "'>\n");
			if (i < groups.size() - 1
					&& groups.get(i + 1).get("GROUPCODE").toString().indexOf(
							groups.get(i).get("GROUPCODE").toString()) < 0)
			{
				int length = (groups.get(i).get("GROUPCODE").toString()
						.length() - 1) / 5;
				int length1 = (groups.get(i + 1).get("GROUPCODE").toString()
						.length() - 1) / 5;
				int count = length - length1;
				for (int j = 0; j < count + 1; j++)
				{
					html.append("</item>\n");
				}
			}
			if (i == groups.size() - 1)
			{
				int lth = (groups.get(i).get("GROUPCODE").toString().length() - 1) / 5;
				int ct = lth;
				for (int j = 0; j < ct; j++)
				{
					html.append("</item>\n");
				}
			}
		}
		html.append("</item>");
		html.append("</tree>");
		request.setAttribute("menu", html.toString());
	}
}
