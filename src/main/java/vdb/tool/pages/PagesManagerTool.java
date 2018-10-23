package vdb.tool.pages;

import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import vdb.metacat.DataSet;
import vdb.metacat.fs.page.Page;
import vdb.metacat.fs.page.PagesManager;
import vdb.mydb.VdbManager;

public class PagesManagerTool
{

	public PagesManager getPagesManager()
	{
		return (PagesManager) VdbManager.getEngine().getApplicationContext()
				.getBean("pagesManager");
	}

	public Page getPageByName(String name, String entityUri) throws Exception
	{
		return getPagesManager().getPageByName(name, entityUri);
	}

	public List<Page> getPageByType(String type, String entityUri)
			throws Exception
	{
		return getPagesManager().getPagesByType(type, entityUri);
	}

	public List<Page> getAllPages(DataSet dateSet) throws Exception
	{
		return getPagesManager().getAllPages(dateSet);
	}

	public Map<String, List<Page>> getPagesByEntityUri(String entityUri)
			throws DocumentException
	{
		return getPagesManager().getPagesByEntityUri(entityUri);
	}

	public List<String> getPageTypes() throws DocumentException
	{
		return getPagesManager().getPageTypes();
	}

	public Page getDefaultPageByType(String entityUri, String type)
			throws DocumentException
	{
		return getPagesManager().getDefaultPageByType(entityUri, type);
	}

	public boolean checkPageName(String name, DataSet ds)
			throws DocumentException
	{

		return getPagesManager().checkPageName(name, ds);
	}
}
