package vdb.mydb.files.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.metacat.VdbDataSet;
import vdb.tool.ui.UrlsTool;

public class FileMetaDataImpl implements FileMetaData, Serializable
{
	private String _contentType;

	private String _dsUri;

	private String _extension;

	private String _filePath;

	private long _fileSize;

	private String _id;

	private long _imageHeight;

	private long _imageWidth;

	private InputStream _is;

	private JdbcFileManager _manager;

	private Date _postTime;

	private String _title;

	public FileMetaDataImpl(JdbcFileManager manager)
	{
		_manager = manager;
	}

	@Override
	public boolean equals(Object arg0)
	{
		return arg0 != null && this._id != null
				&& this._id.equals(((FileMetaDataImpl) arg0)._id);
	}

	public String getContentType()
	{
		return _contentType;
	}

	public DataSet getDataSet()
	{
		return VdbManager.getInstance().getCatalog().fromUri(_dsUri);
	}

	public String getDataSetUri()
	{
		return _dsUri;
	}

	public String getExtension()
	{
		return _extension;
	}

	public String getFilePath()
	{
		return _filePath;
	}

	public long getFileSize()
	{
		return _fileSize;
	}

	public InputStream getFileStream() throws IOException
	{
		if (_is == null)
		{
			_is = new FileInputStream(_manager.getIoFile(getDataSet(), this
					.getFilePath()));
		}

		return _is;
	}

	public InputStream getLocalFileStream() throws IOException
	{

		if (_is == null)
		{
			VdbDataSet ds = (VdbDataSet) getDataSet();
			String path = ds.getRepository().getLocalFilePath();// (String)request.getAttribute("path");
			String path_real = ds.getDataSetXml().getParentFile()
					.getCanonicalPath();// (String)request.getAttribute("path_real");
			path = path.replace("$dsRoot", path_real);
			File file = new File(new File(path), this.getFilePath());
			_is = new FileInputStream(file);
		}

		return _is;
	}

	public String getId()
	{
		return _id;
	}

	public long getImageHeight()
	{
		return _imageHeight;
	}

	public long getImageWidth()
	{
		return _imageWidth;
	}

	public long getImageWidth(long width)
	{
		return _imageWidth > width ? width : _imageWidth;
	}

	public Date getPostTime()
	{
		return _postTime;
	}

	public String getTitle()
	{
		return _title;
	}

	public boolean isImage()
	{
		return _contentType.startsWith("image");
	}

	public void setContentType(String contentType)
	{
		_contentType = contentType;
	}

	public void setDataSet(DataSet ds)
	{
		_dsUri = ds.getUri();
	}

	public void setDataSetUri(String dsUri)
	{
		_dsUri = dsUri;
	}

	public void setExtension(String extension)
	{
		_extension = extension;
	}

	public void setFilePath(String relativeFilePath)
	{
		_filePath = relativeFilePath;
	}

	public void setFileSize(long fileSize)
	{
		_fileSize = fileSize;
	}

	public void setFileStream(InputStream is)
	{
		_is = is;
	}

	public void setId(String id)
	{
		_id = id;
	}

	public void setImageHeight(long imageHeight)
	{
		_imageHeight = imageHeight;
	}

	public void setImageWidth(long imageWidth)
	{
		_imageWidth = imageWidth;
	}

	public void setPostTime(Date postTime)
	{
		_postTime = postTime;
	}

	public void setTitle(String title)
	{
		_title = title;
	}

	public String getServletPath()
	{
		return new UrlsTool().fromFile(this);
	}
}
