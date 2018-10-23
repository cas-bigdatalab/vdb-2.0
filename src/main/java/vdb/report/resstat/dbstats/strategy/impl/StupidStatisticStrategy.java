package vdb.report.resstat.dbstats.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanImpl;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.impl.AnyQueryImpl;
import vdb.mydb.typelib.VdbData;
import vdb.report.resstat.dbstats.strategy.StatisticStrategy;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.resstat.dbstats.vo.FieldIndicator;
import vdb.tool.da.DataAccessTool;

public class StupidStatisticStrategy implements StatisticStrategy
{
	/**
	 * 每次取的数据记录数
	 */
	private int fetchSize;

	@SuppressWarnings("static-access")
	public EntityIndicator getEntityIndicator(Entity entity, String date)
	{
		if (entity == null)
			return null;

		// 判断fetchSize是否为0
		fetchSize = (fetchSize == 0) ? 50 : fetchSize;

		DataAccessTool daTool = new DataAccessTool();
		AnyQuery query = new AnyQueryImpl(entity);

		try
		{
			QueryExecutor qe = daTool.execute(query);
			long entityRecordNum = qe.size();

			EntityIndicator ei = new EntityIndicator();
			ei.setId(entity.getId());
			ei.setTableName(entity.getTableName());
			ei.setRecordNum(entityRecordNum);
			ei.setTitle(entity.getTitle());
			ei.setUri(entity.getUri());
			ei.setLastStatsTime(date);
			ei.setBytes(0);// 计算之前，大小为0

			List<AnyBean> beanList;
			int beginning = 1;
			int numOfFields = entity.getFields().length;
			long[] nullDataNumOfField = new long[numOfFields];

			while (beginning <= entityRecordNum)
			{
				beanList = qe.list(beginning, fetchSize);
				int fetchedSize = beanList.size();
				beginning += fetchedSize;

				for (Object o : beanList)
				{
					AnyBeanImpl bean = (AnyBeanImpl) o;
					for (int i = 0; i < numOfFields; i++)
					{
						Field vf = entity.getFields()[i];
						// 如果没有绑定的列，则不参与计算
						if (vf.getColumnName() == null
								|| vf.getColumnName().trim().equals(""))
							continue;
						VdbData vd = bean.get(vf);
						if (vd != null)
						{
							if (vd.isEmpty())
							{
								nullDataNumOfField[i] += 1;
							}
							else
							{
								long fieldBytes = vd.getBytes();
								ei.setBytes(ei.getBytes() + fieldBytes);
							}
						}
					}
				}
				beanList.clear();
				beanList = null;
				System.gc();
				Thread.currentThread().sleep(1L);
			}

			double entityIntegrity = 0;
			List<FieldIndicator> fieldIndicatorList = new ArrayList<FieldIndicator>();
			FieldIndicator fieldIndicator;
			Field field;
			for (int i = 0; i < numOfFields; i++)
			{
				field = entity.getFields()[i];
				fieldIndicator = new FieldIndicator();
				if (entityRecordNum == 0)
					fieldIndicator.setIntegrityRate(0);
				else
					fieldIndicator
							.setIntegrityRate((entityRecordNum - nullDataNumOfField[i])
									/ (double) entityRecordNum);

				fieldIndicator.setId(field.getId());
				fieldIndicator.setName(field.getName());
				fieldIndicator.setTitle(field.getTitle());
				fieldIndicator.setLastStatsTime(date);

				fieldIndicatorList.add(fieldIndicator);

				entityIntegrity += fieldIndicator.getIntegrityRate();
			}

			// 设置字段指标列表
			ei.setFieldIndicatorList(fieldIndicatorList);

			int indicatorSize = fieldIndicatorList.size();
			if (indicatorSize == 0)
				ei.setIntegrityRate(0);
			else
				ei
						.setIntegrityRate(entityIntegrity
								/ fieldIndicatorList.size());

			return ei;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public int getFetchSize()
	{
		return fetchSize;
	}

	public void setFetchSize(int fetchSize)
	{
		this.fetchSize = fetchSize;
	}
}
