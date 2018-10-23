package vdb.mydb.rpc.simplex;

public interface SimpleFile
{
	public String getFileContentType();

	public void setFileContentType(String fileContentType);

	public String getFileExtension();

	public void setFileExtension(String fileExtension);

	public String getFileName();

	public void setFileName(String fileName);

	public String getFileTitle();

	public void setFileTitle(String fileTitle);

	public String getFilePath();

	public void setFilePath(String filePath);

	public String getFileLastModified();

	public void setFileLastModified(String fileLastModified);

	public String getFileLength();

	public void setFileLength(String fileLength);

	public String getFileSourceURL();

	public void setFileSourceURL(String fileSourceURL);

	public String getRepositoryName();

	public void setRepositoryName(String repositoryName);

	public String getDataSetUri();

	public void setDataSetUri(String dataSetUri);

	public boolean isDirectory();

	public void setDirectory(boolean isDirectory);
}
