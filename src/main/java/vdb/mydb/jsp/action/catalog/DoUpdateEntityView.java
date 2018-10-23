package vdb.mydb.jsp.action.catalog;

import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.CatalogObject;
import vdb.metacat.View;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateEntityView extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		int count = Integer.parseInt(request.getParameter("count"));
		VdbEntity table = VdbManager.getEngine().getCatalog().fromId(
				request.getParameter("mid"));

		for (int i = 0; i < count; i++)
		{
			String viewName = request.getParameter("viewTypeName" + (i + 1));
			View view = table.getView(viewName);
			if (view == null)
				view = table.addView(viewName);

			view.removeItems();// 先清空所有的字段
			String ids = request.getParameter("ids" + (i + 1));
			StringTokenizer st = new StringTokenizer(ids, ";");
			while (st.hasMoreTokens())
			{
				String id = st.nextToken();
				if (id.length() > 0)
				{
					CatalogObject meta = VdbManager.getEngine().getCatalog()
							.fromId(id);
					view.addItem((CatalogObject) meta);
				}
			}
			table.getDataSet().removeView(table.getView(viewName));
			view.setName(viewName);
			table.getDataSet().addView(view);
		}

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				table.getDataSet());
	}
}
