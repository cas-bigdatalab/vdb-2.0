package vdb.mydb.filestat.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPFile;

import vdb.metacat.DataSet;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;

public class FtpRepository implements FileRepository
{

	private String name;

	private String path;

	private String hostname;

	private String username;

	private String password;

	private int port = 21;

	private final static String TYPE = "ftp";

	private DataSet dataSet;

	public DataSet getDataSet()
	{
		return dataSet;
	}

	public void setDataSet(DataSet dataSet)
	{
		this.dataSet = dataSet;
	}

	public String getHostname()
	{
		return hostname;
	}

	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * Description: 通过相对路径获取IFile对象
	 * 
	 * @param String
	 *            filePath：文件或文件夹的相对路径
	 * 
	 * @return IFile
	 */
	public RepositoryFile getFile(String filePath)
	{
		FtpClientUtil fcu = new FtpClientUtil();
		FTPFile ff = null;
		try
		{
			if (fcu.connect(hostname, port, username, password))
			{
				if (fcu.listFiles(path + File.separator + filePath).length == 1)
					ff = fcu.listFiles(path + File.separator + filePath)[0];
			}
			fcu.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (ff != null && ff.isFile())
		{
			FtpFile ftpFile = new FtpFile();
			ftpFile.setRepository(this);
			ftpFile.setFilePath(path + File.separator + filePath);
			ftpFile.setFile(ff);
			return ftpFile;
		}
		return null;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Description: 返回存储位置的路径（绝对路径）
	 * 
	 * @return String
	 */
	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public boolean isAccessible()
	{
		FtpClientUtil fcu = new FtpClientUtil();
		try
		{
			if (fcu.connect(hostname, port, username, password))
			{
				fcu.disconnect();
				return true;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public RepositoryFile getRoot()
	{
		FtpFile ff = new FtpFile();
		ff.setRepository(this);
		ff.setFilePath(this.getPath());
		return ff;
	}

	/**
	 * Description: 通过相对路径获取文件的输入流
	 * 
	 * @param String
	 *            filePath：文件或文件夹的相对路径
	 * 
	 * @return InputStream
	 */
	public InputStream openStream(String filePath)
	{
		FtpClientUtil fcu = new FtpClientUtil();
		try
		{
			if (fcu.connect(hostname, port, username, password))
			{
				InputStream is = fcu.ftpClient.retrieveFileStream(path
						+ File.separator + filePath);
				return is;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fcu.disconnect();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getType()
	{
		return TYPE;
	}
}
