package vdb.mydb.index.lucene;

import java.io.Serializable;
import java.util.Date;

import org.apache.lucene.document.Document;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.index.Hit;

public class LuceneHit implements Hit
{
	private Document _doc;

	public LuceneHit(Document doc)
	{
		_doc = doc;
	}

	public Serializable getBeanId()
	{
		return _doc.get("RECORDID");
	}

	public DataSet getDataSet()
	{
		return VdbManager.getInstance().getCatalog().fromId(
				_doc.get("DATASETID"));
	}

	public Entity getEntity()
	{
		return VdbManager.getInstance().getCatalog().fromId(
				_doc.get("ENTITYID"));
	}

	public Date getIndexTime()
	{
		return new Date(Long.parseLong(_doc.get("INDEXTIME")));
	}

	public String getText()
	{
		return _doc.get("INDEXTEXT");
	}

	public String getTitle()
	{
		return _doc.get("TITLE");
	}

}
