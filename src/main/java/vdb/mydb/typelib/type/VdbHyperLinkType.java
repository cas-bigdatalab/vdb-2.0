package vdb.mydb.typelib.type;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbHyperLink;

/*
 * 
 * @author 刘奇
 */
public class VdbHyperLinkType extends AbstractFieldDriver<VdbHyperLink>
{
	public VdbHyperLink createData() throws Exception
	{
		return new VdbHyperLink();
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbHyperLink data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}

	public VdbHyperLink jdbcSelect(JdbcRowReader jdbcRowReader)
			throws Exception
	{
		VdbHyperLink vd = createData();
		vd.setString((String) jdbcRowReader.get(_field.getColumnName()));
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbHyperLink data,
			VdbHyperLink dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}
}