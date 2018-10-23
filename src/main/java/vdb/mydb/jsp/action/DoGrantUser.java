package vdb.mydb.jsp.action;

import java.net.URLDecoder;
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

public class DoGrantUser implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		JdbcSource jt = VdbManager.getInstance().getAuthJdbcSource();
		List<String> pageTypes = new PagesManagerTool().getPagesManager()
				.getPageTypes();
		String uid = request.getParameter("uid");
		uid = URLDecoder.decode(uid, "UTF-8");
		jt.executeUpdate(new StringSql(
				"delete from VDB_RES_USER_PAGE where USERID=?", uid));

		String[] pvs = request.getParameterValues("module");
		if (pvs != null)
		{
			for (String m : pvs)
			{
				if (!new AuthTool().groupsOfUserIsGranted(uid, m, "module",
						"all"))
				{
					jt.executeUpdate(new StringSql(
							"insert into VDB_RES_USER_PAGE (USERID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
							uid, m, "module", "all"));
				}
			}
		}

		for (DataSet ds : VdbManager.getInstance().getDomain().getDataSets())
		{
			doSaveDataSetEditable(request, ds, uid, jt);

			for (Object o : ds.getEntities())
			{
				Entity table = (Entity) o;

				if (pageTypes != null)
				{
					for (int i = 0; i < pageTypes.size(); i++)
					{
						String filterSelect = request.getParameter(table
								.getId() + "_" + pageTypes.get(i) + "_select");
						if (filterSelect != null)
						{
							if (!new AuthTool().groupsOfUserIsGranted(uid,
									pageTypes.get(i), table.getId(),
									filterSelect))
							{
								if (!filterSelect.equals("empty"))
								{
									jt.executeUpdate(new StringSql(
											"insert into VDB_RES_USER_PAGE (USERID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
											uid, pageTypes.get(i), table
													.getId(), filterSelect));
								}
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
								.getId() + "_" + pageTypes.get(i) + "_select");
						if (filterSelect != null)
						{
							if (!new AuthTool().groupsOfUserIsGranted(uid,
									pageTypes.get(i), query.getId(),
									filterSelect))
							{
								if (!filterSelect.equals("empty"))
								{
									jt.executeUpdate(new StringSql(
											"insert into VDB_RES_USER_PAGE (USERID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
											uid, pageTypes.get(i), query
													.getId(), filterSelect));
								}
							}
						}
					}
				}
			}

		}
		response.getWriter().println(
				"<script>alert('用户授权成功');location.href='grantUser.vpage?uid="
						+ uid + "';</script>");
		response.getWriter().flush();
	}

	private void doSaveDataSetEditable(ServletRequest request, DataSet ds,
			String uid, JdbcSource jt) throws Exception
	{
		//edit_dataset_ checkbox
		String forbidden = request.getParameter("edit_dataset_" + ds.getId());
		if ("forbidden".equals(forbidden))
		{
			jt.executeUpdate(new StringSql(
					"insert into VDB_RES_USER_PAGE (USERID,ROLE,RESOURCECLASS,VIEW) values (?,?,?,?)",
					uid, "edit_dataset", ds.getId(), "forbidden"));
		}
	}
}