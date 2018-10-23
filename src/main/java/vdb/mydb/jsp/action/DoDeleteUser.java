package vdb.mydb.jsp.action;

import java.net.URLDecoder;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoDeleteUser extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		try
		{
			JdbcSource jt = VdbManager.getInstance().getUserJdbcSource();
			String uid = request.getParameter("uid");
			uid = URLDecoder.decode(uid, "UTF-8");
			if (uid.equalsIgnoreCase(new AuthTool().getUserName()))
			{
				request.setAttribute("sucess", false);
				request.setAttribute("info", "不能删除当前登录用户！");
			}
			else
			{
				jt.executeUpdate(new StringSql(
						"delete from VDB_USERS where USERID=?", uid));
				jt.executeUpdate(new StringSql(
						"delete from VDB_USERS_GROUPS where USERID=?", uid));
				jt.executeUpdate(new StringSql(
						"delete from VDB_RES_USER_PAGE where USERID=?", uid));
				request.setAttribute("sucess", true);
			}
		}
		catch (Exception e)
		{
			request.setAttribute("sucess", false);
		}
	}
}