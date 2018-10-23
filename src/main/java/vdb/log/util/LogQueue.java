package vdb.log.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import vdb.log.vo.DataAccessLog;

public class LogQueue
{
	private static LogQueue _instance;

	public static LogQueue getInstance()
	{
		if (_instance == null)
			_instance = new LogQueue();
		return _instance;
	}

	private List<DataAccessLog> _unhanddledLogs;;

	private LogQueue()
	{
		_unhanddledLogs = Collections.synchronizedList(new LinkedList());
	}

	public void addAllLog(List<DataAccessLog> logs)
	{
		_unhanddledLogs.addAll(logs);
	}

	public void addLog(DataAccessLog record)
	{
		_unhanddledLogs.add(record);
	}

	public boolean isEmpty()
	{
		return _unhanddledLogs.isEmpty();
	}

	public List<DataAccessLog> removeAllLogs()
	{
		List ret = new LinkedList(_unhanddledLogs);
		_unhanddledLogs.clear();
		return ret;

	}

	public DataAccessLog removeLog()
	{
		if (_unhanddledLogs.size() > 0)
		{
			return _unhanddledLogs.remove(0);
		}
		return null;
	}

	public int size()
	{
		return _unhanddledLogs.size();
	}

	public void watchLog()
	{
		System.err.println("********************");
		for (DataAccessLog record : _unhanddledLogs)
		{
			System.err.println(record.getOperation() + " " + record.getOpTime()
					+ "  " + record.getEntity() + "  " + record.getParam2());
		}
		System.err.println("********************");
	}

}
