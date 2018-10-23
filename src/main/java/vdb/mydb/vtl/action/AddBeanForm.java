package vdb.mydb.vtl.action;

import java.util.HashMap;
import java.util.Map;

import vdb.metacat.Catalog;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.fs.page.AddItemPage;
import vdb.metacat.fs.page.EditField;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.jsp.ViewRenderContext;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;

public class AddBeanForm implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		String turi = (String) vc.get("uri");
		AddItemPage page = (AddItemPage) vc.get("page");
		Catalog catalog = VdbManager.getEngine().getCatalog();
		Entity table = (Entity) catalog.fromUri(turi);
		VdbEntity te = (VdbEntity) table;
		AnyBeanDao dao = new AnyBeanDao(table);
		AnyBean bean = null;
		bean = dao.create();
		vc.put("bean", bean);
		vc.put("tid", table.getId());
		Map<String, ViewRenderContext> ctxMap = new HashMap<String, ViewRenderContext>();
		for (EditField ef : page.getEditFields())
		{
			// 首先全部添加到vrc，并设置可见、可写，指定默认值，然后依次过滤，从page对象中设置字段的readonly属性，然后设置不可见字段
			Field field = ef.getField();
			ViewRenderContext vrc = new ViewRenderContext();
			vrc.setData(bean.get(field));
			if (ef.getDefaultValue() != null
					&& ef.getDefaultValue().trim().length() > 0)
			{
				if (field.getTypeName().equalsIgnoreCase("Reference"))
					bean.get(field).setAsText(
							field.getRelationKey().getTarget().getUri() + "/"
									+ ef.getDefaultValue());
				else
					bean.get(field).setAsText(ef.getDefaultValue());
			}
			else if (field.getDefaultValue() != null
					&& field.getDefaultValue().toString().trim().length() > 0)
			{
				if (field.getTypeName().equalsIgnoreCase("Reference"))
					bean.get(field).setAsText(
							field.getRelationKey().getTarget().getUri() + "/"
									+ field.getDefaultValue().toString());
				else
					bean.get(field).setAsText(
							field.getDefaultValue().toString());
			}
			vrc.setVisible(true);
			vrc.setReadonly(false);
			if (ef.isReadonly())
				vrc.setReadonly(true);
			ctxMap.put(field.getUri(), vrc);

			if (field == table.getIdentifier().getField()
					&& te.isAutoPrimaryKey())
				vrc.setVisible(false);

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
