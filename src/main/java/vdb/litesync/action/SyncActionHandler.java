package vdb.litesync.action;

import java.io.IOException;
import java.util.List;

import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.SyncSource;

public interface SyncActionHandler
{

	void begin(List<SyncAction> actions);

	void handle(SyncAction action) throws IOException;

	void end(List<SyncAction> actions);

	void init(SyncRepository syncRepository, SyncSource syncSource);

}
