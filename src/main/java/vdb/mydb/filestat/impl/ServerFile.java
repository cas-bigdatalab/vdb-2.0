package vdb.mydb.filestat.impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class ServerFile implements RepositoryFile
{

	File file;

	FileRepository repository;

	List<String> fileInfo;

	public void setFile(File file)
	{
		this.file = file;
		DataSet ds = ((ServerRepository) getRepository()).getDsataSet();
		if (file.isFile())
		{
			try
			{
				fileInfo = new ArrayList<String>();

				JdbcSource extJdbcsource = JdbcSourceManager.getInstance()
						.getFilesJdbcSource(ds);
				String sql = "select TITLE,EXTENSION from VDB_FILES where ID='"
						+ file.getName() + "'";
				extJdbcsource.executeQuery(new StringSql(sql),
						new ResultSetHandler()
						{
							public void afterQuery(ResultSet rs)
									throws SQLException
							{
								if (rs != null)
								{
									while (rs.next())
									{
										fileInfo.add(rs.getString(1));
										fileInfo.add(rs.getString(2));
									}
								}
							}
						});
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

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
			String repositoryPath = ((ServerRepository) this.getRepository())
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
		if (file != null && isFile() && fileInfo != null && fileInfo.size() > 1)
			return fileInfo.get(1);
		if (file != null && isFile())
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
		if (file != null)
		{
			File[] fs = file.listFiles();
			if (fs != null)
			{
				for (File file : fs)
				{
					ServerFile sf = new ServerFile();
					sf.setRepository(this.getRepository());
					sf.setFile(file);
					files.add(sf);
					if (file.isFile() && sf.fileInfo != null
							&& sf.fileInfo.size() == 0)
						files.remove(sf);
				}
			}
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
		if (file != null && isFile() && fileInfo != null && fileInfo.size() > 1)
			return fileInfo.get(0);
		return getFileName();
	}

}
