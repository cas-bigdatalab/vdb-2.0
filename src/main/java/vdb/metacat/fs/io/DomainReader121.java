package vdb.metacat.fs.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import vdb.metacat.Catalog;
import vdb.metacat.Domain;
import vdb.metacat.ctx.CatalogContext;

/*
 * read a domain object from a directory
 * 
 * @author bluejoe
 */
public class DomainReader121 implements DomainReader
{
	private Document _document;

	private Domain _domain;

	private File _domainDir;

	private List<ViewReader> _views = new ArrayList<ViewReader>();

	public DomainReader121(File domainDir) throws FileNotFoundException,
			JDOMException, IOException
	{
		_domainDir = domainDir;
		SAXBuilder parser = new SAXBuilder();
		_document = parser.build(new FileInputStream(new File(domainDir,
				"domain.xml")));
	}

	public void afterCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception
	{
		for (ViewReader viewReader : _views)
		{
			viewReader.afterCatalogBuild(catalog, context);
			if (viewReader.getView().getSource() == null)
			{
				_domain.removeView(viewReader.getView());
			}
		}
	}

	public Domain getDomain()
	{
		return _domain;
	}

	public void onCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception
	{
		_domain = context.create("domain");
		Element rootElement = _document.getRootElement();
		ElementReader nr = new ElementReader(rootElement);
		_domain.meta().putAll(nr.readAttributes());

		catalog.cacheIn(_domain);

		// domain views
		for (Element e : nr.selectChildren("view"))
		{
			ViewReader viewReader = new ViewReader(_domain, e);
			viewReader.onCatalogBuild(catalog, context);
			_domain.addView(viewReader.getView());
			_views.add(viewReader);
		}
	}
}
