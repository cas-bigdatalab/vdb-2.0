package vdb.report.resstat.dbstats.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.report.resstat.dbstats.dao.DbQualityDao;
import vdb.report.resstat.dbstats.vo.DbIndicator;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.resstat.dbstats.vo.FieldIndicator;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DbQualityDaoImpl implements DbQualityDao
{

	private JdbcSource jdbcsource;

	/**
	 * 构造方法
	 * 
	 * @param uri
	 *            数据集的URI
	 */
	public DbQualityDaoImpl(String uri)
	{
		try
		{
			DataSet ds = (DataSet) VdbManager.getEngine().getCatalog().fromUri(
					uri);
			jdbcsource = JdbcSourceManager.getInstance().getReportsJdbcSource(
					ds);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 获得数据集统计指标信息
	 * 
	 * @param uri
	 *            数据集uri
	 */
	public DbIndicator getDbIndicator(String uri)
	{

		List<DbIndicator> dsinList = null;
		final DataSet ds = VdbManager.getEngine().getCatalog().fromUri(uri);

		try
		{
			// 取最新的一条记录
			String sql = "select * from VDB_DQ_DATASET where DSID=? order by DQID DESC";
			dsinList = jdbcsource.queryForObjects(new StringSql(sql, ds
					.getId()), 1, 1, new ResultSetReader<DbIndicator>()
			{
				public DbIndicator read(ResultSet rs, int row)
						throws SQLException
				{
					int dqid = 0;
					final DbIndicator indicator = new DbIndicator();
					dqid = rs.getInt("DQID");
					indicator.setDsid(rs.getString("DSID"));
					indicator.setBytes(rs.getLong("BYTES"));
					indicator.setRecordNum(rs.getLong("RECORDNUM"));
					indicator.setEntityNum(rs.getInt("ENTITYNUM"));
					indicator.setRelationNum(rs.getInt("RELATIONNUM"));
					indicator.setRelationRate(rs.getDouble("RELATIONRATE"));
					indicator.setFieldNum(rs.getInt("FIELDNUM"));
					indicator.setTextFieldNum(rs.getInt("TEXTFIELDNUM"));
					indicator.setIntegrityRate(rs.getFloat("INTEGRITYRATE"));
					indicator.setLastStatsTime(rs.getString("LASTSTATSTIME"));
					try
					{
						// 根据数据库指标ID找实体指标
						String entitySql = "select * from VDB_DQ_TABLE where DQID=?";
						List<EntityIndicator> entityIndicatorList = jdbcsource
								.queryForObjects(
										new StringSql(entitySql, dqid),
										new ResultSetReader<EntityIndicator>()
										{
											public EntityIndicator read(
													ResultSet rs, int row)
													throws SQLException
											{
												int dtid = 0;
												final EntityIndicator ei = new EntityIndicator();
												dtid = rs.getInt("DTID");
												ei.setDbIndicator(indicator);
												ei.setId(rs.getString("ENTITYID"));
												ei.setUri(rs.getString("ENTITYURI"));
												ei.setTitle(rs
														.getString("TITLE"));
												ei
														.setTableName(rs
																.getString("TABLENAME"));
												ei
														.setBytes(rs
																.getLong("BYTES"));
												ei.setRecordNum(rs
														.getLong("RECORDNUM"));
												ei
														.setIntegrityRate(rs
																.getDouble("INTEGRITYRATE"));
												ei
														.setLastStatsTime(rs
																.getString("LASTSTATSTIME"));

												try
												{
													String fieldSql = "select * from VDB_DQ_FIELD where DTID=?";
													List<FieldIndicator> fieldList = jdbcsource
															.queryForObjects(
																	new StringSql(
																			fieldSql,
																			dtid),
																	new ResultSetReader<FieldIndicator>()
																	{
																		public FieldIndicator read(
																				ResultSet rs,
																				int row)
																				throws SQLException
																		{
																			FieldIndicator fi = new FieldIndicator();
																			fi.setEntityIndicator(ei);
																			fi
																					.setId(rs
																							.getString("FIELDID"));
																			fi
																					.setName(rs
																							.getString("NAME"));
																			fi
																					.setTitle(rs
																							.getString("TITLE"));
																			fi.setIntegrityRate(rs
																							.getDouble("INTEGRITYRATE"));
																			fi
																					.setLastStatsTime(rs
																							.getString("LASTSTATSTIME"));
																			return fi;
																		}
																	});
													ei.setFieldIndicatorList(fieldList);
												}
												catch (Exception e)
												{
													e.printStackTrace();
												}
												return ei;
											}

										});
						indicator.setEntityIndicatorList(entityIndicatorList);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}

					return indicator;
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (dsinList != null && dsinList.size() != 0)
		{
			return dsinList.get(0);
		}
		else
		{
			return null;
		}
	}

}
