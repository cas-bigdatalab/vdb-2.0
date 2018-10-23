package vdb.litesync.summary;

import java.util.Collection;
import java.util.List;

import cn.csdb.commons.util.ListMap;

public class Summary
{
	private ListMap<String, SummaryEntry> _entries = new ListMap<String, SummaryEntry>();

	public Summary()
	{
	}

	public Summary(Collection<SummaryEntry> entries)
	{
		setEntries(entries);
	}

	public Summary(Summary source)
	{
		for (SummaryEntry entry : source._entries.list())
		{
			_entries.add(entry.getPath(), entry);
		}
	}

	public void addEntry(String path, String digest, long length, EntryKind kind)
	{
		SummaryEntry entry = new SummaryEntry();
		entry.setDigest(digest);
		entry.setKind(kind);
		entry.setLength(length);
		entry.setPath(path);

		_entries.add(path, entry);
	}

	public void addEntry(SummaryEntry entry)
	{
		_entries.add(entry.getPath(), entry);
	}

	public boolean containsEntry(String path)
	{
		return _entries.map().containsKey(path);
	}

	public boolean containsEntry(SummaryEntry se)
	{
		return se.equals(_entries.map().get(se.getPath()));
	}

	public List<SummaryEntry> getEntries()
	{
		return _entries.list();
	}

	public SummaryEntry removeEntry(String path)
	{
		return _entries.remove(path);
	}

	@Override
	public String toString()
	{
		return _entries.toString();
	}

	public SummaryEntry getEntry(String entryPath)
	{
		return _entries.map().get(entryPath);
	}

	public void setEntries(Collection<SummaryEntry> entries)
	{
		_entries.clear();
		for (SummaryEntry entry : entries)
		{
			_entries.add(entry.getPath(), entry);
		}
	}
}
