package vdb.mydb.jsp.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.impl.JdbcRow;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoDeleteGroup extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		String gcode = request.getParameter("gid");
		List objects = new ArrayList();
		objects = jt
				.queryForObjects(new StringSql(
						"select * from VDB_GROUPS where GROUPCODE like ?",
						gcode + "%"));
		for (int i = 0; i < objects.size(); i++)
		{
			String gid = ((JdbcRow) objects.get(i)).get("GROUPID").toString();
			jt.executeUpdate(new StringSql(
					"delete from VDB_GROUPS where GROUPID=?", gid));
			jt.executeUpdate(new StringSql(
					"delete from VDB_USERS_GROUPS where GROUPID=?", gid));
			jt.executeUpdate(new StringSql(
					"delete from VDB_RES_GROUP_PAGE where GROUPID=?", gid));
		}
	}
}