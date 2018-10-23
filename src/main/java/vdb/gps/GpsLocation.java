package vdb.gps;

import vdb.mydb.typelib.data.AbstractData;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class GpsLocation extends AbstractData
{
	private double _alt;

	private double _lat;

	private double _lon;

	public double getAlt()
	{
		return _alt;
	}

	public double getLat()
	{
		return _lat;
	}

	public double getLon()
	{
		return _lon;
	}

	public void setAlt(double alt)
	{
		_alt = alt;
	}

	public void setLat(double lat)
	{
		_lat = lat;
	}

	public void setLon(double lon)
	{
		_lon = lon;
	}

	public Sdef getAsSdef()
	{
		Sdef sdef = new SimpleSdef("" + getLon() + "," + getLat() + ","
				+ getAlt());
		return sdef;
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		try
		{
			setLon(Double.parseDouble(ddl.getChildren("value").get(0)
					.getNodeValue()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			setLat(Double.parseDouble(ddl.getChildren("value").get(1)
					.getNodeValue()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			setAlt(Double.parseDouble(ddl.getChildren("value").get(2)
					.getNodeValue()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public String getAsText()
	{

		return null;
	}

	public void setAsText(String text)
	{
		if (text != null)
		{
			String[] ts = text.split(",");
			if (ts != null)
			{
				if (ts.length > 0)
					setLon(Double.parseDouble(ts[0]));
				if (ts.length > 1)
					setLat(Double.parseDouble(ts[1]));
				if (ts.length > 2)
					setAlt(Double.parseDouble(ts[2]));
			}
		}

	}

	public long getBytes()
	{
		//FIXME: 24 bytes?
		return 24;
	}

	public boolean isEmpty() throws Exception
	{
		return false;
	}
}
