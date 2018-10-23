package vdb.mydb.types;

public abstract class ViewType
{
	private String _name;

	private String _title;

	public String getName()
	{
		return _name;
	}

	public String getTitle()
	{
		return _title;
	}

	public abstract boolean isSuitableFor(Class cls);

	public void setName(String name)
	{
		_name = name;
	}

	public void setTitle(String title)
	{
		_title = title;
	}
}
