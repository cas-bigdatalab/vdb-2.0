/*
 * 创建日期 2005-5-31
 */
package cn.csdb.commons.sql.jdbc.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.csdb.commons.sql.types.ResultSetColumn;
import cn.csdb.commons.util.StringKeyMap;

/**
 * 完成对结果集的封装，对外体现为List。 该类除了包含记录List之外，还封装了列的信息。
 * 
 * @author bluejoe
 */
public class JdbcRows extends ArrayList
{
	private List _columns;

	private Map _namedColumns;

	public JdbcRows(ResultSet rs) throws SQLException
	{
		_columns = new Vector();
		_namedColumns = new StringKeyMap();

		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++)
		{
			ResultSetColumn column = new ResultSetColumn(rsmd, i);
			_columns.add(column);
			_namedColumns.put(column.getColumnName(), column);
		}
	}

	/**
	 * 获取指定位置的列对象
	 * 
	 * @param i
	 * @return
	 */
	public ResultSetColumn getColumn(int i)
	{
		return (ResultSetColumn) _columns.get(i);
	}

	public ResultSetColumn getColumn(String columnName)
	{
		return (ResultSetColumn) _namedColumns.get(columnName);
	}

	/**
	 * 获取所有列的数目
	 * 
	 * @return
	 */
	public int getColumnCount()
	{
		return _columns.size();
	}

	/**
	 * 获取指定位置的列名
	 * 
	 * @param i
	 * @return
	 */
	public String getColumnName(int i)
	{
		return ((ResultSetColumn) _columns.get(i)).getColumnName();
	}

	/**
	 * 获取所有列，每一个元素为一个SQLColumn对象。
	 * 
	 * @return
	 */
	public List getColumns()
	{
		return _columns;
	}
}