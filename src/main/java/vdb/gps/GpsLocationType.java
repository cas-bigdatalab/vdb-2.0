package vdb.gps;

import vdb.mydb.typelib.JdbcRowReader;
import vdb.mydb.typelib.JdbcRowWriter;
import vdb.mydb.typelib.type.AbstractFieldDriver;

public class GpsLocationType extends AbstractFieldDriver<GpsLocation>
{
	public GpsLocation createData() throws Exception
	{
		return new GpsLocation();
	}

	public void jdbcInsert(JdbcRowWriter jdbcFieldSetter, GpsLocation data)
			throws Exception
	{
		String lonColumnName = _field.get("lonColumn");
		String latColumnName = _field.get("latColumn");
		String altColumnName = _field.get("altColumn");
		jdbcFieldSetter.set(lonColumnName, ((GpsLocation) data).getLon());
		jdbcFieldSetter.set(latColumnName, ((GpsLocation) data).getLat());
		jdbcFieldSetter.set(altColumnName, ((GpsLocation) data).getAlt());
	}

	public GpsLocation jdbcSelect(JdbcRowReader jdbcFieldGetter)
			throws Exception
	{
		GpsLocation data = createData();
		String lonColumnName = _field.get("lonColumn");
		String latColumnName = _field.get("latColumn");
		String altColumnName = _field.get("altColumn");

		if (jdbcFieldGetter.get(lonColumnName) != null)
		{
			// data.setLon(((Float)(Serializable) jdbcFieldGetter
			// .get(lonColumnName)).doubleValue());
			data.setLon(Double.parseDouble(""
					+ jdbcFieldGetter.get(lonColumnName)));
		}

		if (jdbcFieldGetter.get(latColumnName) != null)
		{
			data.setLat(Double.parseDouble(""
					+ jdbcFieldGetter.get(latColumnName)));
		}

		if (jdbcFieldGetter.get(altColumnName) != null)
		{
			data.setAlt(Double.parseDouble(""
					+ jdbcFieldGetter.get(altColumnName)));
		}

		return data;
	}

	public void jdbcUpdate(JdbcRowWriter jdbcFieldSetter, GpsLocation data,
			GpsLocation dataOutOfDate) throws Exception
	{
		String lonColumnName = _field.get("lonColumn");
		String latColumnName = _field.get("latColumn");
		String altColumnName = _field.get("altColumn");
		jdbcFieldSetter.set(lonColumnName, ((GpsLocation) data).getLon());
		jdbcFieldSetter.set(latColumnName, ((GpsLocation) data).getLat());
		jdbcFieldSetter.set(altColumnName, ((GpsLocation) data).getAlt());
	}
}