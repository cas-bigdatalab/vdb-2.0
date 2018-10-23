package vdb.litesync.summary;

import java.io.File;
import java.util.List;

public interface SummaryGeneratorListener
{
	public void init(SummaryGenerator summaryGenerator);

	public void begin(File rootDir, List<File> files);

	public void end();

	public void generateSummaryEntry(File file);
}
