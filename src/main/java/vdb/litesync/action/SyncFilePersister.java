package vdb.litesync.action;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;
import cn.csdb.commons.util.FileUtils;

public class SyncFilePersister implements SyncActionHandler
{
	private SyncRepository _syncRepository;

	private BackupStrategy _backupStrategy;

	private SyncSource _syncSource;

	public void begin(List<SyncAction> actions)
	{
	}

	public void end(List<SyncAction> actions)
	{
	}

	public void handle(SyncAction action) throws IOException
	{
		String entryPath = action.getEntryPath();
		File file = _syncSource.getEntryFile(entryPath);

		if (action.getKind() == ActionKind.ADD_FILE)
		{
			if (file.exists())
				return;
			file.createNewFile();
			updateFileContent(file, entryPath);
			return;
		}

		if (action.getKind() == ActionKind.ADD_DIRECTORY)
		{
			if (file.exists())
				return;

			file.mkdirs();
			return;
		}

		if (action.getKind() == ActionKind.DELETE_FILE)
		{
			if (!file.exists())
				return;

			backup(file, entryPath);
			file.delete();
			return;
		}

		if (action.getKind() == ActionKind.DELETE_DIRECTORY)
		{
			if (!file.exists())
				return;

			backup(file, entryPath);
			FileUtils.rmdirs(file);
			return;
		}

		if (action.getKind() == ActionKind.UPDATE_FILE)
		{
			if (!file.exists())
				return;

			backup(file, entryPath);
			updateFileContent(file, entryPath);
			return;
		}
	}

	private void backup(File file, String entryPath) throws IOException
	{
		if (_backupStrategy != null)
			_backupStrategy.backup(file, entryPath);
	}

	private void updateFileContent(File file, String entryPath)
			throws IOException
	{
		InputStream is = _syncRepository.getEntryInputStream(entryPath);
		FileUtils.saveFile(is, file);
		is.close();
	}

	public void init(SyncRepository syncRepository, SyncSource syncSource)
	{
		_syncRepository = syncRepository;
		_syncSource = syncSource;
	}

	public BackupStrategy getBackupStrategy()
	{
		return _backupStrategy;
	}

	public void setBackupStrategy(BackupStrategy backupStrategy)
	{
		_backupStrategy = backupStrategy;
	}

}
