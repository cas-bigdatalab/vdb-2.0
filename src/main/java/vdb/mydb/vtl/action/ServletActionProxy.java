package vdb.mydb.vtl.action;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.VspContext;
import cn.csdb.commons.action.JspAction;

public abstract class ServletActionProxy implements JspAction, VspAction
{
	public void doAction(VspContext vc) throws Exception
	{
		doAction(vc.getRequest(), vc.getResponse(), vc.getServletContext());
	}
}
