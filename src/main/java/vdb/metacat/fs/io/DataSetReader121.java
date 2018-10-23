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

import vdb.metacat.Cardinality;
import vdb.metacat.Catalog;
import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.Query;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.metacat.Repository;
import vdb.metacat.View;
import vdb.metacat.ctx.CatalogContext;
import vdb.metacat.util.CatalogUtils;
import vdb.mydb.typelib.FieldType;

/*
 * read a data set from a directory
 * 
 * @author bluejoe
 */
public class DataSetReader121 implements DataSetReader
{
	private DataSet _dataSet;

	private File _dataSetDir;

	private Document _document;

	private Domain _domain;

	private List<ViewReader> _views = new ArrayList<ViewReader>();

	public DataSetReader121(Domain domain, File dataSetDir)
			throws FileNotFoundException, JDOMException, IOException
	{
		_domain = domain;
		_dataSetDir = dataSetDir;
		SAXBuilder parser = new SAXBuilder();
		_document = parser.build(new FileInputStream(new File(dataSetDir,
				"schema.xml")));
	}

	public void afterCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception
	{
		// entities
		for (Entity entity : _dataSet.getEntities())
		{
			// identifier
			Identifier identifier = entity.getIdentifier();
			Field primaryField = (Field) catalog.fromUri(identifier
					.get("field"));
			identifier.setField(primaryField);
			identifier.setIdGenerator(context.getIdGenerator(identifier
					.get("idGenerator")));

			Field titleField = (Field) catalog
					.fromUri(entity.get("titleField"));
			entity.setTitleField(titleField);
			entity.setEditorField((Field) catalog.fromUri(entity
					.get("editorField")));
			entity.setTimeStampField((Field) catalog.fromUri(entity
					.get("timeStampField")));
			entity.setOrderField((Field) catalog.fromUri(entity
					.get("orderField")));
			entity.setGroupField((Field) catalog.fromUri(entity
					.get("groupField")));

			// field
			for (Field field : entity.getFields())
			{
				FieldType fieldType = context.getFieldType(field.get("type"));
				// FieldType
				field.setType(fieldType);
				// TypeDriver
				try
				{
					field.setTypeDriver(fieldType.createDriver(field));
				}
				catch (Exception e)
				{
				}

				field.setRelationKey((RelationKey) catalog.fromId(field
						.get("relationKey")));
			}
		}

		// queries
		for (Query query : _dataSet.getQueries())
		{
			query.setEntity((Entity) catalog.fromUri(query.get("entity")));
		}

		// relations
		for (Relation relation : _dataSet.getRelations())
		{
			// keys
			RelationKey a = relation.getKeyA();
			a.setTarget((Entity) catalog.fromUri(a.get("target")));

			RelationKey b = relation.getKeyB();
			b.setTarget((Entity) catalog.fromUri(b.get("target")));

			if (relation.getCardinality().isOneToMany())
			{
				// order:detail
				// a.target->detail, b.target->order
				a.getTarget().addReferenceKey(b);
				b.getTarget().addCollectionKey(a);
			}

			if (relation.getCardinality().isManyToMany())
			{
				// author:book
				// _a.getTarget():author
				// _a.getColumnName():AUTHOR_ID
				// _b.getTarget():book
				// _b.getColumnName():BOOK_ID
				a.getTarget().addCollectionKey(b);
				b.getTarget().addCollectionKey(a);
			}
		}

		// data set views
		for (ViewReader viewReader : _views)
		{
			viewReader.afterCatalogBuild(catalog, context);
			if (viewReader.getView().getSource() == null)
			{
				_dataSet.removeView(viewReader.getView());
			}
		}
	}

	public DataSet getDataSet()
	{
		return _dataSet;
	}

	public void onCatalogBuild(Catalog catalog, CatalogContext context)
			throws Exception
	{
		_dataSet = context.create("dataset");

		Element rootElement = _document.getRootElement();
		ElementReader dr = new ElementReader(rootElement);
		_dataSet.meta().putAll(dr.readAttributes());

		// repository
		{
			CatalogUtils.createRepository(context, _dataSet);
			Repository repository = _dataSet.getRepository();

			Element re = dr.selectChild("repository");
			if (re != null)
			{
				ElementReader ir = new ElementReader(re);
				repository.meta().putAll(ir.readAttributes());
			}

			catalog.cacheIn(repository);
		}

		// entities
		for (Element ee : dr.selectChildren("entity"))
		{
			ElementReader er = new ElementReader(ee);
			Entity entity = context.create("entity");
			entity.meta().putAll(er.readAttributes());
			entity.setDataSet(_dataSet);

			catalog.cacheIn(entity);
			_dataSet.addEntity(entity);

			// identifier
			CatalogUtils.createIdentifier(context, entity);
			Identifier identifier = entity.getIdentifier();
			catalog.cacheIn(identifier);

			Element ie = er.selectChild("identifier");
			if (ie != null)
			{
				ElementReader ir = new ElementReader(ie);
				identifier.meta().putAll(ir.readAttributes());
			}

			catalog.cacheIn(identifier);

			// fields
			for (Element fe : er.selectChildren("field"))
			{
				ElementReader fr = new ElementReader(fe);
				Field field = context.create("field");
				field.setEntity(entity);

				field.meta().putAll(fr.readAttributes());
				catalog.cacheIn(field);
				entity.addField(field);
			}

		}

		// queries
		for (Element ee : dr.selectChildren("query"))
		{
			ElementReader er = new ElementReader(ee);
			Query query = context.create("query");
			query.setDataSet(_dataSet);
			query.meta().putAll(er.readAttributes());
			catalog.cacheIn(query);
			_dataSet.addQuery(query);
		}

		// relations
		for (Element re : dr.selectChildren("relation"))
		{
			ElementReader rr = new ElementReader(re);
			Relation relation = context.create("relation");
			relation.setDataSet(_dataSet);
			relation.meta().putAll(rr.readAttributes());
			relation.setCardinality(Cardinality.forName(rr
					.readAttribute("cardinality")));
			catalog.cacheIn(relation);
			_dataSet.addRelation(relation);

			catalog.cacheIn(relation);

			// keys
			List<Element> keys = rr.selectChildren("key");
			if (keys.size() == 2)
			{
				CatalogUtils.createKeys(context, relation);
				RelationKey keyA = relation.getKeyA();
				RelationKey keyB = relation.getKeyB();

				keyA.meta().putAll(
						new ElementReader(keys.get(0)).readAttributes());
				keyA.setCardinality(relation.getCardinality());

				keyB.meta().putAll(
						new ElementReader(keys.get(1)).readAttributes());
				keyB.setCardinality(relation.getCardinality().inverse());

				catalog.cacheIn(keyA);
				catalog.cacheIn(keyB);
			}
		}

		// data set views
		for (Element e : dr.selectChildren("view"))
		{
			ViewReader viewReader = new ViewReader(_domain, e);
			viewReader.onCatalogBuild(catalog, context);
			View view = viewReader.getView();
			_dataSet.addView(view);
			_views.add(viewReader);
			catalog.cacheIn(view);
		}

		catalog.cacheIn(_dataSet);
	}
}
