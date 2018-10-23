package vdb.tool.generic;

import java.io.OutputStream;

import vdb.tool.VdbTool;

public class SystemTool extends VdbTool
{
	public OutputStream getErr()
	{
		return System.err;
	}

	public OutputStream getOut()
	{
		return System.out;
	}
}
