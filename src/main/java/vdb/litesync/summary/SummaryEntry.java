package vdb.litesync.summary;

public class SummaryEntry
{
	String _digest;

	EntryKind _kind;

	long _length;

	String _path;

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 == null)
			return false;

		if (!(arg0 instanceof SummaryEntry))
			return false;

		SummaryEntry target = (SummaryEntry) arg0;

		return target.getPath().equals(getPath())
				&& target.getKind() == getKind()
				&& target.getLength() == getLength()
				&& target.getDigest().equals(getDigest());
	}

	public String getDigest()
	{
		return _digest;
	}

	public EntryKind getKind()
	{
		return _kind;
	}

	public long getLength()
	{
		return _length;
	}

	public String getPath()
	{
		return _path;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	public void setDigest(String digest)
	{
		_digest = digest;
	}

	public void setKind(EntryKind kind)
	{
		_kind = kind;
	}

	public void setLength(long length)
	{
		_length = length;
	}

	public void setPath(String path)
	{
		_path = path;
	}

	public String toString()
	{
		return _path;
	}
}