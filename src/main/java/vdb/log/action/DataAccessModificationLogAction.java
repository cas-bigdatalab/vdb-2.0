package vdb.log.action;

import vdb.log.dao.DataAccessLogDao;
import vdb.log.vo.DataAccessLog;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspContext;
import cn.csdb.commons.jsp.Pageable;

public class DataAccessModificationLogAction extends AbstractLogAction
{

	@Override
	protected Pageable<DataAccessLog> getPageable(VspContext vc)
	{
		String entityId = vc.getParameter("eid");
		Entity entity = VdbManager.getInstance().getCatalog().fromId(entityId);
		String beanId = vc.getParameter("id");
		DataAccessLogDao dao = new DataAccessLogDao();
		return dao.getDataModificationLogs(entity, beanId);
	}

	@Override
	protected String getPageViewerName()
	{
		return "modifyPageViewer";
	}

	@Override
	protected String getPageIndex(VspContext vc)
	{
		return vc.getParameter("modifyPage");
	}

	@Override
	protected String getSize(VspContext vc)
	{
		return (String) vc.get("modifyPageSize");
	}
}
