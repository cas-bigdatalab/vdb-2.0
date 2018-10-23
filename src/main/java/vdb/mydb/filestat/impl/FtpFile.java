package vdb.mydb.filestat.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;

import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;

public class FtpFile implements RepositoryFile
{

	FTPFile file;

	FileRepository repository;

	String filePath;

	public void setFile(FTPFile file)
	{
		this.file = file;
	}

	public FileRepository getRepository()
	{
		return repository;
	}

	public void setRepository(FileRepository repository)
	{
		this.repository = repository;
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
			return new Date(file.getTimestamp().getTimeInMillis());
		return null;
	}

	public String getFileName()
	{
		if (file != null)
			return file.getName();
		return null;
	}

	public String getFileTitle()
	{
		return getFileName();
	}

	// 返回文件或文件夹的相对路径
	public String getFilePath()
	{
		String repositoryPath = ((FtpRepository) this.getRepository())
				.getPath();
		return filePath.substring(filePath.indexOf(repositoryPath)
				+ repositoryPath.length());
	}

	public long length()
	{
		if (file != null)
			return file.getSize();
		return 0;
	}

	public String getFileSourceType()
	{
		return "LocalFile";
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
		FtpClientUtil fcu = new FtpClientUtil();
		FtpRepository fr = (FtpRepository) this.getRepository();
		try
		{
			if (fcu.connect(fr.getHostname(), fr.getPort(), fr.getUsername(),
					fr.getPassword()))
			{
				for (FTPFile ff : fcu.listFiles(filePath))
				{
					if (ff.getName().equalsIgnoreCase(".")
							|| ff.getName().equalsIgnoreCase(".."))
						continue;
					FtpFile ftpFile = new FtpFile();
					ftpFile.setRepository(this.getRepository());
					ftpFile.setFilePath(filePath + "/" + ff.getName());
					ftpFile.setFile(ff);
					files.add(ftpFile);
				}
				fcu.disconnect();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return files;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
}
