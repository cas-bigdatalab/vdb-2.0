package vdb.mydb.typelib.data;

import junit.framework.TestCase;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefNode;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class SimpleDdlTest extends TestCase
{
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testToString() throws Exception
	{
		Sdef ddl = new SimpleSdef("aaa", "title1");
		ddl.addChild("time", "xxxx");
		SdefNode dn = ddl.addChild("items");

		for (int i = 0; i < 5; i++)
		{
			SdefNode d2 = dn.addChild("item");
			d2.setNodeValue("xxx" + i);
		}

		System.err.println(ddl.getXml());

		String xml = ddl.getXml();
		Sdef ddl2 = new SimpleSdef();
		ddl2.setXml(xml);
		// System.err.println(ddl2.getXml());

		assertEquals(5, ddl2.getChild("items").getChildren("item").size());
		assertEquals("xxxx", ddl2.getChildValue("time"));
		assertEquals("xxx0", ddl2.getChild("items").getChildren("item").get(0)
				.getNodeValue());
	}
}
