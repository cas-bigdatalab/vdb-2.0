package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import vdb.mydb.VdbManager;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoUpdateUser implements JspAction
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{

		JdbcSource jt = VdbManager.getInstance().getUserJdbcSource();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		if (sex != null && sex.equalsIgnoreCase("1"))
		{
			sex = "男";
		}
		if (sex != null && sex.equalsIgnoreCase("2"))
		{
			sex = "女";
		}
		String csny = request.getParameter("csny");
		String email = request.getParameter("email");
		String shouji = request.getParameter("shouji");
		String lxdh = request.getParameter("lxdh");
		String dw = request.getParameter("dw");
		String dz = request.getParameter("dz");
		String ms = request.getParameter("ms");

		jt
				.executeUpdate(new StringSql(
						"update VDB_USERS set PASSWORD=?,NAME=?,SEX=?,BIRTHDAY=?,MAIL=?,MOBILE=?,PHONE=?,UNIT=?,ADDRESS=?,DETAIL=? where USERID='"
								+ username + "'", new Md5PasswordEncoder()
								.encodePassword(password, username), name, sex,
						csny, email, shouji, lxdh, dw, dz, ms));

		((HttpServletResponse) response)
				.sendRedirect("userInfoUpdateSuccess.htm");
	}

}
