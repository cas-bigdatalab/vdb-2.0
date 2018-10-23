package vdb.mydb.jsp.action.editor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.typelib.type.VdbTreeType;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoGetTree extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String fid = request.getParameter("fid");
		String val = request.getParameter("val");
		Catalog catalog = VdbManager.getInstance().getCatalog();
		Field field = catalog.fromId(fid);
		Map<String, String> options = ((VdbTreeType) field.getTypeDriver())
				.getOptions();
		Iterator it = options.entrySet().iterator();
		StringBuilder html = new StringBuilder();
		html.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		html.append("<tree id=\"0\" >\n");
		List<Map.Entry<String, String>> optionsList = new ArrayList<Map.Entry<String, String>>();
		while (it.hasNext())
		{
			Map.Entry<String, String> option = (Map.Entry<String, String>) it
					.next();
			optionsList.add(option);
		}
		for (int i = 0; i < optionsList.size(); i++)
		{
			if (val != null
					&& optionsList.get(i).getKey().equalsIgnoreCase(val))
			{
				html.append("<item text='" + optionsList.get(i).getValue()
						+ "' id='" + optionsList.get(i).getKey()
						+ "' checked='1'>\n");
			}
			else
			{
				html.append("<item text='" + optionsList.get(i).getValue()
						+ "' id='" + optionsList.get(i).getKey() + "'>\n");
			}
			if (i < optionsList.size() - 1
					&& optionsList.get(i + 1).getValue().indexOf(
							optionsList.get(i).getKey()) < 0)
			{
				int length = (optionsList.get(i).getKey().length()) / 4;
				int length1 = (optionsList.get(i + 1).getKey().length()) / 4;
				int count = length - length1;
				for (int j = 0; j < count + 1; j++)
				{
					html.append("</item>\n");
				}
			}
			if (i == optionsList.size() - 1)
			{
				int lth = (optionsList.get(i).getKey().length()) / 4;
				int ct = lth;
				for (int j = 0; j < ct; j++)
				{
					html.append("</item>\n");
				}
			}
		}

		html.append("</tree>");
		request.setAttribute("tree", html.toString());
	}
}
