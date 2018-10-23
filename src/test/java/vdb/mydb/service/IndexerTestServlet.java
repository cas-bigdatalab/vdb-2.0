package vdb.mydb.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.index.Hit;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.index.VdbIndexer;
import vdb.mydb.index.lucene.LuceneIndexSearcher;
import vdb.mydb.index.lucene.LuceneIndexWriter;
import cn.csdb.commons.jsp.Pageable;

public class IndexerTestServlet extends HttpServlet
{

	public Pageable<Hit> entityQuery(String entityUri, String expr,
			String keywords) throws Exception
	{
		Pageable<Hit> query = VdbManager
				.getInstance()
				.getIndexer()
				.getSearcher()
				.search(
						"OR".equalsIgnoreCase(expr) ? IndexSearcher.LogicalOperator.OR
								: IndexSearcher.LogicalOperator.AND,
						keywords.split("\\s"));
		return query;

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{

			// JdbcIndexer i1 = (JdbcIndexer) VdbManager.getInstance()
			// .getIndexer();
			VdbIndexer i2 = VdbManager.getInstance().getIndexer();
			// JdbcIndexWriter w1 = (JdbcIndexWriter) i1.getWriter();
			LuceneIndexWriter w2 = (LuceneIndexWriter) i2.getWriter();
			// JdbcIndexSearcher s1 = (JdbcIndexSearcher) i1.getSearcher();
			LuceneIndexSearcher s2 = (LuceneIndexSearcher) i2.getSearcher();

			long start, end;
			response.setCharacterEncoding("gbk");
			PrintWriter out = response.getWriter();

			start = System.currentTimeMillis();
			// w1.update();
			end = System.currentTimeMillis();
			out.println("Jdbc Write: " + (end - start));

			start = System.currentTimeMillis();
			w2.update();
			end = System.currentTimeMillis();
			out.println("Lucene Write: " + (end - start));

			String[] keywords = new String[] { "java", "��", "��", "����",
					"����" };
			Pageable<Hit> hits;

			for (int i = 0; i < 5; i++)
			{
				/*
				 * start = System.currentTimeMillis(); hits =
				 * s1.search(IndexSearcher.LogicalOperator.OR, keywords[i]); end =
				 * System.currentTimeMillis(); out.println("Jdbc Search \t cost: " +
				 * (end - start) + "ns ,\t got: " + hits.size() + " hits");
				 * 
				 * for (Hit hit : hits.list(1, hits.size())) {
				 * out.println(hit.getText()); }
				 */

				start = System.currentTimeMillis();
				hits = s2.search(IndexSearcher.LogicalOperator.OR, keywords[i]);
				end = System.currentTimeMillis();
				out.println("Lucene Search \t cost: " + (end - start)
						+ "ns ,\t got: " + hits.size() + " hits");

				for (Hit hit : hits.list(1, hits.size()))
				{
					out.println(hit.getText());
				}
				out.println("@@@@@@@@@@@@@@@@@@@@@");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
