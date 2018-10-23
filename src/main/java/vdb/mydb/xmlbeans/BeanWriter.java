package vdb.mydb.xmlbeans;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jdom.Element;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

/**
 * BeanWriter tells how to write the binding bean
 * 
 * @author bluejoe
 * 
 */
public class BeanWriter
{
	private String[] _propNames;

	public BeanWriter(String... propNames)
	{
		_propNames = propNames;
	}

	public void write(Object bean, Element parentElement,
			BeanWriterContext beanWriterContext) throws BeansException,
			NoBeanWritterFoundException
	{
		Element beanElement = new Element("bean");
		parentElement.addContent(beanElement);

		String beanId = beanWriterContext.getBeanId(bean);

		if (beanId != null)
		{
			beanElement.setAttribute("id", beanId);
		}

		beanElement.setAttribute("class", bean.getClass().getName());
		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(bean);

		for (String name : _propNames)
		{
			addBeanProperty(beanElement, name, beanWrapper
					.getPropertyValue(name), beanWriterContext);
		}
	}

	private Element addBeanProperty(Element beanElement, String name,
			Object value, BeanWriterContext beanWriterContext)
			throws NoBeanWritterFoundException
	{
		Element property = new Element("property");
		beanElement.addContent(property);
		property.setAttribute("name", name);

		return setPropertyValue(property, value, beanWriterContext);
	}

	private Element setPropertyAsList(Element property, List list,
			BeanWriterContext beanWriterContext)
			throws NoBeanWritterFoundException
	{
		Element listElement = new Element("list");
		property.addContent(listElement);

		for (Object value : list)
		{
			setPropertyValue(listElement, value, beanWriterContext);
		}

		return property;
	}

	private Element setPropertyAsMap(Element property, Map map,
			BeanWriterContext beanWriterContext)
			throws NoBeanWritterFoundException
	{
		Element mapElement = new Element("map");
		property.addContent(mapElement);

		for (Entry entry : (Set<Entry>) map.entrySet())
		{
			Element entryElement = new Element("entry");
			mapElement.addContent(entryElement);
			entryElement.setAttribute("key", (String) entry.getKey());
			setPropertyValue(entryElement, entry.getValue(), beanWriterContext);
		}

		return property;
	}

	private Element setPropertyAsNull(Element property,
			BeanWriterContext beanWriterContext)
	{
		Element nullElement = new Element("null");
		property.addContent(nullElement);

		return property;
	}

	private Element setPropertyValue(Element property, Object value,
			BeanWriterContext beanWriterContext)
			throws NoBeanWritterFoundException
	{
		if (value == null)
		{
			return setPropertyAsNull(property, beanWriterContext);
		}

		if (value instanceof List)
		{
			return setPropertyAsList(property, (List) value, beanWriterContext);
		}

		if (value instanceof Map)
		{
			return setPropertyAsMap(property, (Map) value, beanWriterContext);
		}

		if (value instanceof Boolean || value instanceof String
				|| value instanceof Number || value instanceof Character)
		{
			return setPropertyAsString(property, value, beanWriterContext);
		}

		return setPropertyAsBean(property, value, beanWriterContext);
	}

	private Element setPropertyAsBean(Element property, Object value,
			BeanWriterContext beanWriterContext)
			throws NoBeanWritterFoundException
	{
		Object bean = value;

		BeanWriter beanWriter = beanWriterContext.getBeanWritter(bean);
		beanWriter.write(bean, property, beanWriterContext);

		return property;
	}

	private Element setPropertyAsString(Element property, Object value,
			BeanWriterContext beanWriterContext)
	{
		if ("property".equals(property.getName()))
		{
			property.setAttribute("value", value.toString());
		}
		else
		{
			Element valueElement = new Element("value");
			valueElement.setText(value.toString());
			property.addContent(valueElement);
		}

		return property;
	}
}
