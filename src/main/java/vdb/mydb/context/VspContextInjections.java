package vdb.mydb.context;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.context.Context;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class VspContextInjections implements BeanFactoryPostProcessor
{
	List<ContextInjection> _contextInjections = new ArrayList<ContextInjection>();

	public void inject(Context ctx)
	{
		for (ContextInjection cj : _contextInjections)
		{
			cj.inject(ctx);
		}
	}

	public void addInjection(ContextInjection arg0)
	{
		_contextInjections.add(arg0);
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException
	{
		for (String name : arg0.getBeanNamesForType(ContextInjection.class))
		{
			ContextInjection ci = (ContextInjection) arg0.getBean(name);
			addInjection(ci);
		}
	}
}
