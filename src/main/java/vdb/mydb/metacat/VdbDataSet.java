package vdb.mydb.metacat;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletRequest;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.HasNoNameExeption;
import vdb.metacat.Query;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.metacat.Repository;
import vdb.metacat.View;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.util.StringUtils;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.catalog.JdbcDatabase;
import cn.csdb.commons.sql.catalog.JdbcTable;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.ListMap;
import cn.csdb.commons.util.StringKeyMap;

public class VdbDataSet extends CatalogObjectImpl implements DataSet
{
	protected ListMap<String, Entity> _entities = new ListMap<String, Entity>(
			new StringKeyMap<Entity>());

	protected ListMap<String, Query> _queries = new ListMap<String, Query>(
			new StringKeyMap<Query>());

	protected ListMap<String, Relation> _relations = new ListMap<String, Relation>(
			new StringKeyMap<Relation>());

	protected Repository _repository;

	List<View> _views = new ArrayList<View>();

	public void addEntity(Entity entity)
	{
		String name = entity.getName();
		if (name == null)
			throw new HasNoNameExeption(entity);

		_entities.add(name, entity);
	}

	public void addQuery(Query query)
	{
		String name = query.getName();
		if (name == null)
			throw new HasNoNameExeption(query);

		_queries.add(name, query);
	}

	public void addRelation(Relation relation)
	{
		_relations.add(relation.getId(), relation);
	}

	public void addView(View view)
	{
		_views.add(view);
	}

	public void attach(ServletRequest request) throws Exception
	{
		new CatalogObjectProxy(this).attach(request);
	}

	public Entity createEntityFromTable(String tableName) throws Exception
	{
		VdbEntity table = new VdbEntity();
		table.setDataSet(this);
		table.setName(StringUtils.formatWords(tableName));
		table.setTableName(tableName);
		table.setTitle(StringUtils.formatWords2(tableName));

		return table;
	}

	public Query createQueryFromName(String queryName) throws Exception
	{
		VdbQuery query = new VdbQuery();
		query.setDataSet(this);
		query.setName(StringUtils.formatWords(queryName));
		query.setName(queryName);
		query.setTitle(StringUtils.formatWords2(queryName));

		return query;
	}

	public File getDataSetXml() throws IOException
	{
		return new File(new File(getRootPath()), "schema.xml");
	}

	public View getDefaultListView(String viewName)
	{
		VdbView view = new VdbView();
		view.setSource(this);
		view.setName(viewName);

		for (Entity entity : this.getEntities())
		{
			if (entity.getReferenceKeys().length > 0)
				continue;

			view.addItem(entity);
		}

		for (Query query : this.getQueries())
		{
			view.addItem(query);
		}

		return view;
	}

	public View getDefaultView(String viewName)
	{
		if ("listEntities".equalsIgnoreCase(viewName))
			return getDefaultListView(viewName);

		VdbView view = new VdbView();
		view.setName(viewName);
		view.setSource(this);

		for (Entity entity : this.getEntities())
		{
			if (entity.getReferenceKeys().length > 0)
				continue;

			view.addItem(entity);
		}

		return view;
	}

	public Entity[] getEntities()
	{
		return _entities.list().toArray(new Entity[0]);
	}

