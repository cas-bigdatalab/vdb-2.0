package vdb.mydb.xmlbeans;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpringBeans
{
	Map<Object, String> _beans = new LinkedHashMap<Object, String>();

	public void addBean(Object bean, String beanId)
	{
		_beans.put(bean, beanId);
	}

	public void addBean(Object bean)
	{
		addBean(bean, null);
	}

	public String getBeanId(Object bean)
	{
		return _beans.get(bean);
	}

	public List<Object> getBeans()
	{
		return new ArrayList<Object>(_beans.keySet());
	}
}
