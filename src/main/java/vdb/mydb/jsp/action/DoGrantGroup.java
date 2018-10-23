package vdb.mydb.jsp.action;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Query;
import vdb.mydb.VdbManager;
import vdb.tool.auth.AuthTool;
import vdb.tool.pages.PagesManagerTool;
import cn.csdb.commons.action.JspAction;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoGrantGroup implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		List<String> pageTypes = new PagesManagerTool().getPagesManager()
				.getPageTypes();
		String gid = request.getParameter("gid");
		jt.executeUpdate(new StringSql(
				"delete from VDB_RES_GROUP_PAGE where GROUPID=?", gid));

		String[] pvs = request.getParameterValues("module");
		if (pvs != null)
		{
			for (String m : pvs)
			{
				jt
						.executeUpdate(new StringSql(
								"insert into VDB_RES_GROUP_PAGE (GROUPID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
								gid, m, "module", "all"));
			}
		}

		for (DataSet ds : VdbManager.getInstance().getDomain().getDataSets())
		{
			for (Object o : ds.getEntities())
			{
				Entity table = (Entity) o;

				if (pageTypes != null)
				{
					for (int i = 0; i < pageTypes.size(); i++)
					{

						String filterSelect = request.getParameter(table
								.getId()
								+ "_" + pageTypes.get(i) + "_select");
						if (filterSelect != null)
						{
							if (!filterSelect.equals("empty"))
							{
								jt
										.executeUpdate(new StringSql(
												"insert into VDB_RES_GROUP_PAGE (GROUPID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
												gid, pageTypes.get(i), table
														.getId(), filterSelect));
							}

						}
					}
				}
			}

			for (Object o : ds.getQueries())
			{
				Query query = (Query) o;

				if (pageTypes != null)
				{
					for (int i = 0; i < pageTypes.size(); i++)
					{

						String filterSelect = request.getParameter(query
								.getId()
								+ "_" + pageTypes.get(i) + "_select");
						if (filterSelect != null)
						{
							if (!filterSelect.equals("empty"))
							{
								jt
										.executeUpdate(new StringSql(
												"insert into VDB_RES_GROUP_PAGE (GROUPID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
												gid, pageTypes.get(i), query
														.getId(), filterSelect));
							}

						}
					}
				}
			}
		}

		response.getWriter().println(
				"<script>alert('用户组授权成功');location.href='grantGroup.vpage?gid="
						+ new AuthTool().getGroup(gid).get("GROUPCODE")
								.toString() + "';</script>");
		response.getWriter().flush();
	}
}