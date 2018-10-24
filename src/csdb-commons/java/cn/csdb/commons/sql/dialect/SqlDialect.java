/*
 * 创建日期 2005-9-8
 */
package cn.csdb.commons.sql.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/*
 * SQLAdapter用以适配不同的jdbc driver
 * 
 * @author bluejoe
 */
public interface SqlDialect
{
	/**
	 * 创建查询语句
	 * 
	 * @param conn
	 * @param isFullQuery
	 *            查询所有记录？还是分页？
	 * @param isReadOnly
	 *            查询出来的结果要修改吗？
	 * @return
	 * @throws SQLException
	 */
	public Statement createQueryStatement(Connection conn, boolean isFullQuery,
			boolean isReadOnly) throws SQLException;

	public List getColumns(Connection conn, String schemaName, String tableName)
			throws SQLException;

	/**
	 * 从result set中获取一个字段的对象，如：DATE-->Date
	 */
	public Object getObject(ResultSet rs, int i) throws SQLException;

	/**
	 * 从result set中获取一个字段的文本值，所有值将转换成String类型
	 */
	public String getString(ResultSet rs, int i) throws SQLException;

	public List getTables(Connection conn) throws SQLException;

	public PreparedStatement prepareQueryStatement(Connection conn, String sql,
			boolean isFullQuery, boolean isReadOnly) throws SQLException;

	/**
	 * 用以分页查询的sql，如：LIMIT/TOP/... beginning从1开始计数，如果语言上不支持分页，则返回null
	 */
	public String sqlForBlockQuery(String sql, int beginning, int size);

	/**
	 * 用以统计记录数的sql，如：select count(*) ...
	 */
	public String sqlForCount(String sql);

	/**
	 * called by prepared statement
	 */
	public void updateObject(PreparedStatement st, int i, Object o)
			throws SQLException;

	/**
	 * called by result set
	 */
	public void updateObject(ResultSet rs, int i, Object o) throws SQLException;

	/**
	 * 将字段值转化为正确类型，以构造sql语句
	 */
	public String updateString(Object o) throws SQLException;

	public List getPrimaryKeys(Connection conn, String tableName)
			throws SQLException;

	public String getIdentifierQuoteString(DatabaseMetaData databaseMetaData)
			throws SQLException;
}