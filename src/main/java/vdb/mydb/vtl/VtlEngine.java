package vdb.mydb.vtl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.config.XmlFactoryConfiguration;
import org.apache.velocity.tools.view.JeeConfig;
import org.apache.velocity.tools.view.VelocityView;
import org.apache.velocity.util.SimplePool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.context.ServletContextAware;

import vdb.mydb.context.VspContextInjections;
import vdb.mydb.module.ModuleManager;
import vdb.mydb.util.FilePathSpan;
import vdb.mydb.vtl.impl.VtlContextImpl;
import vdb.mydb.web.ResourceLoader;
import vdb.mydb.web.VspResourceLoader;
import vdb.webpub.VspLoader;

public class VtlEngine implements InitializingBean, ServletContextAware
{
	private VelocityView _view;

	File _toolsDir;

	ResourceLoader _customResourceLoader;

	File _velocityProperties;

	VspContextInjections _contextInjections;

	ModuleManager _moduleManager;

	private SimplePool writerPool = new SimplePool(50);

	private ServletContext _servletContext;

	private File _macrosDir;

	public Context createContext()
	{
		return createContext(new MockHttpServletRequest(),
				new MockHttpServletResponse());
	}

	public VspContext createContext(HttpServletRequest request,
			HttpServletResponse response)
	{
		VtlContextImpl ctx = new VtlContextImpl(_view.createContext(request,
				response));
		ctx.put("context", ctx);
		_contextInjections.inject(ctx);
		return ctx;
	}

	public String evaluate(String vtl, Context vc) throws Exception
	{
		StringWriter sw = new StringWriter();
		_view.getVelocityEngine().evaluate(vc, sw, "", vtl);
		return sw.toString();
	}

	public Template getTemplate(String templateName) throws Exception
	{
		if (!templateName.startsWith("/"))
			templateName = "/" + templateName;

		Template template = null;
		synchronized (_view)
		{
			template = _view.getTemplate(templateName);
		}

		return template;
	}

	public void layout(String templatePath, Context ctx, Writer writer)
			throws Exception
	{
		ctx.remove("VSP_TEMPLATE_LAYOUT");
		StringWriter sw = new StringWriter();
		render(templatePath, ctx, sw);
		String layout = (String) ctx.get("VSP_TEMPLATE_LAYOUT");
		if (layout != null)
		{
			ctx.put("body", sw.toString());
			layout(layout, ctx, writer);
		}
		else
		{
			writer.write(sw.toString());
		}
	}

	public void loadTools(File toolXml) throws FileNotFoundException,
			IOException
	{
		XmlFactoryConfiguration conf = new XmlFactoryConfiguration();
		conf.read(new FileInputStream(toolXml));

		_view.configure(conf);
		Logger.getLogger(this.getClass()).debug(
				String.format("loading tool: %s", toolXml.getCanonicalPath()));
	}

	private void mergeTemplate(Template template, Context context, Writer writer)
			throws Exception
	{
		VelocityWriter velocityWriter = null;
		try
		{
			velocityWriter = (VelocityWriter) writerPool.get();
			if (velocityWriter == null)
			{
				velocityWriter = new VelocityWriter(writer, 4 * 1024, true);
			}
			else
			{
				velocityWriter.recycle(writer);
			}

			template.merge(context, velocityWriter);
		}
		finally
		{
			if (velocityWriter != null)
			{
				try
				{
					velocityWriter.flush();
					velocityWriter.recycle(null);
					writerPool.put(velocityWriter);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void render(String templateName, Context vc, Writer writer)
			throws Exception
	{
		Template template = getTemplate(templateName);
		vc.put("velocityWriter", writer);
		mergeTemplate(template, vc, writer);
	}

	public void afterPropertiesSet() throws Exception
	{
		VspResourceLoader.setInnerResourceLoader(_customResourceLoader);

		MockServletConfig msc = new MockServletConfig(_servletContext);
		msc.addInitParameter("org.apache.velocity.properties",
				_velocityProperties.getCanonicalPath());
		msc.addInitParameter("org.apache.velocity.tools.loadDefaults", "false");

		// macros
		List<File> files = _moduleManager.getMacroFiles();
		for (File macroFile : _macrosDir.listFiles(new FilenameFilter()
		{
			public boolean accept(File arg0, String arg1)
			{
				return arg1.toLowerCase().endsWith(".macro");
			}
		}))
		{
			files.add(macroFile);
		}

		String macros = null;
		for (File file : files)
		{
			if (macros == null)
				macros = "";
			else
				macros += ",";

			macros += new FilePathSpan(new File(_servletContext
					.getRealPath("/")), file).getRelativePath();
		}

		Logger.getLogger(this.getClass()).debug(
				String.format("macros: %s", macros));
		final String finalMacros = macros;
		msc.getServletContext().setAttribute(
				VdbVelocityView.BEFORE_VELOCITY_INIT, new BeforeVelocityInit()
				{
					public void initVelocity(VelocityEngine velocityEngine)
					{
						velocityEngine.setProperty(RuntimeConstants.VM_LIBRARY,
								finalMacros);
					}
				});

		JeeConfig config = new JeeConfig(msc);
		_view = new VdbVelocityView(config);

		// tools
		List<File> toolFiles = _moduleManager.getToolFiles();
		for (File toolFile : _toolsDir.listFiles(new FilenameFilter()
		{
			public boolean accept(File arg0, String arg1)
			{
				return arg1.toLowerCase().endsWith(".tool");
			}
		}))
		{
			toolFiles.add(toolFile);
		}

		for (File toolFile : toolFiles)
		{
			loadTools(toolFile);
		}

		// load all vpage
		loadTemplates(new File(_servletContext.getRealPath("")));
	}

	private void loadTemplates(File appRoot) throws IOException
	{
		VspLoader loader = new VspLoader();
		loader.load(appRoot);
	}

	public File getToolsDir()
	{
		return _toolsDir;
	}

	public void setToolsDir(File toolsDir)
	{
		_toolsDir = toolsDir;
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public void setVelocityProperties(File velocityProperties)
	{
		_velocityProperties = velocityProperties;
	}

	public void setContextInjections(VspContextInjections contextInjections)
	{
		_contextInjections = contextInjections;
	}

	public ModuleManager getModuleManager()
	{
		return _moduleManager;
	}

	public void setModuleManager(ModuleManager moduleManager)
	{
		_moduleManager = moduleManager;
	}

	public File getMacrosDir()
	{
		return _macrosDir;
	}

	public void setMacrosDir(File macrosDir)
	{
		_macrosDir = macrosDir;
	}

	public ResourceLoader getCustomResourceLoader()
	{
		return _customResourceLoader;
	}

	public void setCustomResourceLoader(ResourceLoader customResourceLoader)
	{
		_customResourceLoader = customResourceLoader;
	}
}
