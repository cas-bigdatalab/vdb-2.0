package vdb.mydb.job;

import java.util.Date;

import junit.framework.TestCase;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;

public class IndexerJobTest extends TestCase
{
	public void test1() throws SchedulerException, InterruptedException
	{
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		JobDetail job = new JobDetail("job1", "group1", IndexerJob.class);
		// compute a time that is on the next round minute
		Date runTime = TriggerUtils.getEvenMinuteDate(new Date());

		// Trigger the job to run on the next round minute
		SimpleTrigger trigger = new SimpleTrigger("trigger1", "group1", runTime);
		sched.scheduleJob(job, trigger);
		sched.start();
		Thread.sleep(90L * 1000L);
		sched.shutdown(true);
	}
}