	/**
	 * @deprecated use getEntities().length instead
	 * @return
	 */
	public int getEntityCount()
	{
		return _entities.list().size();
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbDataSet getEx()
	{
		return this;
	}

	public JdbcDatabase getJdbcDatabase() throws Exception
	{
		return getJdbcSource().getJdbcCatalog().getDatabase();
	}

	public JdbcSource getJdbcSource()
	{
		try
		{
			return JdbcSourceManager.getInstance().getJdbcSource(this);
		}
		catch (Throwable e)
		{
			return null;
		}
	}

	public Long getMaxValue(Entity table) throws Exception
	{
		return getJdbcSource().queryForObject(
				new StringSql(String.format(
						"select %s from %s order by %s desc", table
								.getIdentifier().getField().getColumnName(),
						table.getName(), table.getIdentifier().getField()
								.getColumnName())), new ResultSetReader<Long>()
				{

					public Long read(ResultSet rs, int row) throws SQLException
					{
						return rs.getLong(1);
					}
				});

	}

	@Override
	public String getName()
	{
		return getUri();
	}

	public Query[] getQueries()
	{
		return _queries.list().toArray(new Query[0]);
	}

	/**
	 * @deprecated use getRelations() instead
	 */
	public List<Relation> getRelationDefinitions()
	{
		return _relations.list();
	}

	public Relation[] getRelations()
	{
		return _relations.list().toArray(new Relation[0]);
	}

	public Repository getRepository()
	{
		return _repository;
	}

	public String getRootPath() throws IOException
	{
		return VdbManager.getInstance().getDataSetRoot(this).getCanonicalPath();
	}

	public File getSchemaXml() throws IOException
	{
		return new File(getRootPath(), "schema.xml");
	}

	public String getTitle()
	{
		return get("title");
	}

	public List<JdbcTable> getUntitledTables() throws Exception
	{
		JdbcDatabase jdbcDatabase = getJdbcDatabase();
		List tables = new ArrayList(jdbcDatabase.getTables());
		for (Entity table : this.getEntities())
		{
			JdbcTable jdbcTable = jdbcDatabase.getTable(table.getTableName());
			if (jdbcTable != null)
				tables.remove(jdbcTable);
		}

		return tables;
	}

	@Override
	public String getUri()
	{
		return get("uri");
	}

	public View getView(String viewName)
	{
		View view = new ViewsProxy(this.getViews()).getView(this, viewName);
		if (view == null)
		{
			view = getDefaultView(viewName);
		}

		return view;
	}

	public View[] getViews()
	{
		return _views.toArray(new View[0]);
	}

	public boolean ifTableExists(String tableName) throws Exception
	{
		return getJdbcSource().getJdbcCatalog().getDatabase().getTable(
				tableName) != null;
	}

	public void linkRecord(RelationKey link, String id1, String ids)
			throws Exception
	{
		StringTokenizer st = new StringTokenizer(ids, ";");
		while (st.hasMoreTokens())
		{
			String id2 = st.nextToken();
			unlinkRecord(link, id1, id2);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put(link.getColumnName(), id1);
			map.put(link.getPeer().getColumnName(), id2);
			getJdbcSource().insertRecord(
					link.getRelation().getAssocTableName(), map);
		}
	}

	public String quote(String id) throws Exception
	{
		return getJdbcSource().getQuotedIdentifier(id);
	}

	public void removeEntity(Entity entity)
	{
		String name = entity.getName();
		if (name == null)
			throw new HasNoNameExeption(entity);

		_entities.remove(name);
	}

	public void removeQuery(Query query)
	{
		_queries.remove(query.getName());
	}

	public void removeRelation(Relation relation)
	{
		_relations.remove(relation.getId());
	}

	public void removeView(View view)
	{
		_views.remove(view);
	}

	public void setName(String uri)
	{
		setUri(uri);
	}

	public void setRepository(Repository repository)
	{
		_repository = repository;
	}

	public void setTitle(String title)
	{
		set("title", title);
	}

	public void setUri(String uri)
	{
		set("uri", uri);
	}

	public void unlinkRecord(RelationKey link, Serializable id1)
			throws Exception
	{
		getJdbcSource().deleteRecords(
				link.getRelation().getAssocTableName(),
				new StringSql(String.format("%s=? ", link.getColumnName(), link
						.getPeer().getColumnName()), id1));

	}

	public void unlinkRecord(RelationKey link, String id1, String ids2)
			throws Exception
	{
		StringTokenizer st = new StringTokenizer(ids2, ";");
		while (st.hasMoreTokens())
		{
			String id2 = st.nextToken();
			getJdbcSource().deleteRecords(
					link.getRelation().getAssocTableName(),
					new StringSql(String.format("%s=? and %s=?", link
							.getColumnName(), link.getPeer().getColumnName()),
							id1, id2));

		}
	}

	public void unlinkRecords(RelationKey link, String id1) throws Exception
	{
		getJdbcSource()
				.deleteRecords(
						link.getRelation().getAssocTableName(),
						new StringSql(String.format("%s=?", link
								.getColumnName()), id1));
	}
}
