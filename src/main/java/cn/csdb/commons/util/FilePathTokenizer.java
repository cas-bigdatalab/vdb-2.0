/*
 * Created on 2005-2-13
 */
package cn.csdb.commons.util;

/**
 * @author bluejoe
 */
public class FilePathTokenizer
{
	private String _filePath;

	private String _fileName = "";

	private String _parentPath = "";

	private String _fileTitle = "";

	private String _fileExtension = "";

	public FilePathTokenizer(String filePath)
	{
		if (filePath != null)
		{
			// 剔除最后的分隔符
			if (filePath.endsWith("/") || filePath.endsWith("\\"))
				filePath = filePath.substring(0, filePath.length() - 1);

			_filePath = filePath;
			parse();
		}
	}

	/**
	 * 
	 */
	private void parse()
	{
		// 查找'/'或者'\'
		int li1 = _filePath.lastIndexOf('\\') + 1;
		int li2 = _filePath.lastIndexOf('/') + 1;

		int li3 = (li1 < li2 ? li2 : li1);

		// 找不到
		if (li3 > 0)
		{
			// 获取文件名
			_parentPath = _filePath.substring(0, li3 - 1);
			_fileName = _filePath.substring(li3);
		}
		else
		{
			_parentPath = "";
			_fileName = _filePath;
		}

		// 拆分文件名，取最后的.
		int li14 = _fileName.lastIndexOf('.');
		// 找不到.
		if (li14 < 0)
		{
			_fileTitle = _fileName;
		}
		else
		{
			_fileTitle = _fileName.substring(0, li14);
			_fileExtension = _fileName.substring(li14);
		}
	}

	/**
	 * @return
	 */
	public String getFileExtension()
	{
		return _fileExtension;
	}

	/**
	 * @return
	 */
	public String getFileName()
	{
		return _fileName;
	}

	/**
	 * @return
	 */
	public String getFileTitle()
	{
		return _fileTitle;
	}

	public String getParentPath()
	{
		return _parentPath;
	}

	/**
	 * @return
	 */
	public String getFilePath()
	{
		return _filePath;
	}
}
