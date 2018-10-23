package vdb.mydb.typelib.data;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbChemStructure extends AbstractData
{
	String _string;

	public boolean isEmpty()
	{
		return _string == null || _string.length() == 0;
	}

	public String format(String pattern)
	{
		return String.format(pattern, _string);
	}

	public Sdef getAsSdef()
	{
		return new SimpleSdef(_string);
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		_string = ddl.getValue();
	}

	public String getString()
	{
		return _string;
	}

	public void setString(String string)
	{
		_string = string;
	}

	public String getAsText()
	{
		return getString();
	}

	public void setAsText(String text)
	{
		_string = text;

	}

	public long getBytes() {
		return _string==null ?0:_string.getBytes().length;
	}
}