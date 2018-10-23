package vdb.mydb.engine;

import java.util.Map;

public class RPCServices
{
	String _urlPattern;

	Map<String, Object> _servicesMapping;

	public void setUrlPattern(String urlPattern)
	{
		_urlPattern = urlPattern;
	}

	public void setBaseUrl(String baseUrl)
	{
		_urlPattern = baseUrl + "/%s";
	}

	public void setServicesMapping(Map<String, Object> servicesMapping)
	{
		_servicesMapping = servicesMapping;
	}

	public String getUrlPattern()
	{
		return _urlPattern;
	}

	public String getUrl(String serviceClassName)
	{
		return String.format(_urlPattern, serviceClassName);
	}

	public Map<String, Object> getServicesMapping()
	{
		return _servicesMapping;
	}
}
