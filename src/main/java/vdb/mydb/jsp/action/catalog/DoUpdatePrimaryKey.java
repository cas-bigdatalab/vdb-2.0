package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.CatalogObjectProxy;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdatePrimaryKey extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		String entityId = request.getParameter("id");
		Entity entity = VdbManager.getEngine().getCatalog().fromId(entityId);

		Identifier pk = (Identifier) VdbManager.getEngine().getCatalog()
				.fromId(mid);

		CatalogObjectProxy meta = new CatalogObjectProxy(pk);
		meta.attach(request);
		String idGenerator = request.getParameter("P_idGenerator");
		if (idGenerator != null)
		{
			pk.setIdGenerator(VdbManager.getEngine().getCatalogContext()
					.getIdGenerator(idGenerator));
		}
		Field field = (Field) VdbManager.getEngine().getCatalog().fromId(
				request.getParameter("field"));
		pk.setField(field);
		// 将field设置为非空
		if (field != null)
			field.set("nullable", "false");

		// 注：如果原有主键不为空，原有主键对应的字段是否为空则无法再还原。这里就不再进行更改
		pk.getEntity().setIdentifier(pk);
		VdbManager.getEngine().getCatalogManager().saveDataSet(
				pk.getEntity().getDataSet());
		VdbManager.getEngine().getCatalog().cacheIn(pk);
		request.setAttribute("datasetID", entity.getDataSet().getId());
		request.setAttribute("entityID", entity.getId());
	}
}
