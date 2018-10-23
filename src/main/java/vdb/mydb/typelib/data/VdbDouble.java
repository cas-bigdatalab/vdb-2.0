package vdb.mydb.typelib.data;

import java.text.NumberFormat;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbDouble extends AbstractData
{
	private Double _double;

	public String format(String pattern) throws Exception
	{
		String ret = "";
		if (_double == null)
			return "";
		try
		{
			ret = String.format(pattern, _double);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
		return ret;
	}

	public String format()
	{
		// str = bean.get(field.getName()).getValue();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		// str = nf.format(Double.parseDouble(str));
		if (this._double != null)
			return nf.format(this._double);
		return "";
	}

	public Sdef getAsSdef()
	{
		return new SimpleSdef("" + _double);
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		if (ddl.getValue() != null && ddl.getValue().length() > 0)
			_double = Double.parseDouble(ddl.getValue());
		else
			_double = null;
	}

	public Double getDouble()
	{
		return _double;
	}

	public void setDouble(Double l)
	{
		_double = l;
	}

	public String getAsText()
	{
		return _double + "";
	}

	public void setAsText(String text)
	{
		_double = Double.parseDouble(text);
	}

	public long getBytes()
	{
		if (_double == null)
			return 0;
		else if (("" + _double).equals(""))
			return 0;
		else
			return Double.SIZE / 8;
	}

	public boolean isEmpty()
	{
		if (_double == null || ("" + _double).equals(""))
			return true;
		else
			return false;
	}
}
