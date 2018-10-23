package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Field;
import vdb.metacat.RelationKey;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import cn.csdb.commons.action.JspAction;

public class DoM2M implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String thisAttrUri = request.getParameter("thisFieldUri");
		String thisId = request.getParameter("thisId");
		String thatId = request.getParameter("thatId");
		String mode = request.getParameter("mode");

		Field thisField = (Field) VdbManager.getInstance().getCatalog()
				.fromUri(thisAttrUri);
		RelationKey rk = thisField.getRelationKey();
		// ManyToMany m2m = (ManyToMany) fk.getRelation();

		if ("unlink".equals(mode))
		{
			((VdbDataSet) rk.getTarget().getDataSet()).unlinkRecord(rk, thatId,
					thisId);
		}
		else if ("link".equals(mode))
		{
			((VdbDataSet) rk.getTarget().getDataSet()).linkRecord(rk, thatId,
					thisId);
		}
		else if ("linkAll".equals(mode))
		{
			String[] ids = thatId.split(",");
			for (String id : ids)
			{
				((VdbDataSet) rk.getTarget().getDataSet()).linkRecord(rk, id,
						thisId);
			}
		}
		else
		{
			((VdbDataSet) rk.getTarget().getDataSet()).linkRecord(rk, thatId,
					thisId);
		}
	}

}
