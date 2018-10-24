package cn.csdb.grid.rpc.file;

import java.io.IOException;
import java.io.InputStream;

public interface GetFileService
{
	public FileInfo getFileInfo(String filePath) throws IOException;

	public InputStream openStream(String filePath) throws IOException;
}
