package vdb.mydb.repo;

import java.util.Date;
import java.util.List;

//FIXME: IFile
public interface RepositoryFile
{

	/**
	 * 存储位置
	 */
	FileRepository getRepository();

	/**
	 * 文件名
	 */
	String getFileName();
	
	/**
	 * 文件标题
	 */
	String getFileTitle();

	/**
	 * 文件后缀名（目录时返回null）
	 */
	String getFileExtension();

	/**
	 * 文件格式（目录时返回null）
	 */
	String getContentType();

	/**
	 * 文件修改时间
	 */
	Date getLastModified();

	/**
	 * 文件大小（目录时返回0）
	 */
	long length();

	/**
	 * 文件相对路径
	 */
	String getFilePath();

	/**
	 * 是否是目录
	 */
	boolean isDirectory();

	/**
	 * 是否是文件
	 */
	boolean isFile();

	/**
	 * 路径下所有文件
	 */
	List<RepositoryFile> listFiles();

}
