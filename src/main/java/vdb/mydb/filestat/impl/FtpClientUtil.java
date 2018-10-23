package vdb.mydb.filestat.impl;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

//FIXME: class FtpClientUtil is NOT a Util class, a Util's methods are always static

public class FtpClientUtil
{
	public FTPClient ftpClient = new FTPClient();

	public boolean connect(String hostname, int port, String username,
			String password) throws IOException
	{
		ftpClient.connect(hostname, port);
		ftpClient.setControlEncoding("GBK");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
		{
			if (ftpClient.login(username, password))
			{
				return true;
			}
		}
		disconnect();
		return false;
	}

	public FTPFile[] listFiles(String pathname) throws IOException
	{
		return ftpClient.listFiles(pathname);
	}

	public void disconnect() throws IOException
	{
		if (ftpClient.isConnected())
		{
			ftpClient.disconnect();
		}
	}
}