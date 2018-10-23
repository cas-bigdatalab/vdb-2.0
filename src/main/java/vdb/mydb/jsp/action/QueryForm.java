/*
 * Created on 2006-9-5
 */
package vdb.mydb.jsp.action;

import java.util.List;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.CatalogObject;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.typelib.FieldType;
import cn.csdb.commons.action.JspAction;

public class QueryForm implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String tid = request.getParameter("tid");
		Entity thisEntity = (Entity) VdbManager.getInstance().getCatalog()
				.fromId(tid);

		VdbEntity te = (VdbEntity) thisEntity;
		List fields = new Vector();

		// �򵥲�ѯ
		Field field = thisEntity.getTitleField();

		// ��ϲ�ѯ
		for (CatalogObject item : te.getQueryView().getItems())
		{
			if (!(item instanceof Field))
				continue;

			field = (Field) item;
			FieldType type = field.getType();
			// ���ɲ�ѯ
			if (!type.isQueryable())
			{
				continue;
			}
			fields.add(field);
		}

		request.setAttribute("table", thisEntity);
		request.setAttribute("fields", fields);
	}
}
