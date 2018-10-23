package vdb.mydb.index.db4o;

import java.io.Serializable;
import java.util.Date;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.index.Hit;

public class Db4oHit implements Hit
{
	private Db4oIndexEntry _indexEntry;

	public Db4oHit(Db4oIndexEntry indexEntry)
	{
		_indexEntry = indexEntry;
	}

	public Serializable getBeanId()
	{
		return _indexEntry.getBeanId();
	}

	public DataSet getDataSet()
	{
		Entity en = getEntity();
		return en == null ? null : en.getDataSet();
	}

	public Entity getEntity()
	{
		return VdbManager.getInstance().getCatalog().fromId(
				_indexEntry.getEntityId());
	}

	public Date getIndexTime()
	{
		return _indexEntry.getIndexTime();
	}

	public String getText()
	{
		return _indexEntry.getText();
	}

	public String getTitle()
	{
		return _indexEntry.getTitle();
	}
}
