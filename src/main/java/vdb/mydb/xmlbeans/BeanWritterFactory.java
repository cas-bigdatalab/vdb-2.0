package vdb.mydb.xmlbeans;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * BeanWritterFactory creates proper BeanWritter for each given object
 * 
 * @author bluejoe
 * 
 */
public class BeanWritterFactory
{
	private Map<Class, BeanWriter> _map = new HashMap<Class, BeanWriter>();

	public void registerBeanWriter(Class beanClass, BeanWriter beanWritter)
	{
		_map.put(beanClass, beanWritter);
	}

	public BeanWriter getBeanWritter(Object bean)
			throws NoBeanWritterFoundException
	{
		BeanWriter bw = internalGetBeanWritter(bean);
		if (bw == null)
			throw new NoBeanWritterFoundException(bean);

		return bw;
	}

	private BeanWriter internalGetBeanWritter(Object bean)
	{
		for (Entry<Class, BeanWriter> entry : _map.entrySet())
		{
			Class key = entry.getKey();
			if (key.isAssignableFrom(bean.getClass()))
			{
				return entry.getValue();
			}
		}

		return null;
	}
}
