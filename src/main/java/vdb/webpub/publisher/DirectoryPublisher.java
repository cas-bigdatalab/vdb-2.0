package vdb.webpub.publisher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.velocity.context.Context;

import vdb.mydb.VdbManager;
import vdb.mydb.util.FilePathSpan;
import vdb.tool.generic.ServletContextLocaleTool;
import cn.csdb.commons.util.FileUtils;
import cn.csdb.commons.util.Matcher;

/**
 * publish pages from templates in a given directory
 * 
 * @author bluejoe
 * 
 */
public class DirectoryPublisher
{
	private VdbPublishLogger _publishLogger;

	private File _sourceDir;

	public DirectoryPublisher(VdbPublishLogger publishLogger, File sourceDir)
	{
		_publishLogger = publishLogger;
		_sourceDir = sourceDir;
	}

	private void copy(String sourcePathRelativeToApplicationRoot,
			String sourcePathRelativeToSourceDir, ProgressReporter progressBar)
			throws Exception
	{
		sourcePathRelativeToSourceDir = normalizeSourcePathRelativeToSourceDir(sourcePathRelativeToSourceDir);

		write(new FileInputStream(new File(VdbManager.getInstance()
				.getRealPath(sourcePathRelativeToApplicationRoot))),
				_publishLogger, sourcePathRelativeToSourceDir, progressBar);
	}

	private String normalizeSourcePathRelativeToSourceDir(
			String sourcePathRelativeToSourceDir)
	{
		if (!sourcePathRelativeToSourceDir.startsWith("/"))
			sourcePathRelativeToSourceDir = "/" + sourcePathRelativeToSourceDir;

		return sourcePathRelativeToSourceDir;
	}

	public List<PublishJob> publish(
			final PublishContextFactory publishContextFactory) throws Exception
	{
		List<PublishJob> jobs = new ArrayList<PublishJob>();

		if (!_sourceDir.exists())
		{
			return jobs;
		}

		VdbFileFinder finder = new VdbFileFinder();
		List<File> files = finder.find(_sourceDir, new Matcher<File>()
		{
			public boolean matches(File toMatch)
			{
				return true;
			}
		});

		if (files.isEmpty())
		{
			Logger.getLogger(this.getClass()).warn(
					String.format("no template files exist in directory: %s",
							_sourceDir.getCanonicalPath()));
		}

		final File applicationRoot = VdbManager.getEngine()
				.getApplicationRoot();

		final File sourceDir = _sourceDir;

		for (final File file : files)
		{
			try
			{
				// ignore the file
				if (file.getName().endsWith(".ignore"))
				{
					continue;
				}
				// is a template file?
				else if (file.getName().endsWith(".vtl")
						|| file.getName().endsWith(".parse"))
				{
					jobs.add(new PublishJob()
					{

						public void run(ProgressReporter progressBar)
						{
							try
							{
								Context context = publishContextFactory
										.createContext();
								String sourcePath = new FilePathSpan(
										applicationRoot, file)
										.getRelativePath();
								String targetPath = new FilePathSpan(sourceDir,
										file).getRelativePath();
								publish(sourcePath, targetPath, context,
										progressBar);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
				// is a normal resource, such as an image
				else
				{
					jobs.add(new PublishJob()
					{

						public void run(ProgressReporter progressBar)
						{
							try
							{
								String sourcePath = new FilePathSpan(
										applicationRoot, file)
										.getRelativePath();
								String targetPath = new FilePathSpan(sourceDir,
										file).getRelativePath();
								copy(sourcePath, targetPath, progressBar);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}
		}

		return jobs;
	}

	private void publish(String sourcePathRelativeToApplicationRoot,
			String sourcePathRelativeToSourceDir, Context ctx,
			ProgressReporter progressReporter) throws Exception
	{
		sourcePathRelativeToSourceDir = normalizeSourcePathRelativeToSourceDir(sourcePathRelativeToSourceDir);

		StringWriter sw = new StringWriter();
		ctx.remove("VSP_PUBLISH_PATH");
		try
		{
			VdbManager.getInstance().getVelocityEngine().layout(
					sourcePathRelativeToApplicationRoot, ctx, sw);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		String publishPath = (String) ctx.get("VSP_PUBLISH_PATH");
		if (publishPath != null)
		{
			publishPath = normalizeSourcePathRelativeToSourceDir(publishPath);
		}

		write(new ByteArrayInputStream(sw.toString().getBytes("utf-8")),
				_publishLogger, publishPath, progressReporter);
	}

	private void write(InputStream is, VdbPublishLogger logger,
			String targetPathRelativeToWebRoot,
			ProgressReporter progressReporter) throws IOException,
			FileNotFoundException
	{
		if (targetPathRelativeToWebRoot == null)
		{
			progressReporter.stepIt("");
			return;
		}

		File targetFile = new File(new File(VdbManager.getInstance()
				.getRealPath(VdbManager.getInstance().getWebpub())),
				targetPathRelativeToWebRoot);

		String targetPathRelativeToApplicationRoot = new FilePathSpan(
				VdbManager.getInstance().getApplicationRoot(), targetFile)
				.getRelativePath();

		progressReporter.stepIt(new ServletContextLocaleTool().message(
				"webpub.publishingFile", targetFile.getCanonicalPath()));

		// generates a template file
		// target file exists, also be modified by user
		if (!targetFile.exists())
		{
			targetFile.getParentFile().mkdirs();
		}
		else
		{
			targetFile.delete();
		}

		FileUtils.saveFile(is, targetFile);
		targetFile.setReadOnly();

		FileSnapshot fl = new FileSnapshot();
		fl.snap(targetPathRelativeToApplicationRoot);

		logger.addFileSnapshot(fl.getFilePath(), fl);
	}
}