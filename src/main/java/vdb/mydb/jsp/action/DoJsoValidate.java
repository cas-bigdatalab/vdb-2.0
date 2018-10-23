/*
 * Created on 2006-8-28
 */
package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSONObject;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.typelib.ValidationError;
import vdb.mydb.typelib.sdef.SimpleSdef;
import cn.csdb.commons.action.JspAction;

public class DoJsoValidate implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		// json
		JSONObject form = JSONObject.fromObject(request.getParameter("form"));
		String fieldId = form.getString("fieldId");
		Field field = VdbManager.getInstance().getCatalog().fromId(fieldId);

		JSONObject jsoBean = form.getJSONObject("bean");
		try
		{
			field.getTypeDriver().createData().setAsSdef(
					new SimpleSdef((String) jsoBean.get(field.getName())));
			JSONObject ok = new JSONObject();
			ok.put("code", 200);

			ok.write(response.getWriter());
		}
		catch (ValidationError e)
		{
			JSONObject failed = new JSONObject();
			// У��ʧ�ܣ�
			failed.put("code", 400);
			failed.put("message", e.getMessage());
			failed.put("source", e.getSource().getId());

			failed.write(response.getWriter());
			return;
		}
	}
}
