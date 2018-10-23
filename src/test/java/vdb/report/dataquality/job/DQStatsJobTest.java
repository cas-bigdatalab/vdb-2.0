package vdb.report.dataquality.job;

import org.junit.Test;

import vdb.mydb.VdbManagerTest;
import vdb.report.resstat.dbstats.job.DbStatsJob;

public class DQStatsJobTest extends VdbManagerTest
{

	@Test
	public void testExecuteStatistic()
	{
		DbStatsJob job = new DbStatsJob();
		job.execute();
	}

}
