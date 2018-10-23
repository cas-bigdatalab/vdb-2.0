package vdb.mydb.vtl.action;

import vdb.mydb.VdbManager;
import vdb.mydb.index.IndexSearcher;
import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import cn.csdb.commons.jsp.BeanPageViewer;
import cn.csdb.commons.jsp.PageUrlBuilder;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;

public class CrossQueryAction implements VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		final String keywords = vc.getParameter("keywords");
		// final String keywords = new
		// String(vc.getParameter("keywords").getBytes("iso-8859-1"), "UTF-8");
		final String expr = vc.getParameter("expr");

		vc.put("keywords", keywords);
		vc.getRequest().getSession().setAttribute("keywords", keywords);
		vc.getRequest().getSession().setAttribute("expr", expr);

		Pageable query = VdbManager
				.getInstance()
				.getIndexer()
				.getSearcher()
				.search(

						"OR".equalsIgnoreCase(expr) ? IndexSearcher.LogicalOperator.OR
								: IndexSearcher.LogicalOperator.AND,
						keywords.split("\\s"));
		int gotoPage = 0;
		try
		{
			gotoPage = Integer.parseInt(vc.getParameter("gotoPage"));
		}
		catch (Exception e)
		{
		}

		int pageSize = (Integer) vc.get("pageSize");
		PageViewer pageViewer = new BeanPageViewer(new PageUrlBuilder()
		{
			public String getPageOnClick(String page)
			{
				return null;
			}

			public String getPageUrl(String page)
			{
				return String.format("?keywords=%s&expr=%s&gotoPage=%s",
						keywords, expr, page);
			}
		}, query, gotoPage, pageSize);

		vc.put("pageViewer", pageViewer);
	}
}
