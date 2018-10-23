package vdb.mydb.index.db4o;

import java.util.Date;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.index.IndexWriter;
import vdb.mydb.index.Session;
import vdb.mydb.index.util.IndexUtils;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;

public class Db4oIndexWriter implements IndexWriter
{
	private Db4oIndexer _indexer;

	public Db4oIndexWriter(Db4oIndexer indexer)
	{
		_indexer = indexer;
	}

	public void clear() throws Exception
	{
		Session session = _indexer.openSession();
		session.close();

		_indexer.getDb4oFile().delete();
	}

	public void update() throws Exception
	{
		clear();

		Session session = _indexer.openSession();
		for (DataSet dataset : VdbManager.getInstance().getDomain()
				.getDataSets())
		{
			if (JdbcSourceManager.getInstance().getJdbcSource(dataset) == null)
				continue;

			update(session, dataset);
		}

		session.close();
	}

	private void update(Session session, DataSet dataset) throws Exception
	{
		for (Entity entity : dataset.getEntities())
		{
			update(session, entity);
		}
	}

	private void update(Session session, Entity entity) throws Exception
	{
		// get all records
		VarQuery query = new AnyBeanDao(entity).createQuery();
		Field[] fields = ((VdbEntity) entity).getView("indexEntity").getItems(
				new Field[0]);

		// pageable
		QueryExecutor qe = new AnyBeanDao(entity).execute(query);

		int total = qe.size();
		int i = 1;
		while (i <= total)
		{
			int size = 10;
			List<AnyBean> list = qe.list(i, size);
			for (AnyBean bean : list)
			{
				write(session, bean, fields);
			}

			i += size;
			list = null;

			session.commit();
		}
	}

	private void write(Session session, AnyBean bean, Field[] fields)
			throws Exception
	{
		Db4oIndexEntry hit = new Db4oIndexEntry();
		hit.setBeanId(bean.getId().getValue());
		hit.setEntityId(bean.getEntity().getId());
		hit.setTitle(bean.getTitle().getTitle());
		hit.setText(IndexUtils.getFullText(bean, fields));
		hit.setIndexTime(new Date());
		((Db4oSession) session).getDb4oContainer().store(hit);
	}

}
