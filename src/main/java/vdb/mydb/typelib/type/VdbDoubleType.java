package vdb.mydb.typelib.type;

import java.io.Serializable;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbDouble;
import cn.csdb.commons.util.ObjectHelper;

public class VdbDoubleType extends AbstractFieldDriver<VdbDouble>
{
	public VdbDouble createData() throws Exception
	{
		return new VdbDouble();
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbDouble data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getDouble());
	}

	public VdbDouble jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbDouble vd = createData();
		Serializable o = jdbcRowReader.get(_field.getColumnName());
		vd.setDouble(o == null ? null : new ObjectHelper(o).evalDouble());
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbDouble data,
			VdbDouble dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getDouble());
	}
}