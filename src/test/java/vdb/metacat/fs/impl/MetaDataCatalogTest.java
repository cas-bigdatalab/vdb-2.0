package vdb.metacat.fs.impl;

import java.io.File;
import java.io.Serializable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import vdb.metacat.Catalog;
import vdb.metacat.CatalogObject;
import vdb.metacat.DataSet;
import vdb.metacat.Domain;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.Query;
import vdb.metacat.Repository;
import vdb.metacat.View;
import vdb.metacat.ctx.AbstractCatalogContext;
import vdb.metacat.ctx.CatalogContext;
import vdb.metacat.fs.CatalogImpl;
import vdb.metacat.fs.CatalogManager;
import vdb.metacat.fs.RepositoryImpl;
import vdb.metacat.idgen.IdGenerator;
import vdb.metacat.idgen.IdGeneratorContext;
import vdb.metacat.meta.ValuesGetter;
import vdb.mydb.metacat.VdbDataSet;
import vdb.mydb.metacat.VdbDomain;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.metacat.VdbIdentifier;
import vdb.mydb.metacat.VdbQuery;
import vdb.mydb.metacat.VdbRelation;
import vdb.mydb.metacat.VdbRelationKey;
import vdb.mydb.metacat.VdbView;
import vdb.mydb.typelib.FieldType;
import vdb.mydb.typelib.type.VdbStringType;
import cn.csdb.commons.util.FileUtils;

public class MetaDataCatalogTest
{
	private static final String E_TEST_VDB = "e:\\test\\vdb";

	private Catalog _catalog;

	private CatalogContext _context;

	private Domain _domain;

	private CatalogManager _manager;

	private DataSet createDataSet(String name, String title)
	{
		DataSet dataSet = new VdbDataSet();
		dataSet.setUri(name);
		dataSet.setTitle(title);
		return dataSet;
	}

	private Entity createEntity(DataSet dataSet, String name, String title,
			String tableName)
	{
		Entity entity = new VdbEntity();
		entity.setDataSet(dataSet);
		entity.setName(name);
		entity.setTitle(title);
		entity.setTableName(tableName);
		dataSet.addEntity(entity);

		return entity;
	}

	private Field createField(Entity entity, String name, String title,
			String columnName, String typeName)
	{
		Field field = new VdbField();
		field.setEntity(entity);
		field.setName(name);
		field.setTitle(title);
		field.setColumnName(columnName);
		field.setType(_context.getFieldType(typeName));

		entity.addField(field);

		return field;
	}

	private Query createQuery(DataSet dataSet, String name, String title,
			String sql, boolean isSql)
	{
		Query query = new VdbQuery();
		query.setDataSet(dataSet);
		query.setName(name);
		query.setTitle(title);
		if (isSql)
		{
			query.setSql(sql);
		}
		else
		{
			query.setOql(sql);
		}

		dataSet.addQuery(query);
		return query;
	}

	private Repository createRepository(DataSet dataSet, String host)
			throws Exception
	{
		Repository repository = this._context.create("repository");
		repository.setDataSet(dataSet);
		dataSet.setRepository(repository);
		repository.setHost(host);

		return repository;
	}

	private View createView(DataSet dataSet, CatalogObject source,
			String viewName, CatalogObject... items)
	{
		View view = new VdbView();
		view.setSource(source);
		view.setName(viewName);
		for (CatalogObject item : items)
		{
			view.addItem(item);
		}

		dataSet.addView(view);
		return view;
	}

	private DataSet getTestDataSet()
	{
		return _catalog.fromUri("cn.csdb.vdb.bookstore");
	}

	private Identifier setIdentifier(Field id, String generatorName)
			throws Exception
	{
		Identifier identifier = id.getEntity().getIdentifier();
		if (identifier == null)
		{
			identifier = this._context.create("identifier");
			id.getEntity().setIdentifier(identifier);
		}

		identifier.setField(id);
		identifier.setIdGenerator(_context.getIdGenerator(generatorName));

		return identifier;
	}

