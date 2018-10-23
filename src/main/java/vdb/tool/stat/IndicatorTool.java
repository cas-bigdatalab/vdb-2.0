package vdb.tool.stat;

import java.util.List;

import vdb.report.access.dao.DataAccessDao;
import vdb.report.access.dao.DataAccessDaoImpl;
import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.DailyRecord;
import vdb.report.access.vo.EntityAccessRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.access.vo.OperationAccessRecord;
import vdb.report.resstat.tool.ResstatTool;
import vdb.report.resstat.vo.DatasetIndicator;

/**
 * 获取统计分析指标的统计分析工具
 * 
 * @author 苏贤明
 * 
 */
public class IndicatorTool
{

	/**
	 * 获取数据集的统计分析指标
	 * 
	 * @param datasetUri
	 *            数据集URI
	 * @return
	 */
	public DatasetIndicator getDatasetIndicator(String datasetUri)
	{
		ResstatTool tool = new ResstatTool();
		return tool.getDatasetIndicator(datasetUri);
	}

	/**
	 * 得到数据集云图
	 * @param uri
	 * @return
	 */
	public String getCloudString(String uri)
	{
		ResstatTool tool = new ResstatTool();
		return tool.getCloudString(uri);
	}
	
	/**
	 * 获得指定数据集指定实体的关键词统计指标（前50条）
	 * 
	 * @param uri
	 *            数据集URI
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<KeyWordsStats> getKeywordStatsOfEntity(String uri,
			String entityId)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getKeywordStatsOfEntity(entityId);
	}

	/**
	 * 获得指定数据集指定实体的操作类型统计
	 * 
	 * @param uri
	 *            数据集URI
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<OperationAccessRecord> getOperationStatsOfEntity(String uri,
			String entityId)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationStatsOfEntity(entityId);
	}

	/**
	 * 获得指定数据集指定实体的IP统计指标
	 * 
	 * @param uri
	 *            数据集URI
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<IPRecord> getIPStatsOfEntity(String uri, String entityId)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getIPStatsOfEntity(entityId);
	}

	/**
	 * 获得指定数据集的关键词统计指标
	 * 
	 * @param uri
	 *            数据集URI
	 * @return
	 */
	public List<KeyWordsStats> getKeywordStats(String uri)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getKeywordStats();
	}

	/**
	 * 获得指定数据集的操作统计指标
	 * 
	 * @param uri
	 *            数据集URI
	 * @return
	 */
	public List<OperationAccessRecord> getOperationStats(String uri)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationStats();
	}

	/**
	 * 获得指定数据集的IP统计指标
	 * 
	 * @param uri
	 *            数据集URI
	 * @return
	 */
	public List<IPRecord> getIPStats(String uri)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getIPStats();
	}

	/**
	 * 获得指定数据集内实体被访统计指标的情况
	 * 
	 * @param 数据集URI
	 * @return
	 */
	public List<EntityAccessRecord> getEntityCounts(String uri)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getEntityCounts();
	}

	/**
	 * 获得指定数据集指定操作发生的区域统计指标（即统计指定操作出现在各个区域的次数）
	 * 
	 * @param uri
	 *            数据集URI
	 * @param operation
	 *            指定操作
	 * @return
	 */
	public List<AreaCountRecord> getOperationSpecArea(String uri,
			String operation)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationSpecArea(operation);
	}

	/**
	 * 获得指定数据集指定实体指定操作发生的区域统计指标（即统计指定实体指定操作出现在各个区域的次数）
	 * 
	 * @param uri
	 *            数据集URI
	 * @param operation
	 *            操作类型
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<AreaCountRecord> getOperationSpecArea(String uri,
			String operation, String entityId)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationSpecArea(operation, entityId);
	}

	/**
	 * 获得指定数据集指定操作在指定时间范围内发生的次数
	 * 
	 * @param uri
	 *            数据集URI
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param operation
	 *            操作
	 * @return
	 */
	public List<DailyRecord> getOperationSpecPeriod(String uri,
			String startDate, String endDate, String operation)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationSpecPeriod(startDate, endDate, operation);
	}

	/**
	 * 获得指定数据集指定实体指定操作在指定时间范围内发生的次数
	 * 
	 * @param uri
	 *            数据集URI
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param entityId
	 *            实体ID
	 * @param operation
	 *            操作类型
	 * @return
	 */
	public List<DailyRecord> getOperationSpecPeriod(String uri,
			String startDate, String endDate, String entityId, String operation)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationSpecPeriod(startDate, endDate, entityId,
				operation);
	}

	/**
	 * 获得指定数据集内指定操作在指定时间（小时）发生的次数
	 * 
	 * @param uri
	 *            指定数据集
	 * @param hour
	 *            指定时间（小时）
	 * @param operation
	 *            指定操作
	 * @return
	 */
	public int getOperationSpecToday(String uri, int hour, String operation)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationSpecToday(hour, operation);
	}

	/**
	 * 获得指定数据集内指定实体指定操作在指定时间（小时）发生的次数
	 * 
	 * @param uri
	 *            指定数据集
	 * @param hour
	 *            指定时间（小时）
	 * @param operation
	 *            指定操作
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public int getOperationSpecToday(String uri, int hour, String operation,
			String entityId)
	{
		DataAccessDao dao = new DataAccessDaoImpl(uri);
		return dao.getOperationSpecToday(hour, operation, entityId);
	}
}
