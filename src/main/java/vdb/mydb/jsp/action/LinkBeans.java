package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.RelationKey;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbDataSet;
import cn.csdb.commons.action.JspAction;

public class LinkBeans implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String fid = request.getParameter("fid");
		RelationKey fk = (RelationKey) VdbManager.getInstance().getCatalog()
				.fromId(fid);

		String id1 = request.getParameter("id1");
		String id2 = request.getParameter("id2");

		boolean isUnlink = "unlink".equals(request.getParameter("mode"));
		if (isUnlink)
			((VdbDataSet) fk.getTarget().getDataSet()).unlinkRecord(fk, id1,
					id2);
		else
			((VdbDataSet) fk.getTarget().getDataSet()).linkRecord(fk, id1, id2);
	}

}
