package vdb.mydb.typelib.data;

import org.apache.velocity.context.Context;

import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;
import vdb.mydb.vtl.ExprParser;

public class VdbExpr extends AbstractData
{
	private Context _context;

	private String _expr;

	public VdbExpr(String expr)
	{
		_expr = expr;
	}

	public String getValue()
	{
		try
		{
			Object o = eval();
			return o == null ? null : o.toString();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	private Object eval() throws Exception
	{
		return new ExprParser(_context).eval(_expr);
	}

	public void setContext(Context ctx)
	{
		_context = ctx;
	}

	public Sdef getAsSdef()
	{
		return new SimpleSdef(getValue());
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
	}

	public String getAsText()
	{
		return getValue();
	}

	public void setAsText(String text)
	{
		_expr = text;
	}

	/**
	 * 表达式类型的字节数为0
	 */
	public long getBytes() {
		return 0;
	}

	public boolean isEmpty() throws Exception {
		return getValue()==null || getValue().length()==0;
	}
}
