package cn.csdb.commons.sql;

import java.text.MessageFormat;

public class DataSourceNotFound extends Exception
{
	private String _name;

	public DataSourceNotFound(String name)
	{
		_name = name;
	}

	public String getMessage()
	{
		return MessageFormat.format("failed to find data source `{0}`",
				new Object[] { _name });
	}
}
