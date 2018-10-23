package vdb.mydb.service;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.VdbManagerTest;
import vdb.mydb.index.Hit;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.index.jdbc.JdbcIndexSearcher;
import vdb.mydb.index.jdbc.JdbcIndexWriter;
import vdb.mydb.index.jdbc.JdbcIndexer;
import vdb.mydb.index.lucene.LuceneIndexSearcher;
import vdb.mydb.index.lucene.LuceneIndexWriter;
import vdb.mydb.index.lucene.LuceneIndexer;
import cn.csdb.commons.jsp.Pageable;

public class IndexSearcherCompareDemo extends VdbManagerTest
{
	JdbcIndexer i2;

	LuceneIndexer i;

	JdbcIndexWriter w2;

	LuceneIndexWriter w;

	JdbcIndexSearcher s2;

	LuceneIndexSearcher s;

	public void testLuceneSearch()
	{
		try
		{
			String keyword = "��";
			DataSet dataset = VdbManager.getInstance().getCatalog().fromUri(
					"cn.csdb.vdb.bookstore");
			Entity entity = VdbManager.getInstance().getCatalog().fromUri(
					"cn.csdb.vdb.bookstore.book");
			// search(s1, "jdbc", keyword, dataset, entity);
			// search(s2, "lucene", keyword, dataset, entity);
			compareSearch(keyword, dataset, entity);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		assert (true);
	}

	private void search(IndexSearcher searcher, String type, String keyword,
			DataSet dataset, Entity entity) throws Exception
	{
		long start;
		long end;
		Pageable<Hit> hits;
		start = System.currentTimeMillis();
		hits = searcher.search(IndexSearcher.LogicalOperator.OR, "��", "Java");
		end = System.currentTimeMillis();
		logSerch(type + " logical", start, end, hits);

		start = System.currentTimeMillis();
		hits = searcher.search(dataset, IndexSearcher.LogicalOperator.OR, "��",
				"Java");
		end = System.currentTimeMillis();
		logSerch(type + " logical in dataset", start, end, hits);

		start = System.currentTimeMillis();
		hits = searcher.search(entity, IndexSearcher.LogicalOperator.OR, "��",
				"Java");
		end = System.currentTimeMillis();
		logSerch(type + " logical in entity", start, end, hits);

		start = System.currentTimeMillis();
		hits = searcher.search("�� or Java");
		end = System.currentTimeMillis();
		logSerch(type + " cql", start, end, hits);

		start = System.currentTimeMillis();
		hits = searcher.search(dataset, "�� or Java");
		end = System.currentTimeMillis();
		logSerch(type + " cql in dataset", start, end, hits);

		start = System.currentTimeMillis();
		hits = searcher.search(entity, "�� or Java");
		end = System.currentTimeMillis();
		logSerch(type + " cql in entity", start, end, hits);
	}

	private void compareSearch(String keyword, DataSet dataset, Entity entity)
			throws Exception
	{
		long start, end;
		Pageable<Hit> hits;

		start = System.currentTimeMillis();
		hits = s2.search(IndexSearcher.LogicalOperator.OR, keyword);
		end = System.currentTimeMillis();
		logSerch("jdbc logical", start, end, hits);

		start = System.currentTimeMillis();
		hits = s.search(IndexSearcher.LogicalOperator.OR, keyword);
		end = System.currentTimeMillis();
		logSerch("lucene logical", start, end, hits);

		start = System.currentTimeMillis();
		hits = s2.search(dataset, IndexSearcher.LogicalOperator.OR, keyword);
		end = System.currentTimeMillis();
		logSerch("jdbc logical in dataset", start, end, hits);

		start = System.currentTimeMillis();
		hits = s.search(dataset, IndexSearcher.LogicalOperator.OR, keyword);
		end = System.currentTimeMillis();
		logSerch("lucene logical in dataset", start, end, hits);

		start = System.currentTimeMillis();
		hits = s2.search(entity, IndexSearcher.LogicalOperator.OR, keyword);
		end = System.currentTimeMillis();
		logSerch("jdbc logical in entity", start, end, hits);

		start = System.currentTimeMillis();
		hits = s.search(entity, IndexSearcher.LogicalOperator.OR, keyword);
		end = System.currentTimeMillis();
		logSerch("lucene logical in entity", start, end, hits);

		start = System.currentTimeMillis();
		hits = s2.search(keyword);
		end = System.currentTimeMillis();
		logSerch("jdbc cql", start, end, hits);

		start = System.currentTimeMillis();
		hits = s.search(keyword);
		end = System.currentTimeMillis();
		logSerch("lucene cql", start, end, hits);

		start = System.currentTimeMillis();
		hits = s2.search(dataset, keyword);
		end = System.currentTimeMillis();
		logSerch("jdbc cql in dataset", start, end, hits);

		start = System.currentTimeMillis();
		hits = s.search(dataset, keyword);
		end = System.currentTimeMillis();
		logSerch("lucene cql in dataset", start, end, hits);

		start = System.currentTimeMillis();
		hits = s2.search(entity, keyword);
		end = System.currentTimeMillis();
		logSerch("jdbc cql in entity", start, end, hits);

		start = System.currentTimeMillis();
		hits = s.search(entity, keyword);
		end = System.currentTimeMillis();
		logSerch("lucene cql in entity", start, end, hits);
	}

	private void logSerch(String info, long start, long end, Pageable<Hit> hits)
			throws Exception
	{
		System.err.println(info + "\t cost: " + (end - start) + "ns ,\t got: "
				+ hits.size() + " hits");
		for (Hit hit : hits.list(1, hits.size()))
		{
			System.err.println(hit.getText());
		}
		System.err.println("@@@@@@@@@@@@@@@@@@@@@\n");
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		try
		{
			XmlBeanFactory factory = new XmlBeanFactory(
					new FileSystemResource(
							"E:\\eclipse\\workspace\\VDB1.3.2\\WebRoot\\WEB-INF\\conf\\core\\context.xml"));

			i = (LuceneIndexer) factory.getBean("indexer");
			i2 = (JdbcIndexer) factory.getBean("indexer2");
			w = (LuceneIndexWriter) i.getWriter();
			w2 = (JdbcIndexWriter) i2.getWriter();
			s = (LuceneIndexSearcher) i.getSearcher();
			s2 = (JdbcIndexSearcher) i2.getSearcher();
			// w.update();
			// w2.update();
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
