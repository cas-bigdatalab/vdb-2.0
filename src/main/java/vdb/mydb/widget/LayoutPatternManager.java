package vdb.mydb.widget;

import java.util.List;

public class LayoutPatternManager
{
	List<LayoutPattern> _layouts;

	public List<LayoutPattern> getLayoutPatterns()
	{
		return _layouts;
	}

	public LayoutPattern getLayoutPattern(String name)
	{
		for (int i = 0; i < _layouts.size(); i++)
		{
			if (_layouts.get(i).getName().equalsIgnoreCase(name))
				return _layouts.get(i);
		}
		return null;
	}

	public void setLayoutPatterns(List<LayoutPattern> layouts)
	{
		this._layouts = layouts;
	}

}
