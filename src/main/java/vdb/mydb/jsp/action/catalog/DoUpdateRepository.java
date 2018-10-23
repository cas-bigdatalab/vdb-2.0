package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Repository;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.metacat.CatalogObjectProxy;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateRepository extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		if (mid != null && mid.length() > 0)
		{
			Repository repo = VdbManager.getEngine().getCatalog().fromId(mid);
			CatalogObjectProxy meta = new CatalogObjectProxy(repo);
			meta.attach(request);

			VdbManager.getEngine().getCatalogManager().saveDataSet(
					repo.getDataSet());

			JdbcSourceManager.getInstance().unregisterJdbcSource(
					repo.getDataSet());

			VdbManager.getEngine().getCatalog().cacheIn(repo.getDataSet());
		}
	}
}
