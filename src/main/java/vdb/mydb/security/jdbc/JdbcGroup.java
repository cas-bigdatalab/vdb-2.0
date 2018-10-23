package vdb.mydb.security.jdbc;

import java.util.Map;

import vdb.mydb.security.VdbGroup;

public class JdbcGroup implements VdbGroup
{
	private Map<String, Object> _jdbcRow;

	public JdbcGroup(Map<String, Object> jdbcRow)
	{
		_jdbcRow = jdbcRow;
	}

	public Object get(String key)
	{
		if (_jdbcRow == null)
			return null;
		return _jdbcRow.get(key);
	}
}
