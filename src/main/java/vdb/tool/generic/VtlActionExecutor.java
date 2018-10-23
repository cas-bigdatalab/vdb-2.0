package vdb.tool.generic;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ViewToolContext;

import vdb.mydb.vtl.VspAction;
import vdb.mydb.vtl.impl.VtlContextImpl;
import vdb.tool.VdbTool;

public class VtlActionExecutor extends VdbTool
{
	private Context _context;

	public void execute(String actionName) throws Exception
	{
		((VspAction) Class.forName(actionName).newInstance())
				.doAction(new VtlContextImpl((ViewToolContext) _context));
	}

	public void init(Object obj)
	{
		_context = (Context) obj;
	}
}
