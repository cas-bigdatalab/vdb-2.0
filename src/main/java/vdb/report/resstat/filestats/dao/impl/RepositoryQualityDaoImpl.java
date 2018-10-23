package vdb.report.resstat.filestats.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.report.resstat.filestats.dao.RepositoryQualityDao;
import vdb.report.resstat.filestats.vo.FileIndicator;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;
import vdb.report.resstat.util.DateUtil;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.ResultSetReader;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class RepositoryQualityDaoImpl implements RepositoryQualityDao
{
	private JdbcSource jdbcsource;

	/**
	 * 构造方法
	 * 
	 * @param uri
	 *            数据集的URI
	 */
	public RepositoryQualityDaoImpl(String uri)
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

	public List<RepositoryIndicator> getRepositoryIndicatorList(String uri)
	{
		List<RepositoryIndicator> riList = null;
		final DataSet ds = VdbManager.getEngine().getCatalog().fromUri(uri);

		try
		{
			// 取最新的一条记录
			String lastStatsTime = DateUtil.getTodayAsString();
			List<String> list = jdbcsource.queryForObjects(new StringSql(
					"select LASTSTATSTIME from VDB_DQ_REPOSITORY order by LASTSTATSTIME"),
					1, 1, new ResultSetReader<String>()
					{
						public String read(ResultSet rs, int row)
								throws SQLException
						{
							return rs.getString(1);
						}

					});
			if (list != null && list.size() != 0)
			{
				lastStatsTime = list.get(0);
			}

			String sql = "select * from VDB_DQ_REPOSITORY where DSURI=? and LASTSTATSTIME = '"+lastStatsTime+"'";
			riList = jdbcsource.queryForObjects(
					new StringSql(sql, ds.getUri()),
					new ResultSetReader<RepositoryIndicator>()
					{
						public RepositoryIndicator read(ResultSet rs, int row)
								throws SQLException
						{
							int rid = 0;
							final RepositoryIndicator indicator = new RepositoryIndicator();
							rid = rs.getInt("RID");
							indicator.setDsid(rs.getString("DSID"));
							indicator.setDatasetUri(rs.getString("DSURI"));
							indicator.setRepositoryName(rs
									.getString("REPOSITORYNAME"));
							indicator.setDirectoryNum(rs
									.getLong("DIRECTORYNUM"));
							indicator.setMaxFileBytes(rs
									.getLong("MAXFILEBYTES"));
							indicator.setMinFileBytes(rs
									.getLong("MINFILEBYTES"));
							indicator.setMaxDirDepth(rs.getInt("MAXDIRDEPTH"));
							indicator.setLastStatsTime(rs
									.getString("LASTSTATSTIME"));
							try
							{
								// 根据数据库指标ID找实体指标
								String fileSql = "select * from VDB_DQ_FILE where RID=?";
								List<FileIndicator> fileIndicatorList = jdbcsource
										.queryForObjects(
												new StringSql(fileSql, rid),
												new ResultSetReader<FileIndicator>()
												{
													public FileIndicator read(
															ResultSet rs,
															int row)
															throws SQLException
													{
														final FileIndicator fi = new FileIndicator();
														fi
																.setRepositoryIndicator(indicator);
														fi
																.setContentType(rs
																		.getString("CONTENTTYPE"));
														fi
																.setFileExtension(rs
																		.getString("FILEEXTENSION"));
														fi
																.setBytes(rs
																		.getLong("BYTES"));
														fi
																.setItems(rs
																		.getLong("ITEMS"));
														fi
																.setLastStatsTime(rs
																		.getString("LASTSTATSTIME"));
														return fi;
													}
												});
								if (fileIndicatorList != null
										&& fileIndicatorList.size() != 0)
								{
									long bytes = 0;
									long items = 0;
									Map<String, FileIndicator> map = new HashMap<String, FileIndicator>();
									for (FileIndicator fileIndicator : fileIndicatorList)
									{
										map.put(fileIndicator
												.getFileExtension(),
												fileIndicator);
										bytes = bytes
												+ fileIndicator.getBytes();
										items = items
												+ fileIndicator.getItems();
									}
									indicator.setBytes(bytes);
									indicator.setItems(items);
									indicator.setFileIndicatorMap(map);
								}
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
			return null;
		}
		return riList;
	}
}
