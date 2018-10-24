/*
 * Created on 2006-1-24
 */
package cn.csdb.commons.orm.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import cn.csdb.commons.orm.OrMapping;
import cn.csdb.commons.orm.Persistor;
import cn.csdb.commons.orm.Query;
import cn.csdb.commons.orm.bean.BeanContext;
import cn.csdb.commons.orm.bean.SessionBean;
import cn.csdb.commons.orm.handler.AfterSelect;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.SelectSql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/*
 * @author bluejoe
 */
public class BeanQueryImpl implements Query
{
	private Persistor _persistor;

	private JdbcSource _sqlSource;

	private OrMapping _mapping;

	private QuerySql _sql;

	private SelectSql _selectSql = new SelectSql();

	public BeanQueryImpl(JdbcSource sqlSource, Persistor persistor,
			OrMapping mapping)
	{
		_sqlSource = sqlSource;
		_persistor = persistor;
		_mapping = mapping;

		reset();
	}

	public int size() throws Exception
	{
		return _sqlSource.countRecords(_sql);
	}

	public Persistor getPersistor()
	{
		return _persistor;
	}

	public List list() throws Exception
	{
		return list(1, -1);
	}

	private Object map2Bean(Map<String, Serializable> record) throws Exception
	{
		Object o = _mapping.map2Bean(record);

		if (o instanceof SessionBean)
		{
			BeanContext bc = new BeanContextImpl(_persistor, record);
			((SessionBean) o).setBeanContext(bc);
		}

		if (o instanceof AfterSelect)
		{
			((AfterSelect) o).onAfterSelect();
		}
		return o;
	}

	public Object single() throws Exception
	{
		Map<String, Serializable> map = _sqlSource.queryForObject(_sql);

		if (map == null)
			return null;

		return map2Bean(map);
	}

	public Query reset()
	{
		_selectSql.reset();
		_selectSql.setTableName(_mapping.getTableName());
		_sql = _selectSql;

		return this;
	}

	public Query setFilter(StringSql filter)
	{
		_selectSql.setFilter(filter);
		_sql = _selectSql;
		return this;
	}

	public Query setOrderBy(String orderBy)
	{
		_selectSql.setOrderBy(orderBy);
		_sql = _selectSql;
		return this;
	}

	public Query setTableName(String... tableNames)
	{
		_selectSql.setTableName(tableNames);
		_sql = _selectSql;
		return this;
	}

	public Query setSql(QuerySql fullSql)
	{
		_sql = fullSql;
		return this;
	}

	public List list(int beginning, int size) throws Exception
	{
		List beans = new Vector();
		List records = _sqlSource.queryForObjects(_sql, beginning, size);

		for (int i = 0; i < records.size(); i++)
		{
			Object o = map2Bean((Map) records.get(i));
			beans.add(o);
		}

		return beans;
	}

	public Query setField(String... fields)
	{
		_selectSql.setField(fields);
		_sql = _selectSql;
		return this;
	}
}
