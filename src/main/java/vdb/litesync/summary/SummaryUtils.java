package vdb.litesync.summary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import vdb.mydb.util.FilePathSpan;
import cn.csdb.commons.util.Matcher;

public class SummaryUtils
{
	public static String normalizePath(File rootDir, File file)
			throws IOException
	{
		String entryPath = new FilePathSpan(rootDir, file).getRelativePath();

		if (file.isDirectory())
		{
			if (!entryPath.endsWith("/"))
			{
				entryPath += "/";
			}
		}

		return entryPath;
	}

	public static <T> Collection<T> intersection(Collection<T> source,
			Collection<T>... targets)
	{
		for (Collection<T> target : targets)
		{
			source = CollectionUtils.intersection(source, target);
		}

		return source;
	}

	public static <T> Collection<T> subtract(Collection<T> source,
			Collection<T>... targets)
	{
		for (Collection<T> target : targets)
		{
			source = CollectionUtils.subtract(source, target);
		}

		return source;
	}

	public static <T> Collection<T> symmetricDifference(Collection<T> source,
			Collection<T>... targets)
	{
		for (Collection<T> target : targets)
		{
			source = CollectionUtils.disjunction(source, target);
		}

		return source;
	}

	public static <T> Collection<T> union(Collection<T> source,
			Collection<T>... targets)
	{
		for (Collection<T> target : targets)
		{
			source = CollectionUtils.union(source, target);
		}

		return source;
	}

	public static Collection<String> getEntryPathListOf(
			Collection<SummaryEntry> entries)
	{
		Map<String, SummaryEntry> map = new HashMap<String, SummaryEntry>();
		for (SummaryEntry entry : entries)
		{
			map.put(entry.getPath(), entry);
		}
		return new ArrayList<String>(map.keySet());
	}

	public static Collection<String> sortEntryPathList(
			Collection<String> pathList)
	{
		ArrayList<String> arrayList = new ArrayList<String>(pathList);
		Collections.sort(arrayList, Collections.reverseOrder());

		return arrayList;
	}

	public static Collection<SummaryEntry> sortEntryList(
			Collection<SummaryEntry> entryList)
	{
		ArrayList<SummaryEntry> arrayList = new ArrayList<SummaryEntry>(
				entryList);
		Collections.sort(arrayList, new Comparator<SummaryEntry>()
		{

			public int compare(SummaryEntry arg0, SummaryEntry arg1)
			{
				return arg0.getPath().compareTo(arg1.getPath());
			}
		});

		return arrayList;
	}

	public static boolean isSummaryEquals(Summary summary1, Summary summary2)
	{
		List<SummaryEntry> es1 = summary1.getEntries();
		List<SummaryEntry> es2 = summary2.getEntries();

		for (int i = 0; i < es1.size(); i++)
		{
			if (!es1.get(i).equals(es2.get(i)))
				return false;
		}

		return true;
	}

	public static Matcher<String> createDefaultMatcher()
	{
		EntryPathBlackList blackList = new EntryPathBlackList();
		blackList.addPattern("(.*/)*\\..*/?");
		return blackList;
	}

	public static Collection<String> filterEntryPathList(
			Collection<String> safeSyncEntries, Matcher<String> entryPathMatcher)
	{
		ArrayList<String> newList = new ArrayList<String>();
		for (String entryPath : safeSyncEntries)
		{
			if (entryPathMatcher.matches(entryPath))
			{
				newList.add(entryPath);
			}
		}

		return newList;
	}

	public static void sortSummary(Summary mergedSummary)
	{
		Collection<SummaryEntry> sorted = sortEntryList(mergedSummary
				.getEntries());
		mergedSummary.setEntries(sorted);
	}
}
