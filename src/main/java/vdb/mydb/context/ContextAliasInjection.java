package vdb.mydb.context;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.context.Context;

import vdb.mydb.vtl.ExprParser;

public class ContextAliasInjection implements ContextInjection
{
	private Map<String, String> _alias;

	public void inject(Context ctx)
	{
		// import from application context
		ExprParser ep = new ExprParser(ctx);

		for (Entry<String, String> me : _alias.entrySet())
		{
			String key = me.getKey();
			String value = me.getValue();
			try
			{
				ctx.put(key, ep.eval(value));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void setAlias(Map<String, String> alias)
	{
		_alias = alias;
	}
}
