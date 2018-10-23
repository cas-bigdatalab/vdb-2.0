package vdb.mydb.version;

import vdb.mydb.VdbManager;
import vdb.mydb.VdbManagerTest;

public class ReleaseVersionBuilderTest extends VdbManagerTest
{
	public void test() throws Exception
	{
		ReleaseVersionBuilder releaseVersionBuilder = (ReleaseVersionBuilder) VdbManager
				.getApplicationContext().getBean("releaseVersionBuilder");

		releaseVersionBuilder.build(1024);
	}
}
