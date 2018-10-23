package vdb.mydb.jsp.action.catalog;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Field;
import vdb.metacat.View;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoDeleteAttribute extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String mid = request.getParameter("mid");
		Field field = VdbManager.getEngine().getCatalog().fromId(mid);
		request.setAttribute("fieldId", field.getId());
		request.setAttribute("entityid", field.getEntity().getId());
		request.setAttribute("dsid", field.getEntity().getDataSet().getId());
		field.getEntity().removeField(field);
		for (View view : field.getEntity().getDataSet().getViews())
		{
			field.getEntity().getDataSet().removeView(view);
			view.removeItem(field);
			field.getEntity().getDataSet().addView(view);
		}
		VdbManager.getEngine().getCatalogManager().saveDataSet(
				field.getEntity().getDataSet());
		VdbManager.getEngine().getCatalog().cacheIn(
				field.getEntity().getDataSet());
	}
}
