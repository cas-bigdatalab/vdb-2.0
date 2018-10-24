package cn.csdb.grid.rpc.file;

import java.io.Serializable;
import java.util.Date;

public class FileInfo implements Serializable
{
	String name;

	long length;

	Date lastModified;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getLength()
	{
		return length;
	}

	public void setLength(long l)
	{
		this.length = l;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	public void setLastModified(Date lastModified)
	{
		this.lastModified = lastModified;
	}

}
