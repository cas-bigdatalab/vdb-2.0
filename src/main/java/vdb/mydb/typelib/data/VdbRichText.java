package vdb.mydb.typelib.data;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbRichText extends VdbString
{
	public Sdef getAsSdef()
	{
		return new SimpleSdef(_string, getPlainText());
	}

	public String getPlainText()
	{
		if (_string != null)
			return _string.replaceAll("<(.*?)>", "");
		return null;
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		_string = ddl.getValue();
	}
}
