package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoUpdateGroup implements JspAction
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		jt
				.executeUpdate(new StringSql(
						"update VDB_GROUPS set GROUPNAME=?,DESCRIPTION=? where GROUPID=?",
						request.getParameter("groupName"), request
								.getParameter("description"), request
								.getParameter("gid")));
		response.getWriter().println("<script>alert('修改用户组成功');</script>");
		response.getWriter().flush();
	}
}