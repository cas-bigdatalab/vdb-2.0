package vdb.tool.generic;

import junit.framework.TestCase;

public class FormatToolTest extends TestCase
{

	public void test1()
	{
		FormatTool t = new FormatTool();
		System.out.println(t.formatBytes(0));
		System.out.println(t.formatBytes(1));
		System.out.println(t.formatBytes(1023));
		System.out.println(t.formatBytes(1024));
		System.out.println(t.formatBytes(1025));
		System.out.println(t.formatBytes(1024 + 512));
		System.out.println(t.formatBytes(1024 * 1024));
		System.out.println(t.formatBytes(1024 * 1024 - 1));
		System.out.println(t.formatBytes(1024 * 1024 + 1));
	}
}
