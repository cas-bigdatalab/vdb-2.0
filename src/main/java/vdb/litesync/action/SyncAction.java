package vdb.litesync.action;

public class SyncAction
{
	private String _entryPath;

	private ActionKind _kind;

	public SyncAction(String entryPath, ActionKind kind)
	{
		_entryPath = entryPath;
		_kind = kind;
	}

	public void setEntryPath(String entryPath)
	{
		_entryPath = entryPath;
	}

	public void setKind(ActionKind kind)
	{
		_kind = kind;
	}

	public String getEntryPath()
	{
		return _entryPath;
	}

	public ActionKind getKind()
	{
		return _kind;
	}
}
