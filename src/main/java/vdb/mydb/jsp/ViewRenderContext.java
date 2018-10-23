package vdb.mydb.jsp;

import vdb.mydb.typelib.VdbData;

public class ViewRenderContext
{
	private VdbData _data;

	private boolean _readonly = false;

	private boolean _visible = false;

	public VdbData getData()
	{
		return _data;
	}

	public boolean isReadonly()
	{
		return _readonly;
	}

	public boolean isVisible()
	{
		return _visible;
	}

	public void setData(VdbData data)
	{
		_data = data;
	}

	public void setReadonly(boolean readonly)
	{
		_readonly = readonly;
	}

	public void setVisible(boolean visible)
	{
		_visible = visible;
	}
}
