package vdb.mydb.repo;

import java.io.InputStream;

import vdb.metacat.DataSet;

//FIXME: FileRepository?
public interface FileRepository {
	/**
	 * 获取存储名称
	 */
	public String getName();
	
	/**
	 * 获取数据集
	 */
	public DataSet getDataSet();

	/**
	 * 设置数据集
	 */
	public void setDataSet(DataSet ds);

	/**
	 * 通过相对路径获取单个IFile对象
	 */
	public RepositoryFile getFile(String filePath);

	/**
	 * 获取存储根路径文件信息
	 */
	public RepositoryFile getRoot();

	/**
	 * 是否可访问
	 */
	public boolean isAccessible();

	/**
	 * 获取存储类型
	 */
	public String getType();

	/**
	 * 通过相对路径获取文件流
	 */
	public InputStream openStream(String filePath);
}
