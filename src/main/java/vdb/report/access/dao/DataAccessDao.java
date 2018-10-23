package vdb.report.access.dao;

import java.util.List;

import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.DailyRecord;
import vdb.report.access.vo.EntityAccessRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.access.vo.OperationAccessRecord;

public interface DataAccessDao
{

	/**
	 * 获得指定实体的关键词统计指标（前50条）
	 * 
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<KeyWordsStats> getKeywordStatsOfEntity(String entityId);

	/**
	 * 获得指定实体的操作类型统计
	 * 
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<OperationAccessRecord> getOperationStatsOfEntity(String entityId);

	/**
	 * 获得指定实体的IP统计指标
	 * 
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<IPRecord> getIPStatsOfEntity(String entityId);

	/**
	 * 获得此数据集的关键词统计指标
	 * 
	 * @return
	 */
	public List<KeyWordsStats> getKeywordStats();

	/**
	 * 获得此数据集的操作统计指标
	 * 
	 * @return
	 */
	public List<OperationAccessRecord> getOperationStats();

	/**
	 * 获得此数据集的IP统计指标
	 * 
	 * @return
	 */
	public List<IPRecord> getIPStats();

	/**
	 * 获得此数据集内实体被访统计指标的情况
	 * 
	 * @return
	 */
	public List<EntityAccessRecord> getEntityCounts();

	/**
	 * 获得此数据集指定操作发生的区域统计指标（即统计指定操作出现在各个区域的次数）
	 * 
	 * @param operation
	 *            指定操作
	 * @return
	 */
	public List<AreaCountRecord> getOperationSpecArea(String operation);

	/**
	 * 获得此数据集指定实体指定操作发生的区域统计指标（即统计指定实体指定操作出现在各个区域的次数）
	 * 
	 * @param operation
	 *            操作类型
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public List<AreaCountRecord> getOperationSpecArea(String operation,
			String entityId);

	/**
	 * 获得此数据集指定操作在指定时间范围内发生的次数
	 * 
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @param operation
	 *            操作
	 * @return
	 */
	public List<DailyRecord> getOperationSpecPeriod(String startDate,
			String endDate, String operation);

	/**
	 * 获得此数据集指定实体指定操作在指定时间范围内发生的次数
	 * 
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
	public List<DailyRecord> getOperationSpecPeriod(String startDate,
			String endDate, String entityId, String operation);

	/**
	 * 获得此数据集内指定操作在指定时间（小时）发生的次数
	 * 
	 * @param hour
	 *            指定时间（小时）
	 * @param operation
	 *            指定操作
	 * @return
	 */
	public int getOperationSpecToday(int hour, String operation);

	/**
	 * 获得此数据集内指定实体指定操作在指定时间（小时）发生的次数
	 * 
	 * @param hour
	 *            指定时间（小时）
	 * @param operation
	 *            指定操作
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	public int getOperationSpecToday(int hour, String operation, String entityId);
}
