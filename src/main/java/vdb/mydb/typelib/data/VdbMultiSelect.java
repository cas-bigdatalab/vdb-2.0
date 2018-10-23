package vdb.mydb.typelib.data;

import java.util.Map;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbMultiSelect extends AbstractData
{
	private Map<String, String> _options;

	private String _value;

	public VdbMultiSelect(Map<String, String> options)
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
			String[] vals = _value.split(";");
			String title = "";
			for (int i = 0; i < vals.length; i++)
				title = title + ";" + _options.get(vals[i]);
			if (title.length() > 0)
				return title.substring(1);
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
