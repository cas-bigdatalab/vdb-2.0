package vdb.report.resstat.filestats.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vdb.mydb.repo.RepositoryFile;
import vdb.report.resstat.filestats.vo.FileIndicator;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;

public class FileStatsThread extends Thread
{

	private static List<Thread> runningThreads = new ArrayList<Thread>();

	public static ExecutorService executor = Executors.newFixedThreadPool(5);

	public static Character lock = Character.valueOf('l');

	private RepositoryFile _file;

	private RepositoryIndicator _ri;

	private int _dirDepth;

	public FileStatsThread(RepositoryFile f, RepositoryIndicator ri,
			int dirDepth)
	{
		regist(this);// 构造线程时就注册线程
		_file = f;
		_ri = ri;
		_dirDepth = dirDepth + 1;
	}

	@SuppressWarnings("unchecked")
	public void run()
	{

		List<RepositoryFile> fileList = _file.listFiles();
		String contentType, fileExtension;
		FileIndicator indicator;
		Map<String, FileIndicator> map;
		if (fileList != null)
		{
			for (RepositoryFile f : fileList)
			{
				if (f.isFile())
				{
					contentType = f.getContentType();
					fileExtension = f.getFileExtension();
					if (contentType != null && !contentType.trim().equals(""))
					{
						if (contentType.indexOf("/") > 0)
							contentType = contentType.substring(0, contentType
									.indexOf("/"));
					}
					else
					{
						contentType = "unknown";
					}
					// 同步MAP变量
					synchronized (_ri)
					{
						if (f.length() > _ri.getMaxFileBytes())
						{
							_ri.setMaxFileBytes(f.length());
						}
						if (f.length() < _ri.getMinFileBytes())
						{
							_ri.setMinFileBytes(f.length());
						}
						map = _ri.getFileIndicatorMap();
					}
					synchronized (map)
					{
						if (map.containsKey(fileExtension))
						{
							indicator = map.get(fileExtension);
							indicator.setBytes(indicator.getBytes()
									+ f.length());
							indicator.setItems(indicator.getItems() + 1);
						}
						else
						{
							indicator = new FileIndicator();
							indicator.setContentType(contentType);
							indicator.setFileExtension(f.getFileExtension());
							indicator.setBytes(f.length());
							indicator.setItems(1);
							map.put(fileExtension, indicator);
						}
					}
				}
				if (f.isDirectory())
				{
					synchronized (_ri)
					{
						_ri.setDirectoryNum(_ri.getDirectoryNum() + 1);
						if (_dirDepth > _ri.getMaxDirDepth())
							_ri.setMaxDirDepth(_dirDepth);
					}
					executor.submit(new FileStatsThread(f, _ri, _dirDepth));
				}
			}
		}

		unRegist(this);// 执行完毕后注销
	}

	public void regist(Thread t)
	{
		synchronized (runningThreads)
		{
			runningThreads.add(t);
		}
	}

	public void unRegist(Thread t)
	{
		synchronized (runningThreads)
		{
			runningThreads.remove(t);
		}
		synchronized (lock)
		{
			if (runningThreads.size() == 0)
			{
				lock.notifyAll();
			}
		}
	}

	public static boolean hasThreadRunning()
	{
		// 通过判断runningThreads是否为空就能知道是否还有线程未执行完
		return (runningThreads.size() > 0);
	}

}
