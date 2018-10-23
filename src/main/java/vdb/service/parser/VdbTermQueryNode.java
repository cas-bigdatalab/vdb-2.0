package vdb.service.parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLTermNode;

public class VdbTermQueryNode implements VdbQueryNode
{
	private String _term;

	public VdbTermQueryNode(CQLNode node)
	{
		super();
		CQLTermNode _node = (CQLTermNode) node;
		_term = _node.getTerm();
	}

	public VdbTermQueryNode(String term)
	{
		_term = term;
	}

	public BooleanQuery renderLuceneQuery(Analyzer analyzer, String fieldname)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		QueryParser qp = new QueryParser(Version.LUCENE_CURRENT, fieldname,
				analyzer);
		Query tq = qp.parse(_term);
		bq.add(tq, BooleanClause.Occur.MUST);
		return bq;
	}

	public String renderJdbcQuery(String fieldName) throws Exception
	{
		return " (" + fieldName + " like '%" + _term + "%') ";
	}

	// 需要为属性提供get方法，否则JSONObject的fromObject()方法不能访问此对象的term属性
	public String getTerm()
	{
		return _term;
	}
}
