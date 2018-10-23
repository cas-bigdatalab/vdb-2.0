package vdb.report.resstat.dbstats.strategy;

import vdb.metacat.Entity;
import vdb.report.resstat.dbstats.vo.EntityIndicator;

public interface StatisticStrategy
{
	/**
	 * 得到实体指标对象
	 * 
	 * @param entity
	 *            实体对象
	 * @param today
	 *            统计日期
	 * @return 实体指标
	 */
	public EntityIndicator getEntityIndicator(Entity entity, String today);

}
