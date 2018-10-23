package vdb.litesync.source;

import java.io.File;
import java.io.IOException;

public interface SyncSource
{
	File getLocalSummaryFile() throws IOException;

	File getRemoteSummaryFile() throws IOException;

	File getEntryFile(String entryPath) throws IOException;

	File getWorkingCopyDirectoy();

	File getVersionFile(String path);
}
