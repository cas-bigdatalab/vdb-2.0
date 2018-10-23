package vdb.report.velocity.stringify;

import java.util.Iterator;
import java.util.List;

import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.DailyRecord;
import vdb.report.access.vo.EntityAccessRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.access.vo.OperationAccessRecord;
import vdb.report.util.ColorUtil;

public class AccessIndicatorToString
{

	/**
	 * 获得关键词统计指标的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            关键词统计指标列表
	 * @return
	 */
	public static String getKeyWordsCountsText(List<KeyWordsStats> list)
	{
		if (list == null || list.size() == 0)
		{
			return "<rows></rows>";
		}

		StringBuffer tableText = new StringBuffer();
		tableText.append("<rows>");
		Iterator<KeyWordsStats> kit = list.iterator();
		int no = 1;
		while (kit.hasNext())
		{
			KeyWordsStats record = kit.next();
			// 序号
			tableText.append("<row id='" + no + "'>" + "<cell>" + no
					+ "</cell>");
			// 关键字
			tableText.append("<cell>" + record.getKeyword() + "</cell>");
			// 被访实体
			tableText.append("<cell>" + record.getEntity() + "</cell>");
			// 访问次数
			tableText.append("<cell>" + record.getCounts() + "</cell></row>");
			no++;
		}
		tableText.append("</rows>");
		return tableText.toString();
	}

	/**
	 * 获得IP统计指标的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            IP统计指标列表
	 * @return
	 */
	public static String getIpStatsText(List<IPRecord> list)
	{
		if (list == null || list.size() == 0)
		{
			return "<rows></rows>";
		}

		StringBuffer iPtableText = new StringBuffer();
		iPtableText.append("<rows>");
		Iterator<IPRecord> it = list.iterator();
		int no = 1;
		while (it.hasNext())
		{
			IPRecord record = it.next();
			// 序号
			iPtableText.append("<row id='" + no + "'>" + "<cell>" + no
					+ "</cell>");
			// 来访IP
			iPtableText.append("<cell>" + record.getIp() + "</cell>");
			// IP所属地区
			iPtableText.append("<cell>" + record.getArea() + "</cell>");
			// 访问次数
			iPtableText.append("<cell>" + record.getCount() + "</cell></row>");
			no++;
		}
		iPtableText.append("</rows>");

		return iPtableText.toString();
	}

	/**
	 * 获得操作类型统计指标的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            操作类型统计指标列表
	 * @return
	 */
	public static String getOperationStatsText(List<OperationAccessRecord> list)
	{
		if (list == null || list.size() == 0)
		{
			return "<rows></rows>";
		}
		int no = 1;
		StringBuffer entityTableText = new StringBuffer();
		entityTableText.append("<rows>");
		Iterator<OperationAccessRecord> it = list.iterator();
		while (it.hasNext())
		{
			OperationAccessRecord record = it.next();
			// 序号
			entityTableText.append("<row id='" + no + "'>" + "<cell>" + no
					+ "</cell>");
			// 访问操作
			entityTableText
					.append("<cell>" + record.getOperation() + "</cell>");
			// 访问次数
			entityTableText.append("<cell>" + record.getCount()
					+ "</cell></row>");
			no++;
		}
		entityTableText.append("</rows>");

		return entityTableText.toString();
	}

	/**
	 * 获得今日访问统计指标的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            今日访问统计指标列表
	 * @return
	 */
	public static String getTodayAccessStatsText(List<Integer> list)
	{
		ColorUtil color = new ColorUtil();
		if (list == null || list.size() == 0)
		{
			return "<chart></chart>";
		}

		StringBuffer todayText = new StringBuffer();
		todayText.append("<chart><series>");

		// 设置标题列
		for (int step = 1; step <= list.size(); step++)
		{
			todayText.append("<value xid='" + step + "'>" + step + "</value>");
		}

		todayText.append("</series><graphs><graph gid='0'>");

		// 设置标题列内容
		for (int step = 0; step < list.size(); step++)
		{
			int value = 0;
			if (list.get(step) != null)
				value = list.get(step).intValue();

			todayText.append("<value xid='" + (step + 1) + "'" + " color='"
					+ color.getNextColor() + "'>" + value + "</value>");
		}

		todayText.append("</graph></graphs></chart>");
		return todayText.toString();
	}

