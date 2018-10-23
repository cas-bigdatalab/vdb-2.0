package vdb.mydb.typelib.type;

import java.io.Serializable;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbLong;
import cn.csdb.commons.util.ObjectHelper;

public class VdbLongType extends AbstractFieldDriver<VdbLong>
{
	public VdbLong createData() throws Exception
	{
		return new VdbLong();
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbLong data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getLong());
	}

	public VdbLong jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbLong vd = createData();
		Serializable o = jdbcRowReader.get(_field.getColumnName());
		vd.setLong(o == null ? null : new ObjectHelper(o).evalLong());
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbLong data,
			VdbLong dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getLong());
	}
}