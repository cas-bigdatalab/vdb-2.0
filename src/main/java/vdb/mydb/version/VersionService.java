package vdb.mydb.version;

import java.util.List;

import vdb.litesync.action.SyncActionHandler;
import vdb.mydb.module.VdbModule;

public interface VersionService
{
	Version getRemoteVersion() throws Exception;

	Version getLocalVersion() throws Exception;

	List<VdbModule> getRemoteModules() throws Exception;

	boolean isRepositoryAvaliable();

	List<String> checkSyncConditions() throws Exception;

	public void updateAll(List<String> excludedModuleNames,
			SyncActionHandler syncActionPostHandler) throws Exception;

	boolean isEnabled();
}