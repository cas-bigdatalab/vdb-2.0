/*
 * Created on 2006-12-24
 */
package cn.csdb.commons.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils
{
	public static void rmdirs(File file)
	{
		if (file.isDirectory())
		{
			File[] fs = file.listFiles();
			for (File cf : fs)
			{
				rmdirs(cf);
			}
		}

		file.delete();
	}

	public static void rmfiles(File rootDir, Matcher<File> matcher)
	{
		if (matcher.matches(rootDir))
		{
			if (rootDir.isDirectory())
			{
				rmdirs(rootDir);
			}
			else
			{
				rootDir.delete();
			}

			return;
		}

		if (rootDir.isDirectory())
		{
			File[] fs = rootDir.listFiles();
			for (File cf : fs)
			{
				rmfiles(cf, matcher);
			}

			return;
		}

	}

	public static void copy(File src, File dest, Matcher<File> matcher)
			throws IOException
	{
		if (!matcher.matches(src))
			return;

		if (src.isDirectory())
		{
			dest.mkdirs();

			String[] fs = src.list();
			for (int i = 0; i < fs.length; i++)
			{
				copy(new File(src, fs[i]), new File(dest, fs[i]), matcher);
			}
		}
		else
		{
			FileOutputStream fos = new FileOutputStream(dest);
			FileInputStream fis = new FileInputStream(src);
			while (true)
			{
				byte[] bs = new byte[20480];
				int n = fis.read(bs);
				if (n < 0)
					break;
				fos.write(bs, 0, n);
			}

			fis.close();
			fos.close();
		}
	}

	public static void copy(File src, File dest) throws IOException
	{
		copy(src, dest, new Matcher<File>()
		{
			public boolean matches(File toMatch)
			{
				return true;
			}
		});
	}

	public static long length(File file)
	{
		long size = 0;
		if (file.isFile())
			return file.length();

		if (file.isDirectory())
		{
			String[] files = file.list();
			for (int i = 0; i < files.length; i++)
			{
				size += length(new File(file, files[i]));
			}
		}

		return size;
	}

	public static void saveFile(String body, File file) throws IOException
	{
		saveFile(new ByteArrayInputStream(body.getBytes()), file);
	}

	public static void saveFile(InputStream is, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		while (true)
		{
			byte[] bytes = new byte[20480];
			int read = is.read(bytes, 0, bytes.length);
			if (read == -1)
				break;
			fos.write(bytes, 0, read);
		}

		fos.close();
	}
}
