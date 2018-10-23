package vdb.service.parser;

import java.io.Serializable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanQuery;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLTermNode;

public class VdbExprQueryNode implements VdbQueryNode
{
	private String field;

	private String operator;

	private Serializable value;

	public VdbExprQueryNode(CQLNode node)
	{
		super();
		CQLTermNode _node = (CQLTermNode) node;
		field = _node.getIndex();
		operator = _node.getRelation().getBase();
		value = _node.getTerm() + "";
	}

	public VdbExprQueryNode(String field, String operator, Serializable value)
	{
		super();
		this.field = field;
		this.value = value;
		this.operator = operator;
	}

	public BooleanQuery renderLuceneQuery(Analyzer analyzer, String fieldname)
			throws Exception
	{
		throw new VdbQueryNodeRenderException();
	}

	public String renderJdbcQuery(String fieldName) throws Exception
	{
		throw new VdbQueryNodeRenderException();
	}

	public String getField()
	{
		return field;
	}

	public String getOperator()
	{
		if ("=".equals(operator))
		{
			return "eq";
		}
		else if ("<".equals(operator))
		{
			return "lt";
		}
		else if (">".equals(operator))
		{
			return "gt";
		}
		return operator;
	}

	public Serializable getValue()
	{
		return value;
	}
}
