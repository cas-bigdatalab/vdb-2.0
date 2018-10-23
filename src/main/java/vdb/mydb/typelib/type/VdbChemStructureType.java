package vdb.mydb.typelib.type;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.data.VdbChemStructure;

public class VdbChemStructureType extends AbstractFieldDriver<VdbChemStructure>
{
	public VdbChemStructure createData() throws Exception
	{
		return new VdbChemStructure();
	}

	public void jdbcInsert(JdbcRowWriter jdbcRowWriter, VdbChemStructure data)
			throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}

	public VdbChemStructure jdbcSelect(JdbcRowReader jdbcRowReader)
			throws Exception
	{
		VdbChemStructure vd = createData();
		vd.setString((String) jdbcRowReader.get(_field.getColumnName()));
		return vd;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcRowWriter, VdbChemStructure data,
			VdbChemStructure dataOutOfDate) throws Exception
	{
		jdbcRowWriter.set(_field.getColumnName(), data.getString());
	}
}