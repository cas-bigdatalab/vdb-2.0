package vdb.mydb.typelib;

import junit.framework.TestCase;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;

public class StringClassTest extends TestCase
{
	public void test1()
	{
		XmlBeanFactory factory = new XmlBeanFactory(
				new FileSystemResource(
						"D:\\workspace\\vdb-1.1\\web\\WEB-INF\\typelib\\String\\meta.xml"));
		Object o = factory
				.getBean(factory.getBeanNamesForType(FieldType.class)[0]);
		System.out.println(o);
	}
}
