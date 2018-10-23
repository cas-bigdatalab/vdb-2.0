package vdb.mydb.widget;

import java.util.Map;

public class LayoutPattern
{
	private String _name;

	private String _title;

	private Map<String, String> _columns;

	public Map<String, String> getColumns()
	{
		return _columns;
	}

	public void setColumns(Map<String, String> columns)
	{
		this._columns = columns;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		this._name = name;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String title)
	{
		this._title = title;
	}

}
