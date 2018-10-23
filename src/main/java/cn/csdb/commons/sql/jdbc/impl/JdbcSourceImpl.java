package cn.csdb.commons.sql.jdbc.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.sql.DataSource;

import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.catalog.JdbcCatalog;
import cn.csdb.commons.sql.dialect.HsqlDBDialect;
import cn.csdb.commons.sql.dialect.MssqlDialect;
import cn.csdb.commons.sql.dialect.MySqlDialect;
import cn.csdb.commons.sql.dialect.OracleDialect;
import cn.csdb.commons.sql.dialect.PostgreSQLDialect;
import cn.csdb.commons.sql.dialect.SqlDialect;
import cn.csdb.commons.sql.dialect.XDialect;
import cn.csdb.commons.sql.jdbc.ConnectionHandler;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.ResultSetPeeker;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.sql.jdbc.sql.DeleteSql;
import cn.csdb.commons.sql.jdbc.sql.InsertSql;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.Sql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.sql.jdbc.sql.UpdateSql;
import cn.csdb.commons.util.ObjectProxy;

public class JdbcSourceImpl implements JdbcSource
{
	private DataSource _dataSource;

	private SqlDialect _sqlDialect;

	private JdbcCatalog _sqlCatalog;

	/**
	 * 构造一个SQLSource对象，该构造方法将提取数据库的类型和目录信息。
	 * 建议使用SQLManager.getInstance().getSQLSource()。
	 * 
	 * @param dataSource
	 * @throws SQLException
	 */
	public JdbcSourceImpl(DataSource dataSource) throws Exception
	{
		_dataSource = dataSource;
		final JdbcSourceImpl thisSource = this;

		// 加载目录
		handle(new ConnectionHandler()
		{
			public void handle(Connection conn) throws SQLException
			{
				// 获取dbms类型
				_sqlDialect = createSqlDialect(conn);
				// 获取目录信息
				_sqlCatalog = new JdbcCatalog(conn, thisSource, _sqlDialect);
			}
		});
	}

	public int countRecords(QuerySql sql) throws Exception
	{
		Number number = queryForObject(sql.getSqlForCount(this),
				new ResultSetReader<Number>()
				{
					public Number read(ResultSet rs, int row)
							throws SQLException
					{
						return rs.getLong(1);
					}
				});

		if (number == null)
			return 0;

		return number.intValue();
	}

	private SqlDialect createSqlDialect(Connection conn)
	{
		// 根据产品名或者driverName
		try
		{
			String driverName = conn.getMetaData().getDriverName()
					.toLowerCase();
			SqlDialect adapter = this.createSqlDialectByDriverName(driverName);
			if (adapter != null)
				return adapter;
		}
		catch (Throwable e)
		{
		}

		try
		{
			String productName = conn.getMetaData().getDatabaseProductName()
					.toLowerCase();
			SqlDialect adapter = createSqlDialectByProductName(productName);
			if (adapter != null)
				return adapter;
		}
		catch (Throwable e)
		{
		}

		return new XDialect();
	}

	private SqlDialect createSqlDialectByProductName(String productName)
	{
		if (productName.indexOf("mysql") >= 0)
		{
			return new MySqlDialect();
		}

		if (productName.indexOf("oracle") >= 0)
		{
			return new OracleDialect();
		}

		if (productName.indexOf("sqlserver") >= 0
				|| productName.indexOf("sql server") >= 0
				|| productName.indexOf("mssql") >= 0)
		{
			return new MssqlDialect();
		}

		if (productName.indexOf("hsqldb") >= 0)
		{
			return new HsqlDBDialect();
		}

		return null;
	}

	private SqlDialect createSqlDialectByDriverName(String driverName)
	{
		if (driverName.indexOf("mysql") >= 0)
		{
			return new MySqlDialect();
		}

		if (driverName.indexOf("oracle") >= 0)
		{
			return new OracleDialect();
		}

		if (driverName.indexOf("postgresql") >= 0)
		{
			return new PostgreSQLDialect();
		}

		if (driverName.indexOf("sqlserver") >= 0
				|| driverName.indexOf("sql server") >= 0
				|| driverName.indexOf("mssql") >= 0)
		{
			return new MssqlDialect();
		}

		if (driverName.indexOf("hsql") >= 0)
		{
			return new HsqlDBDialect();
		}

		return null;
	}

	public int deleteRecords(String tableName, StringSql whereFilter)
			throws Exception
	{
		return executeUpdate(new DeleteSql(tableName, whereFilter));
	}

	public void executeQuery(QuerySql sql, ResultSetHandler handler)
			throws SQLException
	{
		executeQuery(sql, 1, -1, handler);
	}

	public void executeQuery(QuerySql sql, int beginning, int size,
			ResultSetHandler handler) throws SQLException
	{
		final QuerySql finalSql = sql;
		final JdbcSource finalSqlSource = this;
		final ResultSetHandler finalHandler = handler;
		final int finalBeginning = beginning;
		final int finalSize = size;

		handle(new ConnectionHandler()
		{
			public void handle(Connection conn) throws SQLException
			{
				finalSql.executeQuery(finalSqlSource, finalBeginning,
						finalSize, conn, finalHandler);
			}
		});
	}

	public int executeUpdate(Sql sql) throws Exception
	{
		return executeUpdate(sql, null);
	}

