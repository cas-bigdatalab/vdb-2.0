/*
 * 创建日期 2005-4-12
 */
package cn.csdb.commons.sql.catalog;

import java.util.List;
import java.util.Map;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.types.ResultSetColumn;
import cn.csdb.commons.util.MapHelper;
import cn.csdb.commons.util.StringKeyMap;

/**
 * @author bluejoe
 */
public class JdbcColumn
{
	private ResultSetColumn _column;

	private Map<String, Object> _properties = new StringKeyMap<Object>();

	private JdbcTable _table;

	public JdbcColumn(JdbcSource sqlSource, JdbcTable table,
			Map<String, Object> properties, ResultSetColumn column)
	{
		_table = table;
		_properties.putAll(properties);

		// oracle的field所包含的数据类型不正确
		_properties.put("DATA_TYPE", "" + column.getColumnType());
		_column = column;
	}

	public int getCharLength()
	{
		return new MapHelper(_properties).getInteger("CHAR_OCTET_LENGTH");
	}

	public ResultSetColumn getColumn()
	{
		return _column;
	}

	public int getColumnSize()
	{
		return new MapHelper(_properties).getInteger("COLUMN_SIZE");
	}

	public int getDataType()
	{
		return new MapHelper(_properties).getInteger("DATA_TYPE");
	}

	public String getDefaultValue()
	{
		return new MapHelper(_properties).getString("COLUMN_DEF");
	}

	public String getColumnName()
	{
		return new MapHelper(_properties).getString("COLUMN_NAME");
	}

	public int getFractionalDigits()
	{
		return new MapHelper(_properties).getInteger("DECIMAL_DIGITS");
	}

	public int getNullable()
	{
		return new MapHelper(_properties).getInteger("NULLABLE");
	}

	public int getOrdinalPosition()
	{
		return new MapHelper(_properties).getInteger("ORDINAL_POSITION");
	}

	public int getRadix()
	{
		return new MapHelper(_properties).getInteger("NUM_PREC_RADIX");
	}

	public String getRemarks()
	{
		return new MapHelper(_properties).getString("REMARKS");
	}

	public JdbcTable getTable()
	{
		return _table;
	}

	public String getTypeName()
	{
		return new MapHelper(_properties).getString("TYPE_NAME");
	}

	public boolean isAutoIncrement()
	{
		return this._column.isAutoIncrement();
	}

	public boolean isPrimaryKey()
	{
		List pkns = this.getTable().getPrimaryKeyNames();
		for (int i = 0; i < pkns.size(); i++)
		{
			if (this.getColumnName().equalsIgnoreCase((String) pkns.get(i)))
				return true;
		}

		return false;
	}

	public String toString()
	{
		return getColumnName() + "[" + getTypeName() + "]";
	}
}
