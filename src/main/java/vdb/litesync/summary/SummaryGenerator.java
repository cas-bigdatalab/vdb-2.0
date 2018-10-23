package vdb.litesync.summary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import vdb.tool.generic.FormatTool;
import vdb.webpub.publisher.FileSnapshot;
import cn.csdb.commons.util.Matcher;

public class SummaryGenerator
{
	public interface ThreadPoolSize
	{
		public int getPoolSize(List<File> entries);
	}

	private static final String DOT_VERSION = "/.version/";

	private SummaryGeneratorListener _generatorListener = new NullSummaryGeneratorListener();

	private Matcher<String> _generatorMatcher = SummaryUtils
			.createDefaultMatcher();

	private ThreadPoolSize _threadPoolSize = new ThreadPoolSize()
	{
		public int getPoolSize(List<File> entries)
		{
			return 1;
		}
	};

	public Summary generateSummary(File rootDir) throws IOException
	{
		_generatorListener.init(this);

		long start = System.currentTimeMillis();
		List<File> files = visitFiles(rootDir, rootDir);
		_generatorListener.begin(rootDir, files);

		Summary summary = generateSummary(rootDir, files);
		long end = System.currentTimeMillis();

		_generatorListener.end();

		Logger.getLogger(this.getClass()).debug(
				String.format("time cost: %s ms", end - start));

		return summary;
	}

	private Summary generateSummary(final File rootDir, List<File> files)
			throws IOException
	{
		class SummaryEntryGeneraionJob implements Callable<SummaryEntry>
		{
			private File _file;

			public SummaryEntryGeneraionJob(File file)
			{
				_file = file;
			}

			public SummaryEntry call() throws Exception
			{
				return generateSummaryEntry(rootDir, _file);
			}
		}

		Collection<Callable<SummaryEntry>> jobs = new ArrayList<Callable<SummaryEntry>>();
		for (File file : files)
		{
			jobs.add(new SummaryEntryGeneraionJob(file));
		}

		try
		{
			List<SummaryEntry> entries = new ArrayList<SummaryEntry>();
			List<Future<SummaryEntry>> futures = Executors.newFixedThreadPool(
					_threadPoolSize.getPoolSize(files)).invokeAll(jobs);

			for (Future<SummaryEntry> future : futures)
			{
				try
				{
					entries.add(future.get());
				}
				catch (ExecutionException e)
				{
					e.printStackTrace();
				}
			}

			SummaryUtils.sortEntryList(entries);
			Summary summary = new Summary(entries);
			return summary;
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private SummaryEntry generateSummaryEntry(File rootDir, File file)
			throws IOException
	{
		SummaryEntry entry = new SummaryEntry();
		String entryPath = SummaryUtils.normalizePath(rootDir, file);
		entry.setPath(entryPath);

		_generatorListener.generateSummaryEntry(file);

		if (file.isDirectory())
		{
			Logger.getLogger(this.getClass())
					.debug(
							String.format("generating summary entry for %s",
									entryPath));

			entry.setDigest("@@@DIRECTORY@@@");
			entry.setKind(EntryKind.DIRECTORY);
			entry.setLength(-1);
		}
		if (file.isFile())
		{
			long fileLength = file.length();
			Logger.getLogger(this.getClass()).debug(
					String
							.format("generating summary entry for %s(%s)",
									entryPath, new FormatTool()
											.formatBytes(fileLength)));

			entry.setDigest(FileSnapshot.digest(file));
			entry.setKind(EntryKind.FILE);
			entry.setLength(fileLength);
		}

		return entry;
	}

	public Matcher<String> getGeneratorMatcher()
	{
		return _generatorMatcher;
	}

	public ThreadPoolSize getThreadPoolSize()
	{
		return _threadPoolSize;
	}

	private boolean isIgnored(String entryPath)
	{
		if (entryPath.startsWith(DOT_VERSION))
		{
			return true;
		}

		return _generatorMatcher != null
				&& !_generatorMatcher.matches(entryPath);
	}

	public void setGeneratorListener(SummaryGeneratorListener generatorListener)
	{
		_generatorListener = generatorListener;
	}

	public void setGeneratorMatcher(Matcher<String> matcher)
	{
		_generatorMatcher = matcher;
	}

	public void setThreadPoolSize(ThreadPoolSize threadPoolSize)
	{
		_threadPoolSize = threadPoolSize;
	}

	private List<File> visitFiles(File rootDir, File dir) throws IOException
	{
		List<File> files = new ArrayList<File>();

		if (!rootDir.exists())
			return files;

		files.add(dir);
		File[] subFiles = dir.listFiles();

		if (subFiles != null)
		{
			for (File subFile : subFiles)
			{
				String entryPath = SummaryUtils.normalizePath(rootDir, subFile);

				if (isIgnored(entryPath))
					continue;

				if (subFile.isDirectory())
				{
					files.addAll(visitFiles(rootDir, subFile));
				}

				if (subFile.isFile())
				{
					files.add(subFile);
				}
			}
		}
		return files;
	}
}
