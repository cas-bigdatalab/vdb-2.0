package vdb.mydb.index.db4o;

import vdb.mydb.index.Session;

import com.db4o.ObjectContainer;

public class Db4oSession implements Session
{
	private ObjectContainer _objectContainer;

	public Db4oSession(ObjectContainer objectContainer)
	{
		_objectContainer = objectContainer;
	}

	public void close()
	{
		_objectContainer.close();
	}

	public void commit()
	{
		_objectContainer.commit();
	}

	public ObjectContainer getDb4oContainer()
	{
		return _objectContainer;
	}
}
