package vdb.tool.generic;

import java.util.Locale;

import vdb.mydb.VdbManagerTest;

public class MessageToolTest extends VdbManagerTest
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void test1()
	{
		ServletContextLocaleTool t = new ServletContextLocaleTool();
		Locale l = t.getLocale();
		assertEquals("en", l.getLanguage());
		assertEquals("hello,bluejoe", t.message("greeting", "bluejoe"));
		t.setLocale("zh_CN");
		l = t.getLocale();
		assertEquals("zh", l.getLanguage());
		assertEquals("CN", l.getCountry());
		assertEquals("你好，bluejoe", t.message("greeting", "bluejoe"));
	}
}
