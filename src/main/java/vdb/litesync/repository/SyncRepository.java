package vdb.litesync.repository;

import java.io.IOException;
import java.io.InputStream;

public interface SyncRepository
{
	public InputStream getVersionFileStream(String path) throws IOException;

	InputStream getEntryInputStream(String path) throws IOException;

	InputStream getSummaryInputStream() throws IOException;

	public boolean isAvaliable();
}