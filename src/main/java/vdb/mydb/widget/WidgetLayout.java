package vdb.mydb.widget;

import java.util.List;
import java.util.Map;

public class WidgetLayout
{
	private String _name;

	private String _title;

	private String _layout;

	private String _theme;

	LayoutPattern _layoutPattern;

	private Map<String, List> _columns;

	private Map<String, String> _style;

	public Map<String, String> getStyle()
	{
		return _style;
	}

	public void setStyle(Map<String, String> style)
	{
		this._style = style;
	}

	public Map<String, List> getColumns()
	{
		return _columns;
	}

	public void setColumns(Map<String, List> columns)
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

	public String getLayout()
	{
		return _layout;
	}

	public LayoutPattern getLayoutPattern()
	{
		return _layoutPattern;
	}

	public void setLayout(String layout)
	{
		this._layout = layout;
	}

	public String getTheme()
	{
		return _theme;
	}

	public void setTheme(String theme)
	{
		this._theme = theme;
	}

	public void setLayoutPattern(LayoutPattern layoutPattern)
	{
		_layoutPattern = layoutPattern;
	}
}
