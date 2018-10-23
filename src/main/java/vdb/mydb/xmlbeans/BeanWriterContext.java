package vdb.mydb.xmlbeans;

public class BeanWriterContext
{
	BeanWritterFactory _factory;

	private SpringBeans _beans;

	public BeanWriterContext(SpringBeans beans, BeanWritterFactory factory)
	{
		_factory = factory;
		_beans = beans;
	}

	String getBeanId(Object bean)
	{
		return _beans.getBeanId(bean);
	}

	public BeanWriter getBeanWritter(Object bean)
			throws NoBeanWritterFoundException
	{
		return _factory.getBeanWritter(bean);
	}
}
