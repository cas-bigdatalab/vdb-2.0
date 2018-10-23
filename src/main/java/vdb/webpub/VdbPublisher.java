package vdb.webpub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.context.Context;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Query;
import vdb.metacat.fs.page.Page;
import vdb.metacat.fs.page.PagesManager;
import vdb.mydb.VdbManager;
import vdb.mydb.theme.Theme;
import vdb.tool.webpub.ThemeTool;
import vdb.webpub.publisher.DirectoryPublisher;
import vdb.webpub.publisher.PublishContextFactory;
import vdb.webpub.publisher.PublishJob;
import vdb.webpub.publisher.VdbPublishLogger;

public class VdbPublisher implements ApplicationContextAware
{
	File _prototypeDir;

	File _publishLogFile;

	String _webpub = "";

	public File getPrototypeDir()
	{
		return _prototypeDir;
	}

	public File getPublishLogFile()
	{
		return _publishLogFile;
	}

	public String getWebpub()
	{
		return _webpub;
	}

	public List<PublishJob> publish(VdbPublishLogger publishLogger)
			throws Exception
	{
		List<PublishJob> jobs = new ArrayList<PublishJob>();

		jobs.addAll(publishLogger.deleteOldFiles());
		jobs.addAll(publishTheme(publishLogger, new ThemeTool().getTheme()));
		jobs.addAll(publishLogger.save());

		return jobs;
	}

	private List<PublishJob> publishDataSet(VdbPublishLogger publishLogger,
			final DataSet dataSet) throws Exception
	{
		List<PublishJob> jobs = new ArrayList<PublishJob>();

		// all pages
		for (Page page : new PagesManager().getAllPages(dataSet))
		{
			jobs.addAll(publishPage(publishLogger, new File(_prototypeDir,
					"page"), page));
		}

		for (Entity entity : ((DataSet) dataSet).getEntities())
		{
			jobs.addAll(publishEntity(publishLogger, new File(_prototypeDir,
					"entity"), entity));
		}

		for (Query query : ((DataSet) dataSet).getQueries())
		{
			jobs.addAll(publishQuery(publishLogger, new File(_prototypeDir,
					"query"), query));
		}

		// NOTICE: publish entities first
		jobs.addAll(new DirectoryPublisher(publishLogger, new File(
				_prototypeDir, "dataset")).publish(new PublishContextFactory()
		{

			public Context createContext()
			{
				Context ctx = VdbManager.getApplicationContext()
						.getVelocityEngine().createContext();

				ctx.put("dataset", dataSet);
				ctx.put("meta", dataSet);

				return ctx;
			}
		}));

		return jobs;
	}

	private List<PublishJob> publishDomain(VdbPublishLogger publishLogger,
			File baseDir) throws Exception
	{
		return new DirectoryPublisher(publishLogger, baseDir)
				.publish(new PublishContextFactory()
				{

					public Context createContext()
					{
						Context ctx = VdbManager.getApplicationContext()
								.getVelocityEngine().createContext();

						return ctx;
					}
				});
	}

	private List<PublishJob> publishEntity(VdbPublishLogger publishLogger,
			File baseDir, final Entity entity) throws Exception
	{
		return new DirectoryPublisher(publishLogger, baseDir)
				.publish(new PublishContextFactory()
				{

					public Context createContext()
					{
						Context ctx = VdbManager.getApplicationContext()
								.getVelocityEngine().createContext();

						ctx.put("entity", entity);
						ctx.put("meta", entity);

						return ctx;
					}
				});
	}

	private List<PublishJob> publishPage(VdbPublishLogger publishLogger,
			File baseDir, final Page page) throws Exception
	{
		return new DirectoryPublisher(publishLogger, baseDir)
				.publish(new PublishContextFactory()
				{

					public Context createContext()
					{
						Context ctx = VdbManager.getApplicationContext()
								.getVelocityEngine().createContext();

						ctx.put("entity", page.getEntity());
						ctx.put("page", page);

						return ctx;
					}
				});
	}

	private List<PublishJob> publishQuery(VdbPublishLogger publishLogger,
			File baseDir, final Query query) throws Exception
	{
		return new DirectoryPublisher(publishLogger, baseDir)
				.publish(new PublishContextFactory()
				{

					public Context createContext()
					{
						Context ctx = VdbManager.getApplicationContext()
								.getVelocityEngine().createContext();

						ctx.put("query", query);
						ctx.put("meta", query);

						return ctx;
					}
				});
	}

	private List<PublishJob> publishTheme(VdbPublishLogger publishLogger,
			final Theme theme) throws Exception
	{
		List<PublishJob> jobs = new ArrayList<PublishJob>();

		// dataset
		for (DataSet dataSet : VdbManager.getInstance().getDomain()
				.getDataSets())
		{
			if (dataSet == null)
				continue;

			jobs.addAll(publishDataSet(publishLogger, dataSet));
		}

		// now, publish domain pages
		jobs.addAll(publishDomain(publishLogger, new File(_prototypeDir,
				"domain")));

		// theme resource files
		jobs.addAll(new DirectoryPublisher(publishLogger, new ThemeTool()
				.getThemeFile(theme, "res"))
				.publish(new PublishContextFactory()
				{
					public Context createContext()
					{
						Context ctx = VdbManager.getApplicationContext()
								.getVelocityEngine().createContext();

						ctx.put("theme", theme);
						return ctx;
					}
				}));

		return jobs;
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
	}

	public void setPrototypeDir(File templatesDir) throws IOException
	{
		_prototypeDir = templatesDir;
		if (!templatesDir.exists())
		{
			Logger
					.getLogger(this.getClass())
					.warn(
							String
									.format(
											"omg! directory `%s` does not exist! do you configure the template directory path correctly?",
											_prototypeDir.getCanonicalPath()));
		}
	}

	public void setPublishLogFile(File publishLogFile)
	{
		_publishLogFile = publishLogFile;
	}

	public void setWebpub(String webpub)
	{
		if (!webpub.startsWith("/"))
			webpub = "/" + webpub;

		if (webpub.endsWith("/"))
			webpub = webpub.substring(0, webpub.length() - 1);

		_webpub = webpub;
	}
}