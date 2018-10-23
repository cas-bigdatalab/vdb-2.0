package vdb.mydb.metacat;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Query;
import vdb.metacat.fs.CatalogObjectImpl;
import vdb.mydb.query.VarQuery;
import vdb.mydb.query.impl.AnyQueryImpl;
import vdb.mydb.query.impl.QueryFromSqlImpl;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class VdbQuery extends CatalogObjectImpl implements Query
{
	private DataSet _dataSet;

	private Entity _entity;

	public VarQuery createQuery()
	{
		if (_meta.contains("sql"))
		{
			QueryFromSqlImpl query = new QueryFromSqlImpl(getEntity());
			query.setSql(new StringSql(getSql()));
			return query;
		}

		AnyQueryImpl query = new AnyQueryImpl(getEntity());
		return query;
	}

	public DataSet getDataSet()
	{
		return _dataSet;
	}

	public Entity getEntity()
	{
		return _entity;
	}

	/**
	 * @deprecated
	 * @return
	 */
	public VdbQuery getEx()
	{
		return this;
	}

	public String getOql()
	{
		return get("oql");
	}

	public String getSql()
	{
		return get("sql");
	}

	public String getUri()
	{
		return _dataSet.getUri() + "." + getName();
	}

	public boolean isOqlQuery()
	{
		return getOql() != null;
	}

	public boolean isQuery()
	{
		return true;
	}

	public boolean isSqlQuery()
	{
		return getSql() != null;
	}

	public void setDataSet(DataSet dataSet)
	{
		_dataSet = dataSet;
	}

	public void setEntity(Entity entity)
	{
		_entity = entity;
	}

	public void setOql(String oql)
	{
		set("sql", null);
		set("oql", oql);
	}

	public void setSql(String sql)
	{
		set("sql", sql);
		set("oql", null);
	}
}
