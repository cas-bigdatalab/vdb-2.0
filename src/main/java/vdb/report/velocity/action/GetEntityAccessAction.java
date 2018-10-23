package vdb.report.velocity.action;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.access.vo.OperationAccessRecord;
import vdb.report.velocity.stringify.AccessIndicatorToString;
import vdb.tool.stat.IndicatorTool;

public class GetEntityAccessAction implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		// 实例化统计分析工具
		IndicatorTool tool = new IndicatorTool();
		String uri = (String) vc.getParameter("dsuri");
		String key = (String) vc.getParameter("key");
		String entityId = (String) vc.getParameter("id");

		// 访问关键字统计top(50)
		if ("tableKeywordAccess".equals(key))
		{
			List<KeyWordsStats> list = tool.getKeywordStatsOfEntity(uri,
					entityId);
			String tableText = AccessIndicatorToString
					.getKeyWordsCountsText(list);
			vc.put("getEntityAccessActionValue", tableText);
		}

		// 来访IP统计top(50)
		if ("tableVisitIPAccess".equals(key))
		{
			List<IPRecord> list = tool.getIPStatsOfEntity(uri, entityId);
			String iPtableText = AccessIndicatorToString.getIpStatsText(list);
			vc.put("getEntityAccessActionValue", iPtableText);
		}

		// 操作类型统计
		if ("tableOperationAccess".equals(key))
		{
			List<OperationAccessRecord> list = tool.getOperationStatsOfEntity(
					uri, entityId);
			String entityTableText = AccessIndicatorToString
					.getOperationStatsText(list);
			vc.put("getEntityAccessActionValue", entityTableText);
		}

		// 今天访问情况统计
		if ("todayHistogram".equals(key) || "todayLine".equals(key))
		{
			List<Integer> list = new ArrayList<Integer>();
			int todayIndex = 24;
			for (int i = 1; i <= todayIndex; i++)
			{
				list.add(tool.getOperationSpecToday(uri, i, "all", entityId));
			}

			String todayText = AccessIndicatorToString
					.getTodayAccessStatsText(list);
			vc.put("graphicsText", todayText);
		}

		// 访问来源情况统计
		if ("sourceHistogram".equals(key) || "sourcePie".equals(key))
		{

			List<AreaCountRecord> list = tool.getOperationSpecArea(uri, "all",
					entityId);

			String type = "";

			if ("sourceHistogram".equals(key) || "".equals(key))
				type = "histogram";
			else
				type = "pie";

			String entityAreaText = AccessIndicatorToString
					.getAccessSourceStatsText(list, type);

			vc.put("graphicsText", entityAreaText);

		}

		Entity entity = (Entity) VdbManager.getEngine().getCatalog().fromId(
				entityId);
		vc.put("entityTitle", entity.getTitle());
		vc.put("dsuri", uri);
		vc.put("id", entityId);
		vc.put("key", key);
	}
}