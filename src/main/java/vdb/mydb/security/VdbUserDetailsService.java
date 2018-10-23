package vdb.mydb.security;

import org.springframework.security.userdetails.UserDetailsService;

import cn.csdb.commons.jsp.Pageable;

public interface VdbUserDetailsService extends UserDetailsService
{
	Pageable<String> getUserNames();

	Pageable<String> getLikeUserNames(String pattern);
}
