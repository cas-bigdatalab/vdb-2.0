package cn.csdb.commons.sql.jdbc.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;

public class JdbcObjectReader implements ResultSetReader<Map>
{
	private JdbcSource _sqlSource;

	public JdbcObjectReader(JdbcSource sqlSource)
	{
		_sqlSource = sqlSource;
	}

	public Map read(ResultSet rs, int row) throws SQLException
	{
		ResultSetMetaData md = rs.getMetaData();
		JdbcRow map = new JdbcRow();
		for (int i = 1; i <= md.getColumnCount(); i++)
		{
			Object o;
			try
			{
				o = _sqlSource.getSqlDialect().getObject(rs, i);
			}
			catch (Throwable e)
			{
				o = null;
			}

			map.put(md.getColumnName(i), o);
		}

		return map;
	}

}
