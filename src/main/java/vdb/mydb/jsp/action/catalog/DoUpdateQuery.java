package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Query;
import vdb.metacat.util.CatalogUtils;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.CatalogObjectProxy;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateQuery extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		String P_entity = request.getParameter("P_entity");
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Query query = catalog.fromId(mid);
		if (query == null)
		{
			String dsid = request.getParameter("dsid");
			DataSet dataset = VdbManager.getEngine().getCatalog().fromId(dsid);

			query = CatalogUtils.addQuery(VdbManager.getEngine()
					.getCatalogContext(), (VdbDataSet) dataset, request
					.getParameter("P_name"));
		}
		query.setEntity((Entity) catalog.fromUri(P_entity));

		CatalogObjectProxy meta = new CatalogObjectProxy(query);
		meta.attach(request);

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				query.getDataSet());
		VdbManager.getEngine().getCatalog().cacheIn(query);
	}

}
