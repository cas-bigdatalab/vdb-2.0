package vdb.tool.webpub;

import vdb.mydb.VdbManager;
import vdb.webpub.VdbPublisher;

public class PublisherTool
{
	public VdbPublisher getVdbPublisher()
	{
		return (VdbPublisher) VdbManager.getEngine().getApplicationContext()
				.getBean("vdbPublisher");
	}
}
