package vdb.mydb.jsp.action.catalog;

import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.CatalogObject;
import vdb.metacat.DataSet;
import vdb.metacat.View;
import vdb.metacat.util.CatalogUtils;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateDataSetView extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String uri = request.getParameter("uri");
		int count = Integer.parseInt(request.getParameter("count"));
		DataSet dataset = VdbManager.getInstance().getCatalog().fromUri(uri);

		for (int i = 0; i < count; i++)
		{
			String viewTypeName = request
					.getParameter("viewTypeName" + (i + 1));
			View view = (View) ((VdbDataSet) dataset).getView(viewTypeName);
			if (view == null)
				view = (View) CatalogUtils.addView(VdbManager.getInstance()
						.getCatalogContext(), (VdbDataSet) dataset,
						viewTypeName);

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
						view.addItem((CatalogObject) meta);
				}
			}
			dataset.removeView(((VdbDataSet) dataset).getView(viewTypeName));
			dataset.addView(view);
		}

		VdbManager.getInstance().getCatalogManager().saveDataSet(dataset);
	}
}
