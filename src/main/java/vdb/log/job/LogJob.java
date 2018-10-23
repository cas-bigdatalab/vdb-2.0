package vdb.log.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import vdb.log.dao.DataAccessLogDao;
import vdb.log.util.LogQueue;
import vdb.log.vo.DataAccessLog;

public class LogJob implements Job
{
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		execute();
	}

	public void execute()
	{
		List<DataAccessLog> logs = LogQueue.getInstance().removeAllLogs();
		insertLog(logs);
	}

	private void insertLog(List<DataAccessLog> logs)
	{
		for (DataAccessLog record : logs)
		{
			DataAccessLogDao dao = new DataAccessLogDao();
			try
			{
				dao.insertLog(record);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
