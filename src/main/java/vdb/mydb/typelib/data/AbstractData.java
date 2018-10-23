package vdb.mydb.typelib.data;

import java.io.Serializable;

import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.sdef.Sdef;

public abstract class AbstractData implements VdbData, Serializable
{
	public String getTitle()
	{
		Sdef sdef = getAsSdef();
		String title = sdef.getTitle();
		if (title == null || title.length() == 0)
			title = sdef.getValue();

		return title;
	}

	public String getValue()
	{
		return getAsSdef().getValue();
	}

	/**
	 * @deprecated
	 * @return
	 */
	public String getJdbcObject()
	{
		return getValue();
	}

	public String toString()
	{
		return getAsSdef().getXml();
	}
}