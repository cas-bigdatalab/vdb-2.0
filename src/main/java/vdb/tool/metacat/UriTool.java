package vdb.tool.metacat;

import java.io.Serializable;

import vdb.metacat.CatalogObject;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;

public class UriTool
{
	public String getDataSetUri(Serializable uri)
	{
		try
		{
			return getEntityUriFromItemID(uri);
		}
		catch (Exception e)
		{
			return null;
		}

	}

	public String getEntityUri(Serializable uri)
	{
		try
		{
			return getEntityUriFromItemID(uri);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public String getId(Serializable uri)
	{
		try
		{
			return getShortIDFromItemID(uri);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public boolean isDataSet(Serializable uri)
	{
		try
		{
			return fromUri(uri) instanceof DataSet;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean isEntity(Serializable uri)
	{
		try
		{
			return fromUri(uri) instanceof Entity;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean isItem(Serializable uri)
	{
		try
		{
			String first = getEntityUriFromItemID(uri);
			String second = getShortIDFromItemID(uri);
			if (first != null && second != null && isEntity(uri))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}

	private CatalogObject fromUri(Serializable uri) throws Exception
	{
		return VdbManager.getEngine().getCatalog().fromUri("" + uri);
	}

	private String getEntityUriFromItemID(Serializable itemId) throws Exception
	{
		String s = "" + itemId;
		return s.substring(0, s.indexOf('/'));
	}

	private String getShortIDFromItemID(Serializable itemId) throws Exception
	{
		String s = "" + itemId;
		return s.substring(s.indexOf('/') + 1);
	}

}
