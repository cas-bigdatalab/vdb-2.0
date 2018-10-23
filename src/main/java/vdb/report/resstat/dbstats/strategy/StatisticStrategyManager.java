package vdb.report.resstat.dbstats.strategy;

public class StatisticStrategyManager
{
	/**
	 * 统计指标抽取策略
	 */
	private StatisticStrategy statisticStrategy;

	public StatisticStrategy getStatisticStrategy()
	{
		return statisticStrategy;
	}

	public void setStatisticStrategy(StatisticStrategy statisticStrategy)
	{
		this.statisticStrategy = statisticStrategy;
	}
}
