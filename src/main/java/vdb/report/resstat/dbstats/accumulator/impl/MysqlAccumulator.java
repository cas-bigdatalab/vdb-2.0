package vdb.report.resstat.dbstats.accumulator.impl;

import java.io.Serializable;
import java.util.Map;

import vdb.report.resstat.dbstats.accumulator.Accumulator;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class MysqlAccumulator implements Accumulator
{
	public long getFieldBytes(String tableName, String fieldName,
			JdbcSource jdbcSource)
	{
		try
		{
			QuerySql sql = new StringSql("select sum(length(" + fieldName
					+ ")) as length_alias from " + tableName);
			Map<String, Serializable> map = jdbcSource.queryForObject(sql);
			long length = 0;
			if (map.get("length_alias") != null)
				length = Long.parseLong(map.get("length_alias").toString());
			return length;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	public long getNullDataNumOfField(String tableName, String fieldName,
			JdbcSource jdbcSource)
	{
		try
		{
			QuerySql sql = new StringSql("select count(*) as count_alias from "
					+ tableName + " where " + fieldName + " is null or length("
					+ fieldName + ") =0");
			Map<String, Serializable> map = jdbcSource.queryForObject(sql);
			long count = 0;
			if (map.get("count_alias") != null)
				count = Long.parseLong(map.get("count_alias").toString());
			return count;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	public long getTableRecordNum(String tableName, JdbcSource jdbcSource)
	{
		try
		{
			QuerySql sql = new StringSql("select count(*) as count_alias from "
					+ tableName);
			Map<String, Serializable> map = jdbcSource.queryForObject(sql);
			long count = 0;
			if (map.get("count_alias") != null)
				count = Long.parseLong(map.get("count_alias").toString());
			return count;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
}
