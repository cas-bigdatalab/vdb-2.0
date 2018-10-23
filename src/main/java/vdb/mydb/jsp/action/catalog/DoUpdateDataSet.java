package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.CatalogObjectProxy;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateDataSet extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		boolean isNew = "true".equalsIgnoreCase(request.getParameter("isNew")
				.trim());
		Domain domain = VdbManager.getEngine().getDomain();
		Catalog catalog = VdbManager.getEngine().getCatalog();
		DataSet dataset;

		if (isNew)
		{
			String uri = request.getParameter("name");
			dataset = ((VdbDomain) domain).createDataSet(uri);
		}
		else
		{
			String dsid = request.getParameter("dsid");
			dataset = catalog.fromId(dsid);
		}

		CatalogObjectProxy meta = new CatalogObjectProxy(dataset);
		meta.attach(request);
		VdbManager.getEngine().getCatalogManager().saveDataSet(dataset);
		if (isNew)
		{
			domain.addDataSet(dataset);
		}

		// 将更改写入缓存
		catalog.cacheIn(dataset);
		request.setAttribute("ds", dataset);
	}
}
