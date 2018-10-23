package vdb.mydb.index;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.service.parser.VdbQueryNode;
import cn.csdb.commons.jsp.Pageable;

public interface IndexSearcher
{
	enum LogicalOperator
	{
		AND, OR
	};

	public Pageable<Hit> search(VdbQueryNode queryNode) throws Exception;

	public Pageable<Hit> search(DataSet dataset, VdbQueryNode queryNode)
			throws Exception;

	public Pageable<Hit> search(Entity entity, VdbQueryNode queryNode)
			throws Exception;

	public Pageable<Hit> search(String queryExpr) throws Exception;

	public Pageable<Hit> search(DataSet dataset, String queryExpr)
			throws Exception;

	public Pageable<Hit> search(Entity entity, String queryExpr)
			throws Exception;

	@Deprecated
	public Pageable<Hit> search(LogicalOperator logicalOperator,
			String... keywords) throws Exception;

	@Deprecated
	public Pageable<Hit> search(DataSet dataset,
			LogicalOperator logicalOperator, String... keywords)
			throws Exception;

	@Deprecated
	public Pageable<Hit> search(Entity entity, LogicalOperator logicalOperator,
			String... keywords) throws Exception;
}