	/**
	 * 获得访问来源统计指标的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            访问来源统计指标列表
	 * @param type
	 *            图形类型
	 * @return
	 */
	public static String getAccessSourceStatsText(List<AreaCountRecord> list,
			String type)
	{
		ColorUtil color = new ColorUtil();

		if (list == null || list.size() == 0)
		{
			return type.equals("histogram") ? "<chart></chart>" : "<pie></pie>";
		}

		int no = 0;
		// 如果访问来源个数超过颜色个数，则按照下面的规则增加

		if (type.equals("histogram"))
		{
			StringBuffer areaText = new StringBuffer();
			areaText.append("<chart><series>");

			Iterator<AreaCountRecord> it = list.iterator();
			Iterator<AreaCountRecord> itValue = list.iterator();
			while (it.hasNext())
			{
				AreaCountRecord record = (AreaCountRecord) it.next();
				areaText.append("<value xid='" + no + "'>" + record.getArea()
						+ "</value>");
				no++;
			}
			areaText.append("</series><graphs><graph gid='0'>");
			no = 0;

			while (itValue.hasNext())
			{
				AreaCountRecord record = (AreaCountRecord) itValue.next();

				areaText.append("<value xid='" + no + "' color='"
						+ color.getNextColor() + "'>" + record.getCount()
						+ "</value>");
				no++;

			}
			areaText.append("</graph></graphs></chart>");
			return areaText.toString();
		}
		else
		{
			StringBuffer entityAreaTextPie = new StringBuffer();
			entityAreaTextPie.append("<pie>");

			Iterator<AreaCountRecord> it = list.iterator();
			while (it.hasNext())
			{
				AreaCountRecord record = (AreaCountRecord) it.next();

				entityAreaTextPie.append("<slice title='" + record.getArea()
						+ "' color='" + color.getNextColor() + "'>"
						+ record.getCount() + "</slice>");
				no++;
			}

			entityAreaTextPie.append("</pie>");
			return entityAreaTextPie.toString();
		}
	}

	/**
	 * 获得被访实体统计指标的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            被访实体统计指标列表
	 * @return
	 */
	public static String getEntityAccessedStatsText(
			List<EntityAccessRecord> list)
	{
		if (list == null || list.size() == 0)
		{
			return "<rows></rows>";
		}

		StringBuffer entityTableText = new StringBuffer();
		entityTableText.append("<rows>");

		Iterator<EntityAccessRecord> it = list.iterator();
		int no = 1;
		while (it.hasNext())
		{
			EntityAccessRecord record = it.next();
			// 序号
			entityTableText.append("<row id='" + no + "'>" + "<cell>" + no
					+ "</cell>");
			// 被访实体
			entityTableText.append("<cell>" + record.getTitle() + "</cell>");
			// 访问次数
			entityTableText.append("<cell>" + record.getCounts() + "</cell>");
			// 最后更新时间
			entityTableText.append("<cell>" + record.getDate()
					+ "</cell></row>");
			no++;
		}
		entityTableText.append("</rows>");
		return entityTableText.toString();
	}

	/**
	 * 获得时段访问统计指标(各种操作的访问次数)的Text字符串，页面Chart组件将其渲染为图形
	 * 
	 * @param list
	 *            时段访问统计指标列表
	 * @return
	 */
	public static String getAccessPeriodText(List<DailyRecord> list)
	{
		ColorUtil color = new ColorUtil();

		int no = 0;
		StringBuffer periodText = new StringBuffer();

		if (list == null || list.size() == 0)
		{
			periodText
					.append("<chart><series><value xid='0'></value></series><graphs>"
							+ "<graph gid='0'><value xid='0'></value></graph></graphs></chart>");
		}
		else
		{
			periodText.append("<chart><series>");
			Iterator<DailyRecord> it = list.iterator();
			Iterator<DailyRecord> itValue = list.iterator();

			DailyRecord record;
			while (it.hasNext())
			{
				record = it.next();
				periodText.append("<value xid='" + no + "'>" + record.getDate()
						+ "</value>");
				no++;
			}
			periodText.append("</series><graphs><graph gid='0'>");
			no = 0;
			while (itValue.hasNext())
			{
				record = (DailyRecord) itValue.next();
				periodText.append("<value xid='" + no + "' color='"
						+ color.getNextColor() + "'>" + record.getCount()
						+ "</value>");
				no++;
			}
			periodText.append("</graph></graphs></chart>");
		}
		return periodText.toString();
	}
}
