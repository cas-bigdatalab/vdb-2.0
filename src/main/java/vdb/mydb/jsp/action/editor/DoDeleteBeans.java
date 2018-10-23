package vdb.mydb.jsp.action.editor;

import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.tool.auth.AuthTool;
import vdb.tool.log.AccessLoggerTool;
import cn.csdb.commons.action.JspAction;

public class DoDeleteBeans implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String tid = request.getParameter("tid");
		Entity thisTable = (Entity) VdbManager.getEngine().getCatalog()
				.fromId(tid);

		String id = request.getParameter("id");
		StringTokenizer st = new StringTokenizer(id, ";");
		AccessLoggerTool loggerTool = new AccessLoggerTool();
		String user = new AuthTool().getUserName();
		String ip = request.getRemoteAddr();
		
		while (st.hasMoreTokens())
		{
			AnyBeanDao dao = new AnyBeanDao(thisTable);
			AnyBean bean = dao.lookup(st.nextToken());
			dao.delete(bean);
			/** *****************BEGIN LOGGING******************** */
			
			loggerTool.logAccess(user, ip, thisTable, "delete", null, bean.getId().getValue(), false);
			/** *****************End LOGGING******************** */
		}
	}

}
