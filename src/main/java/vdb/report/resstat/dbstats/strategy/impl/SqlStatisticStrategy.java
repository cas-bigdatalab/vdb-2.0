package vdb.report.resstat.dbstats.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanImpl;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.impl.AnyQueryImpl;
import vdb.mydb.typelib.VdbData;
import vdb.report.resstat.dbstats.accumulator.Accumulator;
import vdb.report.resstat.dbstats.strategy.StatisticStrategy;
import vdb.report.resstat.dbstats.vo.EntityIndicator;
import vdb.report.resstat.dbstats.vo.FieldIndicator;
import vdb.tool.da.DataAccessTool;
import cn.csdb.commons.sql.JdbcSource;

public class SqlStatisticStrategy implements StatisticStrategy
{
	private Map<String, Accumulator> accumulatorMap;

	public EntityIndicator getEntityIndicator(Entity e, String date)
	{
		if (e == null)
			return null;

		VdbEntity entity = (VdbEntity) e;
		Accumulator accumulator = getAccumulator(entity);

		// 如果没有相应的驱动来计算大小，则采用stupid的逻辑来计算
		if (accumulator == null)
		{
			EntityIndicator entityIndi = getEntityIndicatorOfStupidMethod(
					entity, date);
			return entityIndi;
		}

		JdbcSource jdbcSource = JdbcSourceManager.getInstance().getJdbcSource(
				entity.getDataSet());

		long entityRecordNum = accumulator.getTableRecordNum(entity
				.getTableName(), jdbcSource);

		// 如果得到的记录数为－1，则表示出现异常
		if (entityRecordNum == -1)
			return null;

		EntityIndicator ei = new EntityIndicator();
		ei.setId(entity.getId());
		ei.setTableName(entity.getTableName());
		ei.setRecordNum(entityRecordNum);
		ei.setTitle(entity.getTitle());
		ei.setUri(entity.getUri());
		ei.setLastStatsTime(date);

		long entityBytes = 0;
		long nullDataNum = 0;
		long bytes;

		List<FieldIndicator> fieldIndicatorList = new ArrayList<FieldIndicator>();
		FieldIndicator fieldIndicator;
		double entityIntegrity = 0;
		String columnName;
		for (Field field : entity.getFields())
		{
			fieldIndicator = new FieldIndicator();
			fieldIndicator.setId(field.getId());
			fieldIndicator.setName(field.getName());
			fieldIndicator.setTitle(field.getTitle());
			fieldIndicator.setLastStatsTime(date);

			columnName = field.getColumnName();
			// 如果绑定列为空，则大小为0,利用率为1
			if (columnName == null || columnName.trim().equals(""))
			{
				// 大小为0,entityBytes不变化
				nullDataNum = 0;
			}
			else
			{
				// 字段空值
				nullDataNum = accumulator.getNullDataNumOfField(entity
						.getTableName(), field.getColumnName(), jdbcSource);

				// 等于-1，说明提取空值记录数时出现异常，则取nullDataNum=0
				// 即按照没有空值记录数来进行计算
				if (nullDataNum == -1)
					nullDataNum = 0;

				// 得到绑定列的字段类型（数据库）
				String typeName = "String";
				try
				{
					typeName = entity.getDataClassNameFromJdbcType(entity
							.getJdbcTable().getColumn(field.getColumnName())
							.getDataType());
				}
				catch (Exception e1)
				{
					typeName = "String";
					e1.printStackTrace();
				}

				if (typeName.equalsIgnoreCase("Double")
						|| typeName.equalsIgnoreCase("Long"))
				{
					entityBytes = entityBytes
							+ (ei.getRecordNum() - nullDataNum) * 8;
				}
				else
				{
					bytes = accumulator.getFieldBytes(entity.getTableName(),
							field.getColumnName(), jdbcSource);

					// 如果bytes=-1，则说明计算长度时出现异常，不将长度统计到其中
					if (bytes != -1)
						entityBytes = entityBytes + bytes;
				}
			}

			// 如果nullDataNum为空，则说明绑定的字段为空，利用率为1
			if (nullDataNum == 0)
			{
				fieldIndicator.setIntegrityRate(1);
			}
			else
			{
				double recordNum = ei.getRecordNum();
				if (recordNum == 0)
					fieldIndicator.setIntegrityRate(0);
				else
					fieldIndicator.setIntegrityRate((recordNum - nullDataNum)
							/ recordNum);
			}

			entityIntegrity = entityIntegrity
					+ fieldIndicator.getIntegrityRate();
			fieldIndicatorList.add(fieldIndicator);
		}
		// 设置字段指标列表
		ei.setFieldIndicatorList(fieldIndicatorList);

		int indicatorSize = fieldIndicatorList.size();
		if (indicatorSize == 0)
			ei.setIntegrityRate(0);
		else
			ei.setIntegrityRate(entityIntegrity / fieldIndicatorList.size());

		ei.setBytes(entityBytes);
		return ei;
	}

	private Accumulator getAccumulator(Entity entity)
	{
		DataSet ds = entity.getDataSet();
		String dbType = ds.getRepository().getProductName();

		Accumulator accumulator = accumulatorMap.get(dbType);
		return accumulator;
	}

	public Map<String, Accumulator> getAccumulatorMap()
	{
		return accumulatorMap;
	}

	public void setAccumulatorMap(Map<String, Accumulator> accumulatorMap)
	{
		this.accumulatorMap = accumulatorMap;
	}

	@SuppressWarnings("static-access")
	private EntityIndicator getEntityIndicatorOfStupidMethod(Entity entity,
			String date)
	{
		if (entity == null)
			return null;

		int fetchSize = 50;

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
						// 未绑定列的字段不进行计算
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
}
