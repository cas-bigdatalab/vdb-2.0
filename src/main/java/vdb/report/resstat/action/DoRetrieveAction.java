package vdb.report.resstat.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.report.access.job.AccessStatsJob;
import vdb.report.resstat.dbstats.job.DbStatsJob;
import vdb.report.resstat.filestats.job.FileStatsJob;

public class DoRetrieveAction implements VspAction
{

	//FIXME 立即抽取是暂时的，应该删除；如果独立为单独的应用，提供的立即抽取应该是独立的
	public void doAction(VspContext vc) throws Exception
	{
		try
		{
			HttpServletRequest request = vc.getRequest();
			HttpServletResponse response = vc.getResponse();
			String dsuri = request.getParameter("dsuri");
			String forward = request.getParameter("forward");
			String key = request.getParameter("key");
			if (dsuri == null || dsuri.equals(""))
				return;
			// 执行定时任务
			new DbStatsJob().accumulateDbIndicator(dsuri);
			new FileStatsJob().accumulateFileIndicator(dsuri);
			new AccessStatsJob().executeStatistic(dsuri);
			String forwardPage = "/console/report/" + forward + "?dsuri="
					+ dsuri + "&key=" + key;
			response.sendRedirect(forwardPage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}