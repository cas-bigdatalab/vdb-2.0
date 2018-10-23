package vdb.litesync.source;

import java.io.File;
import java.io.IOException;

public class FileSystemSource implements SyncSource
{
	private File _workingCopyDir;

	private File _localSummaryFile;

	private File _remoteSummaryFile;

	public FileSystemSource()
	{
	}

	public FileSystemSource(File rootDir)
	{
		setDirs(rootDir, new File(rootDir, ".version/local.summary"), new File(
				rootDir, ".version/remote.summary"));
	}

	private void setDirs(File workingCopyDir, File localSummaryFile,
			File remoteSummaryFile)
	{
		_workingCopyDir = workingCopyDir;
		_localSummaryFile = localSummaryFile;
		_remoteSummaryFile = remoteSummaryFile;
	}

	public void setRootDir(File rootDir)
	{
		setDirs(rootDir, new File(rootDir, ".version/local.summary"), new File(
				rootDir, ".version/remote.summary"));
	}

	public File getEntryFile(String entryPath) throws IOException
	{
		return new File(_workingCopyDir, entryPath);
	}

	public File getLocalSummaryFile() throws IOException
	{
		return _localSummaryFile;
	}

	public File getRemoteSummaryFile() throws IOException
	{
		return _remoteSummaryFile;
	}

	public File getWorkingCopyDirectoy()
	{
		return _workingCopyDir;
	}

	public File getVersionFile(String path)
	{
		return new File(new File(_workingCopyDir, ".version"), path);
	}
}
