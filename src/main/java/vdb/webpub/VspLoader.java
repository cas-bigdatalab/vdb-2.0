package vdb.webpub;

import java.io.File;
import java.io.IOException;
import java.util.List;

import vdb.mydb.VdbManager;
import vdb.mydb.util.FilePathSpan;
import vdb.webpub.publisher.VdbFileFinder;
import cn.csdb.commons.util.Matcher;

public class VspLoader
{
	private VdbFileFinder _finder;

	public VspLoader()
	{
		_finder = new VdbFileFinder();
	}

	public void load(File baseDir) throws IOException
	{
		List<File> paths = _finder.find(baseDir, new Matcher<File>()
		{

			public boolean matches(File toMatch)
			{
				String name = toMatch.getName();
				if (name.startsWith("."))
					return false;

				return name.endsWith(".vpage") || name.endsWith(".vsp");
			}
		});

		for (File path : paths)
		{
			try
			{
				File root = new File(VdbManager.getEngine().getRealPath("/"));
				VdbManager.getEngine().getVelocityEngine().getTemplate(
						new FilePathSpan(root, path).getRelativePath());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
