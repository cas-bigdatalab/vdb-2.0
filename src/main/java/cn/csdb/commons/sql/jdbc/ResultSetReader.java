package cn.csdb.commons.sql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetReader<T>
{
	public T read(ResultSet rs, int row) throws SQLException;
}
