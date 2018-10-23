package vdb.mydb.typelib.data;

import java.util.Map;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbEnum extends AbstractData
{
	private Map<String, String> _options;

	private String _value;

	public VdbEnum(Map<String, String> options)
	{
		_options = options;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTitle()
	{
		try
		{
			String title = _options.get(_value);
			if (title == null)
				return null;

			return title;
		}
		catch (Exception e)
		{
			return "";
		}

	}

	public String getValue()
	{
		return _value;
	}

	public void setValue(String value)
	{
		_value = value;
	}

	public Sdef getAsSdef()
	{
		return new SimpleSdef(_value, getTitle());
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		_value = ddl.getValue();
	}

	public String getAsText()
	{
		return getValue();
	}

	public void setAsText(String text)
	{
		_value = text;
	}

	public long getBytes() {
		return _value==null?0:_value.getBytes().length;
	}

	public boolean isEmpty() throws Exception {
		return _value==null || _value.length()==0;
	}
}
