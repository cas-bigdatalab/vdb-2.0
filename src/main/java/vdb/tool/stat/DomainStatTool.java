package vdb.tool.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.EntityAccessRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.resstat.dbstats.vo.DbIndicator;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.resstat.filestats.vo.RepositoryIndicator;
import vdb.report.resstat.tool.ResstatTool;
import vdb.report.resstat.vo.DatasetIndicator;
import vdb.report.util.DateUtil;

public class DomainStatTool
{

	/**
	 * 获取域范围内所有数据集的数据量大小之和
	 * 
	 * @return
	 */
	public double getDomainDataSize()
	{
		ResstatTool tool = new ResstatTool();
		List<DatasetIndicator> list = new ArrayList<DatasetIndicator>();
		DatasetIndicator dsi;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			dsi = tool.getDatasetIndicator(ds.getUri());
			if (dsi != null)
				list.add(dsi);
		}

		double size = 0;
		DbIndicator dbIndicator;
		List<RepositoryIndicator> riIndicatorList;
		RepositoryIndicator riIndicator;
		for (DatasetIndicator indicator : list)
		{
			dbIndicator = indicator.getDbIndicator();
			if (dbIndicator != null)
			{
				size = size + dbIndicator.getBytes();
			}
			riIndicatorList = indicator.getRepositoryIndicatorList();
			if (riIndicatorList != null)
			{
				for (int i = 0; i < riIndicatorList.size(); i++)
				{
					riIndicator = riIndicatorList.get(i);
					size = size + riIndicator.getBytes();
				}

			}
		}
		return size;
	}

	/**
	 * 获取域范围内所有数据集的总记录数
	 * 
	 * @return
	 */
	public double getDomainTotalRecords()
	{
		ResstatTool tool = new ResstatTool();
		List<DatasetIndicator> list = new ArrayList<DatasetIndicator>();
		DatasetIndicator dsi;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			dsi = tool.getDatasetIndicator(ds.getUri());
			if (dsi != null)
				list.add(dsi);
		}

		double items = 0;
		DbIndicator dbIndicator;
		for (DatasetIndicator indicator : list)
		{
			dbIndicator = indicator.getDbIndicator();
			if (dbIndicator != null)
				items = items + dbIndicator.getRecordNum();
		}
		return items;
	}

	/**
	 * 获取域的空间总体利用情况
	 * 
	 * @return
	 */
	public double getDomainUtilization()
	{
		ResstatTool tool = new ResstatTool();

		int allFieldsSize = 0;
		int usedFieldsSize = 0;
		DatasetIndicator dsi;
		DbIndicator indicator;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			dsi = tool.getDatasetIndicator(ds.getUri());
			// 如果数据集质量指标为空，则不进行统计
			if (dsi == null)
				continue;
			else
			{
				indicator = dsi.getDbIndicator();
			}

			// 如果数据库指标为空，则不进行统计
			if (indicator == null)
				continue;

			for (EntityIndicator ei : indicator.getEntityIndicatorList())
			{
				// 获得域内的总单元格数
				allFieldsSize += (ei.getRecordNum() * ei
						.getFieldIndicatorList().size());
				// 获得域内非空的单元格数
				usedFieldsSize += (int) (ei.getRecordNum()
						* ei.getFieldIndicatorList().size() * ei
						.getIntegrityRate());
			}
		}

		if (allFieldsSize == 0)
			return 0;
		else
			return ((double) usedFieldsSize) / allFieldsSize;
	}

	/**
	 * 获取域的总访问次数
	 * 
	 * @return
	 */
	public long getTotalAccessNumber()
	{
		IndicatorTool tool = new IndicatorTool();
		List<EntityAccessRecord> list;
		long totalAccessNumber = 0;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			list = tool.getEntityCounts(ds.getUri());
			if (list == null || list.size() == 0)
				continue;// 如果getEntityCounts方法出现异常，则返回null
			for (EntityAccessRecord ear : list)
			{
				totalAccessNumber += ear.getCounts();
			}
		}
		return totalAccessNumber;
	}

	/**
	 * 获取域的访问来源情况<Top20>
	 * 
	 * @return
	 */
	public HashMap<String, Long> getAccessSource()
	{
		HashMap<String, Long> resultMap = new HashMap<String, Long>();
		IndicatorTool tool = new IndicatorTool();
		List<AreaCountRecord> list;
		int size = 0;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			list = tool.getOperationSpecArea(ds.getUri(), "all");
			if (list == null || list.size() == 0)
				continue;// 如果getOperationSpecArea方法出现异常，则可能返回null
			size = (list.size() > 20) ? 20 : list.size();
			AreaCountRecord acr;
			for (int i = 0; i < size; i++)
			{
				acr = list.get(i);
				if (resultMap.containsKey(acr.getArea()))
					resultMap.put(acr.getArea(), resultMap.get(acr.getArea())
							.longValue()
							+ acr.getCount());
				else
					resultMap.put(acr.getArea(), Long.valueOf(acr.getCount()));
			}
		}
		return resultMap;
	}

	/**
	 * 获取域范围内关键词的访问统计情况<最多取前50条统计记录>
	 * 
	 * @param number
	 *            统计的记录数
	 * @return
	 */
	public HashMap<String, Long> getHotKeyWords(int number)
	{
		HashMap<String, Long> resultMap = new HashMap<String, Long>();
		IndicatorTool tool = new IndicatorTool();

		List<KeyWordsStats> list;
		int size = 0;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			list = tool.getKeywordStats(ds.getUri());
			if (list == null || list.size() == 0)
				continue;// 如果getKeywordStats方法出现异常，则有可能返回null
			number = (number > 50) ? 50 : number;
			size = (list.size() < number) ? list.size() : number;
			KeyWordsStats kws;
			for (int i = 0; i < size; i++)
			{
				kws = list.get(i);
				if (resultMap.containsKey(kws.getKeyword()))
					resultMap.put(kws.getKeyword(), resultMap.get(
							kws.getKeyword()).longValue()
							+ kws.getCounts());
				else
					resultMap.put(kws.getKeyword(), Long.valueOf(kws
							.getCounts()));
			}
		}
		return resultMap;
	}

	/**
	 * 获取访问域的热门IP<最多前50条记录>
	 * 
	 * @param number
	 *            获取的记录数
	 * @return
	 */
	public HashMap<String, Long> getHotAccessIps(int number)
	{
		HashMap<String, Long> resultMap = new HashMap<String, Long>();
		IndicatorTool tool = new IndicatorTool();

		List<IPRecord> list;
		int size = 0;
		for (DataSet ds : VdbManager.getEngine().getDomain().getDataSets())
		{
			list = tool.getIPStats(ds.getUri());
			if (list == null || list.size() == 0)
				continue;// 如果getIPStats方法出现异常，则有可能返回null
			number = (number > 50) ? 50 : number;
			size = (list.size() < number) ? list.size() : number;
			IPRecord ipr;
			for (int i = 0; i < size; i++)
			{
				ipr = list.get(i);
				if (resultMap.containsKey(ipr.getIp()))
					resultMap.put(ipr.getIp(), resultMap.get(ipr.getIp())
							.longValue()
							+ ipr.getCount());
				else
					resultMap.put(ipr.getIp(), Long.valueOf(ipr.getCount()));
			}
		}

		return resultMap;
	}

	/**
	 * 获取统计时间
	 * 
	 * @return
	 */
	public String getStatTime()
	{
		return DateUtil.getTodayAsString();
	}

}
