package vdb.mydb.job;

import java.util.List;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

/**
 * @deprecated
 * @author bluejoe
 *
 */
public class Jobs
{
	private List<JobDetail> _jobs;

	private List<Trigger> _triggers;

	public void setJobs(List<JobDetail> jobs) throws SchedulerException
	{
		_jobs = jobs;
	}

	public void setTriggers(List<Trigger> triggers) throws SchedulerException
	{
		_triggers = triggers;
	}

	public List<JobDetail> getJobs()
	{
		return _jobs;
	}

	public List<Trigger> getTriggers()
	{
		return _triggers;
	}
}
