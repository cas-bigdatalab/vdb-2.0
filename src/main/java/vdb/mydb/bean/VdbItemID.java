package vdb.mydb.bean;

import java.io.Serializable;

public class VdbItemID implements ItemID
{
	private String _entityUri;

	private Serializable _id;

	public VdbItemID(String itemId)
	{
		int i = itemId.indexOf("/");
		if (i > 0)
		{
			_entityUri = itemId.substring(0, i);
			_id = itemId.substring(i + 1);
		}
	}

	public VdbItemID(String entityUri, Serializable itemValue)
	{
		_entityUri = entityUri;
		_id = itemValue;
	}

	public String getText()
	{
		return _entityUri + "/" + _id;
	}

	@Override
	public String toString()
	{
		return getText();
	}

	public String getEntityUri()
	{
		return _entityUri;
	}

	public Serializable getId()
	{
		return _id;
	}
}
