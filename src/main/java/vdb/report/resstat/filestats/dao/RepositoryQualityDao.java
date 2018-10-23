package vdb.report.resstat.filestats.dao;

import java.util.List;

import vdb.report.resstat.filestats.vo.RepositoryIndicator;

public interface RepositoryQualityDao
{
	public List<RepositoryIndicator> getRepositoryIndicatorList(String uri);
}
