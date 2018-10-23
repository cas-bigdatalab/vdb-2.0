package vdb.mydb.filestat.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.repo.FileRepository;
import vdb.mydb.repo.RepositoryFile;

public class VdbAttachmentRepository implements FileRepository
{

	private String name;

	private String path;

	private static final String REPOSITORY_NAME = "upload";

	private final static String TYPE = "server";

	private DataSet dataSet;

	public DataSet getDataSet()
	{
		return dataSet;
	}

	public DataSet getDsataSet()
	{
		return dataSet;
	}

	public void setDataSet(DataSet ds)
	{
		dataSet = ds;
	}

	public VdbAttachmentRepository(DataSet ds)
	{
		name = REPOSITORY_NAME;
		path = VdbManager.getEngine().getDataSetRoot(ds).getPath()
				+ File.separator + "files";
		setDataSet(ds);
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
		File f = new File(getPath() + File.separator + filePath);

		ServerFile sf = new ServerFile();
		sf.setRepository(this);
		sf.setFile(f);

		return sf;
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
		File f = new File(getPath());
		if (f.exists())
			return true;
		return false;
	}

	public RepositoryFile getRoot()
	{
		File f = new File(getPath());
		ServerFile sf = new ServerFile();
		sf.setRepository(this);
		sf.setFile(f);
		return sf;
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
		try
		{
			FileInputStream fs = new FileInputStream(new File(getPath()
					+ File.separator + filePath));
			return fs;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();

		}
		return null;
	}

	public String getType()
	{
		return TYPE;
	}
}