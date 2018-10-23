package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.util.CatalogUtils;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.CatalogObjectProxy;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateEntity extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Entity entity = catalog.fromId(mid);
		if (entity == null)
		{
			String dsid = request.getParameter("dsid");
			DataSet dataset = catalog.fromId(dsid);

			entity = VdbManager.getEngine().getCatalogContext()
					.create("entity");
			entity.setDataSet(dataset);
			entity.setName(request.getParameter("P_name"));
			CatalogUtils.createIdentifier(VdbManager.getEngine()
					.getCatalogContext(), entity);
			dataset.addEntity(entity);
		}

		CatalogObjectProxy meta = new CatalogObjectProxy(entity);
		meta.attach(request);

		entity.setTitleField((Field) catalog.fromId(request
				.getParameter("titleField")));
		entity.setTimeStampField((Field) catalog.fromId(request
				.getParameter("timeStampField")));
		entity.setEditorField((Field) catalog.fromId(request
				.getParameter("editorField")));
		entity.setOrderField((Field) catalog.fromId(request
				.getParameter("orderField")));
		entity.setGroupField((Field) catalog.fromId(request
				.getParameter("groupField")));

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				entity.getDataSet());
		catalog.cacheIn(entity);
		catalog.cacheIn(entity.getIdentifier());
	}
}
