package vdb.mydb;

import java.io.File;

import junit.framework.TestCase;
import vdb.mydb.engine.VdbEngineStarter;

public class VdbManagerTest extends TestCase
{
	protected void setUp() throws Exception
	{
		super.setUp();

		File appRoot = new File("e:\\workspace\\vdb-2.0\\WebRoot");
		try
		{
			new VdbEngineStarter().startEngine(appRoot);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

}
