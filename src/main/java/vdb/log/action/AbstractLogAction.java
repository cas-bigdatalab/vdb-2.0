package vdb.log.action;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import vdb.tool.ui.PagerTool;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;

public abstract class AbstractLogAction implements VspAction
{

	public void doAction(VspContext vc) throws Exception
	{
		int pageIndex = format(getPageIndex(vc));
		int size = format(getSize(vc));
		Pageable pageable = getPageable(vc);
		PagerTool tool = new PagerTool();
		PageViewer pageViewer = tool.create(pageable, pageIndex, size);
		vc.put(getPageViewerName(), pageViewer);
	}

	abstract protected String getPageViewerName();

	abstract protected String getPageIndex(VspContext vc);

	abstract protected String getSize(VspContext vc);

	abstract protected Pageable getPageable(VspContext vc);

	private int format(String s)
	{
		int i;
		try
		{
			i = Integer.parseInt(s);
		}
		catch (Exception e)
		{
			i = 1;
		}
		return i;
	}
}
