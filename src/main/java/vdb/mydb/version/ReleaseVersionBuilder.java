package vdb.mydb.version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import vdb.litesync.source.FileSystemSource;
import vdb.litesync.summary.Summary;
import vdb.litesync.summary.SummaryGenerator;
import vdb.litesync.summary.SummaryUtils;
import vdb.litesync.summary.SummaryWriter;
import vdb.mydb.module.ModuleManager;
import vdb.mydb.module.VdbModule;
import vdb.mydb.xmlbeans.BeanWriter;
import vdb.mydb.xmlbeans.BeanWritterFactory;
import vdb.mydb.xmlbeans.SpringBeans;
import vdb.mydb.xmlbeans.SpringBeansWriter;
import cn.csdb.commons.util.FileUtils;
import cn.csdb.commons.util.Matcher;
import cn.csdb.commons.util.TimeUtils;

/**
 * to make a release version
 * 
 * @author bluejoe
 * 
 */
public class ReleaseVersionBuilder implements ApplicationContextAware
{
	private ApplicationContext _applicationContext;

	private File _sourceDir;

	private Matcher<String> _excludedFilesMatcher;

	private SummaryGenerator _summaryGenerator;

	public void build(long revision) throws Exception
	{
		final File rootDir = _sourceDir;
		FileUtils.rmfiles(_sourceDir, new Matcher<File>()
		{
			public boolean matches(File file)
			{
				try
				{
					String entryPath = SummaryUtils
							.normalizePath(rootDir, file);
					return _excludedFilesMatcher.matches(entryPath);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return false;
				}
			}
		});

		File metaDir = new File(_sourceDir, ".version");
		metaDir.mkdirs();

		// generate summary
		FileSystemSource syncSource = new FileSystemSource(_sourceDir);
		Summary summary = _summaryGenerator.generateSummary(syncSource
				.getWorkingCopyDirectoy());

		new SummaryWriter(syncSource.getLocalSummaryFile()).write(summary);
		new SummaryWriter(syncSource.getRemoteSummaryFile()).write(summary);

		generateVersionProperties(revision, metaDir);
		generateModulesXml(metaDir);
	}

	private void generateVersionProperties(long revision, File metaDir)
			throws IOException, FileNotFoundException
	{
		Properties properties = new Properties();
		properties.put("revision", "" + revision);
		properties.put("releaseDate", TimeUtils.getNowString());
		File propertiesFile = new File(metaDir, "version.properties");

		if (!propertiesFile.exists())
			propertiesFile.createNewFile();

		// save
		properties.store(new FileOutputStream(propertiesFile),
				"properties file of version");
	}

	private void generateModulesXml(File metaDir) throws Exception
	{
		// generate modules.xml
		File xmlFile = new File(metaDir, "modules.xml");

		SpringBeans beans = new SpringBeans();
		ModuleManager moduleManager = (ModuleManager) _applicationContext
				.getBean("moduleManager");
		beans.addBean(moduleManager);
		BeanWritterFactory factory = new BeanWritterFactory();
		factory.registerBeanWriter(ModuleManager.class, new BeanWriter(
				"modules"));
		factory.registerBeanWriter(VdbModule.class, new BeanWriter("name",
				"title", "order", "dependsOn"));

		new SpringBeansWriter(xmlFile).write(beans, factory);
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		_applicationContext = arg0;
	}

	public void setSummaryGenerator(SummaryGenerator summaryGenerator)
	{
		_summaryGenerator = summaryGenerator;
	}

	public void setSourceDir(File sourceDir)
	{
		_sourceDir = sourceDir;
	}

	public void setExcludedFilesMatcher(Matcher<String> cleanedFilesMatcher)
	{
		_excludedFilesMatcher = cleanedFilesMatcher;
	}
}
