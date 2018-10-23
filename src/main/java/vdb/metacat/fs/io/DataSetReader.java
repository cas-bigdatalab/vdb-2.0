package vdb.metacat.fs.io;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.ctx.CatalogContext;

public interface DataSetReader
{

	void afterCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception;

	DataSet getDataSet();

	void onCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception;

}