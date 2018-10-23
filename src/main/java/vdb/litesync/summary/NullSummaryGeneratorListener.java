package vdb.litesync.summary;

import java.io.File;
import java.util.List;

public class NullSummaryGeneratorListener implements SummaryGeneratorListener
{
	public void begin(File rootDir, List<File> files)
	{
	}

	public void end()
	{
	}

	public void generateSummaryEntry(File file)
	{
	}

	public void init(SummaryGenerator summaryGenerator)
	{
	}
}
