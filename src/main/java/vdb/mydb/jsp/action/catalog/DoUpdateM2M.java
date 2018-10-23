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

public class DoUpdateM2M extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		String thisEntityId = request.getParameter("thisEntity");
		String thatEntityId = request.getParameter("thatEntity");
		String relationTableName = request.getParameter("relationTableName");
		String thisForeignKeyName = request.getParameter("thisForeignKeyName");
		String thatForeignKeyName = request.getParameter("thatForeignKeyName");
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Relation relationDef;
		if (mid == null || mid.length() == 0)
		{
			String uri = request.getParameter("uri");
			DataSet dataset = VdbManager.getEngine().getCatalog().fromUri(uri);
			relationDef = CatalogUtils.addManyToMany(VdbManager.getEngine()
					.getCatalogContext(), (VdbDataSet) dataset,
					relationTableName, (Entity) catalog.fromId(thisEntityId),
					thisForeignKeyName, (Entity) catalog.fromId(thatEntityId),
					thatForeignKeyName);
		}
		else
		{
			relationDef = catalog.fromId(mid);
			relationDef.setAssocTableName(request
					.getParameter("relationTableName"));
			relationDef.getKeyA().setColumnName(
					request.getParameter("thisForeignKeyName"));
			relationDef.getKeyA()
					.setTarget(
							(Entity) catalog.fromId(request
									.getParameter("thisEntity")));
			relationDef.getKeyB().setColumnName(
					request.getParameter("thatForeignKeyName"));
			relationDef.getKeyB()
					.setTarget(
							(Entity) catalog.fromId(request
									.getParameter("thatEntity")));
		}

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				relationDef.getDataSet());
		catalog.cacheIn(relationDef);
		catalog.cacheIn(relationDef.getKeyA());
		catalog.cacheIn(relationDef.getKeyB());
	}

}
