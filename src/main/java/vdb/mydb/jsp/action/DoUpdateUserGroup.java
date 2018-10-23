package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoUpdateUserGroup implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		String uid = request.getParameter("T1");
		jt.executeUpdate(new StringSql(
				"delete from VDB_USERS_GROUPS where USERID=?", uid));
		String groupValues = request.getParameter("groupValues");
		if (groupValues != null && groupValues.length() > 0)
		{
			String[] gValues = groupValues.split(";");
			for (int i = 0; i < gValues.length; i++)
			{
				jt
						.executeUpdate(new StringSql(
								"insert into VDB_USERS_GROUPS (USERID,GROUPID) values (?,?)",
								uid, gValues[i]));
			}
		}
		response.getWriter().println(
				"<script>alert('设置用户分组成功');top.groupsOnClick('all');</script>");
		response.getWriter().flush();
	}
}