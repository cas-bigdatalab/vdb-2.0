package vdb.mydb.jsp;

public class RegisterInfo
{

	private static final long serialVersionUID = 1L;

	/** 此VDB应用的IP */
	private String ip;

	/** 此VDB应用的根URL */
	private String baseUrl;

	/** 此VDB应用的端口 */
	private String port;

	/** 此VDB应用的域名称 */
	private String nodeName;

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getNodeName()
	{
		return nodeName;
	}

	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
}
