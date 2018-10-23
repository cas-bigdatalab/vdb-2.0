package vdb.mydb.vtl.action;

import java.util.HashMap;
import java.util.Map;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.fs.page.EditField;
import vdb.metacat.fs.page.UpdateItemPage;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.jsp.ViewRenderContext;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;

public class UpdateBeanForm implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		String turi = (String) vc.get("uri");
		UpdateItemPage page = (UpdateItemPage) vc.get("page");
		String id = java.net.URLDecoder.decode(vc.getParameter("id"), "UTF-8");

		Entity table = null;
		table = (Entity) VdbManager.getEngine().getCatalog().fromUri(turi);

		VdbEntity te = (VdbEntity) table;
		AnyBeanDao dao = new AnyBeanDao(table);
		AnyBean bean = null;
		bean = dao.lookup(id);

		vc.put("bean", bean);
		vc.put("tid", table.getId());
		vc.put("id", id);

		Map<String, ViewRenderContext> ctxMap = new HashMap<String, ViewRenderContext>();
		for (EditField ef : page.getEditFields())
		{
			Field field = ef.getField();
			ViewRenderContext vrc = new ViewRenderContext();
			vrc.setData(bean.get(field));
			vrc.setVisible(true);
			vrc.setReadonly(false);
			if (ef.isReadonly())
				vrc.setReadonly(true);
			ctxMap.put(field.getUri(), vrc);

			if (field == table.getIdentifier().getField()
					&& te.isAutoPrimaryKey())
				vrc.setReadonly(true);

			if (field == table.getEditorField())
				vrc.setVisible(false);

			if (field.isCollection())
				vrc.setVisible(false);

			if (field == table.getTimeStampField())
				vrc.setVisible(false);

			if (field == table.getGroupField())
				vrc.setVisible(false);

			if (field.getType().isReadonly())
				vrc.setReadonly(true);
		}

		vc.put("ctxMap", ctxMap);
		vc.put("entity", table);
	}
}
