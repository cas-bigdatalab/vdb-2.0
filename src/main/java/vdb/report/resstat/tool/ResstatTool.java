package vdb.report.resstat.tool;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.report.resstat.dbstats.dao.DbQualityDao;
import vdb.report.resstat.dbstats.dao.impl.DbQualityDaoImpl;
import vdb.report.resstat.filestats.dao.RepositoryQualityDao;
import vdb.report.resstat.filestats.dao.impl.RepositoryQualityDaoImpl;
import vdb.report.resstat.util.IndicatorToStringUtil;
import vdb.report.resstat.vo.DatasetIndicator;

public class ResstatTool
{
	/**
	 * 得到数据集资源量统计指标，包括数据库统计指标和文件统计指标
	 * 
	 * @param uri
	 *            数据集URI
	 * @return
	 */
	public DatasetIndicator getDatasetIndicator(String uri)
	{
		DataSet ds = VdbManager.getEngine().getCatalog().fromUri(uri);

		DatasetIndicator di = new DatasetIndicator();
		di.setUri(ds.getUri());
		di.setTitle(ds.getTitle());
		di.setDsid(ds.getId());
		di.setDbms(ds.getRepository().getProductName());
		di.setDescription(ds.getDescription());

		DbQualityDao dbDao = new DbQualityDaoImpl(uri);
		di.setDbIndicator(dbDao.getDbIndicator(uri));

		RepositoryQualityDao fqDao = new RepositoryQualityDaoImpl(uri);
		di.setRepositoryIndicatorList(fqDao.getRepositoryIndicatorList(uri));

		return di;
	}

	/**
	 * 得到数据集的云图字符串
	 * 
	 * @param uri
	 *            数据集的URI
	 * @return
	 */
	public String getCloudString(String uri)
	{
		return IndicatorToStringUtil.getDataSetCloud(uri);
	}
}
