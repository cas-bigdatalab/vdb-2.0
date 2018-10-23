package vdb.mydb.jsp;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class RegisterThread extends Thread
{

	/** 注册信息 */
	private RegisterInfo info;

	/** 服务器接受注册请求的URL列表信息 */
	private List<String> urlList;

	public void run()
	{
		Logger logger = Logger.getLogger(RegisterThread.class);
		try
		{
			if (urlList != null && urlList.size() != 0)
			{
				logger
						.debug("************************begin********************");
				for (String url : urlList)
				{
					int statscode = sendInfoToServer(url, info);
					logger.debug("Register Information is sended to '" + url
							+ "', and http stats code is" + statscode + ".");
				}
				logger
						.debug("************************end**********************");
			}
			else
			{
				logger
						.debug("The Server List Which Accept Register Information is Empty");
				logger.debug("Register Information cannot be sended");
			}
		}
		catch (IOException e)
		{
			logger.error("RegisterThread encounters error. error message is"
					+ e.getMessage());
		}
	}

	/**
	 * @param url
	 * @param info
	 *            发送到服务器的对象
	 * @return 服务器HTTP响应码。
	 * @throws IOException
	 */
	private int sendInfoToServer(String url, RegisterInfo info)
			throws HttpException, IOException
	{
		int statuscode = 0;

		HttpClient client = new HttpClient();
		PostMethod post = new UTF8PostMethod(url);
		post.setParameter("baseUrl", info.getBaseUrl());
		post.setParameter("port", info.getPort());
		post.setParameter("ip", info.getIp());
		post.setParameter("nodeName", info.getNodeName());

		client.executeMethod(post);
		statuscode = post.getStatusCode();
		// 释放连接
		post.releaseConnection();

		return statuscode;
	}

	public void setUrlList(List<String> urlList)
	{
		this.urlList = urlList;
	}

	public void setRegisterInfo(RegisterInfo info)
	{
		this.info = info;
	}

	// Inner class for UTF-8 support (PostMethod)
	public static class UTF8PostMethod extends PostMethod
	{
		public UTF8PostMethod(String url)
		{
			super(url);
		}

		@Override
		public String getRequestCharSet()
		{
			return "UTF-8";
		}
	}

}
