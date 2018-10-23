package vdb.mydb.filestat.impl;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import vdb.metacat.DataSet;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;

public class ApacheRepository implements FileRepository
{

	private String name;

	private String path;

	private final static String TYPE = "apache";

	private DataSet dataSet;

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
		ApacheFile af = new ApacheFile();
		
		af.setRepository(this);
		af.setDirectory(false);
		af.setFile(true);
		af.setFilePath(getPath() + File.separator + filePath);
		af.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));

		HttpClient httpClientFile = new HttpClient();
		GetMethod getMethodFile = new GetMethod(getPath() + File.separator
				+ filePath);

		try
		{
			httpClientFile.executeMethod(getMethodFile);
			af.setLength(getMethodFile.getResponseBody().length);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		getMethodFile.releaseConnection();
		return af;
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
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(getPath());
		try
		{
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK)
			{
				return false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public RepositoryFile getRoot()
	{
		ApacheFile af = new ApacheFile();
		af.setRepository(this);
		af.setDirectory(true);
		af.setFile(false);
		af.setFilePath(getPath());

		return af;
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
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(getPath() + File.separator
				+ filePath);
		try
		{
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK)
			{
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
				return null;
			}
			return getMethod.getResponseBodyAsStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public String getType()
	{
		return TYPE;
	}

	public DataSet getDataSet()
	{
		return dataSet;
	}

	public void setDataSet(DataSet dataSet)
	{
		this.dataSet = dataSet;
	}

}