	public int executeUpdate(Sql sql, StatementHandler handler)
			throws Exception
	{
		final Sql finalSql = sql;
		final JdbcSource finalSqlSource = this;
		final StatementHandler finalHandler = handler;
		final ObjectProxy<Integer> o = new ObjectProxy<Integer>();

		handle(new ConnectionHandler()
		{
			public void handle(Connection conn) throws SQLException
			{
				finalSql.executeUpdate(finalSqlSource, conn,
						new StatementHandler()
						{

							public void afterExecute(PreparedStatement st)
									throws SQLException
							{
								o.setObject(st.getUpdateCount());

								if (finalHandler != null)
									finalHandler.afterExecute(st);
							}
						});
			}
		});

		return o.getObject().intValue();
	}

	/**
	 * 判断记录是否存在。
	 * 
	 * @param sql
	 *            SELECT-SQL语句
	 * @return true（存在）/false（不存在）
	 */
	public boolean existsRecord(QuerySql sql) throws Exception
	{
		ResultSetPeeker rp = new ResultSetPeeker();
		executeQuery(sql, rp);

		return !rp.isEmpty();
	}

	public DataSource getDataSource()
	{
		return _dataSource;
	}

	/**
	 * 返回被引号括起的标识符，如：[USER_TAB]。
	 * 
	 * @return
	 */
	public String getQuotedIdentifier(String idName)
	{
		String iqs = _sqlCatalog.getIdentifierQuoteString();

		if (idName.startsWith("@"))
		{
			return idName;
		}

		if (idName.startsWith(iqs) || idName.endsWith(iqs))
		{
			return idName;
		}

		String nid = null;
		StringTokenizer st = new StringTokenizer(idName, ",");
		while (st.hasMoreTokens())
		{
			if (nid == null)
			{
				nid = "";
			}
			else
			{
				nid += ",";
			}

			nid += iqs + st.nextToken() + iqs;
		}

		return nid;
	}

	/**
	 * 获取数据库适配器。
	 * 
	 * @return
	 */
	public SqlDialect getSqlDialect()
	{
		return _sqlDialect;
	}

	/**
	 * 获取数据库的目录。
	 * 
	 * @return
	 */
	public JdbcCatalog getJdbcCatalog()
	{
		return _sqlCatalog;
	}

	/**
	 * 执行自定义的连接操作。
	 * 
	 * @param handler
	 * @throws SQLException
	 */
	public void handle(ConnectionHandler handler) throws SQLException
	{
		Connection conn = null;

		try
		{
			conn = _dataSource.getConnection();
			if (conn != null)
				handler.handle(conn);
		}
		finally
		{
			if (conn != null)
			{
				conn.close();
			}
		}
	}

	public int insertRecord(String tableName, Map columns) throws Exception
	{
		return executeUpdate(new InsertSql(tableName, columns));
	}

	public int insertRecord(String tableName, Map columns,
			StatementHandler handler) throws Exception
	{
		return executeUpdate(new InsertSql(tableName, columns), handler);
	}

	public Map queryForObject(QuerySql sql) throws Exception
	{
		return queryForObject(sql, new JdbcObjectReader(this));
	}

	public <T> T queryForObject(QuerySql sql, ResultSetReader<T> reader)
			throws Exception
	{
		final ResultSetReader<T> finalReader = reader;
		final ObjectProxy<T> o = new ObjectProxy<T>();

		executeQuery(sql, new ResultSetHandler()
		{
			public void afterQuery(ResultSet rs) throws SQLException
			{
				if (rs.next())
				{
					o.setObject(finalReader.read(rs, 0));
				}
			}
		});

		return o.getObject();
	}

	public List queryForObjects(QuerySql sql) throws Exception
	{
		return queryForObjects(sql, new JdbcObjectReader(this));
	}

	public List queryForObjects(QuerySql sql, int beginning, int size)
			throws Exception
	{
		return queryForObjects(sql, beginning, size, new JdbcObjectReader(this));
	}

	public <T> List<T> queryForObjects(QuerySql sql, ResultSetReader<T> reader)
			throws Exception
	{
		return queryForObjects(sql, 1, -1, reader);
	}

	public <T> List<T> queryForObjects(QuerySql sql, int beginning, int size,
			ResultSetReader<T> reader) throws Exception
	{
		final ResultSetReader<T> finalReader = reader;
		final List<T> os = new ArrayList<T>();

		executeQuery(sql, beginning, size, new ResultSetHandler()
		{
			public void afterQuery(ResultSet rs) throws SQLException
			{
				int row = 0;

				while (rs.next())
				{
					os.add(finalReader.read(rs, row));
					row++;
				}
			}
		});

		return os;
	}

	public int updateRecords(String tableName, Map columns,
			StringSql whereFilter) throws Exception
	{
		return executeUpdate(new UpdateSql(tableName, columns, whereFilter));
	}

	public Pageable createQuery(QuerySql sql) throws Exception
	{
		final QuerySql finalSql = sql;

		return new Pageable<Serializable>()
		{

			public List list(int beginning, int size) throws Exception
			{
				return queryForObjects(finalSql, beginning, size);
			}

			public int size() throws Exception
			{
				return countRecords(finalSql);
			}
		};
	}

	public <T> Pageable<T> createQuery(QuerySql sql, ResultSetReader<T> reader)
			throws Exception
	{
		final QuerySql finalSql = sql;
		final ResultSetReader<T> finalReader = reader;

		return new Pageable<T>()
		{

			public List<T> list(int beginning, int size) throws Exception
			{
				return queryForObjects(finalSql, beginning, size, finalReader);
			}

			public int size() throws Exception
			{
				return countRecords(finalSql);
			}
		};
	}
}