package cn.csdb.grid.rpc.file;

import java.io.InputStream;

import cn.csdb.grid.rpc.RPCClient;

public class GetFileClient
{
	public FileInfo getFile(String url, String filePath) throws Exception
	{
		return new RPCClient<GetFileService>(url).getService().getFileInfo(
				filePath);
	}

	public InputStream openStream(String url, String filePath) throws Exception
	{
		return new RPCClient<GetFileService>(url).getService().openStream(
				filePath);
	}

}
