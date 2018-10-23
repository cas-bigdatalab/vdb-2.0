/*
 * 创建日期 2005-9-8
 */
package cn.csdb.commons.sql.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.csdb.commons.util.SqlUtils;

/*
 * @author bluejoe
 */
public class PostgreSQLDialect extends XDialect
{
	public String sqlForBlockQuery(String sql, int beginning, int size)
	{
		// beginning从1开始计数
		return MessageFormat.format("{0} limit {2} offset {1}", new Object[] {
				sql, "" + (beginning - 1), "" + size });
	}

	@Override
	public String getIdentifierQuoteString(DatabaseMetaData databaseMetaData)
			throws SQLException
	{
		return "";
	}

	@Override
	public List getTables(Connection conn) throws SQLException
	{
		List<Map> tables = super.getTables(conn);
		for (Map table : tables)
		{
			String tableName = (String) table.get("TABLE_NAME");
			String schemaName = (String) table.get("TABLE_SCHEM");
			String fullTableName = tableName;

			if (schemaName != null)
			{
				fullTableName = schemaName + "." + fullTableName;
			}

			table.put("TABLE_NAME", fullTableName);
			table.put("SHORT_TABLE_NAME", tableName);
			table.put("FULL_TABLE_NAME", fullTableName);
		}

		return tables;
	}
}