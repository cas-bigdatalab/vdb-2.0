package vdb.litesync.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.csdb.commons.util.FileUtils;
import cn.csdb.commons.util.TimeUtils;

public class BackupInAnotherDirectory implements BackupStrategy
{
	private File _backupDir;

	private List<File> _backupFiles = new ArrayList<File>();

	public BackupInAnotherDirectory()
	{
	}

	public BackupInAnotherDirectory(File backupDir)
	{
		_backupDir = getDateDir(backupDir);
	}

	public void backup(File src, String entryPath) throws IOException
	{
		File newFile = new File(_backupDir, entryPath);
		newFile.getParentFile().mkdirs();
		FileUtils.copy(src, newFile);
		_backupFiles.add(newFile);
	}

	public List<File> getBackupFiles()
	{
		return _backupFiles;
	}

	public void setBackupDir(File backupDir)
	{
		_backupDir = getDateDir(backupDir);
	}

	private File getDateDir(File backupDir)
	{
		return new File(new File(backupDir, TimeUtils
				.getNowString("yyyy-MM-dd")), "backup");
	}

}
