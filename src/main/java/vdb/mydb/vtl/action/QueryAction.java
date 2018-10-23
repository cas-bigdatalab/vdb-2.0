package vdb.mydb.vtl.action;

import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.tool.auth.AuthTool;
import vdb.tool.log.AccessLoggerTool;
import cn.csdb.commons.jsp.BeanPageViewer;
import cn.csdb.commons.jsp.PageUrlBuilder;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;

public class QueryAction implements VspAction
{
	@SuppressWarnings("unchecked")
	public void doAction(VspContext vc) throws Exception
	{
		// 用户信息
		String user = "";
		try
		{
			user = new AuthTool().getUserName();
		}
		catch (Exception e)
		{
			user = "";
		}
		// IP信息
		String ip = vc.getRequest().getRemoteHost();
		final String keywords = vc.getParameter("keywords");
		final String uri = vc.getParameter("uri");
		Entity entity=  VdbManager.getEngine().getCatalog().fromUri(uri);
		vc.getRequest().getSession().setAttribute("keywords", keywords);
		vc.getRequest().getSession().setAttribute("uri", uri);
		Pageable query = null;
		try
		{
			query = VdbManager.getEngine().getIndexer().getSearcher()
					.search(entity, keywords);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		int gotoPage = 0;
		try
		{
			gotoPage = Integer.parseInt(vc.getParameter("gotoPage"));
		}
		catch (Exception e)
		{
			gotoPage = 0;
		}

		int pageSize = (Integer) vc.get("pageSize");
		if (query != null)
		{
			PageViewer pageViewer = new BeanPageViewer(new PageUrlBuilder()
			{
				public String getPageOnClick(String page)
				{
					return null;
				}

				public String getPageUrl(String page)
				{
					return String.format("?keywords=%s&uri=%s&gotoPage=%s",
							keywords, uri, page);
				}
			}, query, gotoPage, pageSize);
			vc.put("pageViewer", pageViewer);
		}
		else
		{
			vc.put("pageViewer", null);
		}


		//如果gotoPage变量为空，则表示是初次查询，过滤掉分页时点击下一页的情况
		if(vc.getParameter("gotoPage")==null){
			AccessLoggerTool loggerTool = new AccessLoggerTool();
			loggerTool.logAccess(user, ip, entity, "query", null, keywords, false);
		}
	}
}
