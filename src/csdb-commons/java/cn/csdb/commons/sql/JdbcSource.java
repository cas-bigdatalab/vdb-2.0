package cn.csdb.commons.sql;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.catalog.JdbcCatalog;
import cn.csdb.commons.sql.dialect.SqlDialect;
import cn.csdb.commons.sql.jdbc.ConnectionHandler;
import cn.csdb.commons.sql.jdbc.ResultSetHandler;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.StatementHandler;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.Sql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

/*
 * DataSource的助手类，JdbcSource封装了常用的数据库操作。
 * 
 * <p> JdbcSource还负责读取DataSource的物理连接信息，即JdbcCatalog。
 * 你可以通过调用getJdbcCatalog()来获取物理连接的目录信息，其中会包括库、表、字段的元数据信息。
 * <p>使用JdbcManager.getJdbcSource()来获取一个JdbcSource对象，相同的JdbcSource对象会被重复使用。 <p>
 * JdbcSource封装了常见的SQL操作，包括记录的查询、增加、删除、修改，等等。
 * 如果你觉得这些方法不够用的话，你可以使用JdbcSource.createStatement()或者prepareStatement()
 * 方法来完成更多的功能。如果……这些还不够用的话，怎么办呢？ <p>
 * JdbcSource提供了handleConnection(connectionHandler)方法，允许你对DataSource的连接对象做任何
 * 操作（除了关闭它）。该方法的参数是一个需要实现ConnectionHandler接口的对象。 <p>
 * JdbcSource故意没有提供getConnection()方法，这样可以避免你直接与连接池
 * 打交道。你要做的就是调用handleConnection()方法，JdbcSource会为你准备好连接对象，并在你使用完毕之后关闭它。 <p>
 * JdbcSource的设计遵循以下原则： <br> 1.
 * 接受更多类型的参数；如：针对insert/update操作，你可以扔进去一个足够变态的数据类型，譬如
 * 往一个BLOB字段里面扔进了一个File对象，或者往一个日期字段里面扔进了一个Calendar类型，只要有可能，JdbcSource都会设法消化了它；
 * <br> 2. 返回更少类型的结果；如：针对query返回的字段值，JdbcSource统一了几种sql类型，对应于文本/
 * 数值/日期/二进制，JdbcSource分别返回String/Number/Date/byte[]类型，请勿试图采用诸如Blob或者
 * long/boolean这样的类型转换来获取字段值； <br> 3.
 * 提供更安全的预检机制；JdbcSource会针对你扔进去的字段做筛选和类型校验，如果你将"abc"扔给一
 * 个int列，会带来一个异常。如果这个列根本就不存在，JdbcSource则会忽略掉该列的赋值； <br> 4.
 * 提供更详尽的异常信息：如果不幸发生了SQL异常，JdbcSource会尽其所能，将异常定位到发生现场的 数据库、表甚至字段上； <p>
 * 更多参数的含义和类型请参见每个函数的注释。
 */
public interface JdbcSource
{
	public int countRecords(QuerySql sql) throws Exception;

	public int deleteRecords(String tableName, StringSql whereFilter)
			throws Exception;

	public void executeQuery(QuerySql sql, ResultSetHandler handler)
			throws SQLException;

	public void executeQuery(QuerySql sql, int beginning, int size,
			ResultSetHandler handler) throws SQLException;

	public int executeUpdate(Sql sql) throws Exception;

	public int executeUpdate(Sql sql, StatementHandler handler)
			throws Exception;

	public boolean existsRecord(QuerySql sql) throws Exception;

	public DataSource getDataSource();

	public String getQuotedIdentifier(String idName);

	public SqlDialect getSqlDialect();

	public JdbcCatalog getJdbcCatalog();

	public void handle(ConnectionHandler handler) throws SQLException;

	public int insertRecord(String tableName, Map columns) throws Exception;

	public int insertRecord(String tableName, Map columns,
			StatementHandler handler) throws Exception;

	public Map<String, Serializable> queryForObject(QuerySql sql)
			throws Exception;

	public <T> T queryForObject(QuerySql sql, ResultSetReader<T> reader)
			throws Exception;

	public List<Map<String, Serializable>> queryForObjects(QuerySql sql)
			throws Exception;

	public <T> List<T> queryForObjects(QuerySql sql, ResultSetReader<T> reader)
			throws Exception;

	public List<Map<String, Serializable>> queryForObjects(QuerySql sql,
			int beginning, int size) throws Exception;

	public <T> List<T> queryForObjects(QuerySql sql, int beginning, int size,
			ResultSetReader<T> reader) throws Exception;

	public Pageable<Map<String, Serializable>> createQuery(QuerySql sql)
			throws Exception;

	public <T> Pageable<T> createQuery(QuerySql sql, ResultSetReader<T> reader)
			throws Exception;

	public int updateRecords(String tableName, Map columns,
			StringSql whereFilter) throws Exception;
}