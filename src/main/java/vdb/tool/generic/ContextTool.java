package vdb.tool.generic;

import org.apache.velocity.context.Context;

import vdb.tool.VdbTool;

public class ContextTool extends VdbTool
{
	private Context _context;

	public void init(Object obj)
	{
		_context = (Context) obj;
	}

	public void remove(String name)
	{
		_context.remove(name);
	}
}
