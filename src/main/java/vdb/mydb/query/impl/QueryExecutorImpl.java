package vdb.mydb.query.impl;

import java.util.List;

import org.apache.log4j.Logger;

import vdb.mydb.bean.AnyBean;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;
import cn.csdb.commons.orm.Persistor;
import cn.csdb.commons.orm.Query;

public class QueryExecutorImpl implements QueryExecutor
{
	private Persistor _persistor;
	private Logger _logger = Logger.getLogger(this.getClass());
	private VarQuery _query;

	public QueryExecutorImpl(Persistor persistor, VarQuery query)
	{
		_persistor = persistor;
		_query = query;
	}

	private Query<AnyBean> createQuery() throws Exception
	{
		return _persistor.createQuery().setSql(_query.toStringSql());
	}

	public List<AnyBean> list() throws Exception
	{
		_logger.debug(String.format("SQL: %s", _query.toStringSql()));
		/** *********** BEGIN LOG **************** */
		// logQuery(_query.getEntity());
		/** *********** END LOG **************** */
		return createQuery().list();
	}

	public List<AnyBean> list(int beginning, int size) throws Exception
	{
		/** *********** BEGIN LOG **************** */
		// logQuery(_query.getEntity());
		/** *********** END LOG **************** */
		_logger.debug(String.format("SQL: %s, LIMIT: (%d, %d)",
				_query.toStringSql(), beginning, size));
		return createQuery().list(beginning, size);
	}

	public AnyBean single() throws Exception
	{
		return createQuery().single();
	}

	public int size() throws Exception
	{
		return createQuery().size();
	}
}
