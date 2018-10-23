package vdb.mydb.files;

import java.io.Serializable;

import vdb.metacat.Field;

public class FileOwner
{
	private Field _field;

	private Serializable _recordId;

	public FileOwner(Serializable recordId, Field field)
	{
		super();
		_recordId = recordId;
		_field = field;
	}

	public Field getField()
	{
		return _field;
	}

	public Serializable getRecordId()
	{
		return _recordId;
	}
}
