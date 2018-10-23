package vdb.mydb.index.db4o;

import java.io.Serializable;
import java.util.Date;

public class Db4oIndexEntry
{
	private Serializable _beanId;

	private String _entityId;

	private Date _indexTime;

	private String _text;

	private String _title;

	public Serializable getBeanId()
	{
		return _beanId;
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

	public void setBeanId(Serializable beanId)
	{
		_beanId = beanId;
	}

	public void setEntityId(String entityId)
	{
		_entityId = entityId;
	}

	public void setIndexTime(Date indexTime)
	{
		_indexTime = indexTime;
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
