package vdb.mydb.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.QueryFromSql;
import vdb.mydb.query.VarQuery;
import vdb.mydb.query.impl.AnyQueryImpl;
import vdb.mydb.query.impl.QueryExecutorImpl;
import vdb.mydb.query.impl.QueryFromSqlImpl;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.VdbFieldDriver;
import vdb.mydb.typelib.data.VdbCollection;
import cn.csdb.commons.orm.OrMapping;
import cn.csdb.commons.orm.Persistor;
import cn.csdb.commons.orm.SimplePersistor;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

@SuppressWarnings("unchecked")
public class AnyBeanDao
{
	class AnyBeanMapping implements OrMapping
	{
		public void bean2Map(Object bean, Map<String, Serializable> map,
				boolean onInsert) throws Exception
		{
			JdbcRowImpl jdbcRow = new JdbcRowImpl(_entity, (AnyBean) bean, map);

			for (Field field : _entity.getFields())
			{
				VdbData data = ((AnyBean) bean).get(field);
				VdbFieldDriver<VdbData> driver = field.getTypeDriver();

				if (onInsert)
				{
					driver.jdbcInsert(jdbcRow, data);
				}
				else
				{
					driver.jdbcUpdate(jdbcRow, data, null);
				}
			}
		}

		public DataSource getDataSource() throws Exception
		{
			return _sqlSource.getDataSource();
		}

		public StringSql getIdFilter(Serializable id)
		{
			return new StringSql(_entity.getIdentifier().getField()
					.getColumnName()
					+ "=?", id);
		}

		public Serializable getPrimaryKeyValue(Object bean) throws Exception
		{
			return ((AnyBean) bean).getId().getValue();
		}

		public String getTableName()
		{
			return _entity.getTableName();
		}

		public Object map2Bean(Map<String, Serializable> map) throws Exception
		{
			AnyBeanImpl bean = new AnyBeanImpl(_entity);
			JdbcRowImpl jdbcRow = new JdbcRowImpl(_entity, (AnyBean) bean, map);

			for (Field field : _entity.getFields())
			{
				try
				{
					VdbFieldDriver<VdbData> driver = field.getTypeDriver();
					VdbData data = driver.jdbcSelect(jdbcRow);

					((AnyBean) bean).set(field, data);
				}
				catch (Exception e)
				{
					Exception e2 = new Exception(String.format(
							"failed to mapping field `%s` to `%s`", field
									.getUri(), field.getTypeName()), e);
					e2.printStackTrace();
				}
			}

			return bean;
		}

		public void setGeneratedKey(Object bean, String key, Serializable value)
				throws Exception
		{
			Field pk = _entity.getIdentifier().getField();
			if (key.equalsIgnoreCase(pk.getColumnName()))
			{
				((AnyBean) bean).setId(value);
			}
		}
	}

	private Entity _entity;

	private Persistor _persistor;

	private AnyBeanMapping _rule;

	private JdbcSource _sqlSource;

	public AnyBeanDao(Entity entity) throws Exception
	{
		_entity = entity;
		DataSet dataSet = _entity.getDataSet();
		synchronized (dataSet)
		{
			_sqlSource = JdbcSourceManager.getInstance().getJdbcSource(dataSet);
		}

		_rule = new AnyBeanMapping();
		_persistor = new SimplePersistor(_rule);
	}

	public AnyBeanDao(DataSet dataSet) throws Exception
	{
		synchronized (dataSet)
		{
			_sqlSource = JdbcSourceManager.getInstance().getJdbcSource(dataSet);
		}
	}

	public AnyBean create() throws Exception
	{
		return new AnyBeanImpl(_entity);
	}

	public AnyQuery createQuery() throws Exception
	{
		return new AnyQueryImpl(_entity);
	}

	public QueryFromSql createQueryFromSql() throws Exception
	{
		return new QueryFromSqlImpl(_entity);
	}

	public int delete(AnyBean bean) throws Exception
	{
		JdbcRowImpl jdbcRow = new JdbcRowImpl(_entity, (AnyBean) bean, null);

		for (Field field : _entity.getFields())
		{
			VdbData data = ((AnyBean) bean).get(field);
			VdbFieldDriver<VdbData> driver = field.getTypeDriver();
			driver.jdbcDelete(jdbcRow, data);
		}

		int rows = _persistor.delete(bean);
		if (rows > 0)
		{
			removeFromCache(bean);
		}
		return rows;
	}

	public void removeFromCache(AnyBean bean) throws Exception
	{
		getCache().remove((Serializable) getPrimaryKeyValue(bean));
	}

	public QueryExecutor execute(VarQuery query) throws Exception
	{
		return new QueryExecutorImpl(_persistor, query);
	}

	private Cache getCache() throws CacheException
	{
		return VdbManager.getEngine().getCache(_entity.getId());
	}

	public JdbcSource getJdbcSource()
	{
		return _sqlSource;
	}

	private Serializable getPrimaryKeyValue(AnyBean bean) throws Exception
	{
		return bean.getId().getValue();
	}

	public int insert(AnyBean bean) throws Exception
	{
		int rows = _persistor.insert(bean);
		if (rows > 0)
		{
			Object key = getPrimaryKeyValue(bean);
			getCache()
					.put(new Element((Serializable) key, (Serializable) bean));
		}

		return rows;
	}

	public AnyBean lookup(Serializable id) throws Exception
	{
		Cache cache = getCache();

		Element element = (Element) cache.get(id);
		if (element == null)
		{
			Object o = _persistor.lookup(id);
			if (o != null)
			{
				element = new Element(id, (Serializable) o);
				cache.put(element);
				/** ********** BEGIN LOG ***************** */
				// logQueryFromJdbc(element);
				/** ********** BEGIN LOG ***************** */
			}
			else
			{
				return null;
			}
		}
		else
		{
			/** ********** BEGIN LOG ***************** */
			// logQueryFromCache(element);
			/** ********** BEGIN LOG ***************** */
		}

		return (AnyBean) element.getValue();
	}

	public int update(AnyBean bean) throws Exception
	{
		int rows = _persistor.update(bean);
		return rows;
	}

	private void visitRecord(AnyBean bean, BeanTreeVisitor visitor,
			boolean visitChildren) throws Exception
	{
		Entity table = bean.getEntity();
		if (visitor.beforeBeanVisit(bean) && visitChildren)
		{
			for (Field field : table.getFields())
			{
				if (field.isCollection())
				{
					List<AnyBean> drs = ((VdbCollection) bean.get(field))
							.list();

					Entity target = field.getRelationKey().getTarget();

					if (drs.size() > 0
							&& visitor.beforeCollectionVisit(target, field))
					{
						for (int j = 0; j < drs.size(); j++)
						{
							AnyBean any2 = (AnyBean) drs.get(j);
							visitRecord(any2, visitor, field
									.isStrongCollection());
						}

						visitor.afterCollectionVisit(target, field);

					}
				}
			}
		}

		visitor.afterBeanVisit(bean);
	}

	/**
	 * @throws Exception
	 */
	public void visitRecords(String ids, BeanTreeVisitor visitor)
			throws Exception
	{
		StringTokenizer st = new StringTokenizer(ids, ";");
		if (st.hasMoreTokens() && visitor.beforeCollectionVisit(_entity, null))
		{
			while (st.hasMoreTokens())
			{
				AnyBeanDao dao = new AnyBeanDao(_entity);
				AnyBean bean = dao.lookup(st.nextToken());
				visitRecord(bean, visitor, true);
			}
		}
	}
}
