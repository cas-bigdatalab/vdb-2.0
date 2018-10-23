package vdb.tool.ui;

import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.VarQuery;
import vdb.tool.VdbTool;
import cn.csdb.commons.jsp.BeanPageViewer;
import cn.csdb.commons.jsp.PageUrlBuilder;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;

public class PagerTool extends VdbTool
{
	public PageViewer create(Pageable query, int pageIndex, int size)
			throws Exception
	{
		return new BeanPageViewer(new PageUrlBuilder()
		{
			public String getPageOnClick(String page)
			{
				return String.format("gotoPage1(this, %s)", page);
			}

			public String getPageUrl(String page)
			{
				return null;
			}
		}, query, pageIndex, size);
	}

	public PageViewer create(VarQuery query, int pageIndex, int size)
			throws Exception
	{
		try
		{
			return create(new AnyBeanDao(query.getEntity()).execute(query),
					pageIndex, size);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
