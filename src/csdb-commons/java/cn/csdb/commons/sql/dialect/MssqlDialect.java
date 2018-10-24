/*
 * Created on 2006-6-24
 */
package cn.csdb.commons.sql.dialect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class MssqlDialect extends XDialect
{
	private static int getSqlAfterSelectInsertPoint(String sql)
	{
		int selectIndex = sql.toLowerCase().indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase().indexOf(
				"select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

	@Override
	public String sqlForBlockQuery(String sql, int beginning, int size)
	{
		// top N is ok
		if (beginning == 1)
		{
			return new StringBuffer(sql.length() + 8).append(sql).insert(
					getSqlAfterSelectInsertPoint(sql), " top " + size)
					.toString();
		}

		int orderByIndex = sql.toLowerCase().lastIndexOf("order by");

		if (orderByIndex <= 0)
		{

			Logger
					.getLogger(this.getClass())
					.warn(
							"must specify 'order by' statement to support limit operation with offset in sql server 2005");

			return null;
		}

		String sqlOrderBy = sql.substring(orderByIndex + 8);
		String sqlRemoveOrderBy = sql.substring(0, orderByIndex);

		int insertPoint = getSqlAfterSelectInsertPoint(sql);
		return new StringBuffer(sql.length() + 100).append(
				"with tempPagination as(").append(sqlRemoveOrderBy)
				.insert(
						insertPoint + 23,
						" ROW_NUMBER() OVER(ORDER BY " + sqlOrderBy
								+ ") as RowNumber,").append(
						") select * from tempPagination where RowNumber between "
								+ beginning + " and " + (beginning + size - 1))
				.toString();
	}

	public Statement createQueryStatement(Connection conn, boolean isFullQuery,
			boolean isReadOnly) throws SQLException
	{
		return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
	}

	public PreparedStatement prepareQueryStatement(Connection conn, String sql,
			boolean isFullQuery, boolean isReadOnly) throws SQLException
	{
		return conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
	}
}
