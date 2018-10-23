package vdb.mydb.index;

public interface VdbIndexer
{
	IndexSearcher getSearcher() throws Exception;

	IndexWriter getWriter() throws Exception;
}