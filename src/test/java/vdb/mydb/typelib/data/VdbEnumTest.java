package vdb.mydb.typelib.data;

import java.util.Map;

import junit.framework.TestCase;
import cn.csdb.commons.util.StringKeyMap;

public class VdbEnumTest extends TestCase
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
		Map<String, String> options = new StringKeyMap<String>();
		options.put("m", "��");
		options.put("f", "Ů");

		VdbEnum vd = new VdbEnum(options);
		vd.setValue("m");
		System.err.println(vd.getTitle());
		System.err.println(vd.getAsSdef().getXml());
	}
}
