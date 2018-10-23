package vdb.mydb.security.jdbc;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;

public class JdbcUserDetails implements UserDetails, Principal
{
	private Map<String, Object> _jdbcRow;

	private GrantedAuthority[] _authorities;

	private JdbcUserDetailsService _jdbcUserDetailsService;

	public JdbcUserDetails(JdbcUserDetailsService jdbcUserDetailsService,
			Map<String, Object> jdbcRow)
	{
		_jdbcUserDetailsService = jdbcUserDetailsService;
		_jdbcRow = jdbcRow;
	}

	public String getPassword()
	{
		return (String) _jdbcRow.get(_jdbcUserDetailsService
				.getColumnPassword());
	}

	public String getUsername()
	{
		return (String) _jdbcRow.get(_jdbcUserDetailsService
				.getColumnUserName());
	}

	public boolean isAccountNonExpired()
	{
		return true;
	}

	public boolean isAccountNonLocked()
	{
		return true;
	}

	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	public boolean isEnabled()
	{
		return true;
	}

	public Object get(Object arg0)
	{
		return _jdbcRow.get(arg0);
	}

	public GrantedAuthority[] getAuthorities()
	{
		return _authorities;
	}

	public void setAuthorities(GrantedAuthority[] authorities)
	{
		_authorities = authorities;
	}

	public String getName()
	{
		return getUsername();
	}

}
