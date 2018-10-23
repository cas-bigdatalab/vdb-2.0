package vdb.tool.ui;

import vdb.metacat.CatalogObject;
import vdb.mydb.files.FileMetaData;
import vdb.tool.VdbTool;

public class UrlsTool extends VdbTool
{
	public static String getUriFromFileName(String fileName)
	{
		int i = fileName.lastIndexOf("[");
		int j = fileName.lastIndexOf("]");

		String uri = null;
		if (i >= 0 && j > 0 && i < j)
			uri = fileName.substring(i + 1, j);

		return uri;
	}

	public String from(CatalogObject en, String viewName)
	{
		return String.format("/%s.vpage?uri=%s", viewName, en.getUri());
		// return String.format("/~%s[%s].vpage", viewName, en.getUri());
	}

	public String fromFile(FileMetaData fi)
	{
		try
		{
			return String.format("/files/%s/%s%s", fi.getDataSet().getUri(), fi
					.getId(), fi.getExtension());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}