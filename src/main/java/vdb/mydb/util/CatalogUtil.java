package vdb.mydb.util;

import java.io.File;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.engine.VdbEngine;

public class CatalogUtil
{
	@SuppressWarnings("deprecation")
	public static File getDataSetRoot(DataSet dataSet)
	{
		return VdbManager.getInstance().getDataSetRoot(dataSet);
	}

	@SuppressWarnings("deprecation")
	public static File getDataSetRoot(String dataSetUri)
	{
		VdbEngine engine = VdbManager.getInstance();
		return engine.getDataSetRoot((DataSet) engine.getCatalog().fromUri(
				dataSetUri));
	}
}
