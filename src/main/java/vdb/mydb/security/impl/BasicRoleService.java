package vdb.mydb.security.impl;

import java.util.ArrayList;
import java.util.List;

import vdb.mydb.security.RoleService;
import vdb.mydb.security.VdbRole;

public class BasicRoleService implements RoleService
{

	private List<VdbRole> _roles;

	public List<VdbRole> getRoles()
	{
		return _roles;
	}

	public void setRoles(List<VdbRole> roles)
	{
		_roles = roles;
	}

	public List<VdbRole> getRolesByResourceClassType(String resourceClassType)
	{
		List<VdbRole> allRoles = getRoles();
		List<VdbRole> roles = new ArrayList<VdbRole>();
		for (VdbRole role : allRoles)
		{
			if (((BasicRole) role).getResourceClassType().equalsIgnoreCase(
					resourceClassType))
			{
				roles.add(role);
			}
		}
		return roles;
	}

}
