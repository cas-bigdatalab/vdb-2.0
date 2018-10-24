/*
 * Created on 2005-7-27
 */
package cn.csdb.commons.sql.pool;

import java.io.PrintWriter;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

import javax.sql.DataSource;

import cn.csdb.commons.pool.Pool;
import cn.csdb.commons.pool.PoolProperties;
import cn.csdb.commons.pool.QPool;
import cn.csdb.commons.sql.JdbcManager;

/*
 * 数据库连接池，DBPool实现了对象池和DataSource的接口。
 * 
 * @author bluejoe
 */
public class JdbcPool implements Pool, DataSource
{
	private QPool _pool;

	private int _loginTimeout;

	/**
	 * @throws Exception
	 */
	public void destroy() throws Exception
	{
		_pool.destroy();
	}

	/**
	 * @param o
	 * @throws Exception
	 */
	public void freeObject(Object o) throws Exception
	{
		_pool.freeObject(o);
	}

	public Connection getConnection() throws SQLException
	{
		try
		{
			return new PooledConnection(this, (Connection) _pool.getObject());
		}
		catch (Throwable e)
		{
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	public Connection getConnection(String arg0, String arg1)
			throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	public int getIdleCount()
	{
		return _pool.getIdleCount();
	}

	public int getLoginTimeout() throws SQLException
	{
		return (int) (_pool.getConnectTimeout() / 1000);
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public Object getObject() throws Throwable
	{
		return _pool.getObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.pool.Pool#getPooledCount()
	 */
	public int getPooledCount()
	{
		return _pool.getPooledCount();
	}

	/**
	 * @return
	 */
	public PoolProperties getProperties()
	{
		return _pool.getProperties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.csdb.commons.sql.dbcpx.ConnectionPool#init(cn.csdb.commons.sql.dbcpx.PoolConfig)
	 */
	public void init(PoolProperties pp) throws Exception
	{
		_pool = new QPool(new ConnectionHolder());
		_pool.init(pp);

		JdbcManager.getInstance().getJdbcSource(this);
	}

	/**
	 * @throws Exception
	 */
	public void ping() throws Exception
	{
		_pool.ping();
	}

	public void setLoginTimeout(int arg0) throws SQLException
	{
		_pool.setConnectTimeout(arg0 * 1000);
	}

	public void setLogWriter(PrintWriter arg0) throws SQLException
	{
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}

/**
 * @author bluejoe
 */
class PooledConnection implements Connection
{
	private JdbcPool _pool;

	private Connection _rawConnection;

	public PooledConnection(JdbcPool pool, Connection rawConnection)
	{
		_pool = pool;
		_rawConnection = rawConnection;
	}

	/**
	 * @throws java.sql.SQLException
	 */
	public void clearWarnings() throws SQLException
	{
		_rawConnection.clearWarnings();
	}

	/**
	 * @throws java.sql.SQLException
	 */
	public void close() throws SQLException
	{
		try
		{
			_pool.freeObject(_rawConnection);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * @throws java.sql.SQLException
	 */
	public void commit() throws SQLException
	{
		_rawConnection.commit();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Statement createStatement() throws SQLException
	{
		return _rawConnection.createStatement();
	}

	/**
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException
	{
		return _rawConnection.createStatement(resultSetType,
				resultSetConcurrency);
	}

	/**
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return _rawConnection.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean getAutoCommit() throws SQLException
	{
		return _rawConnection.getAutoCommit();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public String getCatalog() throws SQLException
	{
		return _rawConnection.getCatalog();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getHoldability() throws SQLException
	{
		return _rawConnection.getHoldability();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public DatabaseMetaData getMetaData() throws SQLException
	{
		return _rawConnection.getMetaData();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public int getTransactionIsolation() throws SQLException
	{
		return _rawConnection.getTransactionIsolation();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Map getTypeMap() throws SQLException
	{
		return _rawConnection.getTypeMap();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public SQLWarning getWarnings() throws SQLException
	{
		return _rawConnection.getWarnings();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean isClosed() throws SQLException
	{
		return _rawConnection.isClosed();
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public boolean isReadOnly() throws SQLException
	{
		return _rawConnection.isReadOnly();
	}

	/**
	 * @param sql
	 * @return
	 * @throws java.sql.SQLException
	 */
	public String nativeSQL(String sql) throws SQLException
	{
		return _rawConnection.nativeSQL(sql);
	}

	/**
	 * @param sql
	 * @return
	 * @throws java.sql.SQLException
	 */
	public CallableStatement prepareCall(String sql) throws SQLException
	{
		return _rawConnection.prepareCall(sql);
	}

	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return
	 * @throws java.sql.SQLException
	 */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return _rawConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency);
	}

	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	 * @return
	 * @throws java.sql.SQLException
	 */
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return _rawConnection.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	/**
	 * @param sql
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String sql) throws SQLException
	{
		return _rawConnection.prepareStatement(sql);
	}

	/**
	 * @param sql
	 * @param autoGeneratedKeys
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException
	{
		return _rawConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException
	{
		return _rawConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency);
	}

	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @param resultSetHoldability
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException
	{
		return _rawConnection.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability);
	}

	/**
	 * @param sql
	 * @param columnIndexes
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException
	{
		return _rawConnection.prepareStatement(sql, columnIndexes);
	}

	/**
	 * @param sql
	 * @param columnNames
	 * @return
	 * @throws java.sql.SQLException
	 */
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException
	{
		return _rawConnection.prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException {
		return null;
	}

	public Blob createBlob() throws SQLException {
		return null;
	}

	public NClob createNClob() throws SQLException {
		return null;
	}

	public SQLXML createSQLXML() throws SQLException {
		return null;
	}

	public boolean isValid(int timeout) throws SQLException {
		return false;
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {

	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {

	}

	public String getClientInfo(String name) throws SQLException {
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		return null;
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return null;
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return null;
	}

	public void setSchema(String schema) throws SQLException {

	}

	public String getSchema() throws SQLException {
		return null;
	}

	public void abort(Executor executor) throws SQLException {

	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

	}

	public int getNetworkTimeout() throws SQLException {
		return 0;
	}

	/**
	 * @param savepoint
	 * @throws java.sql.SQLException
	 */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException
	{
		_rawConnection.releaseSavepoint(savepoint);
	}

	/**
	 * @throws java.sql.SQLException
	 */
	public void rollback() throws SQLException
	{
		_rawConnection.rollback();
	}

	/**
	 * @param savepoint
	 * @throws java.sql.SQLException
	 */
	public void rollback(Savepoint savepoint) throws SQLException
	{
		_rawConnection.rollback(savepoint);
	}

	/**
	 * @param autoCommit
	 * @throws java.sql.SQLException
	 */
	public void setAutoCommit(boolean autoCommit) throws SQLException
	{
		_rawConnection.setAutoCommit(autoCommit);
	}

	/**
	 * @param catalog
	 * @throws java.sql.SQLException
	 */
	public void setCatalog(String catalog) throws SQLException
	{
		_rawConnection.setCatalog(catalog);
	}

	/**
	 * @param holdability
	 * @throws java.sql.SQLException
	 */
	public void setHoldability(int holdability) throws SQLException
	{
		_rawConnection.setHoldability(holdability);
	}

	/**
	 * @param readOnly
	 * @throws java.sql.SQLException
	 */
	public void setReadOnly(boolean readOnly) throws SQLException
	{
		_rawConnection.setReadOnly(readOnly);
	}

	/**
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Savepoint setSavepoint() throws SQLException
	{
		return _rawConnection.setSavepoint();
	}

	/**
	 * @param name
	 * @return
	 * @throws java.sql.SQLException
	 */
	public Savepoint setSavepoint(String name) throws SQLException
	{
		return _rawConnection.setSavepoint(name);
	}

	/**
	 * @param level
	 * @throws java.sql.SQLException
	 */
	public void setTransactionIsolation(int level) throws SQLException
	{
		_rawConnection.setTransactionIsolation(level);
	}

	/**
	 * @param map
	 * @throws java.sql.SQLException
	 */
	public void setTypeMap(Map map) throws SQLException
	{
		_rawConnection.setTypeMap(map);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}
}
