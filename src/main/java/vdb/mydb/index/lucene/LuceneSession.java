package vdb.mydb.index.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import vdb.mydb.index.Session;

public class LuceneSession implements Session
{
	private static String _indexFilePath;

	private static Analyzer _analyzer;

	private static Similarity _similarity;

	private static Directory _directory;

	private IndexWriter _writer;

	private IndexSearcher _searcher;

	public LuceneSession(String indexFilePath, Analyzer analyzer,
			Similarity similarity) throws IOException
	{
		if (_indexFilePath == null)
		{
			_indexFilePath = indexFilePath;
		}
		if (_analyzer == null)
		{
			_analyzer = analyzer;
			// _analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		}
		if (_similarity == null)
		{
			_similarity = similarity;
		}
		if (_directory == null)
		{
			_directory = FSDirectory.open(new File(_indexFilePath));
		}
	}

	public IndexWriter getWriter()
	{
		if (_writer == null)
		{
			openWriter();
		}
		return _writer;
	}

	public void openWriter()
	{
		try
		{
			_writer = new IndexWriter(_directory, _analyzer, true,
					IndexWriter.MaxFieldLength.UNLIMITED);
			_writer.setMergeFactor(100);
			_writer.setMaxMergeDocs(1000);
			_writer.setMaxBufferedDocs(200);
			_writer.setUseCompoundFile(false);
			_writer.optimize();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setWriter(IndexWriter writer)
	{
		_writer = writer;
	}

	public void closeWriter()
	{
		if (_writer != null)
		{
			try
			{
				_writer.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public IndexSearcher getSearcher()
	{
		if (_searcher == null)
		{
			openSearcher();
		}
		return _searcher;
	}

	public void openSearcher()
	{
		try
		{
			_searcher = new IndexSearcher(_directory);
			// _searcher.setSimilarity(_similarity);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setSearcher(IndexSearcher searcher)
	{
		_searcher = searcher;
	}

	public void closeSearcher()
	{
		if (_searcher != null)
		{
			try
			{
				_searcher.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void commit()
	{
	}

	public void close()
	{
	}

}
