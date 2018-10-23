package vdb.service.parser;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.z3950.zing.cql.CQLAndNode;
import org.z3950.zing.cql.CQLNode;

public class VdbAndQueryNode implements VdbBooleanQueryNode
{
	private VdbQueryNode a;

	private VdbQueryNode b;

	private String operator = "and";

	public VdbAndQueryNode(CQLNode node) throws VdbQueryNodeParseException
	{
		super();
		CQLAndNode _node = (CQLAndNode) node;
		a = VdbQueryNodeFactory.createNode(_node.getLeftOperand());
		b = VdbQueryNodeFactory.createNode(_node.getRightOperand());
	}

	public VdbAndQueryNode(VdbQueryNode a, VdbQueryNode b)
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
				BooleanClause.Occur.MUST);
		bq.add(b.renderLuceneQuery(analyzer, fieldname),
				BooleanClause.Occur.MUST);
		return bq;
	}

	public String renderJdbcQuery(String fieldName) throws Exception
	{
		return "((" + a.renderJdbcQuery(fieldName) + ") and ("
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
