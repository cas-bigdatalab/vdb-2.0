package vdb.mydb.util;

import java.io.File;
import java.io.IOException;

public class FilePathSpan
{
	private String _relativePath;

	public FilePathSpan(File baseDir, File file) throws IOException
	{
		super();
		String fullPath = file.getCanonicalPath();
		String basePath = baseDir.getCanonicalPath();
		if (!fullPath.startsWith(basePath))
		{
			throw new IOException();
		}

		_relativePath = fullPath.substring(basePath.length());
		_relativePath = _relativePath.replace('\\', '/');
	}

	public FilePathSpan(String relativePath) throws IOException
	{
		_relativePath = relativePath.replace('\\', '/');
	}

	public String getRelativePath() throws IOException
	{
		return _relativePath;
	}

	public File relativeTo(File newBaseDir) throws IOException
	{
		return new File(newBaseDir, getRelativePath());
	}
}
