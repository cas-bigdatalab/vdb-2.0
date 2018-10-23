package vdb.mydb;

import vdb.metacat.DataSet;

public class DefaultsTest extends VdbManagerTest
{
	public void testDefaultValues()
	{
		DataSet ds = VdbManager.getEngine().getCatalog().fromUri("bookstore");
		assertEquals(60, ds.getRepository().getLoginTimeout());
	}
}
