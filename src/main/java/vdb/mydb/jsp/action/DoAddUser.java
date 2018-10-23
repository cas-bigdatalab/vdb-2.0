package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoAddUser extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getUserJdbcSource();
		String uid = request.getParameter("T1");
		try
		{
			if (jt
					.queryForObjects(
							new StringSql(
									"select * from VDB_USERS where USERID=?",
									uid)).isEmpty())
			{
				String up = request.getParameter("T2");
				String xuewei = request.getParameter("xuewei");
				String zhicheng = request.getParameter("zhicheng");
				String lingyu = request.getParameter("lingyu");
				String phone = request.getParameter("phone");
				String fax = request.getParameter("fax");
				String mail = request.getParameter("mail");
				String youbian = request.getParameter("youbian");
				String unit = request.getParameter("unit");
				String address = request.getParameter("address");
				String detail = request.getParameter("detail");
				jt
						.executeUpdate(new StringSql(
								"insert into VDB_USERS (USERID,PASSWORD,XUEWEI,ZHICHENG,LINGYU,PHONE,FAX,MAIL,YOUBIAN,UNIT,ADDRESS,DETAIL) values (?,?,?,?,?,?,?,?,?,?,?,?)",
								uid, new Md5PasswordEncoder().encodePassword(
										up, uid), xuewei, zhicheng, lingyu,
								phone, fax, mail, youbian, unit, address,
								detail));
				String groupValues = request.getParameter("groupValues");
				if (groupValues != null && groupValues.length() > 0)
				{
					String[] gvalue = groupValues.split(";");
					for (int i = 0; i < gvalue.length; i++)
					{
						jt
								.executeUpdate(new StringSql(
										"insert into VDB_USERS_GROUPS (USERID,GROUPID) values (?,?)",
										uid, gvalue[i]));
					}
					if (gvalue != null && gvalue.length > 0)
					{

						if (new AuthTool().getGroup(gvalue[0]).get("GROUPCODE") != null)
							request.setAttribute("group", new AuthTool()
									.getGroup(gvalue[0]).get("GROUPCODE")
									.toString());
					}
				}
				request.setAttribute("sucess", true);

			}
			else
			{
				request.setAttribute("sucess", false);
			}
		}
		catch (Exception e)
		{
			request.setAttribute("sucess", false);
		}
	}

}
