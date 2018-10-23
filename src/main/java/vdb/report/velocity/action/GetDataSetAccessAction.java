package vdb.report.velocity.action;

import java.util.ArrayList;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.access.vo.AreaCountRecord;
import vdb.report.access.vo.EntityAccessRecord;
import vdb.report.access.vo.IPRecord;
import vdb.report.access.vo.KeyWordsStats;
import vdb.report.access.vo.OperationAccessRecord;
import vdb.report.velocity.stringify.AccessIndicatorToString;
import vdb.tool.stat.IndicatorTool;

public class GetDataSetAccessAction implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		IndicatorTool tool = new IndicatorTool();
		String key = (String) vc.getParameter("key");
		String uri = (String) vc.getParameter("dsuri");

		// 访问关键字统计top(50)
		if ("dataKeywordAccess".equals(key))
		{
			List<KeyWordsStats> list = tool.getKeywordStats(uri);
			String tableText = AccessIndicatorToString
					.getKeyWordsCountsText(list);
			vc.put("setAccessSelectGrid", tableText);
		}

		// 来访IP统计top(50)
		else if ("dataVisitIPAccess".equals(key))
		{
			List<IPRecord> list = tool.getIPStats(uri);
			String iPtableText = AccessIndicatorToString.getIpStatsText(list);
			vc.put("setAccessSelectGrid", iPtableText);
		}

		// 被访实体统计
		else if ("dataVisitedAccess".equals(key))
		{
			List<EntityAccessRecord> list = tool.getEntityCounts(uri);
			String entityTableText = AccessIndicatorToString
					.getEntityAccessedStatsText(list);
			vc.put("setAccessSelectGrid", entityTableText);
		}

		// 操作类型统计
		else if ("dataOperationAccess".equals(key))
		{
			List<OperationAccessRecord> list = tool.getOperationStats(uri);
			String entityTableText = AccessIndicatorToString
					.getOperationStatsText(list);
			vc.put("setAccessSelectGrid", entityTableText);
		}

		// 今日访问统计
		else if ("todayHistogram".equals(key) || "todayLine".equals(key))
		{
			List<Integer> list = new ArrayList<Integer>();
			int todayIndex = 24;
			for (int i = 1; i <= todayIndex; i++)
			{
				list.add(tool.getOperationSpecToday(uri, i, "all"));
			}

			String histogramOrLineText = AccessIndicatorToString
					.getTodayAccessStatsText(list);
			vc.put("graphicsText", histogramOrLineText);
		}

		// 访问来源情况统计的柱状图和饼状图
		else if ("sourceHistogram".equals(key) || "sourcePie".equals(key))
		{
			List<AreaCountRecord> list = tool.getOperationSpecArea(uri, "all");

			String type = "";

			if ("sourceHistogram".equals(key) || "".equals(key))
				type = "histogram";
			else
				type = "pie";

			String areaText = AccessIndicatorToString.getAccessSourceStatsText(
					list, type);
			vc.put("graphicsText", areaText);
		}

		DataSet dataSet = (DataSet) VdbManager.getEngine().getCatalog()
				.fromUri(uri);
		vc.put("datasetTitle", dataSet.getTitle());
		vc.put("dsuri", uri);
		vc.put("key", key);
	}
}