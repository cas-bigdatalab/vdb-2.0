package vdb.mydb.typelib;

import java.io.Serializable;
import java.util.List;

import vdb.metacat.Field;
import vdb.mydb.typelib.sdef.Sdef;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/*
 * a VdbFieldDriver object tells how to create, store a VdbData for a VdbField,
 * every VdbField object has its driver.
 * 
 * @author bluejoe
 */
public interface VdbFieldDriver<MyData extends VdbData> extends Serializable
{
	public Field getField();

	public void setField(Field field);

	public StringSql buildFilter(String operatorName, Sdef ddl,
			boolean isCrossQuery) throws Exception;

	public void consumeColumns(List<String> columns);

	public MyData createData() throws Exception;

	public String getDefaultProperty(String name);

	public MyData jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception;

	public void jdbcDelete(JdbcRowWriter jdbcRowWriter, MyData data)
			throws Exception;

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, MyData data)
			throws Exception;

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, MyData data,
			MyData dataOutOfDate) throws Exception;
}
