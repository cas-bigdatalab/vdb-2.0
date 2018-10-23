package vdb.report.velocity.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.access.vo.DailyRecord;
import vdb.report.util.AmChartsSettingUtil;
import vdb.report.util.DateUtil;
import vdb.report.velocity.stringify.AccessIndicatorToString;
import vdb.tool.stat.IndicatorTool;

public class DoPeriodAccessAction implements VspAction
{

	public void doAction(VspContext vc) throws Exception
	{
		IndicatorTool tool = new IndicatorTool();

		HttpServletRequest request = vc.getRequest();
		HttpServletResponse response = vc.getResponse();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String streamName = request.getParameter("streamName");
		String dsuri = request.getParameter("dsuri");
		String id = request.getParameter("id");

		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		if (streamName.startsWith("access_period"))
		{
			String operation = request.getParameter("operation");
			String startDate = "";
			String endDate = "";

			if (streamName.equals("access_period_week"))
			{
				startDate = DateUtil.generateWeekstart();
				endDate = DateUtil.generateWeekend();
			}
			else if (streamName.equals("access_period_month"))
			{
				if (request.getParameter("month") == null
						|| request.getParameter("month").equals(""))
				{
					startDate = DateUtil.getFirstDayofMonth(Calendar
							.getInstance().get(Calendar.MONTH) + 1);
					endDate = DateUtil.getLastDayofMonth(Calendar.getInstance()
							.get(Calendar.MONTH) + 1);
				}
				else
				{
					startDate = DateUtil.getFirstDayofMonth(Integer
							.parseInt(request.getParameter("month")));
					endDate = DateUtil.getLastDayofMonth(Integer
							.parseInt(request.getParameter("month")));
				}
			}
			else if (streamName.equals("access_period_date"))
			{

				startDate = request.getParameter("startDate");
				endDate = request.getParameter("endDate");
			}

			String[] resultArray = new String[2];
			long days = DateUtil.getDays(sdf.parse(startDate), sdf
					.parse(endDate));

			long v = (days / 5 <= 0 ? 1 : days / 5);
			String value = Long.valueOf(v).toString();
			// 修改参数文件
			String settings = AmChartsSettingUtil.modifyValue(
					AmChartsSettingUtil.PERIOD_HISTOGRAM_SETTING_FILEPATH,
					"/settings/values/category/frequency", value);
			resultArray[0] = (settings == null) ? "" : settings;

			List<DailyRecord> list = new ArrayList<DailyRecord>();
			if (operation == null || operation.equals(""))
			{
				list = tool.getOperationSpecPeriod(dsuri, startDate, endDate,
						"all");
			}
			else
			{
				list = tool.getOperationSpecPeriod(dsuri, startDate, endDate,
						operation);
			}

			resultArray[1] = AccessIndicatorToString.getAccessPeriodText(list);
			JSONArray json = JSONArray.fromObject(resultArray);
			response.getWriter().print(json.toString());
		}

		else if (streamName.startsWith("access_entity_period") && id != null
				&& !id.equals(""))
		{
			String operation = request.getParameter("operation");
			String startDate = "";
			String endDate = "";
			if (streamName.equals("access_entity_period_week") && id != null
					&& !id.equals(""))
			{
				startDate = DateUtil.generateWeekstart();
				endDate = DateUtil.generateWeekend();

			}
			else if (streamName.equals("access_entity_period_curmonth")
					&& id != null && !id.equals(""))
			{
				startDate = DateUtil.getFirstDayofMonth(Calendar.getInstance()
						.get(Calendar.MONTH) + 1);
				endDate = DateUtil.getLastDayofMonth(Calendar.getInstance()
						.get(Calendar.MONTH) + 1);
			}
			else if (streamName.equals("access_entity_period_month")
					&& id != null && !id.equals(""))
			{
				startDate = DateUtil.getFirstDayofMonth(Integer
						.parseInt(request.getParameter("month")));
				endDate = DateUtil.getLastDayofMonth(Integer.parseInt(request
						.getParameter("month")));
			}
			else if (streamName.equals("access_entity_period_date"))
			{

				startDate = request.getParameter("startDate");
				endDate = request.getParameter("endDate");
			}

			String[] resultArray = new String[2];

			long days = DateUtil.getDays(sdf.parse(startDate), sdf
					.parse(endDate));

			long v = (days / 5 <= 0 ? 1 : days / 5);
			String value = Long.valueOf(v).toString();

			String settings = AmChartsSettingUtil.modifyValue(
					AmChartsSettingUtil.PERIOD_HISTOGRAM_SETTING_FILEPATH,
					"/settings/values/category/frequency", value);

			resultArray[0] = (settings == null) ? "" : settings;

			List<DailyRecord> list = new ArrayList<DailyRecord>();
			if (operation == null || operation.equals(""))
			{
				list = tool.getOperationSpecPeriod(dsuri, startDate, endDate,
						id, "all");
			}
			else
			{
				list = tool.getOperationSpecPeriod(dsuri, startDate, endDate,
						id, operation);
			}

			resultArray[1] = AccessIndicatorToString.getAccessPeriodText(list);
			JSONArray json = JSONArray.fromObject(resultArray);
			response.getWriter().print(json.toString());
		}
	}
}
