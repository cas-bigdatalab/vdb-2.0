package vdb.tool.auth;

import java.util.List;

import org.springframework.security.userdetails.UserDetails;

import vdb.metacat.fs.page.Page;
import vdb.mydb.VdbManager;
import vdb.mydb.security.VdbGroup;
import vdb.mydb.security.VdbRole;
import vdb.mydb.security.VdbSecurityManager;

public class AuthTool
{
	private VdbSecurityManager getSecurityManager()
	{
		return VdbManager.getEngine().getSecurityManager();
	}

	public VdbGroup getGroup(String id)
	{
		return getSecurityManager().getGroup(id);
	}

	public VdbGroup getGroupByCode(String code)
	{
		return getSecurityManager().getGroupByCode(code);
	}

	public List<VdbGroup> getGroupList()
	{
		return getSecurityManager().getGroupList();
	}

	public List<VdbGroup> getGroupsOfUser(String uid)
	{
		return getSecurityManager().getGroupsOfUser(uid);
	}

	public List<VdbRole> getRolesByResourceClassType(String resourceClassType)
	{
		return getSecurityManager().getRolesByResourceClassType(
				resourceClassType);
	}

	public String getUserName()
	{
		return getSecurityManager().getUserName();
	}

	public UserDetails getUser()
	{
		return getSecurityManager().getUser();
	}

	public List<String> getUsersOfGroup(String gid) throws Exception
	{
		return getSecurityManager().getUserNamesOfGroup(gid);
	}

	public boolean groupsOfUserIsGranted(String uid, String roleName,
			String resourceClass, String pageName)
	{
		return getSecurityManager().isGroupsGranted(uid, roleName,
				resourceClass, pageName);
	}

	public boolean isUserInRole(String roleName)
	{
		return getSecurityManager().isUserInRole(roleName);
	}

	public boolean userIsGranted(String uid, String roleName,
			String resourceClass, String pageName)
	{
		return getSecurityManager().isUserGrantedExplicitly(uid, roleName,
				resourceClass, pageName);
	}

	public boolean groupIsGranted(String gid, String roleName,
			String resourceClass, String page)
	{
		return getSecurityManager().isGroupGranted(gid, roleName,
				resourceClass, page);
	}

	public UserDetails getUser(String uid)
	{
		return getSecurityManager().getUser(uid);
	}

	public Page getGrantedPageByTypeAndRes(String uid, String pageType,
			String resourceId)
	{
		return getSecurityManager().getGrantedPage(uid, pageType, resourceId);
	}

	public String mergeFilter(String filter, String entityId)
	{
		return getSecurityManager().mergeFilter(filter, entityId);
	}

	public boolean isIdGranted(String uid, String entityId, String pageType,
			String id)
	{
		return getSecurityManager().isIdGranted(uid, entityId, pageType, id);
	}

}