package vdb.mydb.module;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

public class VdbModuleImpl implements VdbModule
{
	String _title;

	String _name;

	String _version;

	long _order;

	List<String> _dependsOn;

	boolean _isHidden;
	
	String description;

	private File _moduleDir;

	public String getTitle()
	{
		return _title;
	}

	public void setTitle(String name)
	{
		_title = name;
	}

	public List<String> getDependsOn()
	{
		return _dependsOn;
	}

	public void setDependsOn(List<String> dependsOn)
	{
		_dependsOn = dependsOn;
	}

	public String getVersion()
	{
		return _version;
	}

	public void setVersion(String version)
	{
		this._version = version;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String name)
	{
		_name = name;
	}

	public void setModuleDir(File moduleDir)
	{
		_moduleDir = moduleDir;
	}

	public File getModuleDir()
	{
		return _moduleDir;
	}

	public void onLoad(ModuleManager moduleManager) throws Exception
	{
		// ioc
		File moduleIocDir = new File(_moduleDir, moduleManager.getBeansDir())
				.getCanonicalFile();
		if (moduleIocDir.exists())
		{
			for (File file : moduleIocDir.listFiles(new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.startsWith("ioc-") && name.endsWith(".xml");
				}
			}))
			{
				moduleManager.addIoc(file);
			}
		}

		// tools
		File moduleToolsDir = new File(_moduleDir, moduleManager.getToolsDir())
				.getCanonicalFile();
		if (moduleToolsDir.exists())
		{
			for (File file : moduleToolsDir.listFiles(new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.endsWith(".tool");
				}
			}))
			{
				moduleManager.addTool(file);
			}
		}

		// macros
		File moduleMacrosDir = new File(_moduleDir, moduleManager
				.getMacrosDir()).getCanonicalFile();
		if (moduleMacrosDir.exists())
		{
			for (File file : moduleMacrosDir.listFiles(new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.endsWith(".macro");
				}
			}))
			{
				moduleManager.addMacro(file);
			}
		}
	}

	public long getOrder()
	{
		return _order;
	}

	public void setOrder(long order)
	{
		_order = order;
	}

	public boolean isHidden()
	{
		return _isHidden;
	}

	public void setHidden(boolean isHidden)
	{
		_isHidden = isHidden;
	}

	public String getDescription() 
	{
		// TODO Auto-generated method stub
		return description;
	}
	public void setDescription(String des) 
	{
		this.description = des;
	}
}
