package vdb.mydb.typelib.idgen;

import vdb.metacat.idgen.IdGenerator;

public abstract class AbstractIdGenerator implements IdGenerator
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

	public void setName(String name)
	{
		_name = name;
	}

	public void setTitle(String title)
	{
		_title = title;
	}
}
