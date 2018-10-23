package vdb.metacat.fs.io;

import org.jdom.Element;

import vdb.metacat.Catalog;
import vdb.metacat.CatalogObject;
import vdb.metacat.Domain;
import vdb.metacat.View;
import vdb.metacat.ctx.CatalogContext;

public class ViewReader
{
	private Domain _domain;

	private ElementReader _elementReader;

	private View _view;

	public ViewReader(Domain domain, Element element)
	{
		_domain = domain;
		_elementReader = new ElementReader(element);
	}

	public void afterCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception
	{
		CatalogObject source = catalog.fromUri(_elementReader
				.readAttribute("source"));
		_view.setSource(source);
		_view.removeItems();

		// domain views
		for (Element e : _elementReader.selectChildren("item/source"))
		{
			if (catalog.fromUri(e.getTextNormalize()) != null)
				_view.addItem(catalog.fromUri(e.getTextNormalize()));
		}
	}

	public View getView()
	{
		return _view;
	}

	public void onCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception
	{
		_view = context.create("view");
		_view.meta().putAll(_elementReader.readAttributes());

		catalog.cacheIn(_view);
	}
}
