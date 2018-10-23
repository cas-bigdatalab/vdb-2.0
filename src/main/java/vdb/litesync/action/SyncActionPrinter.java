package vdb.litesync.action;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;

public class SyncActionPrinter implements SyncActionHandler
{
	int _progress;

	private PrintStream _printer;

	private Map<String, SyncAction> _actions = new HashMap<String, SyncAction>();

	public SyncActionPrinter()
	{
		_printer = System.out;
	}

	public SyncActionPrinter(OutputStream os)
	{
		_printer = new PrintStream(os);
	}

	public void begin(List<SyncAction> actions)
	{
		_progress = 0;
		_actions.clear();
		_printer.println(String.format(
				"begin to handle actions, total %d entries", actions.size()));
	}

	public void end(List<SyncAction> actions)
	{
		_printer.println(String.format("end handling all actions"));
	}

	public void handle(SyncAction action)
	{
		_printer.println(String.format("handling action #%d: %s-%s", _progress,
				action.getKind(), action.getEntryPath()));
		_actions.put(action.getEntryPath(), action);
		_progress++;
	}

	public Map<String, SyncAction> getActions()
	{
		return _actions;
	}

	public void init(SyncRepository syncRepository, SyncSource syncSource)
	{
	}

}
