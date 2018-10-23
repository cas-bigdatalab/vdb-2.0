package vdb.tool.mydb;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.index.Hit;
import vdb.service.parser.VdbQueryNode;
import cn.csdb.commons.jsp.Pageable;

public class QueryTool
{
	public Pageable<Hit> query(VdbQueryNode queryNode)
	{
		try
		{
			return VdbManager.getInstance().getIndexer().getSearcher().search(
					queryNode);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Pageable<Hit> query(DataSet dataset, VdbQueryNode queryNode)
	{
		try
		{
			return VdbManager.getInstance().getIndexer().getSearcher().search(
					dataset, queryNode);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Pageable<Hit> query(Entity entity, VdbQueryNode queryNode)
	{
		try
		{
			return VdbManager.getInstance().getIndexer().getSearcher().search(
					entity, queryNode);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Pageable<Hit> query(String queryExpr)
	{
		try
		{
			return VdbManager.getInstance().getIndexer().getSearcher().search(
					queryExpr);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Pageable<Hit> query(DataSet dataset, String queryExpr)
	{
		try
		{
			return VdbManager.getInstance().getIndexer().getSearcher().search(
					dataset, queryExpr);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Pageable<Hit> query(Entity entity, String queryExpr)
	{
		try
		{
			return VdbManager.getInstance().getIndexer().getSearcher().search(
					entity, queryExpr);
		}
		catch (Exception e)
		{
			return null;
		}
	}
}
