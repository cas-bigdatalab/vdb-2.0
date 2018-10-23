package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoDeleteEntity extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		Entity entity = VdbManager.getEngine().getCatalog().fromId(mid);

		request.setAttribute("dsid", entity.getDataSet().getId());
		request.setAttribute("entityId", entity.getId());
		VdbManager.getEngine().getCatalogManager().removeEntity(entity);
		VdbManager.getEngine().getCatalogManager().saveDataSet(
				entity.getDataSet());
	}

}
