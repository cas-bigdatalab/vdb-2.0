package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.RelationKey;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.CatalogObjectProxy;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.typelib.FieldType;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateField extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String isNew = request.getParameter("isNew");// 用于判断用户的操作：修改or描述新的字段[包括增加新的字段]
		String mid = request.getParameter("mid");// 实体（字段）属性ID
		String P_type = request.getParameter("P_type");// 字段类型
		String P_foreignKey = request.getParameter("P_foreignKey");// 此变量存储的是关系键的ID

		Catalog catalog = VdbManager.getEngine().getCatalog();

		// catalog.fromId(mid)会根据实体属性ID从缓存中找到此对象
		// 如果用户点击“添加新的属性”或“描述属性信息或修改”，则缓存中不会存在此对象
		VdbField field = catalog.fromId(mid);// “修改”属性信息时使用

		// 如果用户点击“添加新的属性”或者“描述属性信息”时，此条件均为真
		if ("true".equalsIgnoreCase(isNew))
		{
			// 添加属性信息到相应的实体中
			// 此时会重新NEW一个Field对象，FieldId和UpdateField.java与UpdateField.vpage页面中的ID不再一致
			field = ((VdbEntity) catalog.fromId(request.getParameter("tid")))
					.addField(request.getParameter("P_name"));
		}

		// 如果字段大小不为空，则设置属性的字段大小属性
		if (request.getParameter("columnSize") != null
				&& !request.getParameter("columnSize").equalsIgnoreCase(""))
		{
			field.setSize(Integer.parseInt(request.getParameter("columnSize")));
		}

		field.setTypeName(P_type);

		FieldType fieldType = VdbManager.getEngine().getCatalogContext()
				.getFieldType(P_type);
		field.setType(fieldType);
		field.setTypeDriver(fieldType.createDriver(field));

		// 如果关系键ID不为空，则设置字段的关系键
		if (P_foreignKey != null)
		{
			RelationKey relationKey = (RelationKey) catalog
					.fromId(P_foreignKey);
			if (relationKey != null)
			{
				field.setRelationKey(relationKey);
			}
		}
		// 如果关系键为空，则设置字段关系键为空
		else
		{
			field.setRelationKey(null);
		}

		CatalogObjectProxy meta = new CatalogObjectProxy(field);
		meta.attach(request);

		VdbManager.getEngine().getCatalogManager().saveDataSet(
				field.getEntity().getDataSet());
		catalog.cacheIn(field);
	}
}
