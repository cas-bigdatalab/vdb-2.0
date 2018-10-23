package vdb.litesync.action;

import java.io.IOException;
import java.util.List;

import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;

public class CompoundSyncActionHandler implements SyncActionHandler
{
	private SyncActionHandler[] _sequencedHandlers;

	public CompoundSyncActionHandler()
	{
		_sequencedHandlers = new SyncActionHandler[0];
	}

	public CompoundSyncActionHandler(SyncActionHandler... sequencedHandlers)
	{
		setHandlers(sequencedHandlers);
	}

	public void setHandlers(SyncActionHandler... sequencedHandlers)
	{
		_sequencedHandlers = sequencedHandlers;
	}

	public void setHandlers(List<SyncActionHandler> sequencedHandlers)
	{
		_sequencedHandlers = sequencedHandlers
				.toArray(new SyncActionHandler[0]);
	}

	public void begin(List<SyncAction> actions)
	{
		for (SyncActionHandler handler : _sequencedHandlers)
		{
			handler.begin(actions);
		}
	}

	public void end(List<SyncAction> actions)
	{
		for (SyncActionHandler handler : _sequencedHandlers)
		{
			handler.end(actions);
		}
	}

	public void handle(SyncAction action) throws IOException
	{
		for (SyncActionHandler handler : _sequencedHandlers)
		{
			handler.handle(action);
		}
	}

	public void init(SyncRepository syncRepository, SyncSource syncSource)
	{
		for (SyncActionHandler handler : _sequencedHandlers)
		{
			handler.init(syncRepository, syncSource);
		}
	}

}
