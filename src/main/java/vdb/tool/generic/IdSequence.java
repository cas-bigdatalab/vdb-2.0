package vdb.tool.generic;

import vdb.tool.VdbTool;

public class IdSequence extends VdbTool
{
	private int _ticks;

	public String getId()
	{
		_ticks++;
		return "" + _ticks;
	}

	public void init(Object obj)
	{
		_ticks = 0;
	}
}