	@Before
	public void init() throws Exception
	{
		FileUtils.rmdirs(new File(E_TEST_VDB));

		AbstractCatalogContext context = new AbstractCatalogContext()
		{
			public ValuesGetter<String, String> getDefaults(String className)
			{
				return null;
			}

			public FieldType getFieldType(String name)
			{
				FieldType fieldTypeDef = new FieldType();
				fieldTypeDef.setName(name);
				try
				{
					fieldTypeDef.setFieldClass(VdbStringType.class);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				return fieldTypeDef;
			}

			public IdGenerator getIdGenerator(final String name)
			{
				return new IdGenerator()
				{

					public boolean accepts(Field field) throws Exception
					{
						return true;
					}

					public Serializable generateId(IdGeneratorContext context)
							throws Exception
					{
						return null;
					}

					public String getName()
					{
						return name;
					}

					public String getTitle()
					{
						return null;
					}
				};
			}
		};

		context.registerClass("domain", VdbDomain.class);
		context.registerClass("dataset", VdbDataSet.class);
		context.registerClass("entity", VdbEntity.class);
		context.registerClass("repository", RepositoryImpl.class);
		context.registerClass("relation", VdbRelation.class);
		context.registerClass("identifier", VdbIdentifier.class);
		context.registerClass("relationKey", VdbRelationKey.class);
		context.registerClass("view", VdbView.class);
		context.registerClass("field", VdbField.class);
		context.registerClass("query", VdbQuery.class);

		_context = context;
		_catalog = new CatalogImpl();
		_manager = new CatalogManager(new File(E_TEST_VDB), _catalog, _context);

		// domain
		Domain domain = new VdbDomain();
		domain.setName("cn.csdb.vdb");
		domain.setTitle("我的本地域");

		// dataset
		DataSet bookStore = createDataSet("cn.csdb.vdb.bookstore", "网上书店");
		createRepository(bookStore, "localhost");

		// entity: book
		Entity book = createEntity(bookStore, "book", "图书信息", "BOOKS");
		Entity author = createEntity(bookStore, "author", "作者信息", "AUTHORS");
		Entity order = createEntity(bookStore, "order", "订单", "ORDERS");
		Entity detail = createEntity(bookStore, "details", "订单明细", "DETAILS");

		// field: id
		Field bookId = createField(book, "id", "图书ID", "ID", "Long");
		// field: name
		Field bookName = createField(book, "name", "图书名字", "ID", "String");

		setIdentifier(bookId, "autoinc");

		Field authorId = createField(author, "id", "作者ID", "ID", "Long");
		setIdentifier(authorId, "autoinc");
		Field orderId = createField(order, "id", "订单ID", "ID", "Long");
		setIdentifier(orderId, "autoinc");
		Field detailId = createField(detail, "id", "订单明细ID", "ID", "Long");
		setIdentifier(detailId, "autoinc");

		createQuery(bookStore, "query1", "男作者",
				"select * from AUTHORS where GENDER='female'", true);
		createQuery(
				bookStore,
				"query3",
				"女作者",
				"$query.where($query.and($query.lt('price', 50), $query.notNull('title')))",
				false);

		// views
		createView(bookStore, bookStore, "indexEntity", book);
		createView(bookStore, book, "listBeans", bookId, bookName);

		// bookAuthors.setRelationKey(relation2.getKeyA());

		_manager.saveDataSet(bookStore);

		domain.addDataSet(bookStore);

		View mv = new VdbView();
		mv.setSource(domain);
		mv.setName("listDataSets");
		mv.addItem(bookStore);

		domain.addView(mv);

		_manager.saveDomain(domain);
		_domain = _manager.loadDomain();

		_manager.saveDomain(_domain);
		_catalog.clear();
		_domain = _manager.loadDomain();

	}

	@Test
	public void testCreateDataSet() throws Exception
	{
		DataSet[] dss1 = _domain.getDataSets();

		DataSet ds = new VdbDataSet();
		ds.setUri("cn.csdb.test.yahoo");
		_domain.addDataSet(ds);
		_manager.saveDataSet(ds);

		_catalog.clear();
		_domain = _manager.loadDomain();
		DataSet[] dss2 = _domain.getDataSets();
		Assert.assertEquals(dss1.length + 1, dss2.length);
	}

	@Test
	public void testDeleteDataSet() throws Exception
	{
		testCreateDataSet();

		DataSet ds = _catalog.fromUri("cn.csdb.test.yahoo");

		DataSet[] dss1 = _domain.getDataSets();
		_manager.deleteDataSet(_domain, ds);
		DataSet[] dss2 = _domain.getDataSets();
		Assert.assertEquals(dss1.length - 1, dss2.length);
		Assert.assertNull(_catalog.fromUri("cn.csdb.test.yahoo"));
	}

	@Test
	public void testGetDataSet() throws Exception
	{
		DataSet ds = getTestDataSet();
		Assert.assertEquals("cn.csdb.vdb.bookstore", ds.getUri());
		_manager.saveDataSet(ds);
	}

	@Test
	public void testGetDomain()
	{
		Assert.assertEquals("cn.csdb.vdb", _domain.getUri());

		View[] vs = _domain.getViews();
		Assert.assertEquals(1, vs.length);
		View vs0 = vs[0];
		Assert.assertEquals(_domain, vs0.getSource());
		Assert.assertEquals("listDataSets", vs0.getName());

		CatalogObject[] cos = vs0.getItems();
		Assert.assertEquals(1, cos.length);
		DataSet cos0 = (DataSet) cos[0];
		Assert.assertEquals(getTestDataSet(), cos0);
	}

	@Test
	public void testSaveDomain() throws Exception
	{
		DataSet ds = getTestDataSet();
		_domain.addDataSet(ds);
		_manager.saveDomain(_domain);
	}
}
