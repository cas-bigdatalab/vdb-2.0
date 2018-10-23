package vdb.mydb.version;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.InputStreamResource;

import vdb.litesync.LiteSynchronizer;
import vdb.litesync.action.CompoundSyncActionHandler;
import vdb.litesync.action.SyncActionHandler;
import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.FileSystemSource;
import vdb.litesync.summary.EntryPathBlackList;
import vdb.litesync.summary.SummaryGenerator;
import vdb.mydb.module.ModuleManager;
import vdb.mydb.module.VdbModule;
import cn.csdb.commons.util.Matcher;
import cn.csdb.commons.util.TimeUtils;

public class LiteSyncVersionService implements VersionService
{
	private SyncActionHandler _syncActionHandler;

	private SyncRepository _syncRepository;

	private boolean _enabled;

	private FileSystemSource _syncSource;

	private SummaryGenerator _summaryGenerator = new SummaryGenerator();

	private Matcher<String> _synchronizationEntryPathMatcher;

	public List<String> checkSyncConditions() throws IOException
	{
		List<String> errors = new ArrayList<String>();

		if (!_syncSource.getLocalSummaryFile().exists())
		{
			errors.add(String.format("`%s` does not exist in local copy", _syncSource
					.getLocalSummaryFile().getPath()));
		}

		if (_syncRepository.getSummaryInputStream() == null)
		{
			errors.add(String.format("summary file does not exist in repository"));
		}

		if (_syncRepository.getVersionFileStream("/modules.xml") == null)
		{
			errors
					.add(String
							.format("modules.xml does not exist in repository"));
		}

		return errors;
	}

	public List<VdbModule> getRemoteModules() throws BeansException,
			IOException
	{
		ModuleManager moduleManager = (ModuleManager) new XmlBeanFactory(
				new InputStreamResource(_syncRepository
						.getVersionFileStream("/modules.xml")))
				.getBeansOfType(ModuleManager.class);

		return moduleManager.getModules();
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public boolean isRepositoryAvaliable()
	{
		return _syncRepository.isAvaliable();
	}

	public void updateAll(List<String> excludedModuleNames,
			SyncActionHandler syncActionPostHandler) throws Exception
	{
		EntryPathBlackList synchronizerMatcher = new EntryPathBlackList();

		for (String moduleName : excludedModuleNames)
		{
			String dirPath = "/console/" + moduleName;
			synchronizerMatcher.addPattern(dirPath + "/(.*)$");
		}

		new LiteSynchronizer(_syncRepository, _syncSource, _summaryGenerator)
				.sync(_synchronizationEntryPathMatcher,
						new CompoundSyncActionHandler(_syncActionHandler,
								syncActionPostHandler));
	}

	public void setEnabled(boolean enabled)
	{
		_enabled = enabled;
	}

	public Version getLocalVersion() throws Exception
	{
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File(_syncSource
				.getWorkingCopyDirectoy(), "/.version/version.properties")));
		Version version = getVersion(properties);
		return version;
	}

	public Version getRemoteVersion() throws Exception
	{
		Properties properties = new Properties();
		properties.load(_syncRepository
				.getVersionFileStream("/version.properties"));
		Version version = getVersion(properties);
		return version;
	}

	private Version getVersion(Properties properties) throws ParseException
	{
		Version version = new Version();
		version
				.setRevision(Long
						.parseLong((String) properties.get("revision")));
		version.setReleaseDate(TimeUtils.parseDate((String) properties
				.get("releaseDate")));
		return version;
	}

	public SyncActionHandler getSyncActionHandler()
	{
		return _syncActionHandler;
	}

	public void setSyncActionHandler(SyncActionHandler syncActionHandler)
	{
		_syncActionHandler = syncActionHandler;
	}

	public SyncRepository getSyncRepository()
	{
		return _syncRepository;
	}

	public void setSyncRepository(SyncRepository syncRepository)
	{
		_syncRepository = syncRepository;
	}

	public void setSummaryGenerator(SummaryGenerator summaryGenerator)
	{
		_summaryGenerator = summaryGenerator;
	}

	public void setSynchronizationEntryPathMatcher(
			Matcher<String> synchronizationEntryPathMatcher)
	{
		_synchronizationEntryPathMatcher = synchronizationEntryPathMatcher;
	}

	public void setSyncSource(FileSystemSource syncSource)
	{
		_syncSource = syncSource;
	}

}
