package vdb.tool.mydb;

import vdb.service.parser.VdbAndQueryNode;
import vdb.service.parser.VdbOrQueryNode;
import vdb.service.parser.VdbQueryNode;
import vdb.service.parser.VdbTermQueryNode;

public class TermTool
{
	public VdbQueryNode create(String term)
	{
		return new VdbTermQueryNode(term);
	}

	public VdbQueryNode and(VdbQueryNode a, VdbQueryNode b)
	{
		return new VdbAndQueryNode(a, b);
	}

	public VdbQueryNode or(VdbQueryNode a, VdbQueryNode b)
	{
		return new VdbOrQueryNode(a, b);
	}
}
