package vdb.mydb.metacat;

import vdb.metacat.CatalogObject;
import vdb.metacat.View;

public class ViewsProxy
{
	private View[] _views;

	public ViewsProxy(View[] views)
	{
		_views = views;
	}

	public View getView(CatalogObject co, String viewName)
	{
		for (View view : _views)
		{
			if (view.getSource() == co && viewName != null
					&& viewName.equalsIgnoreCase(view.getName()))
				return view;
		}

		return null;
	}
}
