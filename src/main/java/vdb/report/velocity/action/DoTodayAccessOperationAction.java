package vdb.report.velocity.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.velocity.stringify.AccessIndicatorToString;
import vdb.tool.stat.IndicatorTool;

public class DoTodayAccessOperationAction implements VspAction
{

	public void doAction(VspContext vc) throws Exception
	{
		IndicatorTool tool = new IndicatorTool();

		HttpServletRequest request = vc.getRequest();
		HttpServletResponse response = vc.getResponse();

		String streamName = request.getParameter("streamName");
		String dsuri = request.getParameter("dsuri");
		String id = request.getParameter("id");

		response.setContentType("text/xml;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		if (streamName.equals("access_today"))
		{
			String operation = request.getParameter("operation");

			List<Integer> list = new ArrayList<Integer>();
			int todayIndex = 24;
			for (int i = 1; i <= todayIndex; i++)
			{
				list.add(tool.getOperationSpecToday(dsuri, i, operation));
			}

			String text = AccessIndicatorToString.getTodayAccessStatsText(list);
			response.getWriter().print(text);
		}

		else if (streamName.equals("access_entity_today"))
		{
			String operation = request.getParameter("operation");

			List<Integer> list = new ArrayList<Integer>();
			int todayIndex = 24;
			for (int i = 1; i <= todayIndex; i++)
			{
				list.add(tool.getOperationSpecToday(dsuri, i, operation, id));
			}

			String text = AccessIndicatorToString.getTodayAccessStatsText(list);
			response.getWriter().print(text);
		}
	}
}
