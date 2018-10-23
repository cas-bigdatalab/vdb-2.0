package vdb.mydb.rpc.simplex;

import java.io.Serializable;

public class SimpleFileImpl implements SimpleFile, Serializable
{

	private String fileContentType;

	private String fileExtension;

	private String fileName;

	private String fileTitle;

	private String filePath;

	private String fileLastModified;

	private String fileLength;

	private String fileSourceURL;

	private String repositoryName;

	private String dataSetUri;

	private boolean isDirectory;

	public boolean isDirectory()
	{
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}

	public String getFileContentType()
	{
		return fileContentType;
	}

	public void setFileContentType(String fileContentType)
	{
		this.fileContentType = fileContentType;
	}

	public String getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(String fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFileTitle()
	{
		return fileTitle;
	}

	public void setFileTitle(String fileTitle)
	{
		this.fileTitle = fileTitle;
	}

	public String getFilePath()
	{
		return filePath;
	}

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getFileLastModified()
	{
		return fileLastModified;
	}

	public void setFileLastModified(String fileLastModified)
	{
		this.fileLastModified = fileLastModified;
	}

	public String getFileLength()
	{
		return fileLength;
	}

	public void setFileLength(String fileLength)
	{
		this.fileLength = fileLength;
	}

	public String getFileSourceURL()
	{
		return fileSourceURL;
	}

	public void setFileSourceURL(String fileSourceURL)
	{
		this.fileSourceURL = fileSourceURL;
	}

	public String getRepositoryName()
	{
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName)
	{
		this.repositoryName = repositoryName;
	}

	public String getDataSetUri()
	{
		return dataSetUri;
	}

	public void setDataSetUri(String dataSetUri)
	{
		this.dataSetUri = dataSetUri;
	}
}
