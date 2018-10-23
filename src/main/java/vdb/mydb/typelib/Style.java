package vdb.mydb.typelib;

public class Style
{
	private boolean _isUnaryExpr = false;

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

	public boolean isUnaryExpr()
	{
		return _isUnaryExpr;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public void setUnaryExpr(boolean isUnaryExpr)
	{
		_isUnaryExpr = isUnaryExpr;
	}
}