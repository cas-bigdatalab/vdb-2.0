package vdb.mydb.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vdb.mydb.VdbManager;

public class IndexerJob implements Job
{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		execute();
	}

	public void execute()
	{
		try
		{
			Logger.getLogger(this.getClass()).debug(
					"ready to create index database...");

			VdbManager.getEngine().getIndexer().getWriter().update();

			Logger.getLogger(this.getClass()).debug(
					"index database is created successfully.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
