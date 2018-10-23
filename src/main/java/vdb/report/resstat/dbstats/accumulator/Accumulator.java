package vdb.report.resstat.dbstats.accumulator;

import cn.csdb.commons.sql.JdbcSource;

public interface Accumulator
{
	/**
	 * 获得指定数据表的大小
	 * 
	 * @param tableName
	 *            数据表名称
	 * @param fieldName
	 *            字段名称
	 * @param jdbcSource
	 *            数据库连接对象
	 * @return 数据表的字节数
	 */
	public long getFieldBytes(String tableName, String fieldName,
			JdbcSource jdbcSource);

	/**
	 * 获得指定数据表的记录数
	 * 
	 * @param tableName
	 *            数据表名称
	 * @param jdbcSource
	 *            数据库连接对象
	 * @return 数据表的记录数
	 */
	public long getTableRecordNum(String tableName, JdbcSource jdbcSource);

	/**
	 * 获得指定数据表指定字段数据为空的行数，记录为0时返回-1
	 * 
	 * @param tableName
	 *            数据表名
	 * @param fieldName
	 *            字段名称
	 * @param jdbcSource
	 *            数据库连接对象
	 * @return
	 */
	public long getNullDataNumOfField(String tableName, String fieldName,
			JdbcSource jdbcSource);
}
