package vdb.mydb.index.lucene;

import java.io.File;
import java.util.List;

import org.apache.lucene.document.Document;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.index.Session;
import vdb.mydb.index.util.IndexUtils;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;

public class LuceneIndexWriter implements vdb.mydb.index.IndexWriter
{
	private LuceneIndexer _indexer;

	public LuceneIndexWriter(LuceneIndexer indexer)
	{
		_indexer = indexer;
	}

	public void clear() throws Exception
	{
		Session session = _indexer.openSession();
		// ((LuceneSession) session).openWriter();
		((LuceneSession) session).closeWriter();

		File dir = new File(_indexer.getRealPath());
		for (File f : dir.listFiles())
		{
			f.delete();
		}
	}

	public void update() throws Exception
	{
		clear();
		Session session = _indexer.openSession();
		((LuceneSession) session).openWriter();
		for (DataSet dataset : VdbManager.getInstance().getDomain()
				.getDataSets())
		{
			update(session, dataset);
		}
		((LuceneSession) session).getWriter().optimize();
		((LuceneSession) session).closeWriter();
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
		VarQuery query = new AnyBeanDao(entity).createQuery();
		vdb.metacat.Field[] fields = ((VdbEntity) entity)
				.getView("indexEntity").getItems(new vdb.metacat.Field[0]);

		if (fields.length > 0)
		{
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
						write(session, bean, fields);
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

	private void write(Session session, AnyBean bean, vdb.metacat.Field[] fields)

	{
		try
		{
			Document doc = new Document();
			doc.add(new org.apache.lucene.document.Field("DATASETID", bean
					.getEntity().getDataSet().getId(),
					org.apache.lucene.document.Field.Store.YES,
					org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			doc.add(new org.apache.lucene.document.Field("ENTITYID", bean
					.getEntity().getId(),
					org.apache.lucene.document.Field.Store.YES,
					org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			doc.add(new org.apache.lucene.document.Field("INDEXTEXT",
					IndexUtils.getFullText(bean, fields),
					org.apache.lucene.document.Field.Store.YES,
					org.apache.lucene.document.Field.Index.ANALYZED));
			doc.add(new org.apache.lucene.document.Field("RECORDID", bean
					.getId().getValue(),
					org.apache.lucene.document.Field.Store.YES,
					org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			doc.add(new org.apache.lucene.document.Field("INDEXTIME", System
					.currentTimeMillis()
					+ "", org.apache.lucene.document.Field.Store.YES,
					org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			doc.add(new org.apache.lucene.document.Field("TITLE", bean
					.getTitle().getTitle(),
					org.apache.lucene.document.Field.Store.YES,
					org.apache.lucene.document.Field.Index.NOT_ANALYZED));
			((LuceneSession) session).getWriter().addDocument(doc,
					_indexer.getAnalyzer());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
