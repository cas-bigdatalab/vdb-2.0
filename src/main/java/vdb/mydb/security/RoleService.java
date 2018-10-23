package vdb.mydb.security;

import java.util.List;

public interface RoleService
{

	List<VdbRole> getRoles();

	List<VdbRole> getRolesByResourceClassType(String resourceClassType);

}