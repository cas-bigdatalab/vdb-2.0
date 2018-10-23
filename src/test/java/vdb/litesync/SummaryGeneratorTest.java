package vdb.litesync;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import vdb.litesync.summary.Summary;
import vdb.litesync.summary.SummaryGenerator;
import vdb.litesync.summary.SummaryReader;
import vdb.litesync.summary.SummaryUtils;
import vdb.litesync.summary.SummaryWriter;

public class SummaryGeneratorTest extends TestCase
{
	SummaryGenerator _summaryGenerator;

	private File _appDir;

	protected void setUp() throws Exception
	{
		super.setUp();
		_summaryGenerator = new SummaryGenerator();
		_appDir = new File("E:\\workspace\\svn-test");
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGenerate() throws IOException
	{
		Summary summary = _summaryGenerator.generateSummary(_appDir);
		assertTrue(summary.getEntries().size() > 0);
		assertSummaryEquals(summary, summary);

		Summary summary4 = _summaryGenerator.generateSummary(_appDir);
		assertSummaryEquals(summary, summary4);

		SummaryWriter sw = new SummaryWriter(new File(
				"E:\\workspace\\svn-test\\.summary"));
		sw.write(summary);

		SummaryReader sr = new SummaryReader(new File(
				"E:\\workspace\\svn-test\\.summary"));
		Summary summary2 = sr.read();
		assertSummaryEquals(summary, summary2);

		SummaryReader sr2 = new SummaryReader(new File(
				"E:\\workspace\\svn-test\\.summary"));
		Summary summary3 = sr2.read();
		assertSummaryEquals(summary, summary3);
	}

	private void assertSummaryEquals(Summary summary1, Summary summary2)
	{
		assertTrue(SummaryUtils.isSummaryEquals(summary1, summary2));
	}
}
