package vdb.mydb.typelib.type;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbCascade;
import vdb.mydb.typelib.data.VdbEnum;

public class VdbCascadeType extends AbstractFieldDriver<VdbCascade>
{

	public VdbCascade createData() throws Exception
	{

		return null;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbEnum data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getValue());
	}

	public VdbCascade jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbCascade vd = createData();
		vd.setValue("" + jdbcRowReader.get(_field.getColumnName()));
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbEnum data,
			VdbEnum dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getValue());
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbCascade data)
			throws Exception
	{

	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbCascade data,
			VdbCascade dataOutOfDate) throws Exception
	{

	}

}
