package vdb.log.action;

import vdb.log.dao.UserLoginLogDao;
import vdb.log.vo.UserLoginLog;
import vdb.mydb.vtl.VspContext;
import cn.csdb.commons.jsp.Pageable;

public class UserLoginLogAction extends AbstractLogAction
{
	@Override
	protected Pageable<UserLoginLog> getPageable(VspContext vc)
	{
		String username = vc.getParameter("username");
		UserLoginLogDao dao = new UserLoginLogDao();
		return dao.getUserLoginLogs(username);
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
