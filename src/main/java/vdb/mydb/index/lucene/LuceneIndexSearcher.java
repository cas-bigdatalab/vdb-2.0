package vdb.mydb.index.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParser;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.index.Hit;
import vdb.mydb.index.IndexSearcher;
import vdb.service.parser.VdbQueryNode;
import vdb.service.parser.VdbQueryNodeFactory;
import cn.csdb.commons.jsp.Pageable;

public class LuceneIndexSearcher implements IndexSearcher
{
	private LuceneIndexer _indexer;

	private LuceneSession _session;

	public LuceneIndexSearcher(LuceneIndexer indexer)
	{
		_indexer = indexer;
	}

	@Deprecated
	public Pageable<Hit> search(LogicalOperator logicalOperator,
			String... keywords) throws Exception
	{
		BooleanQuery bq = getLogicalQuery(logicalOperator, keywords);
		return queryFromLucene(bq);
	}

	@Deprecated
	public Pageable<Hit> search(DataSet dataset,
			LogicalOperator logicalOperator, String... keywords)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(getLogicalQuery(logicalOperator, keywords),
				BooleanClause.Occur.MUST);
		String dsId = dataset.getId();
		bq.add(new TermQuery(new Term("DATASETID", dsId)),
				BooleanClause.Occur.MUST);
		return queryFromLucene(bq);
	}

	@Deprecated
	public Pageable<Hit> search(Entity entity, LogicalOperator logicalOperator,
			String... keywords) throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(getLogicalQuery(logicalOperator, keywords),
				BooleanClause.Occur.MUST);
		String entityId = entity.getId();
		bq.add(new TermQuery(new Term("ENTITYID", entityId)),
				BooleanClause.Occur.MUST);
		return queryFromLucene(bq);
	}

	public Pageable<Hit> search(String queryExpr) throws Exception
	{
		BooleanQuery bq = getCQLQuery(queryExpr);
		return queryFromLucene(bq);
	}

	public Pageable<Hit> search(DataSet dataset, String queryExpr)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(getCQLQuery(queryExpr), BooleanClause.Occur.MUST);
		String dsId = dataset.getId();
		bq.add(new TermQuery(new Term("DATASETID", dsId)),
				BooleanClause.Occur.MUST);
		return queryFromLucene(bq);
	}

	public Pageable<Hit> search(Entity entity, String queryExpr)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(getCQLQuery(queryExpr), BooleanClause.Occur.MUST);
		String entityId = entity.getId();
		bq.add(new TermQuery(new Term("ENTITYID", entityId)),
				BooleanClause.Occur.MUST);
		return queryFromLucene(bq);
	}

	public Pageable<Hit> search(VdbQueryNode queryNode) throws Exception
	{
		BooleanQuery bq = getNodeQuery(queryNode);
		return queryFromLucene(bq);
	}

	public Pageable<Hit> search(DataSet dataset, VdbQueryNode queryNode)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(getNodeQuery(queryNode), BooleanClause.Occur.MUST);
		String dsId = dataset.getId();
		bq.add(new TermQuery(new Term("DATASETID", dsId)),
				BooleanClause.Occur.MUST);
		return queryFromLucene(bq);
	}

	public Pageable<Hit> search(Entity entity, VdbQueryNode queryNode)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(getNodeQuery(queryNode), BooleanClause.Occur.MUST);
		String entityId = entity.getId();
		bq.add(new TermQuery(new Term("ENTITYID", entityId)),
				BooleanClause.Occur.MUST);
		return queryFromLucene(bq);
	}

	public void close()
	{
		_session.closeSearcher();
	}

	private BooleanQuery getNodeQuery(VdbQueryNode node) throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(node.renderLuceneQuery(_indexer.getAnalyzer(), "INDEXTEXT"),
				BooleanClause.Occur.MUST);
		return bq;
	}

	private BooleanQuery getCQLQuery(String queryExpr) throws Exception
	{
		CQLParser parser = new CQLParser();
		CQLNode node = parser.parse(queryExpr);
		VdbQueryNode n = VdbQueryNodeFactory.createNode(node);
		BooleanQuery bq = new BooleanQuery();
		bq.add(n.renderLuceneQuery(_indexer.getAnalyzer(), "INDEXTEXT"),
				BooleanClause.Occur.MUST);
		return bq;
	}

	private BooleanQuery getLogicalQuery(LogicalOperator logicalOperator,
			String... keywords) throws ParseException
	{
		Occur occur;
		if (logicalOperator.equals(LogicalOperator.AND))
		{
			occur = BooleanClause.Occur.MUST;
		}
		else
		{
			occur = BooleanClause.Occur.SHOULD;
		}
		BooleanQuery bq = new BooleanQuery();
		for (String key : keywords)
		{
			QueryParser qp = new QueryParser(Version.LUCENE_CURRENT,
					"INDEXTEXT", _indexer.getAnalyzer());
			Query tq = qp.parse(key);
			bq.add(tq, occur);
		}
		return bq;
	}

	private Pageable<Hit> queryFromLucene(BooleanQuery bq) throws Exception,
			IOException
	{
		_session = (LuceneSession) _indexer.openSession();
		final org.apache.lucene.search.IndexSearcher searcher = _session
				.getSearcher();
		final TopDocs td = searcher.search(bq, searcher.maxDoc());
		final ScoreDoc[] sds = td.scoreDocs;
		System.out.println(td.totalHits);
		Pageable<Hit> hits = new Pageable<Hit>()
		{
			private int _size = td.totalHits;

			public List<Hit> list(int beginning, int size) throws Exception
			{
				if (beginning < 1)
				{
					beginning = 1;
				}
				beginning--;
				List<Hit> hits = new ArrayList<Hit>();
				int end = beginning + size;
				if (end > _size)
					end = _size;

				for (int i = beginning; i < end; i++)
				{
					hits.add(new LuceneHit(searcher.doc(sds[i].doc)));
				}
				return hits;
			}

			public int size() throws Exception
			{
				return _size;
			}
		};
		log(hits);
		return hits;
	}

	private void log(Pageable<Hit> hits) throws Exception
	{
		Log logger = LogFactory.getLog(getClass());
		for (Hit hit : hits.list(1, hits.size()))
		{
			logger.debug("Lucene hit: " + hit.getText());
		}
	}

}
