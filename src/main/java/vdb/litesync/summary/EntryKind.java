package vdb.litesync.summary;

public enum EntryKind
{
	FILE("file"), DIRECTORY("directory");

	private EntryKind(String name)
	{
		_name = name;
	}

	@Override
	public String toString()
	{
		return _name;
	}

	private String _name;

	public static EntryKind forName(String name)
	{
		return "file".equalsIgnoreCase(name) ? FILE : DIRECTORY;
	}
}
