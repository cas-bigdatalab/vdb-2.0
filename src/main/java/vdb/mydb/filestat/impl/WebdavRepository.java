package vdb.mydb.filestat.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;
import vdb.sardine.DavResource;
import vdb.sardine.Sardine;
import vdb.sardine.SardineFactory;
import vdb.sardine.util.SardineException;

public class WebdavRepository implements FileRepository
{

	private String name;

	private String path;

	private String username;

	private String password;

	private final static String TYPE = "webdav";

	private DataSet dataSet;

	public DataSet getDataSet()
	{
		return dataSet;
	}

	public void setDataSet(DataSet dataSet)
	{
		this.dataSet = dataSet;
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
		WebdavFile wf = new WebdavFile();
		wf.setRepository(this);
		wf.setDirectory(false);
		wf.setFile(true);
		wf.setFilePath(getPath() + File.separator + filePath);
		wf.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

		Sardine sardine;
		try
		{
			sardine = SardineFactory.begin(username, password);
			List<DavResource> resources = sardine.getResources(getPath()
					+ File.separator + filePath);
			if (resources != null && resources.size() > 0)
			{
				wf.setLength(resources.get(0).getContentLength());
				wf.setLastModified(resources.get(0).getModified());
			}
		}
		catch (SardineException e)
		{
			e.printStackTrace();
		}

		return wf;
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
		if (getPath() == null)
			return false;

		Sardine sardine;

		try
		{
			sardine = SardineFactory.begin(username, password);
			List<DavResource> resources = sardine.getResources(getPath());
			if (resources != null && resources.size() > 0)
				return true;
			else
				return false;

		}
		catch (SardineException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public RepositoryFile getRoot()
	{
		WebdavFile wf = new WebdavFile();
		wf.setRepository(this);
		wf.setDirectory(true);
		wf.setFile(false);
		wf.setFilePath(getPath());

		return wf;
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
		Sardine sardine;
		try
		{
			sardine = SardineFactory.begin(username, password);
			return sardine
					.getInputStream(getPath() + File.separator + filePath);
		}
		catch (SardineException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public String getType()
	{
		return TYPE;
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
}
