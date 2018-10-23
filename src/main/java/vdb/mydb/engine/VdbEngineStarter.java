package vdb.mydb.engine;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import vdb.mydb.module.ModuleManager;
import vdb.mydb.util.FilePathSpan;
import vdb.mydb.util.VdbApplicationContextUtil;
import cn.csdb.commons.util.FileUtils;
import cn.csdb.commons.util.Matcher;

public class VdbEngineStarter
{
	private WebApplicationContext _applicationContext;

	private WebApplicationContext _parentApplicationContext;

	public VdbEngine startEngine(File webRoot) throws Exception
	{
		MockServletConfig msc = new MockServletConfig(new MockServletContext(
				webRoot.getCanonicalPath(), new FileSystemResourceLoader()));

		return startEngine(msc);
	}

	private List<File> loadXmlList(File dir, FileFilter filter)
	{
		List<File> files = new ArrayList<File>();
		if (dir.exists())
		{
			for (File file : dir.listFiles(filter))
			{
				files.add(file);
			}
		}

		return files;
	}

	public VdbEngine startEngine(ServletContext servletContext,
			String iocStartXml, String vdbProperties) throws Exception
	{
		if (iocStartXml == null)
		{
			iocStartXml = "./WEB-INF/conf/ioc-start.xml";
		}

		if (vdbProperties == null)
		{
			vdbProperties = "./WEB-INF/conf/vdb.properties";
		}

		deleteLockFiles(servletContext);

		// load modules
		_parentApplicationContext = VdbApplicationContextUtil
				.createWebApplicationContext(servletContext, null, iocStartXml);

		ModuleManager moduleManager = (ModuleManager) _parentApplicationContext
				.getBean("moduleManager");

		// ioc files
		List<File> files = new ArrayList<File>();
		files.addAll(loadXmlList(
				new File(servletContext.getRealPath("/WEB-INF/conf/ioc")),
				new FileFilter()
				{
					public boolean accept(File file)
					{
						return file.getName().endsWith(".xml");
					}
				}));

		files.addAll(moduleManager.getIocFiles());

		String contextConfigLocation = getContextConfigLocation(servletContext,
				files);

		_applicationContext = VdbApplicationContextUtil
				.createWebApplicationContext(servletContext,
						_parentApplicationContext, contextConfigLocation);
		VdbEngine engine = (VdbEngine) _applicationContext.getBean("engine");

		return engine;
	}

	private void deleteLockFiles(ServletContext servletContext)
	{
		FileUtils.rmfiles(new File(servletContext.getRealPath("")),
				new Matcher<File>()
				{

					public boolean matches(File toMatch)
					{
						return toMatch.isFile()
								&& toMatch.getName().endsWith(".lck");
					}
				});
	}

	private String getContextConfigLocation(ServletContext servletContext,
			List<File> files) throws IOException
	{
		File webRoot = new File(servletContext.getRealPath("/"));
		String contextConfigLocation = null;
		for (File file : files)
		{
			if (contextConfigLocation == null)
				contextConfigLocation = "";
			else
				contextConfigLocation += ",";

			contextConfigLocation += new FilePathSpan(webRoot, file)
					.getRelativePath();
		}

		return contextConfigLocation;
	}

	public VdbEngine startEngine(ServletConfig serveltConfig) throws Exception
	{
		return startEngine(serveltConfig.getServletContext(),
				serveltConfig.getInitParameter("iocStartXml"),
				serveltConfig.getInitParameter("vdbProperties"));
	}

	private void closeContext(WebApplicationContext applicationContext)
	{
		if (applicationContext instanceof ConfigurableWebApplicationContext)
		{
			((ConfigurableWebApplicationContext) applicationContext).close();
		}
	}

	public void shutdown()
	{
		closeContext(_applicationContext);
		closeContext(_parentApplicationContext);
	}
}
