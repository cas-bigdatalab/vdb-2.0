package vdb.mydb.job;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @deprecated
 * @author bluejoe
 *
 */
public class VdbJobScheduler implements BeanFactoryPostProcessor
{
	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException
	{
		Scheduler scheduler;
		try
		{
			scheduler = new StdSchedulerFactory().getScheduler();

			for (String name : arg0.getBeanNamesForType(Jobs.class))
			{
				Jobs jobs = (Jobs) arg0.getBean(name);
				for (JobDetail job : jobs.getJobs())
				{
					scheduler.addJob(job, true);
					Logger.getLogger(this.getClass()).debug(
							String.format("loading job %s", job));
				}

				for (Trigger trigger : jobs.getTriggers())
				{
					scheduler.scheduleJob(trigger);
				}
			}

			scheduler.start();
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}
	}
}
