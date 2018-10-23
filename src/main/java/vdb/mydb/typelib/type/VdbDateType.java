package vdb.mydb.typelib.type;

import java.io.Serializable;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbDate;
import cn.csdb.commons.util.ObjectHelper;

public class VdbDateType extends AbstractFieldDriver<VdbDate>
{
	public VdbDate createData() throws Exception
	{
		VdbDate vd = new VdbDate();
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbDate data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getDate());
	}

	public VdbDate jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbDate vd = createData();
		Serializable o = jdbcRowReader.get(_field.getColumnName());
		vd.setDate(new ObjectHelper(o).evalDate());
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbDate data,
			VdbDate dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getDate());
	}
}