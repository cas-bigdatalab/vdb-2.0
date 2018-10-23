package vdb.service.parser;

import org.z3950.zing.cql.CQLAndNode;
import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLOrNode;
import org.z3950.zing.cql.CQLTermNode;

public class VdbQueryNodeFactory
{
	public static VdbQueryNode createNode(CQLNode node)
			throws VdbQueryNodeParseException
	{
		if (node instanceof CQLTermNode)
		{
			if (((CQLTermNode) node).getIndex().equals("cql.serverChoice"))
			{
				return new VdbTermQueryNode(node);
			}
			else
			{
				return new VdbExprQueryNode(node);
			}
		}
		else if (node instanceof CQLAndNode)
		{
			return new VdbAndQueryNode(node);
		}
		else if (node instanceof CQLOrNode)
		{
			return new VdbOrQueryNode(node);
		}
		else
		{
			throw new VdbQueryNodeParseException();
		}
	}

}
