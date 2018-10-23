package vdb.mydb.vtl.toolbox;

import vdb.mydb.VdbManagerTest;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.typelib.data.VdbLong;

public class VdbToolTest extends VdbManagerTest
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGetBeanStringSerializable()
	{
		try
		{
			AnyBean bean = new VdbTool().getBean("cn.csdb.vdb.vdbqhh.birds", 1);
			VdbLong id = (VdbLong) bean.get("id");
		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}

	}

}
