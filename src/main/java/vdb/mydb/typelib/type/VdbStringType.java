package vdb.mydb.typelib.type;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbString;
import cn.csdb.commons.util.ObjectHelper;

public class VdbStringType extends AbstractFieldDriver<VdbString>
{
	public VdbString createData() throws Exception
	{
		return new VdbString();
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbString data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}

	public VdbString jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbString vd = createData();
		vd
				.setString(new ObjectHelper(jdbcRowReader.get(_field
						.getColumnName())).evalString());
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbString data,
			VdbString dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}
}