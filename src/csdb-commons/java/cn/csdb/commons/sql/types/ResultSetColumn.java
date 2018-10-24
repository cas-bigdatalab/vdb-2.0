/**
 * @author bluejoe
 * 
 * Copyright (c) 2001-2003 SDB. All rights reserved.
 */
package cn.csdb.commons.sql.types;

import java.sql.ResultSetMetaData;

/**
 * 该类用于描述查询结果的列信息。 不同于JdbcColumn，ResultSetColumn用以描述动态查询结果的列信息。
 * 
 * @author bluejoe
 */
public class ResultSetColumn
{
	private boolean _autoIncrement;

	private boolean _caseSensitive;

	private String _catalogName;

	private int _columnDisplaySize;

	private String _columnLabel;

	private String _columnName;

	private int _columnType;

	private String _columnTypeName;

	private boolean _currency;

	private boolean _definitelyWritable;

	private int _nullable;

	private int _precision;

	private boolean _readOnly;

	private int _scale;

	private String _schemaName;

	private boolean _searchable;

	private boolean _signed;

	private String _tableName;

	private boolean _writable;

	/**
	 * 构造函数
	 * 
	 */
	public ResultSetColumn(ResultSetMetaData rsmd, int column)
	{
		try
		{
			_scale = rsmd.getScale(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_precision = rsmd.getPrecision(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_columnType = rsmd.getColumnType(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_columnDisplaySize = rsmd.getColumnDisplaySize(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_tableName = rsmd.getTableName(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_schemaName = rsmd.getSchemaName(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_columnName = rsmd.getColumnName(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_catalogName = rsmd.getCatalogName(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_columnLabel = rsmd.getColumnLabel(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_columnTypeName = rsmd.getColumnTypeName(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_signed = rsmd.isSigned(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_nullable = rsmd.isNullable(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_writable = rsmd.isWritable(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_readOnly = rsmd.isReadOnly(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_currency = rsmd.isCurrency(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_searchable = rsmd.isSearchable(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_autoIncrement = rsmd.isAutoIncrement(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_caseSensitive = rsmd.isCaseSensitive(column);
		}
		catch (Exception e)
		{
		}

		try
		{
			_definitelyWritable = rsmd.isDefinitelyWritable(column);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * @return
	 */
	public String getCatalogName()
	{
		return _catalogName;
	}

	/**
	 * @return
	 */
	public int getColumnDisplaySize()
	{
		return _columnDisplaySize;
	}

	/**
	 * @return
	 */
	public String getColumnLabel()
	{
		return _columnLabel;
	}

	/**
	 * @return
	 */
	public String getColumnName()
	{
		return _columnName;
	}

	/**
	 * @return
	 */
	public int getColumnType()
	{
		return _columnType;
	}

	/**
	 * @return
	 */
	public String getColumnTypeName()
	{
		return _columnTypeName;
	}

	/**
	 * @return
	 */
	public int getNullable()
	{
		return _nullable;
	}

	/**
	 * @return
	 */
	public int getPrecision()
	{
		return _precision;
	}

	/**
	 * @return
	 */
	public int getScale()
	{
		return _scale;
	}

	/**
	 * @return
	 */
	public String getSchemaName()
	{
		return _schemaName;
	}

	/**
	 * @return
	 */
	public String getTableName()
	{
		return _tableName;
	}

	/**
	 * @return
	 */
	public boolean isAutoIncrement()
	{
		return _autoIncrement;
	}

	/**
	 * @return
	 */
	public boolean isCaseSensitive()
	{
		return _caseSensitive;
	}

	/**
	 * @return
	 */
	public boolean isCurrency()
	{
		return _currency;
	}

	/**
	 * @return
	 */
	public boolean isDefinitelyWritable()
	{
		return _definitelyWritable;
	}

	/**
	 * @return
	 */
	public boolean isReadOnly()
	{
		return _readOnly;
	}

	/**
	 * @return
	 */
	public boolean isSearchable()
	{
		return _searchable;
	}

	/**
	 * @return
	 */
	public boolean isSigned()
	{
		return _signed;
	}

	/**
	 * @return
	 */
	public boolean isWritable()
	{
		return _writable;
	}
}
