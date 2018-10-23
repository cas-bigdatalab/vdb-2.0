package vdb.report.resstat.dbstats.dao;

import vdb.report.resstat.dbstats.vo.DbIndicator;

public interface DbQualityDao
{
	public DbIndicator getDbIndicator(String dsuri);
}
