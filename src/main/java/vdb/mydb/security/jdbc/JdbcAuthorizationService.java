package vdb.mydb.security.jdbc;

import java.io.Serializable;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

import vdb.metacat.Catalog;
import vdb.metacat.CatalogObject;
import vdb.metacat.Entity;
import vdb.metacat.Query;
import vdb.metacat.fs.page.ListEditItemsPage;
import vdb.metacat.fs.page.ListItemsPage;
import vdb.metacat.fs.page.Page;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;
import vdb.mydb.query.impl.QueryFromSqlImpl;
import vdb.mydb.security.AuthorizationService;
import vdb.mydb.security.VdbGroup;
import vdb.tool.pages.PagesManagerTool;
import cn.csdb.commons.sql.JdbcManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.impl.JdbcRow;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class JdbcAuthorizationService implements AuthorizationService,
		Serializable
{
	JdbcSource _jdbcSource;

	public GrantedAuthority[] getAuthorities(String userName)
	{
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

		List<String> userRoleNames = getUserRoleNames(userName);

		for (String userRoleName : userRoleNames)
		{
			list.add(new GrantedAuthorityImpl(userRoleName));
		}

		List<VdbGroup> groupsOfUser = getGroupsOfUser(userName);

		for (VdbGroup jr : groupsOfUser)
		{
			List<String> groupRoleNames = getGroupRoleNames(jr.get("GROUPID")
					.toString());
			for (String groupRoleName : groupRoleNames)
			{
				list.add(new GrantedAuthorityImpl(groupRoleName));
			}
		}

		return ((GrantedAuthority[]) list.toArray(new GrantedAuthority[list
				.size()]));
	}

	public Page getGrantedPage(String uid, String pageType, String resourceId)
	{
		String entityUri;
		Catalog catalog = VdbManager.getEngine().getCatalog();
		PagesManagerTool pt = new PagesManagerTool();
		if (((CatalogObject) catalog.fromId(resourceId)).getClass()
				.getSimpleName().toLowerCase().indexOf("query") > 0)
		{
			entityUri = ((Query) catalog.fromId(resourceId)).getEntity()
					.getUri();
		}
		else
		{
			entityUri = ((Entity) catalog.fromId(resourceId)).getUri();
		}
		try
		{
			JdbcSource jt = _jdbcSource;
			List objects = new ArrayList();
			// 用户是否有页面被授权
			objects = jt
					.queryForObjects(new StringSql(
							"select VIEW from VDB_RES_USER_PAGE where USERID=? and ROLE = ? and RESOURCECLASS = ?",
							uid, pageType, resourceId));
			if (objects.size() > 0)
				return pt.getPageByName(((JdbcRow) objects.get(0)).get("VIEW")
						.toString(), entityUri);

			// 用户组是否有页面被授权
			List<VdbGroup> groups = getGroupsOfUser(uid);
			if (groups.size() > 0)
			{
				objects = jt
						.queryForObjects(new StringSql(
								"select VIEW from VDB_RES_GROUP_PAGE where GROUPID=? and ROLE = ? and RESOURCECLASS = ?",
								groups.get(0).get("GROUPID").toString(),
								pageType, resourceId));
				if (objects.size() > 0)
					return pt.getPageByName(((JdbcRow) objects.get(0)).get(
							"VIEW").toString(), entityUri);
			}

			// 返回默认页面
			return pt.getDefaultPageByType(entityUri, pageType);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public VdbGroup getGroup(String id)
	{
		JdbcRow group = null;
		List objects = new ArrayList();
		try
		{
			JdbcSource jt = _jdbcSource;
			objects = jt.queryForObjects(new StringSql(
					"select * from VDB_GROUPS where BLOCKED=FALSE and GROUPID ="
							+ id));
			if (objects.size() > 0)
				group = (JdbcRow) objects.get(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new JdbcGroup(group);
	}

	public VdbGroup getGroupByCode(String code)
	{
		JdbcRow group = null;
		List objects = new ArrayList();
		try
		{
			JdbcSource jt = _jdbcSource;
			objects = jt.queryForObjects(new StringSql(
					"select * from VDB_GROUPS where BLOCKED=FALSE and GROUPCODE ='"
							+ code + "'"));
			if (objects.size() > 0)
				group = (JdbcRow) objects.get(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new JdbcGroup(group);
	}

	public List<VdbGroup> getGroupList()
	{
		List<VdbGroup> groups = new ArrayList<VdbGroup>();
		List objects = new ArrayList();
		try
		{
			JdbcSource jt = _jdbcSource;
			objects = jt
					.queryForObjects(new StringSql(
							"select * from VDB_GROUPS where BLOCKED=FALSE order by GROUPCODE"));
			for (Object o : objects)
			{
				groups.add(new JdbcGroup((JdbcRow) o));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return groups;
	}

	public List<String> getGroupRoleNames(String gid)
	{
		List<String> groupRoleNames = new ArrayList<String>();
		List objects = new ArrayList();
		try
		{
			JdbcSource jt = _jdbcSource;
			objects = jt.queryForObjects(new StringSql(
					"select * from VDB_RES_GROUP_PAGE where GROUPID='" + gid
							+ "'"));
			for (Object o : objects)
			{
				groupRoleNames.add(((JdbcRow) o).get("ROLE").toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return groupRoleNames;
	}

	public List<VdbGroup> getGroupsOfUser(String uid)
	{
		List<VdbGroup> groups = new ArrayList<VdbGroup>();
		List objects = new ArrayList();
		try
		{
			objects = _jdbcSource
					.queryForObjects(new StringSql(
							"select * from VDB_GROUPS where GROUPID in (select GROUPID from VDB_USERS_GROUPS where USERID='"
									+ URLDecoder.decode(uid, "UTF-8") + "')"));
			for (Object o : objects)
			{
				groups.add(new JdbcGroup((JdbcRow) o));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return groups;
	}

	public List<String> getUserRoleNames(String uid)
	{
		List<String> userRoleNames = new ArrayList<String>();
		List objects = new ArrayList();
		try
		{
			JdbcSource jt = _jdbcSource;
			objects = jt.queryForObjects(new StringSql(
					"select * from VDB_RES_USER_PAGE where USERID='" + uid
							+ "'"));
			for (Object o : objects)
			{
				userRoleNames.add(((JdbcRow) o).get("ROLE").toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return userRoleNames;
	}

	public List<String> getUserNamesOfGroup(String gid) throws Exception
	{
		return _jdbcSource.queryForObjects(new StringSql(
				"select USERID from VDB_USERS_GROUPS where GROUPID='" + gid
						+ "'"), new ResultSetReader<String>()
		{

			public String read(ResultSet arg0, int arg1) throws SQLException
			{
				return arg0.getString(1);
			}
		});
	}

	public List<String> getAllUserNames() throws Exception
	{

		return VdbManager.getEngine().getUserJdbcSource().queryForObjects(
				new StringSql(VdbManager.getEngine()
						.getJdbcUserDetailsService().getSqlGetUserNames()),
				new ResultSetReader<String>()
				{

					public String read(ResultSet arg0, int arg1)
							throws SQLException
					{
						return arg0.getString(1);
					}
				});
	}

	public boolean isGroupsGranted(String uid, String roleName,
			String resourceClass, String page)
	{
		for (VdbGroup jr : getGroupsOfUser(uid))
		{
			if (isGroupGranted(jr.get("GROUPID").toString(), roleName,
					resourceClass, page))
			{
				return true;
			}
		}

		return false;
	}

	public boolean isGroupGranted(String gid, String roleName,
			String resourceClass, String view)
	{
		try
		{
			JdbcSource jt = _jdbcSource;
			if (jt
					.queryForObjects(
							new StringSql(
									"select * from VDB_RES_GROUP_PAGE where GROUPID=? and ROLE = ? and RESOURCECLASS = ? and VIEW = ?",
									gid, roleName, resourceClass, view))
					.isEmpty())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public boolean isUserGrantedExplicitly(String uid, String roleName,
			String resourceClass, String page)
	{
		try
		{
			JdbcSource jt = _jdbcSource;
			if (jt
					.queryForObjects(
							new StringSql(
									"select * from VDB_RES_USER_PAGE where USERID=? and ROLE = ? and RESOURCECLASS = ? and VIEW = ?",
									uid, roleName, resourceClass, page))
					.isEmpty())
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public boolean isUserInRole(String userName, String roleName)
	{
		for (String str : getUserRoleNames(userName))
		{
			if (str.equalsIgnoreCase(roleName))
				return true;
		}

		return false;
	}

	public void setDataSource(DataSource dataSource) throws Exception
	{
		_jdbcSource = JdbcManager.getInstance().getJdbcSource(dataSource);
	}

	public boolean isUserGranted(String uid, String roleName,
			String resourceClass, String page)
	{
		if (isUserGrantedExplicitly(uid, roleName, resourceClass, page)
				|| isGroupsGranted(uid, roleName, resourceClass, page))
			return true;

		return false;
	}

	public boolean isIdGranted(String uid, String entityId, String pageType,
			String id)
	{

		if (isUserInRole(uid, "ROLE_ADMIN"))
		{
			return true;
		}
		Page page = getGrantedPage(uid, pageType, entityId);

		String sql = "";
		Entity entity = page.getEntity();
		VarQuery vq = new QueryFromSqlImpl(entity);
		if (entity.getIdentifier().getField().getTypeName().equalsIgnoreCase(
				"Long")
				|| entity.getIdentifier().getField().getTypeName()
						.equalsIgnoreCase("Double"))
			sql = "select " + entity.getIdentifier().getField().getColumnName()
					+ " from " + entity.getTableName() + " where "
					+ entity.getIdentifier().getField().getColumnName() + "= "
					+ id;
		else
			sql = "select " + entity.getIdentifier().getField().getColumnName()
					+ " from " + entity.getTableName() + " where "
					+ entity.getIdentifier().getField().getColumnName() + "= '"
					+ id + "'";

		String tempsql = "1=1";
		if (pageType.equalsIgnoreCase("listItems"))
			tempsql = ((ListItemsPage) page).getGrantFilter();

		if (pageType.equalsIgnoreCase("listEditItems"))
			tempsql = ((ListEditItemsPage) page).getGrantFilter();
		tempsql = VdbManager.getInstance().getSecurityManager().mergeFilter(
				tempsql, entity.getId());

		if (tempsql != null && tempsql.length() > 1)
		{
			sql = sql + " and (" + tempsql + ")";
		}

		((QueryFromSqlImpl) vq).setSql(new StringSql(sql));

		QueryExecutor qe = null;
		try
		{
			qe = new AnyBeanDao(entity).execute(vq);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		List<AnyBean> list = null;
		try
		{
			if (qe != null)
				list = qe.list();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (list != null && list.size() > 0)
			return true;

		return false;
	}

}
