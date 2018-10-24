package cn.csdb.grid.rpc.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.csdb.grid.rpc.RPCServlet;

public class GetFileServlet extends RPCServlet implements GetFileService
{
	public FileInfo getFileInfo(String filePath) throws IOException
	{
		File file = new File(this.getServletContext().getRealPath(filePath));
		FileInfo fileInfo = new FileInfo();
		fileInfo.setLength(file.length());
		fileInfo.setName(file.getName());

		java.util.Date myDate = new java.util.Date();
		myDate.setTime(file.lastModified());
		fileInfo.setLastModified(myDate);

		return fileInfo;
	}

	public InputStream openStream(String filePath) throws IOException
	{
		File file = new File(this.getServletContext().getRealPath(filePath));
		return new FileInputStream(file);
	}

}
