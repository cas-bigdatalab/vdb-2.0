package vdb.mydb.engine;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

public class VdbContextLoader extends ContextLoader
{
	private ApplicationContext _parentParentContext;

	@Override
	protected ApplicationContext loadParentContext(ServletContext arg0)
			throws BeansException
	{
		return _parentParentContext;
	}

	public void setParentParentContext(ApplicationContext parentParentContext)
	{
		_parentParentContext = parentParentContext;
	}

}
