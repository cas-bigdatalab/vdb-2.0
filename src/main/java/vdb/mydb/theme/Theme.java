package vdb.mydb.theme;

/*
 * 
 * @author bluejoe
 */
public class Theme
{
	private String _baseTheme;

	private boolean _hidden;

	private String _name;

	private int _order;

	private String _provider;

	private String _title;

	public String getBaseTheme()
	{
		return _baseTheme;
	}

	public String getName()
	{
		return _name;
	}

	public int getOrder()
	{
		return _order;
	}

	public String getProvider()
	{
		return _provider;
	}

	public String getTitle()
	{
		return _title;
	}

	public boolean isHidden()
	{
		return _hidden;
	}

	public void setBaseTheme(String baseTheme)
	{
		_baseTheme = baseTheme;
	}

	public void setHidden(boolean hidden)
	{
		_hidden = hidden;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setOrder(int order)
	{
		_order = order;
	}

	public void setProvider(String provider)
	{
		_provider = provider;
	}

	public void setTitle(String title)
	{
		_title = title;
	}
}
