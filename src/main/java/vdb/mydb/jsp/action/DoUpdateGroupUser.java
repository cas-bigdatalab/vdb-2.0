package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoUpdateGroupUser implements JspAction
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		String gid = request.getParameter("T1");
		jt.executeUpdate(new StringSql(
				"delete from VDB_USERS_GROUPS where GROUPID=?", gid));
		String userValues = request.getParameter("userValues");
		String[] uValues = userValues.split(";&@");
		for (int i = 0; i < uValues.length; i++)
		{
			jt
					.executeUpdate(new StringSql(
							"insert into VDB_USERS_GROUPS (USERID,GROUPID) values (?,?)",
							uValues[i], gid));
		}
		response.getWriter().println(
				"<script>alert('设置用户组成功');top.refreshGroups();</script>");
		response.getWriter().flush();
	}
}