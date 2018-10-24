package cn.csdb.commons.sql.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneratedKeyGetter implements StatementHandler
{
	private String _generatedKey = null;

	public void afterExecute(PreparedStatement st) throws SQLException
	{
		_generatedKey = null;

		// 获取自动生成的键值
		try
		{
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next())
			{
				_generatedKey = rs.getString(1);
			}
		}
		catch (Throwable e)
		{
		}
	}

	public String getGeneratedKey()
	{
		return _generatedKey;
	}
}
