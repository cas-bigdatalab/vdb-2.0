package vdb.mydb.jsp;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import vdb.mydb.VdbManager;

@SuppressWarnings("unchecked")
public class RegisterFilter implements Filter
{

	/**
	 * 静态变量，服务器每启动一次，重置一次
	 */
	private static boolean toSend = true;

	/**
	 * 服务器接受注册请求的URL列表信息
	 */
	private List urlList;

	public void destroy()
	{
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException
	{

		if (toSend)// 判断是否发送注册信息
		{
			toSend = false;

			// 创建要发送的数据
			RegisterInfo info = new RegisterInfo();
			HttpServletRequest request = (HttpServletRequest) arg0;

			if (null != request)
			{
				info.setIp(request.getLocalAddr());
				info.setBaseUrl(request.getRequestURL().toString());
				info.setPort(Integer.toString(request.getLocalPort()));
				info.setNodeName(VdbManager.getEngine().getDomain().getTitle());

				// 过滤掉域名为localhost的情况
				if (!info.getBaseUrl().contains("localhost")
						&& !info.getBaseUrl().contains("127.0.0.1")
						&& !info.getIp().contains("127.0.0.1"))
				{

					// 创建发送注册信息的线程
					RegisterThread registerThread = new RegisterThread();
					registerThread.setRegisterInfo(info);
					registerThread.setUrlList(urlList);
					registerThread.start();
				}
				else
				{
					toSend = true;
				}

			}
			else
			{
				Logger
						.getLogger(RegisterFilter.class)
						.debug(
								"Request is null, Register Information cannot be send!");
			}
		}

		arg2.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

	public List getUrlList()
	{
		return urlList;
	}

	public void setUrlList(List urlList)
	{
		this.urlList = urlList;
	}
}
