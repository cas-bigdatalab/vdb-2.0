package vdb.litesync.action;

import java.io.File;
import java.io.PrintStream;
import java.util.List;

import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;
import cn.csdb.commons.util.TimeUtils;

public class SyncActionLogger implements SyncActionHandler
{
	private PrintStream _printer;

	private File _logDir;

	private int _progress;

	public void begin(List<SyncAction> actions)
	{
		_progress = 0;
		_printer.println(String.format(
				"begin to handle actions, total %d entries", actions.size()));
	}

	public void end(List<SyncAction> actions)
	{
		_printer.println(String.format("end handling all actions"));
		_printer.close();
	}

	public void handle(SyncAction action)
	{
		_printer.println(String.format("handling action #%d: %s-%s", _progress,
				action.getKind(), action.getEntryPath()));
	}

	public void init(SyncRepository syncRepository, SyncSource syncSource)
	{
		File file = new File(new File(_logDir, TimeUtils
				.getNowString("yyyy-MM-dd")), "update.log");

		try
		{
			file.getParentFile().mkdirs();
			if (!file.exists())
				file.createNewFile();
			_printer = new PrintStream(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public File getLogDir()
	{
		return _logDir;
	}

	public void setLogDir(File logDir)
	{
		_logDir = logDir;
	}

}
