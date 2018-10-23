package vdb.mydb.files;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import vdb.metacat.DataSet;

public interface FileMetaData
{
	String getContentType();

	DataSet getDataSet();

	String getExtension();

	long getFileSize();

	InputStream getFileStream() throws IOException;

	InputStream getLocalFileStream() throws IOException;

	String getId();

	long getImageHeight();

	long getImageWidth();

	long getImageWidth(long width);

	Date getPostTime();

	String getTitle();

	boolean isImage();

	void setContentType(String contentType);

	void setDataSet(DataSet ds);

	void setExtension(String extension);

	void setFileSize(long fileSize);

	void setFileStream(InputStream is);

	void setId(String id);

	void setImageHeight(long imageHeight);

	void setImageWidth(long imageWidth);

	void setPostTime(Date postTime);

	void setTitle(String title);

	String getServletPath();
}