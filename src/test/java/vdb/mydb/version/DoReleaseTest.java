package vdb.mydb.version;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DoReleaseTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testMain() throws Exception
	{
		DoRelease.main(new String[] { "C:\\vdb-2.0-release\\update" });
	}

}
