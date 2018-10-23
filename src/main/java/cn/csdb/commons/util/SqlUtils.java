/*
 * 创建日期 2005-5-22
 */
package cn.csdb.commons.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;

import cn.csdb.commons.pool.PoolManager;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.catalog.JdbcColumn;
import cn.csdb.commons.sql.catalog.JdbcTable;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.sql.types.JdbcObject;

/**
 * @author bluejoe
 */
public class SqlUtils
{
	public static void debugParameters(StringSql sql)
	{
		Level level = PoolManager.getInstance().getLogger().getEffectiveLevel();
		if (level != null && Level.DEBUG.isGreaterOrEqual(level))
		{
			List ps = sql.getParameters();

			if (ps != null && ps.size() > 0)
			{
				PoolManager.getInstance().getLogger().debug(
						"SQL Parameters: " + ps);
			}
		}
	}

	/**
	 * 过滤掉不存在的字段，并包装成SQLObject
	 * 
	 * @param sqlTable
	 * @param columns
	 * @return
	 * @throws SQLException
	 */
	public static Map formatColumns(JdbcTable sqlTable, Map columns,
			boolean omitNulls, boolean omitPrimaryKeys,
			boolean omitAutoIncrementKeys) throws SQLException
	{
		Map formatted = new StringKeyMap();

		Iterator it = columns.entrySet().iterator();
		while (it.hasNext())
		{
			Entry me = (Entry) it.next();

			String name = (String) me.getKey();
			Object value = me.getValue();

			if (omitNulls && value == null)
				continue;

			// 指定字段不存在，过滤掉
			JdbcColumn field = sqlTable.getColumn(name);
			if (field == null)
				continue;

			if (omitPrimaryKeys && field.isPrimaryKey())
				continue;

			if (omitAutoIncrementKeys && field.isAutoIncrement())
				continue;

			formatted.put(field.getColumnName(), new JdbcObject(
					(Serializable) value, field));
		}

		return formatted;
	}

	public static JdbcTable getNotNullTable(JdbcSource sqlSource,
			String tableName) throws SQLException
	{
		tableName = tableName.replaceAll(sqlSource.getJdbcCatalog()
				.getIdentifierQuoteString(), "");
		JdbcTable table = sqlSource.getJdbcCatalog().getDatabase().getTable(
				tableName);
		if (table == null)
		{
			throw new SQLException(MessageFormat.format(
					"table `{0}` does not exist in database `{1}`",
					new Object[] { tableName,
							sqlSource.getJdbcCatalog().getCatalogName() }));
		}

		return table;
	}

	public static Map loadResultSetProperties(ResultSet rs) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		// 读取属性
		Map fdp = new StringKeyMap();
		for (int i = 1; i <= rsmd.getColumnCount(); i++)
		{
			String key = rsmd.getColumnName(i);
			String value = rs.getString(key);

			fdp.put(key, value);
		}

		return fdp;
	}
}