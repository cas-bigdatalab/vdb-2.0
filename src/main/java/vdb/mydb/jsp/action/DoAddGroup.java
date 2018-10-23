package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoAddGroup implements JspAction
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		String groupName = request.getParameter("groupName");
		if (jt.queryForObjects(
				new StringSql("select * from VDB_GROUPS where GROUPNAME=?",
						groupName)).isEmpty())
		{
			String description = request.getParameter("description");
			String groupCode = request.getParameter("groupCode");
			jt
					.executeUpdate(new StringSql(
							"insert into VDB_GROUPS (GROUPNAME,GROUPCODE,DESCRIPTION) values (?,?,?)",
							groupName, groupCode, description));
			response.getWriter().println(
					"<script>alert('添加用户组成功');top.addItem('"
							+ groupCode.substring(0, groupCode.length() - 5)
							+ "','" + groupCode + "','" + groupName
							+ "');</script>");
			response.getWriter().flush();
		}
		else
		{
			response
					.getWriter()
					.println(
							"<script>alert('添加用户组失败，该用户组名已存在！');history.back();</script>");
			response.getWriter().flush();
		}
	}
}