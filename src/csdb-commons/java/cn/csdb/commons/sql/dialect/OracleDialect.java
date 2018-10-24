/*
 * 创建日期 2005-9-8
 */
package cn.csdb.commons.sql.dialect;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.MessageFormat;

import cn.csdb.commons.sql.types.ResultSetColumn;
import cn.csdb.commons.util.StringUtils;

/*
 * @author bluejoe
 */
public class OracleDialect extends XDialect
{
	public String getSchemaName(Connection conn) throws SQLException
	{
		return conn.getMetaData().getUserName();
	}

	public String sqlForBlockQuery(String sql, int beginning, int size)
	{
		return MessageFormat
				.format(
						"select * from (select T_{0,number,0}.*, rownum as F_{0,number,0} from ({1}) T_{0,number,0} where rownum <{3,number,0}) where F_{0,number,0}>={2,number,0}",
						new Object[] { new Long(System.currentTimeMillis()),
								sql, new Integer(beginning),
								new Integer(beginning + size) });
	}

	/**
	 * 将字段值转化为正确类型
	 * 
	 * @param o
	 *            字段值
	 * @param sqlType
	 *            字段类型
	 * @return 正确类型的字段值
	 */
	protected String escapeString(ResultSetColumn column, String s)
	{
		if (s == null)
			return "NULL";

		switch (column.getColumnType())
		{
			case Types.CLOB:
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
				return "'" + StringUtils.strictReplaceAll(s, "\'", "''") + "'";
			case Types.DATE:
				return "TO_DATE('" + s + "','YYYY-MM-DD')";
			case Types.TIME:
			case Types.TIMESTAMP:
				return "TO_DATE('" + s + "','YYYY-MM-DD HH24:MI:SS')";
		}

		return s;
	}
}