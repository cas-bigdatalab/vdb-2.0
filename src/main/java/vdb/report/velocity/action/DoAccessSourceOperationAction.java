package vdb.report.velocity.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.access.vo.AreaCountRecord;
import vdb.report.velocity.stringify.AccessIndicatorToString;
import vdb.tool.stat.IndicatorTool;

public class DoAccessSourceOperationAction implements VspAction
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

		if (streamName.equals("access_area"))
		{
			String operation = request.getParameter("operation");

			String type = "histogram";

			List<AreaCountRecord> list = tool.getOperationSpecArea(dsuri,
					operation);
			String text = AccessIndicatorToString.getAccessSourceStatsText(
					list, type);

			response.getWriter().print(text);
		}

		else if (streamName.equals("access_area_pie"))
		{
			String operation = request.getParameter("operation");

			String type = "pie";

			List<AreaCountRecord> list = tool.getOperationSpecArea(dsuri,
					operation);
			String text = AccessIndicatorToString.getAccessSourceStatsText(
					list, type);
			response.getWriter().print(text);
		}

		else if (streamName.equals("access_entity_area"))
		{
			String operation = request.getParameter("operation");

			String type = "histogram";

			List<AreaCountRecord> list = tool.getOperationSpecArea(dsuri,
					operation, id);
			String text = AccessIndicatorToString.getAccessSourceStatsText(
					list, type);
			response.getWriter().print(text);
		}

		else if (streamName.equals("access_entity_area_pie"))
		{
			String operation = request.getParameter("operation");

			String type = "pie";

			List<AreaCountRecord> list = tool.getOperationSpecArea(dsuri,
					operation, id);
			String text = AccessIndicatorToString.getAccessSourceStatsText(
					list, type);

			response.getWriter().print(text);
		}
	}
}
