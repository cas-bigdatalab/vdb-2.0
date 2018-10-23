package vdb.mydb.rpc.simplex;

import java.io.Serializable;
import java.util.Map;

public class SimpleAnyBeanImpl implements SimpleAnyBean, Serializable
{

	private Map<String, Object> bean;

	private String dataSetUri;

	public Map<String, Object> getBean()
	{
		return bean;
	}

	public void setBean(Map<String, Object> bean)
	{
		this.bean = bean;
	}

	public String getDataSetUri()
	{
		return dataSetUri;
	}

	public void setDataSetUri(String dataSetUri)
	{
		this.dataSetUri = dataSetUri;
	}
}
