package vdb.mydb.service;

import java.io.File;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.VdbManagerTest;
import vdb.mydb.index.Hit;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.index.IndexWriter;
import vdb.mydb.index.VdbIndexer;
import cn.csdb.commons.jsp.Pageable;

public class IndexSearcherTest extends VdbManagerTest
{
	VdbIndexer i;

	IndexWriter w;

	IndexSearcher s;

	public void testLuceneSearch()
	{
		try
		{
			String keyword = "java";
			DataSet dataset = VdbManager.getInstance().getCatalog().fromUri(
					"cn.csdb.vdb.bookstore");
			Entity entity = VdbManager.getInstance().getCatalog().fromUri(
					"cn.csdb.vdb.bookstore.book");

			long start, end;
			Pageable<Hit> hits;
			start = System.currentTimeMillis();
			hits = s.search(IndexSearcher.LogicalOperator.OR, "��", "Java");
			end = System.currentTimeMillis();
			logSerch("logical", start, end, hits);

			start = System.currentTimeMillis();
			hits = s.search(dataset, IndexSearcher.LogicalOperator.OR, "��",
					"Java");
			end = System.currentTimeMillis();
			logSerch("logical in dataset", start, end, hits);

			start = System.currentTimeMillis();
			hits = s.search(entity, IndexSearcher.LogicalOperator.OR, "��",
					"Java");
			end = System.currentTimeMillis();
			logSerch("logical in entity", start, end, hits);

			start = System.currentTimeMillis();
			hits = s.search("�� or Java");
			end = System.currentTimeMillis();
			logSerch("cql", start, end, hits);

			start = System.currentTimeMillis();
			hits = s.search(dataset, "�� or Java");
			end = System.currentTimeMillis();
			logSerch("cql in dataset", start, end, hits);

			start = System.currentTimeMillis();
			hits = s.search(entity, "�� or Java");
			end = System.currentTimeMillis();
			logSerch("cql in entity", start, end, hits);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assert (true);
	}

	private void logSerch(String info, long start, long end, Pageable<Hit> hits)
			throws Exception
	{
		System.err.println(info + "\t cost: " + (end - start) + "ns,\t got "
				+ hits.size() + " hits");
	}

	protected void setUp() throws Exception
	{
		File appRoot = new File("E:\\eclipse\\workspace\\VDB1.3.2\\WebRoot");
		try
		{
			i = VdbManager.getInstance().getIndexer();
			w = i.getWriter();
			s = i.getSearcher();
			w.update();
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

}
