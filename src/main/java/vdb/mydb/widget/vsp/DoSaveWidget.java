package vdb.mydb.widget.vsp;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.mydb.widget.Widget;
import vdb.mydb.widget.WidgetDAO;
import vdb.mydb.widget.WidgetFactory;
import vdb.tool.webpub.WidgetTool;

public class DoSaveWidget implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		HttpServletRequest request = vc.getRequest();
		String name = request.getParameter("name");

		Properties propsWrite = new Properties();

		Enumeration enum1 = request.getParameterNames();
		while (enum1.hasMoreElements())
		{
			String key = (String) enum1.nextElement();
			if (key.startsWith("p_"))
			{
				propsWrite.setProperty(key.substring(2), request
						.getParameter(key));
			}
		}

		WidgetFactory widgetFactory = new WidgetTool().getWidgetFactory();
		Widget widget = widgetFactory.getWidget(name);
		widget.setProperties(propsWrite);
		new WidgetDAO().saveWidget(widget);
		request.setAttribute("widget", widget);
		request.setAttribute("reload", request.getParameter("reload"));
	}
}
