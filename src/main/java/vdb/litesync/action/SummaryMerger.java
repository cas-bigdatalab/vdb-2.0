package vdb.litesync.action;

import java.io.IOException;
import java.util.List;

import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;
import vdb.litesync.summary.Summary;
import vdb.litesync.summary.SummaryUtils;

public class SummaryMerger implements SyncActionHandler
{
	private Summary _mergedSummary;

	private Summary _localSummary;

	private Summary _remoteSummary;

	public void begin(List<SyncAction> actions)
	{
	}

	public void end(List<SyncAction> actions)
	{
		SummaryUtils.sortSummary(_mergedSummary);
	}

	public void handle(SyncAction action) throws IOException
	{
		String entryPath = action.getEntryPath();

		if (action.getKind() == ActionKind.ADD_FILE
				|| action.getKind() == ActionKind.ADD_DIRECTORY)
		{
			_mergedSummary.addEntry(_remoteSummary.getEntry(entryPath));
			return;
		}

		if (action.getKind() == ActionKind.DELETE_FILE
				|| action.getKind() == ActionKind.DELETE_DIRECTORY)
		{
			_mergedSummary.removeEntry(entryPath);
			return;
		}

		if (action.getKind() == ActionKind.UPDATE_FILE)
		{
			_mergedSummary.removeEntry(entryPath);
			_mergedSummary.addEntry(_remoteSummary.getEntry(entryPath));
			return;
		}
	}

	public void init(SyncRepository syncRepository, SyncSource syncSource)
	{
	}

	public void setSummaries(Summary localSummary, Summary remoteSummary)
	{
		_localSummary = localSummary;
		_remoteSummary = remoteSummary;

		_mergedSummary = new Summary(_localSummary);
	}

	public Summary getMergedSummary()
	{
		return _mergedSummary;
	}

}
