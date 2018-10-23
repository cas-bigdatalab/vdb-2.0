package vdb.mydb.index.db4o;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.index.Hit;
import vdb.mydb.index.IndexSearcher;
import vdb.service.parser.VdbQueryNode;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.util.Matcher;

import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class Db4oIndexSearcher implements IndexSearcher
{
	private Db4oIndexer _indexer;

	public Db4oIndexSearcher(Db4oIndexer indexer)
	{
		_indexer = indexer;
	}

	public Pageable<Hit> search(LogicalOperator logicalOperator,
			String... keywords) throws Exception
	{
		final Db4oSession session = (Db4oSession) _indexer.openSession();
		Matcher<Db4oIndexEntry>[] matchers = new Matcher[keywords.length];
		int i = 0;
		for (String keyword : keywords)
		{
			matchers[i] = new KeywordsMatcher(keyword);
			i++;
		}

		final Matcher<Db4oIndexEntry> matcher = logicalOperator == LogicalOperator.AND ? new AndExpr(
				matchers)
				: new OrExpr(matchers);

		final ObjectSet<Db4oIndexEntry> results = session.getDb4oContainer()
				.query((new Predicate<Db4oIndexEntry>()
				{
					@Override
					public boolean match(Db4oIndexEntry hit)
					{
						return matcher.matches(hit);
					}
				}));

		Pageable<Hit> hits = new Pageable<Hit>()
		{
			private int _size = results.size();

			public List<Hit> list(int beginning, int size) throws Exception
			{
				if (beginning < 1)
				{
					beginning = 1;
				}
				beginning--;
				List<Hit> hits = new ArrayList<Hit>();
				int end = beginning + size;
				if (end > _size)
					end = _size;

				List<Db4oIndexEntry> sl = results.subList(beginning, end);
				for (Db4oIndexEntry in : sl)
				{
					hits.add(new Db4oHit(in));
				}

				session.close();
				return hits;
			}

			public int size() throws Exception
			{
				return _size;
			}
		};

		return hits;
	}

	public Pageable<Hit> search(DataSet dataset,
			LogicalOperator logicalOperator, String... keywords)
			throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(DataSet dataset, String cql) throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(Entity entity, LogicalOperator logicalOperator,
			String... keywords) throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(Entity entity, String cql) throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(String cql) throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(DataSet dataset, VdbQueryNode queryNode)
			throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(Entity entity, VdbQueryNode queryNode)
			throws Exception
	{
		return null;
	}

	public Pageable<Hit> search(VdbQueryNode queryNode) throws Exception
	{
		return null;
	}

}
