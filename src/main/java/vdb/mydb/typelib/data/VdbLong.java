package vdb.mydb.typelib.data;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbLong extends AbstractData
{
	private Long _long;

	public String format(String pattern)
	{
		String ret = "";
		if (_long == null)
			return "";
		try
		{
			ret = String.format(pattern, _long);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		return ret;
	}

	public Sdef getAsSdef()
	{
		return new SimpleSdef("" + _long);
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		if (ddl.getValue() != null && ddl.getValue().length() > 0)
			_long = Long.parseLong(ddl.getValue());
		else
			_long = null;
	}

	public Long getLong()
	{
		return _long;
	}

	public void setLong(Long number)
	{
		_long = number;
	}

	public String getAsText()
	{
		return _long + "";
	}

	public void setAsText(String text)
	{
		_long = Long.parseLong(text);
	}

	public long getBytes()
	{
		if (_long == null)
			return 0;
		else if (("" + _long).equals(""))
			return 0;
		else
			return Long.SIZE / 8;
	}

	public boolean isEmpty()
	{
		if (_long == null || ("" + _long).equals(""))
			return true;
		else
			return false;
	}
}
