package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Query;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoDeleteQuery extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse arg1,
			ServletContext arg2) throws Exception
	{
		String mid = request.getParameter("mid");
		Query query = VdbManager.getEngine().getCatalog().fromId(mid);
		request.setAttribute("dsid", query.getDataSet().getId());
		request.setAttribute("queryId", query.getId());
		query.getDataSet().removeQuery(query);

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				query.getDataSet());
	}

}
