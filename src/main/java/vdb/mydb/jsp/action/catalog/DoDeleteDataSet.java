package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoDeleteDataSet extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String uri = request.getParameter("uri");
		DataSet dataset = VdbManager.getEngine().getCatalog().fromUri(uri);
		request.setAttribute("dsid", dataset.getId());
		VdbManager.getEngine().getCatalogManager().deleteDataSet(
				VdbManager.getEngine().getDomain(), dataset);
		VdbManager.getEngine().getCatalog().cacheIn(dataset);
	}

}
