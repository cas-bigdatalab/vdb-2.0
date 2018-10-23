package vdb.mydb.security.jdbc;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;

import vdb.mydb.security.AuthorizationService;
import vdb.mydb.security.VdbUserDetailsService;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.impl.JdbcRow;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class JdbcUserDetailsService implements VdbUserDetailsService,
		Serializable
{
	JdbcSource _jdbcSource;

	AuthorizationService _authorizationService;

	String _sqlGetUserNames;

	String _sqlGetLikeUserNames;

	String _sqlGetUserByName;

	String _columnUserName;

	String _columnPassword;

	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException
	{
		try
		{
			JdbcRow row = (JdbcRow) _jdbcSource.queryForObject(new StringSql(
					_sqlGetUserByName, userName));
			if (row != null)
			{
				JdbcUserDetails user = new JdbcUserDetails(this, row);
				user.setAuthorities(_authorizationService.getAuthorities(user
						.getUsername()));
				return user;
			}
		}
		catch (Exception e)
		{
		}

		return null;
	}

	public void setAuthorizationService(
			AuthorizationService authorizationService)
	{
		_authorizationService = authorizationService;
	}

	public void setColumnPassword(String columnPassword)
	{
		_columnPassword = columnPassword;
	}

	public void setColumnUserName(String columnUserName)
	{
		_columnUserName = columnUserName;
	}

	public void setDataSource(DataSource dataSource) throws Exception
	{
		_jdbcSource = JdbcManager.getInstance().getJdbcSource(dataSource);
	}

	public void setSqlGetLikeUserNames(String sqlGetLikeUserNames)
	{
		_sqlGetLikeUserNames = sqlGetLikeUserNames;
	}

	public void setSqlGetUserByName(String sqlGetUserByName)
	{
		_sqlGetUserByName = sqlGetUserByName;
	}

	public void setSqlGetUserNames(String sqlGetUserNames)
	{
		_sqlGetUserNames = sqlGetUserNames;
	}

	public String getColumnPassword()
	{
		return _columnPassword;
	}

	public String getColumnUserName()
	{
		return _columnUserName;
	}

	public String getSqlGetUserNames()
	{
		return _sqlGetUserNames;
	}

	public Pageable<String> getLikeUserNames(String pattern)
	{
		return null;
	}

	public Pageable<String> getUserNames()
	{
		return null;
	}
}
