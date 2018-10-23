package vdb.litesync.summary;

import java.io.File;
import java.util.List;

import vdb.litesync.summary.SummaryGenerator.ThreadPoolSize;

public class StaticThreadPoolSize implements ThreadPoolSize
{
	int _size;

	public void setSize(int size)
	{
		_size = size;
	}

	public int getPoolSize(List<File> entries)
	{
		return _size;
	}
}
