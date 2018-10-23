package vdb.mydb.jsp.action.catalog;

import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.CatalogObject;
import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.View;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateDomainView extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		Domain domain = VdbManager.getInstance().getDomain();
		int count = Integer.parseInt(request.getParameter("count"));

		for (int i = 0; i < count; i++)
		{
			String viewName = request.getParameter("viewTypeName" + (i + 1));

			View view = (View) ((VdbDomain) domain).internalGetView(viewName);
			if (view == null)
				view = (View) ((VdbDomain) domain).addView(viewName);

			view.removeItems();
			String ids = request.getParameter("ids" + (i + 1));
			StringTokenizer st = new StringTokenizer(ids, ";");
			while (st.hasMoreTokens())
			{
				String id = st.nextToken();
				if (id.length() > 0)
				{
					CatalogObject meta = VdbManager.getInstance().getCatalog()
							.fromId(id);
					if (meta != null)
						view.addItem((DataSet) meta);
				}
			}
		}

		VdbManager.getInstance().getCatalogManager().saveDomain(domain);
	}
}
