package vdb.metacat.fs.io;

import vdb.metacat.Catalog;
import vdb.metacat.Domain;
import vdb.metacat.ctx.CatalogContext;

public interface DomainReader
{

	void afterCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception;

	Domain getDomain();

	void onCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception;

}