package vdb.mydb.typelib.data;

import java.util.Date;

import junit.framework.TestCase;

public class VdbRichTextTest extends TestCase
{
	private VdbRichText _rt;

	protected void setUp() throws Exception
	{
		super.setUp();
		_rt = new VdbRichText();
		_rt.setString("hello, <div id=123>my name is <b>bluejoe</b></div><p>");
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGetAsDdl()
	{
		System.err.println(_rt.getAsSdef().getXml());
	}

	public void testGetPlainText()
	{
		// System.err.println(_rt.getPlainText());

		VdbLong vl = new VdbLong();
		vl.setLong(123L);
		System.err.println(vl.getAsSdef().getXml());

		VdbDate vd = new VdbDate();
		vd.setDate(new Date());
		System.err.println(vd.getAsSdef().getXml());
	}

}
