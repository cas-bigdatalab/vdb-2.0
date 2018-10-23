package vdb.tool.engine;

import vdb.mydb.VdbManager;
import vdb.mydb.engine.VdbApplicationContext;

public class VdbApplicationContextTool extends VdbApplicationContext
{
	public VdbApplicationContextTool()
	{
		super.setApplicationContext(VdbManager.getEngine()
				.getApplicationContext());
	}
}