package vdb.mydb.module;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.ServletContextAware;

import cn.csdb.commons.util.ListUtils;
import cn.csdb.commons.util.Matcher;

public class ModuleManager implements ServletContextAware, InitializingBean
{
	private List<VdbModule> _modules;

	String _modulesDir;

	String _webInfDir;

	String _toolsDir;

	String _beansDir;

	String _macrosDir;

	private ServletContext _servletContext;

	List<File> _toolFiles = new ArrayList<File>();

	List<File> _iocFiles = new ArrayList<File>();

	List<File> _macroFiles = new ArrayList<File>();

	public void addTool(File toolFile)
	{
		_toolFiles.add(toolFile);
	}

	public void addIoc(File iocFile)
	{
		_iocFiles.add(iocFile);
	}

	public void addMacro(File macroFile)
	{
		_macroFiles.add(macroFile);
	}

	public String getWebInfDir()
	{
		return _webInfDir;
	}

	public void setWebInfDir(String dirWebInf)
	{
		_webInfDir = dirWebInf;
	}

	public String getModulesDir()
	{
		return _modulesDir;
	}

	public void setModulesDir(String modulesDir)
	{
		_modulesDir = modulesDir;
	}

	public List<VdbModule> getModules()
	{
		return _modules;
	}

	public List<VdbModule> retrieveVisibleModules()
	{
		return ListUtils.subList(_modules, new Matcher<VdbModule>()
		{
			public boolean matches(VdbModule toMatch)
			{
				return !((VdbModuleImpl) toMatch).isHidden();
			}
		});
	}

	public boolean isModuleInstalled(String moduleName)
	{
		for (VdbModule module : _modules)
		{
			if (module.getName().equalsIgnoreCase(moduleName))
				return true;
		}

		return false;
	}

	public void setModules(List<VdbModule> modules)
	{
		this._modules = modules;
	}

	public void sortModules(List<VdbModule> modules)
	{
		Collections.sort(modules, new Comparator<VdbModule>()
		{
			public int compare(VdbModule o1, VdbModule o2)
			{
				return o1.getOrder() < o2.getOrder() ? -1
						: (o1.getOrder() == o2.getOrder() ? 0 : 1);
			}
		});
	}

	public void loadModules()
	{
		List<VdbModule> modules = new ArrayList<VdbModule>();
		File modulesDir = new File(_servletContext.getRealPath(_modulesDir));
		File[] files = modulesDir.listFiles();
		for (File file : files)
		{
			if (file.isDirectory())
			{
				File metaXml = new File(file, _webInfDir + "/meta.xml");
				if (metaXml.exists())
				{
					try
					{
						XmlBeanFactory factory = new XmlBeanFactory(
								new FileSystemResource(metaXml));
						VdbModuleImpl module = (VdbModuleImpl) factory
								.getBean(factory.getBeanDefinitionNames()[0]);
						String name = file.getName();
						module.setName(name);
						module.setModuleDir(file);
						Logger.getLogger(this.getClass()).debug(
								String.format("detected module: %s(%s)", name,
										module.getTitle()));

						modules.add(module);
						module.onLoad(this);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		sortModules(modules);
		setModules(modules);
	}

	public String getMacrosDir()
	{
		return _macrosDir;
	}

	public void setMacrosDir(String macrosDir)
	{
		_macrosDir = macrosDir;
	}

	public String getToolsDir()
	{
		return _toolsDir;
	}

	public void setToolsDir(String toolsDir)
	{
		_toolsDir = toolsDir;
	}

	public void setServletContext(ServletContext arg0)
	{
		_servletContext = arg0;
	}

	public List<File> getMacroFiles()
	{
		return _macroFiles;
	}

	public List<File> getToolFiles()
	{
		return _toolFiles;
	}

	public List<File> getIocFiles()
	{
		return _iocFiles;
	}

	public String getBeansDir()
	{
		return _beansDir;
	}

	public void setBeansDir(String beansDir)
	{
		_beansDir = beansDir;
	}

	public void afterPropertiesSet() throws Exception
	{
		loadModules();
	}
}
