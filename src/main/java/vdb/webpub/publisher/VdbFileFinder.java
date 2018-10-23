package vdb.webpub.publisher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.csdb.commons.util.Matcher;

public class VdbFileFinder
{
	private void find(File file, List<File> paths, Matcher<File> matcher)
			throws IOException
	{
		String fn = file.getName();
		// .svn!
		if (fn.startsWith("."))
			return;

		if (fn.startsWith("#"))
			return;

		if (file.isDirectory())
		{
			for (File cf : file.listFiles())
			{
				find(cf, paths, matcher);
			}
		}
		else
		{
			if (matcher.matches(file))
			{
				paths.add(file);
			}
		}
	}

	public List<File> find(File baseDir, Matcher<File> matcher)
			throws IOException
	{
		List<File> paths = new ArrayList<File>();
		find(baseDir, paths, matcher);
		return paths;
	}
}
