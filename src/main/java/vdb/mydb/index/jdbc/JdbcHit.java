package vdb.mydb.index.jdbc;

import java.io.Serializable;
import java.util.Date;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.index.Hit;

public class JdbcHit implements Hit
{
	private String _dataSetId;

	private String _entityId;

	private Date _indexTime;

	private Serializable _recordId;

	private String _text;

	private String _title;

	public Serializable getBeanId()
	{
		return _recordId;
	}

	public DataSet getDataSet()
	{
		return VdbManager.getEngine().getCatalog().fromId(_dataSetId);
	}

	public String getDataSetId()
	{
		return _dataSetId;
	}

	public Entity getEntity()
	{
		return VdbManager.getEngine().getCatalog().fromId(_entityId);
	}

	public String getEntityId()
	{
		return _entityId;
	}

	public Date getIndexTime()
	{
		return _indexTime;
	}

	public String getText()
	{
		return _text;
	}

	public String getTitle()
	{
		return _title;
	}

	public void setDataSetId(String dataSetId)
	{
		_dataSetId = dataSetId;
	}

	public void setEntityId(String entityId)
	{
		_entityId = entityId;
	}

	public void setIndexTime(Date indexTime)
	{
		_indexTime = indexTime;
	}

	public void setRecordId(Serializable recordId)
	{
		_recordId = recordId;
	}

	public void setText(String text)
	{
		_text = text;
	}

	public void setTitle(String title)
	{
		_title = title;
	}
}
