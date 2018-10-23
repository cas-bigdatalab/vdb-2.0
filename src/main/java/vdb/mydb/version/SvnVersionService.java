package vdb.mydb.version;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.context.ServletContextAware;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.ISVNStatusHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import vdb.litesync.action.SyncActionHandler;
import vdb.mydb.module.ModuleManager;
import vdb.mydb.module.VdbModule;
import vdb.mydb.module.VdbModuleImpl;
import vdb.mydb.util.FilePathSpan;
import vdb.webpub.publisher.ProgressReporter;
import vdb.webpub.publisher.VdbProgressReporter;
import cn.csdb.commons.util.Matcher;

public class SvnVersionService implements InitializingBean,
		ServletContextAware, VersionService
{
	private class MySVNEventHandler implements ISVNEventHandler
	{
		private ProgressReporter _reporter;

		public MySVNEventHandler(ProgressReporter reporter)
		{
			_reporter = reporter;
		}

		/*
		 * progress is currently reserved for future purposes and now is always
		 * ISVNEventHandler.UNKNOWN
		 */
		public void handleEvent(SVNEvent event, double progress)
		{
			/*
			 * Gets the current action. An action is represented by
			 * SVNEventAction. In case of an update an action can be determined
			 * via comparing SVNEvent.getAction() and
			 * SVNEventAction.UPDATE_-like constants.
			 */
			SVNEventAction action = event.getAction();
			String pathChangeType = " ";
			if (action == SVNEventAction.UPDATE_ADD)
			{
				/*
				 * the item was added
				 */
				pathChangeType = "A";
			}
			else if (action == SVNEventAction.UPDATE_DELETE)
			{
				/*
				 * the item was deleted
				 */
				pathChangeType = "D";
			}
			else if (action == SVNEventAction.UPDATE_UPDATE)
			{
				/*
				 * Find out in details what state the item is (after having been
				 * updated).
				 * 
				 * Gets the status of file/directory item contents. It is
				 * SVNStatusType who contains information on the state of an
				 * item.
				 */
				SVNStatusType contentsStatus = event.getContentsStatus();
				if (contentsStatus == SVNStatusType.CHANGED)
				{
					/*
					 * the item was modified in the repository (got the changes
					 * from the repository
					 */
					pathChangeType = "U";
				}
				else if (contentsStatus == SVNStatusType.CONFLICTED)
				{
					/*
					 * The file item is in a state of Conflict. That is, changes
					 * received from the repository during an update, overlap
					 * with local changes the user has in his working copy.
					 */
					pathChangeType = "C";
				}
				else if (contentsStatus == SVNStatusType.MERGED)
				{
					/*
					 * The file item was merGed (those changes that came from
					 * the repository did not overlap local changes and were
					 * merged into the file).
					 */
					pathChangeType = "G";
				}
			}
			else if (action == SVNEventAction.UPDATE_EXTERNAL)
			{
				/* for externals definitions */
				_reporter.setPrompt("Fetching external item into '"
						+ event.getFile().getAbsolutePath() + "'");
				_reporter.setPrompt("External at revision "
						+ event.getRevision());
				return;
			}
			else if (action == SVNEventAction.UPDATE_COMPLETED)
			{
				/*
				 * Updating the working copy is completed. Prints out the
				 * revision.
				 */
				_reporter.setPrompt("At revision " + event.getRevision());
				return;
			}
			else if (action == SVNEventAction.ADD)
			{
				_reporter.setPrompt("A     " + event.getFile());
				return;
			}
			else if (action == SVNEventAction.DELETE)
			{
				_reporter.setPrompt("D     " + event.getFile());
				return;
			}
			else if (action == SVNEventAction.LOCKED)
			{
				_reporter.setPrompt("L     " + event.getFile());
				return;
			}
			else if (action == SVNEventAction.LOCK_FAILED)
			{
				_reporter.setPrompt("failed to lock    " + event.getFile());
				return;
			}

			/*
			 * Now getting the status of properties of an item. SVNStatusType
			 * also contains information on the properties state.
			 */
			SVNStatusType propertiesStatus = event.getPropertiesStatus();
			/*
			 * At first consider properties are normal (unchanged).
			 */
			String propertiesChangeType = " ";
			if (propertiesStatus == SVNStatusType.CHANGED)
			{
				/*
				 * Properties were updated.
				 */
				propertiesChangeType = "U";
			}
			else if (propertiesStatus == SVNStatusType.CONFLICTED)
			{
				/*
				 * Properties are in conflict with the repository.
				 */
				propertiesChangeType = "C";
			}
			else if (propertiesStatus == SVNStatusType.MERGED)
			{
				/*
				 * Properties that came from the repository were merged with the
				 * local ones.
				 */
				propertiesChangeType = "G";
			}

			/*
			 * Gets the status of the lock.
			 */
			String lockLabel = " ";
			SVNStatusType lockType = event.getLockStatus();

			if (lockType == SVNStatusType.LOCK_UNLOCKED)
			{
				/*
				 * The lock is broken by someone.
				 */
				lockLabel = "B";
			}

			_reporter.setPrompt(pathChangeType + propertiesChangeType
					+ lockLabel + "       " + event.getFile());
		}

		public void checkCancelled() throws SVNCancelException
		{
		}
	}

	String _url;

	boolean _enabled;

	String _account;

	String _password;

	List<Pattern> _ignorePatterns = new ArrayList<Pattern>();

	private File _webroot;

	private SVNRepository _repository;

	private SVNClientManager _clientManager;

	private List<String> _ignoredDirs;

	private ServletContext _servletContext;

	private Matcher<String> _generatorMatcher;

	public void setAccount(String account)
	{
		_account = account;
	}

	public void setPassword(String password)
	{
		_password = password;
	}

	public void setUrl(String url)
	{
		_url = url;
	}

	public long getRemoteRevision() throws Exception
	{
		SVNRepository repository = getRepository();
		return repository.getLatestRevision();
	}

	public long getLocalRevision() throws Exception
	{
		SVNClientManager clientManager = getClientManager();

		SVNStatusClient sc = clientManager.getStatusClient();
		SVNStatus status = sc.doStatus(_webroot, false);
		return status.getRevision().getNumber();
	}

	public List<VdbModule> getRemoteModules() throws SVNException
	{
		SVNRepository repository = getRepository();
		// get subdirectories in console
		String consoleDirPath = "WebRoot/console";
		ModuleManager moduleManager = new ModuleManager();
		List<VdbModule> modules = new ArrayList<VdbModule>();

		for (SVNDirEntry entry : (List<SVNDirEntry>) repository.getDir(
				consoleDirPath, -1, null, (Collection) null))
		{
			if (entry.getKind() == SVNNodeKind.DIR)
			{
				try
				{
					String name = entry.getName();
					XmlBeanFactory factory = getRemoteXmlBeanFactory(consoleDirPath
							+ "/" + name + "/WEB-INF/meta.xml");
					VdbModuleImpl module = (VdbModuleImpl) factory
							.getBean(factory
									.getBeanNamesForType(VdbModuleImpl.class)[0]);
					module.setName(name);
					modules.add(module);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		moduleManager.sortModules(modules);
		return modules;
	}

	private XmlBeanFactory getRemoteXmlBeanFactory(String path)
			throws SVNException
	{
		SVNRepository repository = getRepository();
		SVNProperties fileProperties = new SVNProperties();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		repository.getFile(path, -1, fileProperties, baos);
		XmlBeanFactory factory = new XmlBeanFactory(new ByteArrayResource(baos
				.toByteArray()));
		return factory;
	}

	public boolean isRepositoryAvaliable()
	{
		try
		{
			getRepository();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public List<String> checkSyncConditions() throws SVNException
	{
		SVNRepository repository = getRepository();
		List<String> errors = new ArrayList<String>();

		if (repository.checkPath("WebRoot", -1) == SVNNodeKind.NONE)
		{
			errors.add(String.format("entry '%s' does not exist", "WebRoot"));
		}

		for (String dirPath : _ignoredDirs)
		{
			if (dirPath.startsWith("/"))
				dirPath = dirPath.substring(1);

			if (repository.checkPath(dirPath, -1) != SVNNodeKind.NONE)
			{
				errors.add(String
						.format("entry '%s' should not exist", dirPath));
			}
		}

		return errors;
	}

	private SVNRepository getRepository() throws SVNException
	{
		if (_repository != null)
			return _repository;

		SVNURL url = SVNURL.parseURIEncoded(_url);
		SVNRepository repository = SVNRepositoryFactory.create(url);
		ISVNAuthenticationManager authManager = SVNWCUtil
				.createDefaultAuthenticationManager(_account, _password);
		repository.setAuthenticationManager(authManager);
		_repository = repository;
		return repository;
	}

	public void updateAll(List<String> excludedModuleNames,
			SyncActionHandler syncActionPostHandler) throws Exception
	{
		SVNClientManager clientManager = getClientManager();

		// clean up first
		try
		{
			clientManager.getWCClient().doCleanup(_webroot);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// differ and get update list from repository
		List<File> updateList = getUpdateList(clientManager,
				excludedModuleNames);

		List<Runnable> jobs = new ArrayList<Runnable>();

		final SVNUpdateClient updateClient = clientManager.getUpdateClient();
		clientManager.setEventHandler(new MySVNEventHandler(
				new VdbProgressReporter()));

		for (final File file : updateList)
		{
			jobs.add(new Runnable()
			{

				public void run()
				{
					try
					{
						updateClient.doUpdate(file, SVNRevision.HEAD,
								SVNDepth.EMPTY, true, false);
					}
					catch (SVNException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	}

	private SVNClientManager getClientManager()
	{
		if (_clientManager != null)
			return _clientManager;

		DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
		SVNClientManager clientManager = SVNClientManager.newInstance(options,
				_account, _password);
		_clientManager = clientManager;
		return clientManager;
	}

	private List<File> getUpdateList(SVNClientManager clientManager,
			List<String> excludedModuleNames) throws SVNException, IOException
	{
		List<SVNStatus> updateList = differDir(clientManager, _webroot,
				SVNDepth.INFINITY);

		// exclude ignored files
		List<Pattern> excludedPatterns = new ArrayList<Pattern>();
		for (String dirPath : _ignoredDirs)
		{
			if (!dirPath.startsWith("/"))
				dirPath = "/" + dirPath;

			excludedPatterns.add(Pattern.compile(dirPath + "(/.*)"));
		}

		// console modules
		for (String moduleName : excludedModuleNames)
		{
			String dirPath = "/console/" + moduleName;
			excludedPatterns.add(Pattern.compile(dirPath + "(/.*)"));
		}

		List<File> filteredList = new ArrayList<File>();
		for (SVNStatus status : updateList)
		{
			File file = status.getFile();

			if (!isEntryIgnored(file, excludedPatterns))
			{
				filteredList.add(file);
				System.err.println(file);
			}
		}

		return filteredList;
	}

	private boolean isEntryIgnored(File file, List<Pattern> excludedPatterns)
			throws IOException
	{
		String path = new FilePathSpan(_webroot, file).getRelativePath();
		if (!path.startsWith("/"))
			path = "/" + path;

		for (Pattern pattern : excludedPatterns)
		{
			if (pattern.matcher(path).matches())
			{
				// System.err.println(pattern.pattern() + " xxxx==== " + path);
				return true;
			}
		}

		return false;
	}

	private List<SVNStatus> differDir(SVNClientManager clientManager, File dir,
			SVNDepth depth) throws SVNException
	{
		final List<SVNStatus> updateList = new ArrayList<SVNStatus>();

		boolean remote = true;
		boolean reportAll = false;
		boolean includeIgnored = false;
		boolean collectParentExternals = true;

		clientManager.getStatusClient().doStatus(dir, SVNRevision.HEAD, depth,
				remote, reportAll, includeIgnored, collectParentExternals,
				new ISVNStatusHandler()
				{

					public void handleStatus(SVNStatus arg0)
							throws SVNException
					{
						try
						{
							SVNStatusType lsc = arg0.getContentsStatus();

							File file = arg0.getFile();
							if (lsc.getCode() == ' ')
							{
								updateList.add(arg0);
							}
							System.out.println(""
									+ arg0.getRemoteContentsStatus().getCode()
									+ arg0.getRemoteContentsStatus().getID());
							System.out.println(""
									+ arg0.getContentsStatus().getCode()
									+ arg0.getContentsStatus().getID());
							System.out.println(file);
							System.out.println("------------------------");
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}, null);

		return updateList;
	}

	public void afterPropertiesSet() throws Exception
	{
		if (_webroot == null)
		{
			_webroot = new File(_servletContext.getRealPath(""))
					.getCanonicalFile();
		}

		DAVRepositoryFactory.setup();
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public void setIgnoredDirs(List<String> ignoredDirs)
	{
		_ignoredDirs = ignoredDirs;
	}

	public boolean isEnabled()
	{
		return _enabled;
	}

	public void setEnabled(boolean enabled)
	{
		_enabled = enabled;
	}

	public void setWebroot(File webroot)
	{
		_webroot = webroot;
	}

	public Matcher<String> getGeneratorMatcher()
	{
		return _generatorMatcher;
	}

	public void setGeneratorMatcher(Matcher<String> generatorMatcher)
	{
		_generatorMatcher = generatorMatcher;
	}

	public Version getLocalVersion() throws Exception
	{
		return null;
	}

	public Version getRemoteVersion() throws Exception
	{
		return null;
	}
}
