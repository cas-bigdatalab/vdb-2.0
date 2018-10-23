package vdb.mydb;

import vdb.mydb.engine.VdbApplicationContext;
import vdb.mydb.engine.VdbEngine;

/**
 * VdbManager is a singleton object which helps user to get global objects such
 * as VdbEngine, VdbApplicationContext and so on.
 * 
 * @author bluejoe
 */
public class VdbManager
{
	private static VdbEngine _engine;

	public static VdbEngine getEngine()
	{
		return _engine;
	}

	public static VdbApplicationContext getApplicationContext()
	{
		return _engine;
	}

	/**
	 * @deprecated use getEngine() instead!
	 * @return
	 */
	public static VdbEngine getInstance()
	{
		return _engine;
	}

	public static void setEngine(VdbEngine engine)
	{
		_engine = engine;
	}
}
