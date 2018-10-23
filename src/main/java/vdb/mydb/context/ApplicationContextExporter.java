package vdb.mydb.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.velocity.context.Context;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import vdb.mydb.vtl.ExprParser;

public class ApplicationContextExporter implements ContextInjection,
		InitializingBean, ApplicationContextAware
{
	private Map<String, String> _alias;

	private ApplicationContext _applicationContext;

	List<Object> _depends;

	Map<String, Object> _exports = new HashMap<String, Object>();

	public void afterPropertiesSet() throws Exception
	{
		// import from application context
		ExprParser ep = new ExprParser(
				new ApplicationContext2VtlContextAdapter(_applicationContext));
		for (Entry<String, String> me : _alias.entrySet())
		{
			String key = me.getKey();
			String value = me.getValue();
			try
			{
				_exports.put(key, ep.eval(value));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void inject(Context ctx)
	{
		for (Entry<String, Object> me : _exports.entrySet())
		{
			String key = me.getKey();
			Object value = me.getValue();
			ctx.put(key, value);
		}
	}

	public void setAlias(Map<String, String> applicationContextImports)
	{
		_alias = applicationContextImports;
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		_applicationContext = arg0;
	}

	public void setDepends(List<Object> depends)
	{
		_depends = depends;
	}

}
