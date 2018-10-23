/*
 * 创建日期 2005-9-8
 */
package cn.csdb.commons.sql.dialect;

import java.text.MessageFormat;

import cn.csdb.commons.sql.JdbcSource;

/*
 * @author bluejoe
 */
public class HsqlDBDialect extends XDialect
{
	public String sqlForBlockQuery(String sql, int beginning, int size)
	{
		// beginning从1开始计数
		return MessageFormat.format("{0} limit {2} OFFSET {1}", new Object[] { sql,
				"" + (beginning - 1), "" + size });
	}
}