package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoUpdatePassword implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getUserJdbcSource();
		String uid = request.getParameter("uid");
		String up = request.getParameter("T1");
		StringSql sql = new StringSql(
				"update VDB_USERS set PASSWORD=? where USERID=?");
		sql.setParameter(1, new Md5PasswordEncoder().encodePassword(up, uid));
		sql.setParameter(2, uid);
		jt.executeUpdate(sql);
		response.getWriter().println("<script>alert('修改密码成功');</script>");
		response.getWriter().flush();
	}
}