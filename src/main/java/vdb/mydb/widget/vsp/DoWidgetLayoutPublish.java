package vdb.mydb.widget.vsp;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.mydb.widget.WidgetLayout;
import vdb.mydb.widget.WidgetLayoutDAO;
import vdb.tool.webpub.WidgetTool;

public class DoWidgetLayoutPublish implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		String layoutName = (String) vc.get("layoutName");

		WidgetLayoutDAO wtm = new WidgetTool().getWidgetLayoutDAO();
		WidgetLayout wl = wtm.loadWidgetLayout(layoutName);

		vc.put("widgetLayout", wl);
	}
}
