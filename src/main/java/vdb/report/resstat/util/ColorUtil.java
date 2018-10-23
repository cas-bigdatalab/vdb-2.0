package vdb.report.resstat.util;

/*
 * @author Ren Zhang
 */

public class ColorUtil
{
	private String colors[] = { "6B0418", "6B1000", "6B4D00", "296500",
			"006D21", "006D5A", "004D63", "001C6B", "5A046B", "63004A",
			"396D00", "8F8FBD", "FDC12E", "FF0000", "00FF00", "0000FF",
			"FF00FF", "00FFFF", "7B68EE", "00FDAB", "70DB93", "5C3317",
			"9F5F9F" };

	private int curIndex;

	public ColorUtil()
	{
		curIndex = 0;
	}

	public String getNextColor()
	{
		return colors[curIndex++ % 23];
	}
}
