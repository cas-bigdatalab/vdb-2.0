package vdb.litesync.action;

import java.io.File;
import java.io.IOException;

public interface BackupStrategy
{
	void backup(File src, String entryPath) throws IOException;
}
