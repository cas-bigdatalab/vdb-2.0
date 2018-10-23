package vdb.mydb.typelib.type;

import java.io.Serializable;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Field;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.Style;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.VdbFieldDriver;
import vdb.mydb.typelib.sdef.Sdef;
import cn.csdb.commons.sql.catalog.JdbcColumn;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.sql.types.JdbcObject;

public abstract class AbstractFieldDriver<MyData extends VdbData> implements
		VdbFieldDriver<MyData>
{
	protected Field _field;

	public StringSql buildFilter(String operatorName, Sdef ddl,
			boolean isCrossQuery) throws Exception
	{
		Style queryerStyle = _field.getType().getQueryerStyle(operatorName);

		DataSet ds = _field.getEntity().getDataSet();
		VdbDataSet dsex = ((VdbDataSet) ds);

		final String columnName = dsex.quote(_field.getEntity()
				.getTableName())
				+ "." + dsex.quote(_field.getColumnName());

		// ������͹����ѯ
		return buildFilter(columnName, queryerStyle.getName(),
				normalizeColumnValue(ddl.getValue()));
	}

	protected StringSql buildFilter(String qualifiedColumnName,
			String operatorName, Serializable columnValue)
	{
		if ("eq".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s=?", qualifiedColumnName),
					columnValue);

		if ("ne".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s!=?", qualifiedColumnName),
					columnValue);

		if ("gt".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s>?", qualifiedColumnName),
					columnValue);

		if ("lt".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s<?", qualifiedColumnName),
					columnValue);

		if ("in".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s in (%s)",
					qualifiedColumnName, columnValue));

		if ("ge".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s>=?", qualifiedColumnName),
					columnValue);

		if ("le".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s<=?", qualifiedColumnName),
					columnValue);

		if ("like".equalsIgnoreCase(operatorName))
			return new StringSql(String
					.format("%s like ?", qualifiedColumnName), "%"
					+ columnValue + "%");

		if ("notLike".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s not like ?",
					qualifiedColumnName), "%" + columnValue + "%");

		if ("isNull".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s is null",
					qualifiedColumnName));

		if ("notNull".equalsIgnoreCase(operatorName))
			return new StringSql(String.format("%s is not null",
					qualifiedColumnName));

		return null;
	}

	public void consumeColumns(List<String> columns)
	{
		columns.remove(_field.getColumnName());
	}

	public String getDefaultProperty(String name)
	{
		return null;
	}

	protected Serializable normalizeColumnValue(Serializable columnValue)
			throws Exception
	{
		JdbcColumn column = ((VdbField) _field).getJdbcColumn();
		if (column != null)
			return new JdbcObject(columnValue, column);

		return columnValue;
	}

	public void jdbcDelete(JdbcRowWriter jdbcRowWriter, MyData data)
			throws Exception
	{
	}

	public Field getField()
	{
		return _field;
	}

	public void setField(Field field)
	{
		_field = field;
	}
}
