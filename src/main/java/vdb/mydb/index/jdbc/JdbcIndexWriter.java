package vdb.mydb.index.jdbc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.index.IndexWriter;
import vdb.mydb.index.util.IndexUtils;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.util.StringKeyMap;

public class JdbcIndexWriter implements IndexWriter
{
	private JdbcSource _jdbcSource;

	public JdbcIndexWriter(JdbcIndexer indexer) throws Exception
	{
		_jdbcSource = indexer.getJdbcSource();
	}

	public void clear() throws Exception
	{
		delete();
	}

	private void delete() throws Exception
	{
		_jdbcSource.deleteRecords("INDEX_TAB", null);
	}

	// 保留方法，删除某个实体的某条记录的索引
	// private void delete(AnyBean bean) throws Exception
	// {
	// _jdbcSource.deleteRecords("INDEX_TAB", new StringSql(
	// "ENTITYID=? and RECORDID=?", bean.getEntity().getId(), bean
	// .getId().getValue()));
	// }
	//
	// 保留方法，删除某个数据集的所有索引
	// private void delete(DataSet dataset) throws Exception
	// {
	// _jdbcSource.deleteRecords("INDEX_TAB", new StringSql("DATASETID=?",
	// dataset.getId()));
	// }
	//
	// 保留方法，删除某个实体的所有索引
	// private void delete(Entity entity) throws Exception
	// {
	// _jdbcSource.deleteRecords("INDEX_TAB", new StringSql("ENTITYID=? ",
	// entity.getId()));
	// }

	public void update() throws Exception
	{
		clear();

		for (DataSet dataset : VdbManager.getEngine().getDomain().getDataSets())
		{
			if (JdbcSourceManager.getInstance().getJdbcSource(dataset) == null)
				continue;
			try
			{
				update(dataset);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}

	private void update(DataSet dataset) throws Exception
	{
		for (Entity entity : dataset.getEntities())
		{
			try
			{
				update(entity);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
		}
	}

	private void update(Entity entity) throws Exception
	{
		// 得到索引记录
		VarQuery query = new AnyBeanDao(entity).createQuery();
		Field[] fields = ((VdbEntity) entity).getView("indexEntity").getItems(
				new Field[0]);

		if (fields.length > 0)
		{
			// 对每一条记录进行索引
			QueryExecutor qe = new AnyBeanDao(entity).execute(query);

			int total = qe.size();
			int i = 1;
			while (i <= total)
			{
				int size = 10;
				List<AnyBean> list = qe.list(i, size);
				for (AnyBean bean : list)
				{
					try
					{
						write(bean, fields);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						continue;
					}
				}

				i += size;
				list = null;
			}
		}

	}

	private void write(AnyBean bean, Field[] fields) throws Exception
	{
		Map<String, Serializable> columns = new StringKeyMap<Serializable>();
		columns.put("DATASETID", bean.getEntity().getDataSet().getId());
		columns.put("ENTITYID", bean.getEntity().getId());
		columns.put("INDEXTEXT", IndexUtils.getFullText(bean, fields));
		columns.put("RECORDID", bean.getId().getValue());
		columns.put("INDEXTIME", Calendar.getInstance());
		columns.put("TITLE", bean.getTitle().getTitle());

		_jdbcSource.insertRecord("INDEX_TAB", columns);
	}
}
