package vdb.webpub.vsp;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.generic.ServletContextLocaleTool;
import vdb.tool.webpub.PublisherTool;
import vdb.webpub.VdbPublisher;
import vdb.webpub.publisher.ProgressReporter;
import vdb.webpub.publisher.PublishJob;
import vdb.webpub.publisher.VdbProgressReporter;
import vdb.webpub.publisher.VdbPublishLogger;

public class StartPublish extends ServletActionProxy
{
	public void doAction(final ServletRequest request,
			final ServletResponse response, ServletContext servletContext)
			throws Exception
	{
		final ProgressReporter progressBar = new VdbProgressReporter();

		progressBar.setMax(100);
		progressBar.setPos(0);
		progressBar.setPrompt(new ServletContextLocaleTool()
				.message("webpub.startPublishing"));

		HttpSession session = ((HttpServletRequest) request).getSession();
		session.setAttribute(VdbProgressReporter.class.getName(), progressBar);

		new Thread(new Runnable()
		{
			public void run()
			{
				VdbPublishLogger logger = new VdbPublishLogger(
						new PublisherTool().getVdbPublisher()
								.getPublishLogFile());

				try
				{
					List<PublishJob> jobs = doPublish(logger);

					progressBar.setMax(jobs.size());
					int i = 0;
					for (PublishJob job : jobs)
					{
						job.run(progressBar);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	private List<PublishJob> doPublish(VdbPublishLogger logger)
			throws Exception
	{
		VdbPublisher publisher = new PublisherTool().getVdbPublisher();
		return publisher.publish(logger);
	}
}