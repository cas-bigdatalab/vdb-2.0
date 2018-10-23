package vdb.litesync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import vdb.litesync.action.ActionKind;
import vdb.litesync.action.CompoundSyncActionHandler;
import vdb.litesync.action.SummaryMerger;
import vdb.litesync.action.SyncAction;
import vdb.litesync.action.SyncActionHandler;
import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;
import vdb.litesync.summary.Summary;
import vdb.litesync.summary.SummaryEntry;
import vdb.litesync.summary.SummaryGenerator;
import vdb.litesync.summary.SummaryReader;
import vdb.litesync.summary.SummaryUtils;
import vdb.litesync.summary.SummaryWriter;
import cn.csdb.commons.util.FileUtils;
import cn.csdb.commons.util.Matcher;

public class LiteSynchronizer
{
	private SummaryGenerator _summaryGenerator;

	private SyncRepository _syncRepository;

	private SyncSource _syncSource;

	public LiteSynchronizer(SyncRepository syncRepository, SyncSource syncSource)
	{
		this(syncRepository, syncSource, new SummaryGenerator());
	}

	public LiteSynchronizer(SyncRepository syncRepository,
			SyncSource syncSource, SummaryGenerator summaryGenerator)
	{
		_syncRepository = syncRepository;
		_syncSource = syncSource;
		_summaryGenerator = summaryGenerator;
	}

	public void sync(SyncActionHandler handler) throws IOException
	{
		sync(SummaryUtils.createDefaultMatcher(), handler);
	}

	public void sync(Matcher<String> synchronizationEntryPathMatcher,
			SyncActionHandler handler) throws IOException
	{
		SummaryMerger summaryMerger = new SummaryMerger();
		CompoundSyncActionHandler compoundHandler = new CompoundSyncActionHandler(
				handler, summaryMerger);

		compoundHandler.init(_syncRepository, _syncSource);

		// get remote summary
		Summary remoteSummary = new SummaryReader(_syncRepository
				.getSummaryInputStream()).read();

		// get local summary
		Summary localSummary = _syncSource.getWorkingCopyDirectoy().exists() ? _summaryGenerator
				.generateSummary(_syncSource.getWorkingCopyDirectoy())
				: new Summary();

		Summary lastLocalSummary = _syncSource.getLocalSummaryFile().exists() ? new SummaryReader(
				_syncSource.getLocalSummaryFile()).read()
				: new Summary();

		Summary lastRemoteSummary = _syncSource.getRemoteSummaryFile().exists() ? new SummaryReader(
				_syncSource.getRemoteSummaryFile()).read()
				: lastLocalSummary;

		summaryMerger.setSummaries(localSummary, remoteSummary);

		// compares local & last local summaries
		Collection<SummaryEntry> localModifiedSummary = SummaryUtils
				.symmetricDifference(localSummary.getEntries(),
						lastLocalSummary.getEntries());

		// compares remote & last remote summaries
		Collection<SummaryEntry> remoteModifiedSummary = SummaryUtils
				.symmetricDifference(remoteSummary.getEntries(),
						lastRemoteSummary.getEntries());

		// compares local & remote summaries
		Collection<SummaryEntry> identicalSummary = SummaryUtils.intersection(
				localSummary.getEntries(), remoteSummary.getEntries());

		Collection<String> localNotModifiedAndRemoteModified = SummaryUtils
				.subtract(SummaryUtils
						.getEntryPathListOf(remoteModifiedSummary),
						SummaryUtils.getEntryPathListOf(localModifiedSummary),
						SummaryUtils.getEntryPathListOf(identicalSummary));

		// safeSyncEntries is not safe
		Collection<SummaryEntry> localUnmodifiedSummary = SummaryUtils
				.intersection(localSummary.getEntries(), lastLocalSummary
						.getEntries());

		Collection<SummaryEntry> synchronizedSummary = SummaryUtils
				.intersection(lastLocalSummary.getEntries(), lastRemoteSummary
						.getEntries());

		Collection<String> unmodifiedButNotSynchronizedSummary = SummaryUtils
				.subtract(SummaryUtils
						.getEntryPathListOf(localUnmodifiedSummary),
						SummaryUtils.getEntryPathListOf(synchronizedSummary));

		// so, to be true safe
		Collection<String> safeSyncEntries = SummaryUtils.subtract(
				localNotModifiedAndRemoteModified,
				unmodifiedButNotSynchronizedSummary);

		safeSyncEntries = SummaryUtils.filterEntryPathList(safeSyncEntries,
				synchronizationEntryPathMatcher);
		safeSyncEntries = SummaryUtils.sortEntryPathList(safeSyncEntries);

		List<SyncAction> actions = new ArrayList<SyncAction>();

		for (String entryPath : safeSyncEntries)
		{
			boolean existsLocal = localSummary.containsEntry(entryPath);
			boolean existsRemote = remoteSummary.containsEntry(entryPath);

			if (existsLocal)
			{
				if (existsRemote)
				{
					actions.add(new SyncAction(entryPath,
							ActionKind.UPDATE_FILE));
				}
				else
				{
					boolean isDirectory = entryPath.endsWith("/");
					actions.add(new SyncAction(entryPath,
							isDirectory ? ActionKind.DELETE_DIRECTORY
									: ActionKind.DELETE_FILE));
				}
			}
			else
			{
				if (existsRemote)
				{
					boolean isDirectory = entryPath.endsWith("/");
					actions.add(new SyncAction(entryPath,
							isDirectory ? ActionKind.ADD_DIRECTORY
									: ActionKind.ADD_FILE));
				}
			}
		}

		compoundHandler.begin(actions);

		for (SyncAction action : actions)
		{
			compoundHandler.handle(action);
		}

		compoundHandler.end(actions);

		// generate summary and store
		if (!actions.isEmpty())
		{
			new SummaryWriter(_syncSource.getLocalSummaryFile())
					.write(summaryMerger.getMergedSummary());

			if (false)
			{
				new SummaryWriter(_syncSource.getLocalSummaryFile())
						.write(_summaryGenerator.generateSummary(_syncSource
								.getWorkingCopyDirectoy()));
			}
		}

		new SummaryWriter(_syncSource.getRemoteSummaryFile())
				.write(remoteSummary);

		FileUtils.saveFile(_syncRepository
				.getVersionFileStream("version.properties"), _syncSource
				.getVersionFile("version.properties"));
	}
}
