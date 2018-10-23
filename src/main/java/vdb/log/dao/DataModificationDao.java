package vdb.log.dao;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import vdb.log.vo.DataModifications;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.report.util.DateUtil;
import vdb.tool.generic.FormatTool;
import cn.csdb.commons.jsp.Pageable;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.QuerySql;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DataModificationDao
{

	public Pageable<DataModifications> getEntityModifications(Entity entity,
			Date startDate, Date endDate)
	{
		try
		{
			final String entityUri = entity.getUri();
			FormatTool ft = new FormatTool();
			String ssd = ft.formatDate(startDate);
			String sed = ft.formatDate(endDate);
			JdbcSource jdbcSource = getJdbcSource(entity.getDataSet());
			String ssql = "select OPERATION,PARAM2 from LOGRECORDS where ENTITY = ? "
					+ " and OP_TIME > ? and OP_TIME < ? "
					+ " and (OPERATION = 'update' or OPERATION = 'insert' or OPERATION = 'delete') ";
			QuerySql sql = new StringSql(ssql, entity.getId(), ssd, sed);
			return jdbcSource.createQuery(sql,
					new ResultSetReader<DataModifications>()
					{
						public DataModifications read(ResultSet rs, int row)
								throws SQLException
						{
							String operation = rs.getString("OPERATION");
							String op = "";
							if ("update".equals(operation))
							{
								op = "U";
							}
							else if ("insert".equals(operation))
							{
								op = "I";
							}
							else if ("delete".equals(operation))
							{
								op = "D";
							}
							return new DataModifications(entityUri, rs
									.getString("PARAM2"), op);

						}
					});

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public Date getDataSetLastModificationTime(String datasetUri)
	{
		try
		{
			DataSet dataset = VdbManager.getEngine().getCatalog().fromUri(
					datasetUri);

			String ssql = "select top(1) OP_TIME from LOGRECORDS where DATASET = ? "
					+ " and (OPERATION = 'update' or OPERATION = 'insert' or OPERATION = 'delete') "
					+ " order by OP_TIME desc";
			QuerySql sql = new StringSql(ssql, dataset.getId());
			Map<String, Serializable> map = getJdbcSource(dataset)
					.queryForObject(sql);
			String sdate = map.get("OP_TIME").toString().substring(0, 19);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = sdf.parse(sdate);
			return date;

		}
		catch (Exception e)
		{
			return null;
		}
	}

	public Date getEntityLastModificationTime(String entityUri)
	{
		try
		{
			Entity entity = VdbManager.getEngine().getCatalog().fromUri(
					entityUri);

			String ssql = "select top(1) OP_TIME from LOGRECORDS where ENTITY = ? "
					+ " and (OPERATION = 'update' or OPERATION = 'insert' or OPERATION = 'delete') "
					+ " order by OP_TIME desc";
			QuerySql sql = new StringSql(ssql, entity.getId());
			Map<String, Serializable> map = getJdbcSource(entity.getDataSet())
					.queryForObject(sql);
			String sdate = map.get("OP_TIME").toString().substring(0, 19);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = sdf.parse(sdate);
			return date;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * This method get last modification date of dataset and format is
	 * YYYY-MM-DD HH:MM:SS.
	 * 
	 * @author jack
	 * @param entityUri
	 *            URI of entity like cn.csdb.*
	 * @return String
	 * 
	 */
	public String getStrByEntityLastModificationTime(String entityUri)
	{
		Date d = getDataSetLastModificationTime(entityUri);
		return (DateUtil.formatDate2ChineseCustom(d));
	}

	private JdbcSource getJdbcSource(DataSet dataset) throws Exception
	{
		return JdbcSourceManager.getInstance().getLogsJdbcSource(dataset);
	}

}
