package vdb.service.parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLOrNode;

public class VdbOrQueryNode implements VdbBooleanQueryNode
{
	private VdbQueryNode a;

	private VdbQueryNode b;

	private String operator = "and";

	public VdbOrQueryNode(CQLNode node) throws VdbQueryNodeParseException
	{
		super();
		CQLOrNode _node = (CQLOrNode) node;
		a = VdbQueryNodeFactory.createNode(_node.getLeftOperand());
		b = VdbQueryNodeFactory.createNode(_node.getRightOperand());
	}

	public VdbOrQueryNode(VdbQueryNode a, VdbQueryNode b)
	{
		super();
		this.a = a;
		this.b = b;
	}

	public BooleanQuery renderLuceneQuery(Analyzer analyzer, String fieldname)
			throws Exception
	{
		BooleanQuery bq = new BooleanQuery();
		bq.add(a.renderLuceneQuery(analyzer, fieldname),
				BooleanClause.Occur.SHOULD);
		bq.add(b.renderLuceneQuery(analyzer, fieldname),
				BooleanClause.Occur.SHOULD);
		return bq;
	}

	public String renderJdbcQuery(String fieldName) throws Exception
	{
		return "((" + a.renderJdbcQuery(fieldName) + ") or ("
				+ b.renderJdbcQuery(fieldName) + "))";
	}

	public VdbQueryNode getA()
	{
		return a;
	}

	public VdbQueryNode getB()
	{
		return b;
	}

	public String getOperator()
	{
		return operator;
	}

}
