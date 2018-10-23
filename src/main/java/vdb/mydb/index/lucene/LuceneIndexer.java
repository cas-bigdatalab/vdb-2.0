package vdb.mydb.index.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Similarity;
import org.apache.velocity.context.Context;

import vdb.mydb.VdbManager;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.index.IndexWriter;
import vdb.mydb.index.Session;
import vdb.mydb.index.VdbIndexer;

public class LuceneIndexer implements VdbIndexer
{
	private String indexFilePath;

	private String realPath;

	private Analyzer analyzer;

	private Similarity similarity;

	public String getRealPath()
	{
		return realPath;
	}

	public void setRealPath(String realPath)
	{
		this.realPath = realPath;
	}

	public String getIndexFilePath()
	{
		return indexFilePath;
	}

	public void setIndexFilePath(String indexFilePath) throws Exception
	{
		this.indexFilePath = indexFilePath;
		Context ctx = VdbManager.getInstance().getVelocityEngine()
				.createContext();
		this.realPath = VdbManager.getInstance().getVelocityEngine().evaluate(
				indexFilePath, ctx);
	}

	public Analyzer getAnalyzer()
	{
		return analyzer;
	}

	public void setAnalyzer(Analyzer analyzer)
	{
		this.analyzer = analyzer;
	}

	public Similarity getSimilarity()
	{
		return similarity;
	}

	public void setSimilarity(Similarity similarity)
	{
		this.similarity = similarity;
	}

	public IndexSearcher getSearcher() throws Exception
	{
		return new LuceneIndexSearcher(this);
	}

	public IndexWriter getWriter() throws Exception
	{
		return new LuceneIndexWriter(this);
	}

	public Session openSession() throws Exception
	{
		return new LuceneSession(realPath, analyzer, similarity);
	}

}
