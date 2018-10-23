package vdb.mydb.widget.vsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.JDOMException;

import vdb.metacat.Domain;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.mydb.widget.WidgetLayout;
import vdb.mydb.widget.WidgetLayoutDAO;
import vdb.tool.webpub.ThemeTool;
import vdb.tool.webpub.WidgetTool;

public class DoSaveLayout implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		String theme = vc.getParameter("theme");
		if (!theme.equals(new ThemeTool().getTheme().getName()))
		{
			Domain domain = VdbManager.getInstance().getDomain();
			VdbDomain domainEx = ((VdbDomain) domain);
			domain.set("theme", theme);
			VdbManager.getInstance().getCatalogManager().saveDomain(domain);
		}

		String pageStr = vc.getParameter("page");
		String param = vc.getParameter("param");
		String[] widget = param.split(";");

		WidgetLayout widgetlayout = new WidgetLayout();
		widgetlayout.setLayout(widget[0]);
		widgetlayout.setTheme(widget[1]);
		widgetlayout.setName(pageStr);

		Map<String, List> map = new HashMap<String, List>();
		for (int i = 2; i < widget.length; i++)
		{
			List list = new ArrayList();
			String[] com = widget[i].split(":");
			if (com.length > 1 && com[1] != null && com[1].trim().length() > 0)
			{
				String[] componentsList = com[1].split(",");

				for (int j = 0; j < componentsList.length; j++)
				{
					list.add(componentsList[j]);
				}
			}
			map.put(com[0], list);
		}

		widgetlayout.setColumns(map);
		WidgetLayoutDAO wtm = new WidgetTool().getWidgetLayoutDAO();

		try
		{
			wtm.saveWidgetLayout(widgetlayout);
		}
		catch (JDOMException e)
		{
			e.printStackTrace();
		}
	}
}
