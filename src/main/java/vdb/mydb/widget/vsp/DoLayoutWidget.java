package vdb.mydb.widget.vsp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.mydb.widget.Widget;
import vdb.tool.webpub.WidgetTool;

public class DoLayoutWidget extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String widgetName = request.getParameter("widget");
		String closeButton = request.getParameter("closeButton");

		Widget widget = new WidgetTool().getWidgetFactory().getWidget(
				widgetName);
		Map map = new HashMap();
		map.putAll(widget.getProperties());

		StringBuffer strbu = new StringBuffer();
		String widgetHeaderStyle = "";
		String widgetBodyStyle = "";
		String bgcolor = map.get("bgcolor") == null ? "default" : map.get(
				"bgcolor").toString();
		String lineWidth = map.get("lineWidth") == null ? "default" : map.get(
				"lineWidth").toString();
		String lineColor = map.get("lineColor") == null ? "default" : map.get(
				"lineColor").toString();
		String headerAlign = map.get("headerAlign") == null ? "default" : map
				.get("headerAlign").toString();
		String padding = map.get("padding") == null ? "default" : map.get(
				"padding").toString();

		widgetHeaderStyle = "border:" + " " + lineColor + ";" + "text-align:"
				+ headerAlign + ";" + "background:" + bgcolor;
		widgetBodyStyle = "border:" + lineWidth + ";" + "padding:" + padding;

		if (map.get("headerDisplay") == null
				|| map.get("headerDisplay").toString().trim().length() <= 0)
		{
			widgetHeaderStyle = "";
			widgetBodyStyle = "";
		}

		if (map.get("headerDisplay") != null
				&& map.get("headerDisplay").equals("false"))
		{
			widgetHeaderStyle += ";border-style:dashed;border-color:red;border-width:1px;";
		}

		strbu.append("<dt class='basebat' style='" + widgetHeaderStyle
				+ "'><span style='float:left;'>");
		String widgetTitle = widget.getProperties().get("caption") == null ? widget
				.getTitle()
				: widget.getProperties().get("caption");
		strbu.append(widgetTitle + "</span>");
		if (closeButton != null && closeButton.equals("yes"))
		{
			strbu.append("<span style='float:right;'>"
					+ "<a href='#' onclick=\"updateWidget" + "('"
					+ widget.getName() + "');\">修改</a>&nbsp;&nbsp;");
			strbu
					.append("<a href='#' onclick=\"removeWidget"
							+ "(this.parentNode.parentNode.parentNode);\">删除</a></span>");
		}

		strbu.append("</dt>");

		request.setAttribute("widgetBodyStyle", widgetBodyStyle);
		request.setAttribute("data", strbu);
		request.setAttribute("widget", widget);
	}
}
