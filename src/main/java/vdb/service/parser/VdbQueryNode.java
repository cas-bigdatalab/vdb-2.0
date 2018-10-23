package vdb.service.parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;

public interface VdbQueryNode
{
	public BooleanQuery renderLuceneQuery(Analyzer analyzer, String fieldname)
			throws Exception;

	public String renderJdbcQuery(String fieldName) throws Exception;
}
