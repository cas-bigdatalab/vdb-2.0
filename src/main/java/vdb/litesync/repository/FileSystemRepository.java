package vdb.litesync.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSystemRepository implements SyncRepository
{
	private File _rootDir;

	public InputStream getEntryInputStream(String path) throws IOException
	{
		return new FileInputStream(new File(_rootDir, path));
	}

	public InputStream getSummaryInputStream() throws IOException
	{
		return getVersionFileStream("local.summary");
	}

	public InputStream getVersionFileStream(String path) throws IOException
	{
		File file = new File(_rootDir, String.format(".version/%s", path));
		if (!file.exists())
		{
			file.createNewFile();
		}

		return new FileInputStream(file);
	}

	public boolean isAvaliable()
	{
		return _rootDir.exists();
	}

	public File getRootDir()
	{
		return _rootDir;
	}

	public void setRootDir(File rootDir)
	{
		_rootDir = rootDir;
	}
}
