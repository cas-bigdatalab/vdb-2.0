package vdb.mydb.security;

import java.util.List;

import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

import vdb.metacat.Catalog;
import vdb.metacat.Entity;
import vdb.metacat.fs.page.Page;
import vdb.mydb.VdbManager;
import vdb.tool.auth.AuthTool;

/**
 * composite class of AuthorizationService, RoleService and UserDetailsService
 * 
 * @author bluejoe
 * 
 */
public class VdbSecurityManager
{

	AuthorizationService _authorizationService;

	RoleService _roleService;

	UserDetailsService _userDetailsService;

	public String mergeFilter(String filter, String entityId)
	{

		if (null == filter || filter.trim().length() == 0)
			return "1=1";
		else
		{
			String ff = filter;
			Catalog catalog = VdbManager.getEngine().getCatalog();
			Entity entity = catalog.fromId(entityId);
			if (entity.getEditorField() != null)
				ff = ff.replace("$editor", entity.getEditorField()
						.getColumnName());
			if (entity.getGroupField() != null)
				ff = ff.replace("$group", entity.getGroupField()
						.getColumnName());
			AuthTool auth = new AuthTool();
			String userName = auth.getUserName();
			if (userName != null)
				ff = ff.replace("$user", userName);

			if (auth.getGroupsOfUser(userName) != null
					&& auth.getGroupsOfUser(userName).size() > 0)
				ff = ff.replace("$code", auth.getGroupsOfUser(userName).get(0)
						.get("GROUPCODE").toString());
			return ff;
		}
	}

	// 首先判断用户是否有页面被授权，然后判断用户组是否有页面被授权，如果都没有，则返回默认页面
	public Page getGrantedPage(String uid, String pageType, String resourceId)
	{
		return _authorizationService.getGrantedPage(uid, pageType, resourceId);
	}

	public VdbGroup getGroup(String id)
	{
		return _authorizationService.getGroup(id);

	}

	public VdbGroup getGroupByCode(String code)
	{
		return _authorizationService.getGroupByCode(code);

	}

	public List<VdbGroup> getGroupList()
	{
		return _authorizationService.getGroupList();

	}

	public List<String> getGroupRoleNames(String gid)
	{
		return _authorizationService.getGroupRoleNames(gid);

	}

	public List<VdbGroup> getGroupsOfUser(String uid)
	{
		return _authorizationService.getGroupsOfUser(uid);

	}

	public List<VdbRole> getRolesByResourceClassType(String resourceClassType)
	{
		return _roleService.getRolesByResourceClassType(resourceClassType);
	}

	public UserDetails getUser()
	{
		SecurityContext ctx = SecurityContextHolder.getContext();
		Authentication auth = ctx.getAuthentication();
		if (auth == null
				|| auth.getPrincipal().equals(
						AuthorizationService.ROLE_ANONYMOUS))
			return null;

		UserDetails user = (UserDetails) auth.getPrincipal();
		return user;
	}

	public UserDetails getUser(String uid)
	{
		return _userDetailsService.loadUserByUsername(uid);
	}

	public String getUserName()
	{
		UserDetails user = getUser();
		if (user == null)
		{
			return AuthorizationService.USER_ANONYMOUS;
		}

		return user.getUsername();
	}

	public List<String> getUserRoleNames(String uid)
	{
		return _authorizationService.getUserRoleNames(uid);
	}

	public List<String> getUserNamesOfGroup(String gid) throws Exception
	{
		return _authorizationService.getUserNamesOfGroup(gid);
	}

	public List<String> getAllUserNames() throws Exception
	{
		return _authorizationService.getAllUserNames();
	}

	public boolean isGroupsGranted(String uid, String roleName,
			String resourceClass, String page)
	{
		return _authorizationService.isGroupsGranted(uid, roleName,
				resourceClass, page);
	}

	public boolean isGroupGranted(String gid, String roleName,
			String resourceClass, String view)
	{
		return _authorizationService.isGroupGranted(gid, roleName,
				resourceClass, view);

	}

	public boolean isUserGrantedExplicitly(String uid, String roleName,
			String resourceClass, String page)
	{
		return _authorizationService.isUserGrantedExplicitly(uid, roleName,
				resourceClass, page);

	}

	public boolean isUserInRole(String roleName)
	{
		return _authorizationService.isUserInRole(getUserName(), roleName);

	}

	public void setAuthorizationService(
			AuthorizationService authorizationService)
	{
		_authorizationService = authorizationService;
	}

	public void setRoleService(RoleService roleService)
	{
		_roleService = roleService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService)
	{
		_userDetailsService = userDetailsService;
	}

	public boolean isUserGranted(String uid, String roleName,
			String resourceClass, String page)
	{
		return _authorizationService.isUserGranted(uid, roleName,
				resourceClass, page);

	}

	public boolean isIdGranted(String uid, String entityId, String pageType,
			String id)
	{
		return _authorizationService.isIdGranted(uid, entityId, pageType, id);
	}
}
