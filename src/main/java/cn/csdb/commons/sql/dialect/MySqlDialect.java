/*
 * 创建日期 2005-9-8
 */
package cn.csdb.commons.sql.dialect;

import java.sql.Types;
import java.text.MessageFormat;

import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.types.ResultSetColumn;
import cn.csdb.commons.util.StringUtils;

/*
 * @author bluejoe
 */
public class MySqlDialect extends XDialect
{
	public String sqlForBlockQuery(String sql, int beginning, int size)
	{
		// beginning从1开始计数
		return MessageFormat.format("{0} limit {1},{2}", new Object[] { sql,
				"" + (beginning - 1), "" + size });
	}

	protected String escapeString(ResultSetColumn column, String s)
	{
		if (s == null)
			return "NULL";

		switch (column.getColumnType())
		{
			case Types.CLOB:
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
				return "'"
						+ StringUtils.strictReplaceAll(StringUtils
								.strictReplaceAll(s, "\'", "''"), "\\", "\\\\")
						+ "'";
			case Types.DATE:
			case Types.TIME:
			case Types.TIMESTAMP:
				return "'" + s + "'";
		}

		return s;
	}
}