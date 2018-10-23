package vdb.report.resstat.action;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.resstat.tool.ResstatTool;
import vdb.report.resstat.util.IndicatorToStringUtil;
import vdb.report.resstat.vo.DatasetIndicator;

public class GetDataQualityAction implements VspAction
{

	public void doAction(VspContext vc) throws Exception
	{

		String dsuri = (String) vc.getParameter("dsuri");
		String key = (String) vc.getParameter("key");
		String tableId = (String) vc.getParameter("id");

		String data = null;
		String space = null;
		String graphicData = null;

		ResstatTool tool = new ResstatTool();
		DatasetIndicator dsin = tool.getDatasetIndicator(dsuri);

		// 数据集表格数据
		if ("dataOnAbstract".equals(key))
		{
			String dhxXmlData = IndicatorToStringUtil.getTableTextofEntityData(
					dsin, space);
			vc.put("data", dhxXmlData);
		}
		else if ("dataOnDataVolume".equals(key))
		{
			data = IndicatorToStringUtil.getTableTextofEntityBytes(dsin.getDbIndicator(), space);
			vc.put("data", data);
		}
		else if ("dataOnRecords".equals(key))
		{
			data = IndicatorToStringUtil.getTableTextofEntityRecords(dsin.getDbIndicator(), space);
			vc.put("data", data);
		}
		else if ("dataOnUtilization".equals(key))
		{
			data = IndicatorToStringUtil.getTableTextOfEntityUtilization(dsin.getDbIndicator(),
					space);
			vc.put("data", data);
		}
		else if ("dataOnCloud".equals(key))
		{
			data = IndicatorToStringUtil.getDataSetCloud(dsuri);
			vc.put("data", data);
		}
		// 数据实体表格数据
		else if ("tableOnUtilization".equals(key))
		{
			data = IndicatorToStringUtil.getTableTextofFieldUsedRate(dsin.getDbIndicator(),
					tableId);
			vc.put("data", data);
		}
		else if ("tableOnAbstract".equals(key))
		{
			data = IndicatorToStringUtil.getTableTextOfEntityFieldValue(dsin.getDbIndicator(),
					tableId);
			vc.put("data", data);
		}
		// 数据集图形数据
		else if ("recordsHistogram".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramLineTextofEntityRecords(dsin.getDbIndicator(), space);
			vc.put("graphics", graphicData);
		}
		else if ("recordsLine".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramLineTextofEntityRecords(dsin.getDbIndicator(), space);
			vc.put("graphics", graphicData);
		}
		else if ("recordsPie".equals(key))
		{
			graphicData = IndicatorToStringUtil.getPieTextofEntityRecords(dsin.getDbIndicator(),
					space);
			vc.put("graphics", graphicData);
		}
		else if ("volumeHistogram".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramLineTextOfEntityBytes(dsin.getDbIndicator(), space);
			vc.put("graphics", graphicData);
		}
		else if ("volumeLine".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramLineTextOfEntityBytes(dsin.getDbIndicator(), space);
			vc.put("graphics", graphicData);
		}
		else if ("volumePie".equals(key))
		{
			graphicData = IndicatorToStringUtil.getPieTextOfEntityBytes(dsin.getDbIndicator(),
					space);
			vc.put("graphics", graphicData);
		}
		else if ("utilizationHistogram".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramLineTextOfEntityUtilization(dsin.getDbIndicator(), space);
			vc.put("graphics", graphicData);
		}
		else if ("utilizationLine".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramLineTextOfEntityUtilization(dsin.getDbIndicator(), space);
			vc.put("graphics", graphicData);
		}
		// 数据实体表格数据
		else if ("tableUtilizationHistogram".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramTextOfEntityFieldUsedRate(dsin.getDbIndicator(), tableId);
			vc.put("graphics", graphicData);
		}
		else if ("tableUtilizationLine".equals(key))
		{
			graphicData = IndicatorToStringUtil
					.getHistogramTextOfEntityFieldUsedRate(dsin.getDbIndicator(), tableId);
			vc.put("graphics", graphicData);
		}

		vc.put("key", key);
	}
}