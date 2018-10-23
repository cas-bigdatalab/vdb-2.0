package vdb.mydb.index.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.z3950.zing.cql.CQLNode;
import org.z3950.zing.cql.CQLParser;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.index.Hit;
import vdb.mydb.index.IndexSearcher;
import vdb.service.parser.VdbQueryNode;
import vdb.service.parser.VdbQueryNodeFactory;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class JdbcIndexSearcher implements IndexSearcher
{
	private JdbcSource _jdbcSource;

	public JdbcIndexSearcher(JdbcSource jdbcSource)
	{
		_jdbcSource = jdbcSource;
	}

	@Deprecated
	public Pageable<Hit> search(LogicalOperator logicalOperator,
			String... keywords) throws Exception
	{
		StringSql ss = new StringSql("select * from INDEX_TAB");
		String filter = getLogicalQuery(logicalOperator, ss, keywords);
		if (filter != null)
		{
			ss.setString(ss.getString() + " where " + filter);
		}
		return queryFromJdbc(ss);
	}

	@Deprecated
	public Pageable<Hit> search(DataSet dataset,
			LogicalOperator logicalOperator, String... keywords)
			throws Exception
	{
		String dsId = dataset.getId();
		StringSql ss = new StringSql("select * from INDEX_TAB");
		String filter = getLogicalQuery(logicalOperator, ss, keywords);
		if (filter != null)
		{
			filter = " (" + filter + ") and (DATASETID =?)";
			ss.addParameter(dsId);
			ss.setString(ss.getString() + " where " + filter);
		}
		else
		{
			ss.setString(ss.getString() + " where DATASETID = ?");
			ss.addParameter(dsId);
		}
		return queryFromJdbc(ss);
	}

	@Deprecated
	public Pageable<Hit> search(Entity entity, LogicalOperator logicalOperator,
			String... keywords) throws Exception
	{
		String entityId = entity.getId();
		StringSql ss = new StringSql("select * from INDEX_TAB");
		String filter = getLogicalQuery(logicalOperator, ss, keywords);
		if (filter != null)
		{
			filter = " (" + filter + ") and (ENTITYID =?)";
			ss.addParameter(entityId);
			ss.setString(ss.getString() + " where " + filter);
		}
		else
		{
			ss.setString(ss.getString() + " where ENTITYID = ?");
			ss.addParameter(entityId);
		}
		return queryFromJdbc(ss);
	}

	public Pageable<Hit> search(String queryExpr) throws Exception
	{
		StringSql ss = new StringSql("select * from INDEX_TAB ");
		String filter = getCQLQuery(queryExpr);
		if (filter != null)
		{
			ss.setString(ss.getString() + " where " + filter);
		}
		return queryFromJdbc(ss);
	}

	public Pageable<Hit> search(DataSet dataset, String queryExpr)
			throws Exception
	{
		String dsId = dataset.getId();
		StringSql ss = new StringSql("select * from INDEX_TAB ");
		String filter = getCQLQuery(queryExpr);
		if (filter != null)
		{
			filter += " and DATASETID = '" + dsId + "'";
			ss.setString(ss.getString() + " where " + filter);
		}
		else
		{
			ss.setString(ss.getString() + " where DATASETID = '" + dsId + "'");
		}
		return queryFromJdbc(ss);
	}

	public Pageable<Hit> search(Entity entity, String queryExpr)
			throws Exception
	{
		String entityId = entity.getId();
		StringSql ss = new StringSql("select * from INDEX_TAB ");
		String filter = getCQLQuery(queryExpr);
		if (filter != null)
		{
			filter += " and ENTITYID = '" + entityId + "'";
			ss.setString(ss.getString() + " where " + filter);
		}
		else
		{
			ss.setString(ss.getString() + " where ENTITYID = '" + entityId
					+ "'");
		}
		return queryFromJdbc(ss);
	}

	public Pageable<Hit> search(VdbQueryNode queryNode) throws Exception
	{
		StringSql ss = new StringSql("select * from INDEX_TAB ");
		String filter = getNodeQuery(queryNode);
		if (filter != null)
		{
			ss.setString(ss.getString() + " where " + filter);
		}
		return queryFromJdbc(ss);
	}

	public Pageable<Hit> search(DataSet dataset, VdbQueryNode queryNode)
			throws Exception
	{
		String dsId = dataset.getId();
		StringSql ss = new StringSql("select * from INDEX_TAB ");
		String filter = getNodeQuery(queryNode);
		if (filter != null)
		{
			filter += " and DATASETID = '" + dsId + "'";
			ss.setString(ss.getString() + " where " + filter);
		}
		else
		{
			ss.setString(ss.getString() + " where DATASETID = '" + dsId + "'");
		}
		return queryFromJdbc(ss);
	}

	public Pageable<Hit> search(Entity entity, VdbQueryNode queryNode)
			throws Exception
	{
		String entityId = entity.getId();
		StringSql ss = new StringSql("select * from INDEX_TAB ");
		String filter = getNodeQuery(queryNode);
		if (filter != null)
		{
			filter += " and ENTITYID = '" + entityId + "'";
			ss.setString(ss.getString() + " where " + filter);
		}
		else
		{
			ss.setString(ss.getString() + " where ENTITYID = '" + entityId
					+ "'");
		}
		return queryFromJdbc(ss);
	}

	private String getLogicalQuery(LogicalOperator logicalOperator,
			StringSql ss, String... keywords)
	{
		String filter = null;
		for (String kw : keywords)
		{
			if (filter == null)
			{
				filter = "";
			}
			else
			{
				filter += (logicalOperator == LogicalOperator.AND ? " and "
						: " or ");
			}
			filter += "upper(INDEXTEXT) like ?";

			char[] c = kw.toCharArray();
			for (int i = 0; i < c.length; i++)
			{
				if (c[i] >= 'a' && c[i] <= 'z')
					c[i] = (char) (c[i] + 'A' - 'a');
			}

			ss.addParameter("%" + new String(c) + "%");
		}
		return filter;
	}

	private String getNodeQuery(VdbQueryNode node) throws Exception
	{
		return node.renderJdbcQuery("INDEXTEXT");
	}

	private String getCQLQuery(String queryExpr) throws Exception
	{
		CQLParser parser = new CQLParser();
		CQLNode node = parser.parse(queryExpr);
		VdbQueryNode n = VdbQueryNodeFactory.createNode(node);
		return n.renderJdbcQuery("INDEXTEXT");
	}

	private Pageable<Hit> queryFromJdbc(StringSql ss) throws Exception
	{
		return _jdbcSource.createQuery(ss, new ResultSetReader<Hit>()
		{
			public Hit read(ResultSet rs, int row) throws SQLException
			{
				JdbcHit hit = new JdbcHit();
				hit.setDataSetId(rs.getString("DATASETID"));
				hit.setRecordId(rs.getString("RECORDID"));
				hit.setEntityId(rs.getString("ENTITYID"));
				hit.setIndexTime(rs.getTimestamp("INDEXTIME"));
				hit.setText(rs.getString("INDEXTEXT"));
				hit.setTitle(rs.getString("TITLE"));
				return hit;
			}
		});
	}

}
