package vdb.mydb.index.jdbc;

import vdb.mydb.VdbManager;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.index.IndexWriter;
import vdb.mydb.index.Session;
import vdb.mydb.index.VdbIndexer;
import vdb.mydb.jdbc.DataSourceDetail;
import vdb.mydb.jdbc.JdbcSourceManager;
import cn.csdb.commons.sql.JdbcSource;

public class JdbcIndexer implements VdbIndexer
{
	private DataSourceDetail _dataSourceDetail;

	private JdbcSource _jdbcSource;

	public JdbcSource getJdbcSource() throws Exception
	{
		if (_jdbcSource == null)
		{
			_jdbcSource = JdbcSourceManager.getInstance().getJdbcSource(
					"vdb.index", _dataSourceDetail,
					VdbManager.getEngine().getVelocityEngine().createContext());
		}

		return _jdbcSource;
	}

	public IndexSearcher getSearcher() throws Exception
	{
		return new JdbcIndexSearcher(getJdbcSource());
	}

	public IndexWriter getWriter() throws Exception
	{
		return new JdbcIndexWriter(this);
	}

	public Session openSession() throws Exception
	{
		return new JdbcSession(getJdbcSource());
	}

	public void setDataSource(DataSourceDetail dataSourceDetail)
	{
		_dataSourceDetail = dataSourceDetail;
	}
}
