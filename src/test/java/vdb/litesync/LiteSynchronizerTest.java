package vdb.litesync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;
import vdb.litesync.action.ActionKind;
import vdb.litesync.action.BackupInAnotherDirectory;
import vdb.litesync.action.SyncAction;
import vdb.litesync.action.SyncActionPrinter;
import vdb.litesync.action.SyncFilePersister;
import vdb.litesync.repository.FileSystemRepository;
import vdb.litesync.repository.SyncRepository;
import vdb.litesync.source.FileSystemSource;
import vdb.litesync.source.SyncSource;
import vdb.litesync.summary.EntryPathBlackList;
import vdb.litesync.summary.Summary;
import vdb.litesync.summary.SummaryGenerator;
import vdb.litesync.summary.SummaryReader;
import vdb.litesync.summary.SummaryUtils;
import vdb.litesync.summary.SummaryWriter;
import cn.csdb.commons.util.FileUtils;

public class LiteSynchronizerTest extends TestCase
{
	LiteSynchronizer _dirSynchronizer;

	private SyncRepository _syncRepository;

	private SyncSource _syncSource;

	private int _count = 1000;

	private File _remote;

	private File _local;

	private File _backup;

	private SummaryGenerator _summaryGenerator;

	private EntryPathBlackList _synchronizationEntryPathMatcher;

	protected void setUp() throws Exception
	{
		super.setUp();
		_remote = new File("c:\\sync-remote");
		_local = new File("c:\\sync-local");
		_backup = new File("c:\\sync-backup");

		FileUtils.rmdirs(_remote);
		FileUtils.rmdirs(_local);
		FileUtils.rmdirs(_backup);

		_remote.mkdirs();

		FileSystemRepository fsr = new FileSystemRepository();
		fsr.setRootDir(_remote);
		_syncRepository = fsr;

		new File(_remote, "remoteDeletedFile").createNewFile();
		new File(_remote, "remoteDeletedDir").mkdirs();
		new File(_remote, "remoteDeletedDir/file1").createNewFile();
		new File(_remote, "remoteUnchangedFile").createNewFile();
		new File(_remote, "remoteChangedFile").createNewFile();
		new File(_remote, "remoteFileToBeDeletedLocally").createNewFile();
		new File(_remote, "remoteFileToBeModifiedLocally").createNewFile();

		FileUtils.copy(_remote, _local);
		_syncSource = new FileSystemSource(_local);

		_summaryGenerator = new SummaryGenerator();

		Summary localSummary = _summaryGenerator.generateSummary(_syncSource
				.getWorkingCopyDirectoy());
		new SummaryWriter(_syncSource.getLocalSummaryFile())
				.write(localSummary);
		new SummaryWriter(_syncSource.getRemoteSummaryFile())
				.write(localSummary);

		// remote
		new File(_remote, "remoteAddedFile").createNewFile();
		new File(_remote, "remoteDeletedFile").delete();
		modifyFile(new File(_remote, "remoteChangedFile"));
		FileUtils.rmdirs(new File(_remote, "remoteDeletedDir"));

		new File(_remote, ".iamignored").createNewFile();
		new File(_remote, "iamalsoignored").createNewFile();

		// local
		new File(_local, "localAddedFile").createNewFile();
		new File(_local, ".ignoredFile").createNewFile();
		new File(_local, "localAddedDir").mkdir();
		new File(_local, "remoteFileToBeDeletedLocally").delete();
		modifyFile(new File(_local, "remoteFileToBeModifiedLocally"));

		new SummaryWriter(new File(_remote, "/.version/local.summary"))
				.write(_summaryGenerator.generateSummary(_remote));

		_synchronizationEntryPathMatcher = new EntryPathBlackList();
		_synchronizationEntryPathMatcher.addPattern("(.*/)\\..*/?$");
		_synchronizationEntryPathMatcher.addPattern("/iamalsoignored.*");

		_dirSynchronizer = new LiteSynchronizer(_syncRepository, _syncSource,
				_summaryGenerator);
	}

	private void modifyFile(File file) throws IOException
	{
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("" + _count + "\r\n");
		_count++;
		fileWriter.close();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testSync1() throws IOException
	{
		SyncActionPrinter syncActionPrinter = new SyncActionPrinter();
		_dirSynchronizer.sync(_synchronizationEntryPathMatcher,
				syncActionPrinter);
		Map<String, SyncAction> actions = syncActionPrinter.getActions();

		assertAction(actions, "/remoteDeletedFile", ActionKind.DELETE_FILE);
		assertAction(actions, "/remoteChangedFile", ActionKind.UPDATE_FILE);
		assertAction(actions, "/remoteAddedFile", ActionKind.ADD_FILE);

		assertFalse(actions.containsKey("/remoteUnchangedFile"));
		assertFalse(actions.containsKey("/remoteFileToBeDeletedLocally"));
		assertFalse(actions.containsKey("/remoteFileToBeModifiedLocally"));
		assertFalse(actions.containsKey("/localAddedFile"));
		assertFalse(actions.containsKey("/.iamignored"));
		assertFalse(actions.containsKey("/iamalsoignored"));

		// new updates in remote
		modifyFile(new File(_remote, "remoteFileToBeModifiedLocally"));

		_dirSynchronizer.sync(_synchronizationEntryPathMatcher,
				syncActionPrinter);
		assertFalse(actions.containsKey("/remoteFileToBeModifiedLocally"));
		assertEquals(true, syncActionPrinter.getActions().isEmpty());
	}

	public void testSync2() throws IOException
	{
		BackupInAnotherDirectory bk = new BackupInAnotherDirectory(_backup);
		SyncFilePersister sfh = new SyncFilePersister();
		sfh.setBackupStrategy(bk);
		_dirSynchronizer.sync(_synchronizationEntryPathMatcher, sfh);
		System.out.print(bk.getBackupFiles());

		assertTrue(SummaryUtils.isSummaryEquals(_summaryGenerator
				.generateSummary(_syncSource.getWorkingCopyDirectoy()),
				new SummaryReader(_syncSource.getLocalSummaryFile()).read()));
	}

	public void assertAction(Map<String, SyncAction> actions, String path,
			ActionKind kind)
	{
		assertTrue(actions.containsKey(path));
		assertEquals(actions.get(path).getKind(), kind);
	}
}
