package vdb.webpub.publisher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import vdb.mydb.VdbManager;
import vdb.tool.generic.ServletContextLocaleTool;
import vdb.tool.webpub.PublisherTool;
import cn.csdb.commons.util.FileUtils;

public class VdbPublishLogger
{
	private File _file;

	private String _webroot;

	FileSnapshotList _fileSnapshotList = new FileSnapshotList();

	public VdbPublishLogger(File file)
	{
		_file = file;
		_webroot = new PublisherTool().getVdbPublisher().getWebpub();
		_fileSnapshotList = load();
	}

	private FileSnapshotList load()
	{
		FileSnapshotList fileSnapshotList = new FileSnapshotList();
		try
		{
			if (_file.exists())
			{
				BufferedReader reader = new BufferedReader(
						new FileReader(_file));
				while (true)
				{
					String line = reader.readLine();
					if (line == null)
						break;

					String prefix = "webroot:";
					if (line.startsWith(prefix))
					{
						_webroot = line.substring(prefix.length());
						continue;
					}

					StringTokenizer st = new StringTokenizer(line, "\t");
					String fileName = st.nextToken();
					FileSnapshot fl = new FileSnapshot();
					fl.setFilePath(fileName);
					fl.setLastModified(st.nextToken());
					fl.setFileLength(Long.parseLong(st.nextToken()));
					fl.setDigest(st.nextToken());

					fileSnapshotList.add(fileName, fl);
				}

				reader.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return fileSnapshotList;
	}

	public List<PublishJob> deleteOldFiles()
	{
		List<PublishJob> jobs = new ArrayList<PublishJob>();

		for (final FileSnapshot fileSnapshot : _fileSnapshotList.list())
		{
			jobs.add(new PublishJob()
			{
				public void run(ProgressReporter progressBar)
				{
					progressBar
							.stepIt(new ServletContextLocaleTool().message(
									"webpub.deleteOldFile", fileSnapshot
											.getFilePath()));
					fileSnapshot.delete();
				}
			});
		}

		if (_file.exists())
		{
			_file.delete();
		}

		jobs.add(new PublishJob()
		{
			public void run(ProgressReporter progressBar)
			{
				progressBar.stepIt(new ServletContextLocaleTool().message(
						"webpub.deleteFolder", _webroot));
				// delete empty folders
				deleteFolderIfEmpty(new File(VdbManager.getEngine()
						.getApplicationRoot(), _webroot));

				_fileSnapshotList.clear();
			}
		});

		return jobs;
	}

	// TODO deleteFolderIfEmpty
	private boolean deleteFolderIfEmpty(File folder)
	{
		if (!folder.exists())
			return false;

		boolean isEmpty = true;

		for (File file : folder.listFiles())
		{
			if (file.isFile())
			{
				isEmpty &= false;
			}

			if (file.isDirectory())
			{
				isEmpty &= deleteFolderIfEmpty(file);
			}
		}

		if (isEmpty)
		{
			folder.delete();
			return true;
		}

		return false;
	}

	public List<PublishJob> save()
	{
		List<PublishJob> jobs = new ArrayList<PublishJob>();

		jobs.add(new PublishJob()
		{
			public void run(ProgressReporter progressBar)
			{
				try
				{
					progressBar.stepIt(new ServletContextLocaleTool().message(
							"webpub.save", _file.getCanonicalPath()));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				saveInternal();
			}
		});

		return jobs;
	}

	private void saveInternal()
	{
		try
		{
			StringWriter writer = new StringWriter();
			writer.write(String.format("webroot:%s\r\n", _webroot));

			for (FileSnapshot fl : _fileSnapshotList.list())
			{
				writer.write(String.format("%s\t%s\t%s\t%s\r\n", fl
						.getFilePath(), "" + fl.getLastModified(), ""
						+ fl.getFileLength(), fl.getDigest()));
			}

			writer.close();

			if (!_file.exists())
			{
				_file.createNewFile();
			}

			FileUtils.saveFile(writer.toString(), _file);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void addFileSnapshot(String filePath, FileSnapshot fileSnapshot)
	{
		_fileSnapshotList.add(filePath, fileSnapshot);
	}
}
