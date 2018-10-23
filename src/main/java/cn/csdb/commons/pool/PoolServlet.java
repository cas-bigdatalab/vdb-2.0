package cn.csdb.commons.pool;

import java.io.File;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/*
 * PoolServlet自动从XML文件中装载信息配置连接池。
 * 
 * <pre> 你可以在web.xml里面添加以下语句：
 * 
 * &lt;servlet servlet-name='PoolServlet'
 * servlet-class='cn.csdb.commons.pool.PoolServlet'&gt; &lt;load-on-startup/&gt;
 * &lt;init-param xml-path=&quot;./WEB-INF/pool.xml&quot;/&gt; &lt;/servlet&gt;
 * 
 * </pre>
 * 
 * @author bluejoe
 */
public class PoolServlet extends HttpServlet
{
	public void init() throws ServletException
	{
		String xmlPath = getInitParameter("xml-path");

		if (xmlPath == null)
		{
			xmlPath = "./WEB-INF/pool.xml";
		}

		try
		{
			File f = new File(xmlPath);
			// relative path
			if (!f.isAbsolute())
			{
				f = new File(getServletContext().getRealPath("/"), xmlPath);
			}

			xmlPath = f.getCanonicalPath();

			System.out.println(MessageFormat.format(
					"building pools from `{0}`...", new Object[] { xmlPath }));
			PoolManager pm = PoolManager.getInstance();
			pm.loadFromXML(xmlPath);

			String prompt = "PoolServer" + pm.getVersion()
					+ " started successfully! pool count: "
					+ pm.getPools().size();
			System.out.println(prompt);

			pm.getLogger().info(prompt);
			this.getServletContext().setAttribute(PoolManager.class.getName(),
					pm);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void destroy()
	{
		String prompt = "PoolServer shutdown normally!";
		PoolManager.getInstance().getLogger().info(prompt);
		PoolManager.getInstance().destroy();
	}
}
