package vdb.webpub.vsp;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.webpub.PublisherTool;
import vdb.webpub.publisher.ProgressReporter;
import vdb.webpub.publisher.PublishJob;
import vdb.webpub.publisher.VdbPublishLogger;

public class DoDeletePublishFiles extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		VdbPublishLogger logger = new VdbPublishLogger(new PublisherTool()
				.getVdbPublisher().getPublishLogFile());

		List<PublishJob> jobs = logger.deleteOldFiles();
		for (PublishJob job : jobs)
		{
			job.run(new ProgressReporter()
			{
				public void stepIt(String prompt)
				{
					Logger.getLogger(DoDeletePublishFiles.class).debug(prompt);
				}

				public long getElapse()
				{
					return 0;
				}

				public long getLeftTime()
				{
					return 0;
				}

				public long getMax()
				{
					return 0;
				}

				public long getPos()
				{
					return 0;
				}

				public String getPrompt()
				{
					return null;
				}

				public void setMax(long max)
				{
				}

				public void setPos(long current)
				{
				}

				public void setPrompt(String prompt)
				{
				}

				public void stepIt()
				{
				}
			});
		}
	}
}