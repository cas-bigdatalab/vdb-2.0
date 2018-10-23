package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Relation;
import vdb.metacat.util.CatalogUtils;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateO2M extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		String thisEntityId = request.getParameter("thisEntity");
		String thatEntityId = request.getParameter("thatEntity");
		String thatForeignKeyName = request.getParameter("thatForeignKeyName");
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Relation relationDef;
		if (mid == null || mid.length() == 0)
		{
			String uri = request.getParameter("uri");
			DataSet dataset = VdbManager.getEngine().getCatalog().fromUri(uri);
			relationDef = CatalogUtils.addOneToMany(VdbManager.getEngine()
					.getCatalogContext(), (VdbDataSet) dataset,
					(Entity) catalog.fromId(thatEntityId), (Entity) catalog
							.fromId(thisEntityId), thatForeignKeyName);
		}
		else
		{
			relationDef = catalog.fromId(mid);
			relationDef.getKeyA()
					.setTarget(
							(Entity) catalog.fromId(request
									.getParameter("thatEntity")));
			relationDef.getKeyB().setColumnName(
					request.getParameter("thatForeignKeyName"));
			relationDef.getKeyB()
					.setTarget(
							(Entity) catalog.fromId(request
									.getParameter("thisEntity")));
		}
		VdbManager.getEngine().getCatalogManager().saveDataSet(
				relationDef.getDataSet());
		catalog.cacheIn(relationDef);
		catalog.cacheIn(relationDef.getKeyA());
		catalog.cacheIn(relationDef.getKeyB());
	}
}
