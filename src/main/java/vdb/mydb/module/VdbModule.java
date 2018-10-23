package vdb.mydb.module;

import java.util.List;

public interface VdbModule
{
	public void onLoad(ModuleManager moduleManager) throws Exception;

	public String getTitle();

	public List<String> getDependsOn();

	public String getVersion();

	public String getName();

	public long getOrder();
	
	public String getDescription();
}