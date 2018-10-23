package vdb.metacat.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Query;
import vdb.metacat.Relation;
import vdb.metacat.View;
import vdb.metacat.ctx.CatalogContext;
import vdb.metacat.ctx.CatalogContextProxy;
import vdb.metacat.fs.io.DataSetReader;
import vdb.metacat.fs.io.DataSetReader121;
import vdb.metacat.fs.io.DataSetWriter;
import vdb.metacat.fs.io.DomainReader;
import vdb.metacat.fs.io.DomainReader121;
import vdb.metacat.fs.io.DomainWriter;
import vdb.metacat.fs.io.ElementReader;

public class CatalogManager
{
	private Catalog _catalog;

	CatalogContext _context;

	private File _domainRoot;

	public CatalogManager(File domainRoot, Catalog catalog,
			CatalogContext context)
	{
		_domainRoot = domainRoot;
		_catalog = catalog;
		_context = new CatalogContextProxy(context);
	}

	public void deleteDataSet(Domain domain, DataSet dataSet)
	{
		domain.removeDataSet(dataSet);

		unregisterDataSet(dataSet);
		DataSetWriter dsWriter = new DataSetWriter(new File(_domainRoot,
				dataSet.getUri()));
		dsWriter.delete();

		try
		{
			saveDomain(domain);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void unregisterDataSet(DataSet dataSet)
	{

		if (dataSet != null)
		{
			if (dataSet.getRepository() != null)
			{
				_catalog.cacheOut(dataSet.getRepository());
			}

			// entities

			for (Entity entity : dataSet.getEntities())
			{
				unregisterEntity(entity);
			}

			// queries
			for (Query query : dataSet.getQueries())
			{
				_catalog.cacheOut(query);
			}

			// relations
			for (Relation relation : dataSet.getRelations())
			{
				_catalog.cacheOut(relation);
			}

			// views
			for (View view : dataSet.getViews())
			{
				_catalog.cacheOut(view);
			}

			_catalog.cacheOut(dataSet);
		}
	}

	public void unregisterEntity(Entity entity)
	{
		_catalog.cacheOut(entity);
		_catalog.cacheOut(entity.getIdentifier());

		// fields
		for (Field field : entity.getFields())
		{
			_catalog.cacheOut(field);
		}
	}

	public CatalogContext getContext()
	{
		return _context;
	}

	private DataSetReader getDataSetReader(Domain domain, File dataSetDir)
			throws FileNotFoundException, JDOMException, IOException
	{
		SAXBuilder parser = new SAXBuilder();
		File schemaXml = new File(dataSetDir, "schema.xml");
		Document document = parser.build(new FileInputStream(schemaXml));

		Element rootElement = document.getRootElement();
		ElementReader dr = new ElementReader(rootElement);
		long version = dr.getVersion();
		if (version == -1)
		{
			Logger.getLogger(this.getClass()).info(
					String.format("find schema file in old version: %s",
							schemaXml.getCanonicalPath()));
			// upgrade it
			updateSchemaXml12To121(schemaXml);
		}

		return new DataSetReader121(domain, dataSetDir);
	}

	private DomainReader getDomainReader(File domainRoot)
			throws FileNotFoundException, JDOMException, IOException
	{
		SAXBuilder parser = new SAXBuilder();
		File domainXml = new File(domainRoot, "domain.xml");
		Document document = parser.build(new FileInputStream(domainXml));

		Element rootElement = document.getRootElement();
		ElementReader dr = new ElementReader(rootElement);
		long version = dr.getVersion();
		if (version == -1)
		{
			Logger.getLogger(this.getClass()).info(
					String.format("find domain.xml in old version: %s",
							domainXml.getCanonicalPath()));
			// upgrade it
			updateDomainXml12To121(domainXml);
		}

		return new DomainReader121(domainRoot);
	}

	public Domain loadDomain() throws Exception
	{
		// load from domainRoot
		DomainReader domainReader = getDomainReader(_domainRoot);
		domainReader.onCatalogBuild(_catalog, _context);

		Domain domain = domainReader.getDomain();
		Logger.getLogger(this.getClass()).debug(
				String.format("loading domain: %s(%s)", domain.getName(),
						domain.getTitle()));

		// datasets
		File[] dataSetDirs = _domainRoot.listFiles(new FileFilter()
		{
			public boolean accept(File file)
			{
				if (file.isDirectory())
				{
					String fn = file.getName().toLowerCase();

					if (fn.length() > 0 && Character.isLetter(fn.charAt(0)))
					{
						// Ѱ��schema.xml
						File dsXml = new File(file, "schema.xml");
						if (dsXml.exists())
							return true;
					}
				}

				return false;
			}
		});

		List<DataSetReader> dataSetReaders = new ArrayList<DataSetReader>();

		for (File dataSetDir : dataSetDirs)
		{
			DataSetReader dataSetReader = getDataSetReader(domain, dataSetDir);
			dataSetReader.onCatalogBuild(_catalog, _context);

			dataSetReaders.add(dataSetReader);
			domain.addDataSet(dataSetReader.getDataSet());
		}

		domainReader.afterCatalogBuild(_catalog, _context);
		for (DataSetReader dataSetReader : dataSetReaders)
		{
			dataSetReader.afterCatalogBuild(_catalog, _context);
			DataSet ds = dataSetReader.getDataSet();
			Logger.getLogger(this.getClass()).debug(
					String.format("loading dataset: %s(%s)", ds.getUri(), ds
							.getTitle()));
		}

		return domain;
	}

	private void saveAs(File domainXml, Document document, Element rootElement)
			throws FileNotFoundException, IOException
	{
		// version
		rootElement.removeChild("version");
		Element ve = new Element("version");
		rootElement.addContent(0, ve.setText("1.2.1"));

		Format format = Format.getPrettyFormat();
		format.setEncoding("gbk");
		format.setIndent("\t");
		XMLOutputter out = new XMLOutputter(format);
		FileOutputStream fos = new FileOutputStream(domainXml);
		out.output(document, fos);
		fos.close();
	}

	public void saveDataSet(DataSet dataSet) throws Exception
	{
		DataSetWriter dsWriter = new DataSetWriter(new File(_domainRoot,
				dataSet.getUri()));
		dsWriter.write(dataSet);
	}

	public void saveDomain(Domain domain) throws Exception
	{
		DomainWriter domainWriter = new DomainWriter(_domainRoot);
		domainWriter.write(domain);
	}

	public void setContext(CatalogContext context)
	{
		_context = context;
	}

	private void updateDomainXml12To121(File domainXml) throws JDOMException,
			IOException
	{
		SAXBuilder parser = new SAXBuilder();
		Document document = parser.build(new FileInputStream(domainXml));
		Element rootElement = document.getRootElement();

		// :/views/view-->/view
		Element viewsNode = (Element) XPath.selectSingleNode(rootElement,
				"views");
		if (viewsNode != null)
		{
			viewsNode.removeChild("id");
			viewsNode.removeChild("lastModified");
			rootElement.addContent(viewsNode.cloneContent());
			rootElement.removeChildren("views");
		}

		saveAs(domainXml, document, rootElement);
	}

	private void updateSchemaXml12To121(File schemaXml)
			throws FileNotFoundException, JDOMException, IOException
	{
		SAXBuilder parser = new SAXBuilder();
		Document document = parser.build(new FileInputStream(schemaXml));
		Element rootElement = document.getRootElement();

		// :/views/view-->/view
		Element viewsNode = (Element) XPath.selectSingleNode(rootElement,
				"views");
		if (viewsNode != null)
		{
			viewsNode.removeChild("id");
			viewsNode.removeChild("lastModified");
			rootElement.addContent(viewsNode.cloneContent());
			rootElement.removeChildren("views");
		}

		List<Element> nodes;
		// :item
		nodes = XPath.selectNodes(rootElement, "//item");
		for (Element item : nodes)
		{
			// oh! <source> node exists
			if (item.getChild("source") != null)
				continue;

			String source = item.getTextNormalize();
			item.removeContent();
			Element se = new Element("source");
			se.setText(source);
			item.addContent(se);
		}

		// foreignKey-->relationKey
		nodes = XPath.selectNodes(rootElement, "//field/foreignKey");
		for (Element item : nodes)
		{
			item.setName("relationKey");
		}

		// :relation/foreignKey-->key
		nodes = XPath.selectNodes(rootElement, "//relation/foreignKey");
		for (Element item : nodes)
		{
			item.setName("key");
		}

		saveAs(schemaXml, document, rootElement);
	}

	/**
	 * remove an entity object
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity)
	{
		DataSet dataSet = entity.getDataSet();
		dataSet.removeEntity(entity);

		for (Relation relation : dataSet.getRelations())
		{
			if (relation.getKeyA().getTarget() == entity
					|| relation.getKeyB().getTarget() == entity)
			{
				dataSet.removeRelation(relation);
			}
		}

		unregisterEntity(entity);
	}
}
