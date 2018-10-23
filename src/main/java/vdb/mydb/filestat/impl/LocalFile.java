package vdb.mydb.filestat.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;

public class LocalFile implements RepositoryFile
{

	File file;

	FileRepository repository;

	public void setFile(File file)
	{
		this.file = file;
	}

	public String getContentType()
	{
		if (file != null)
		{
			if (file.isFile())
			{
				String postfix = "";
				postfix = getFileExtension();
				String contentType = "";
				contentType = FileUtil.getContentType(postfix);
				return contentType;
			}
		}
		return null;
	}

	public Date getLastModified()
	{
		if (file != null)
			return new Date(file.lastModified());
		return null;
	}

	public String getFileName()
	{
		if (file != null)
			return file.getName();
		return null;
	}

	// 返回文件或文件夹的相对路径
	public String getFilePath()
	{
		if (file != null)
		{
			String repositoryPath = ((LocalRepository) this.getRepository())
					.getPath();
			String filePath = file.getPath();
			return filePath.substring(filePath.indexOf(repositoryPath)
					+ repositoryPath.length());
		}
		return null;
	}

	public long length()
	{
		if (file != null && isFile())
			return file.length();
		return 0;
	}

	public String getFileExtension()
	{
		if (file != null && isFile() && file.getName().lastIndexOf(".") != -1)
			return file.getName().substring(file.getName().lastIndexOf("."));

		return null;
	}

	public boolean isDirectory()
	{
		if (file != null)
			return file.isDirectory();
		return false;
	}

	public boolean isFile()
	{
		if (file != null)
			return file.isFile();
		return false;
	}

	public List<RepositoryFile> listFiles()
	{
		List<RepositoryFile> files = new ArrayList<RepositoryFile>();
		File[] fs = file.listFiles();
		for (File file : fs)
		{
			LocalFile lf = new LocalFile();
			lf.setRepository(this.getRepository());
			lf.setFile(file);
			files.add(lf);
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

}
