package vdb.mydb.version;

import java.util.Date;

public class Version
{
	private Date _releaseDate;

	private long _revision;

	public Date getReleaseDate()
	{
		return _releaseDate;
	}

	public void setReleaseDate(Date releaseDate)
	{
		_releaseDate = releaseDate;
	}

	public long getRevision()
	{
		return _revision;
	}

	public void setRevision(long revision)
	{
		_revision = revision;
	}
}
