/*
 * Created on 2005-8-21
 */
package cn.csdb.commons.sql.jdbc.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import cn.csdb.commons.util.StringKeyMap;

/**
 * 完成对一条查询记录的封装。
 * 
 * @author Administrator
 */
public class JdbcRow extends StringKeyMap
{
	public byte[] getBlob(String columnName) throws SQLException
	{
		Object o = get(columnName);
		byte[] bytes = null;

		if (o instanceof byte[])
		{
			bytes = (byte[]) o;
		}

		else if (o instanceof Blob)
		{
			Blob b = (Blob) o;
			bytes = b.getBytes(1, (int) b.length());
		}

		return bytes;
	}

	public int getBlob(String columnName, File file) throws IOException,
			SQLException
	{
		OutputStream os = new FileOutputStream(file);
		return getBlob(columnName, os);
	}

	public int getBlob(String columnName, OutputStream os) throws IOException,
			SQLException
	{
		byte[] bytes = getBlob(columnName);
		os.write(bytes);

		return bytes.length;
	}
}
