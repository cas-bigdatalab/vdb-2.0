package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSONObject;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.sdef.SimpleSdef;
import cn.csdb.commons.action.JspAction;

public class DoM2O implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{

		String thisEntityUri = request.getParameter("thisTableUri");
		String thisAttrUri = request.getParameter("thisFieldUri");
		String thisId = request.getParameter("thisId");
		String thatId = request.getParameter("thatId");

		Entity thisTable = (Entity) VdbManager.getInstance().getCatalog()
				.fromUri(thisEntityUri);
		AnyBeanDao dao = new AnyBeanDao(thisTable);
		AnyBean bean;
		bean = dao.lookup(thisId);
		Field[] refFields = thisTable.getFields();
		Field refField = null;
		for (int i = 0; i < refFields.length; i++)
		{
			if (refFields[i].getUri().equalsIgnoreCase(thisAttrUri))
			{
				refField = refFields[i];
				break;
			}
		}

		VdbData data = bean.get(refField);
		SimpleSdef ddl = new SimpleSdef();
		if (refField != null)
			ddl.setValue(refField.getRelationKey().getTarget().getUri() + "/"
					+ thatId);
		data.setAsSdef(ddl);

		try
		{
			dao.update(bean);
		}
		catch (Exception e)
		{
			JSONObject updateFailed = new JSONObject();
			updateFailed.put("code", 400);
			updateFailed.put("message", "����ʧ�ܣ�<br><br>"
					+ "<div style='line-height:20px'>������Ϣ���£�<br>"
					+ e.getMessage() + "</div>");
			updateFailed.write(response.getWriter());
			return;
		}

	}

}
