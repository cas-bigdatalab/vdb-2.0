package vdb.log.action;

import vdb.log.dao.DataAccessLogDao;
import vdb.log.vo.DataAccessLog;
import vdb.metacat.DataSet;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspContext;
import cn.csdb.commons.jsp.Pageable;

public class UserAccessLogAction extends AbstractLogAction
{
	@Override
	protected Pageable<DataAccessLog> getPageable(VspContext vc)
	{
		String datasetId = vc.getParameter("datasetId");
		String username = vc.getParameter("username");
		DataSet dataset = VdbManager.getInstance().getCatalog().fromId(
				datasetId);
		DataAccessLogDao dao = new DataAccessLogDao();
		return dao.getUserAccessLogsInDataset(dataset, username);
	}

	@Override
	protected String getPageViewerName()
	{
		return "pageViewer";
	}

	@Override
	protected String getPageIndex(VspContext vc)
	{
		return vc.getParameter("page");
	}

	@Override
	protected String getSize(VspContext vc)
	{
		return (String) vc.get("pageSize");
	}
}
