package vdb.mydb.filestat.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;
import vdb.sardine.DavResource;
import vdb.sardine.Sardine;
import vdb.sardine.SardineFactory;
import vdb.sardine.util.SardineException;

public class WebdavFile implements RepositoryFile
{

	FileRepository repository;
	boolean isDirectory;
	boolean isFile;
	String filePath;
	String fileName;
	long length;
	Date lastModified;

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getContentType()
	{
		if (this.isFile())
		{
			String postfix = "";
			postfix = getFileExtension();
			String contentType = "";
			contentType = FileUtil.getContentType(postfix);
			return contentType;
		}

		return null;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	public String getFileName()
	{
		if (this.isFile())
		{
			return fileName;
		}
		return null;
	}

	// 返回文件或文件夹的相对路径
	public String getFilePath()
	{
		String repositoryPath = ((WebdavRepository) this.getRepository())
				.getPath();
		return filePath.substring(filePath.indexOf(repositoryPath)
				+ repositoryPath.length());
	}

	public long length()
	{
		if (this.isFile())
		{
			return length;
		}
		return 0;
	}

	public String getFileExtension()
	{
		if (isFile() && this.getFileName().lastIndexOf(".") != -1)
			return this.getFileName().substring(
					this.getFileName().lastIndexOf("."));

		return null;
	}

	public boolean isDirectory()
	{
		return isDirectory;
	}

	public boolean isFile()
	{
		return isFile;
	}

	public List<RepositoryFile> listFiles()
	{
		String path = filePath;
		List<RepositoryFile> files = new ArrayList<RepositoryFile>();

		Sardine sardine;
		try
		{
			sardine = SardineFactory.begin(((WebdavRepository) this
					.getRepository()).getUsername(), ((WebdavRepository) this
					.getRepository()).getPassword());
			List<DavResource> resources = sardine.getResources(path);
			for (DavResource res : resources)
			{
				if (!res.isCurrentDirectory())
				{
					WebdavFile wf = new WebdavFile();
					wf.setRepository(this.getRepository());
					wf.setDirectory(res.isDirectory());
					wf.setFile(!res.isDirectory());
					wf.setFilePath(res.getAbsoluteUrl());
					wf.setLastModified(res.getModified());
					if (!res.isDirectory())
					{
						wf.setFileName(path
								.substring(path.lastIndexOf("/") + 1));
						wf.setLength(res.getContentLength());
					}
					files.add(wf);
				}
			}
		}
		catch (SardineException e)
		{
			e.printStackTrace();
		}
		return files;
	}

	public FileRepository getRepository()
	{
		return repository;
	}

	public void setRepository(FileRepository repository)
	{
		this.repository = repository;
	}

	public String getFileTitle()
	{
		return getFileName();
	}

	public void setDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}

	public void setFile(boolean isFile)
	{
		this.isFile = isFile;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public void setLength(long length)
	{
		this.length = length;
	}

	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}

}
