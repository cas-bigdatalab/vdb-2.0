package vdb.mydb.security;

import java.util.List;

import org.springframework.security.GrantedAuthority;

import vdb.metacat.fs.page.Page;

public interface AuthorizationService
{
	public static final String ROLE_ANONYMOUS = "roleAnonymous";

	public static final String USER_ANONYMOUS = "Anonymous";

	public GrantedAuthority[] getAuthorities(String userName);

	boolean isGroupsGranted(String uid, String roleName, String resourceClass,
			String page);

	boolean isUserInRole(String userName, String roleName);

	boolean isUserGrantedExplicitly(String uid, String roleName,
			String resourceClass, String page);

	boolean isGroupGranted(String gid, String roleName, String resourceClass,
			String view);

	List<VdbGroup> getGroupsOfUser(String uid);

	List<String> getUserNamesOfGroup(String gid) throws Exception;

	public List<String> getAllUserNames() throws Exception;

	VdbGroup getGroup(String id);

	VdbGroup getGroupByCode(String code);

	List<VdbGroup> getGroupList();

	List<String> getUserRoleNames(String uid);

	boolean isUserGranted(String uid, String roleName, String resourceClass,
			String page);

	List<String> getGroupRoleNames(String gid);

	Page getGrantedPage(String uid, String pageType, String resourceId);

	public boolean isIdGranted(String uid, String entityId, String pageType,
			String id);

}