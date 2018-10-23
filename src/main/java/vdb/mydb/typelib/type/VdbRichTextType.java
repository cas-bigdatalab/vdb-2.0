package vdb.mydb.typelib.type;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbRichText;

public class VdbRichTextType extends AbstractFieldDriver<VdbRichText>
{

	public VdbRichText createData() throws Exception
	{
		VdbRichText vd = new VdbRichText();
		return vd;
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbRichText data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}

	public VdbRichText jdbcSelect(JdbcRowReader jdbcRowReader) throws Exception
	{
		VdbRichText vd = createData();
		vd.setString((String) jdbcRowReader.get(_field.getColumnName()));
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbRichText data,
			VdbRichText dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}

}
