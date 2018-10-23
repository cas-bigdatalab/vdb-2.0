package vdb.service.httpservice;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.velocity.context.Context;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import vdb.mydb.VdbManager;

@SuppressWarnings("serial")
public class HttpServiceServlet extends HttpServlet
{

	private String innerErrorXmlPath;

	private String unkonwnVerbXmlPath;

	private String badRequestXmlPath;

	private String httpServicePath;

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml;charset=utf-8");

		Context ctx = VdbManager.getEngine().getVelocityEngine().createContext(
				httpRequest, httpResponse);

		String verb = httpRequest.getParameter("verb");
		if (verb == null || "".equals(verb))
		{
			verb = "default";
		}

		String url = httpServicePath + verb + ".verb";
		HttpServiceVerbManager verbManager = (HttpServiceVerbManager) VdbManager
				.getEngine().getHttpServiceVerbManager();

		try
		{
			if (verbManager.isAvailable(verb))
			{
				VdbManager.getEngine().getVelocityEngine()
						.layout(url, ctx, out);
			}
			else
			{
				// 已经调用过getWriter方法，再通过forward的方式输出XML文档会出现乱码
				// RequestDispatcher rd =
				// request.getRequestDispatcher(unkonwnVerbXmlPath);
				out.write(getXMLString(unkonwnVerbXmlPath));
				out.flush();
				return;
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			// 已经调用过getWriter方法，再通过forward的方式输出XML文档会出现乱码
			// RequestDispatcher rd =
			// request.getRequestDispatcher(innerErrorXmlPath);
			out.write(getXMLString(innerErrorXmlPath));
			out.flush();
			return;
		}
	}

	// 根据XML文档的路径得到XML文档的内容
	private String getXMLString(String filePath)
	{
		filePath = VdbManager.getEngine().getRootPath().replace("\\", "/")
				+ "/" + filePath;
		File f = new File(filePath);
		if (!f.exists())
		{
			String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ "<response><head>code>0</code><message>提示性文档已经被删除</message>"
					+ "</head><body></body></response>";
			return str;
		}

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = null;
		try
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(f);
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		DOMSource ds = new DOMSource(doc);

		// 得到转换器工厂类的实例
		TransformerFactory tf = TransformerFactory.newInstance();
		StringWriter sw = new StringWriter();
		;
		try
		{
			StreamResult sr = new StreamResult(sw);
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(ds, sr);
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
		return sw.toString();
	}

	public String getInnerErrorXmlPath()
	{
		return innerErrorXmlPath;
	}

	public void setInnerErrorXmlPath(String innerErrorXmlPath)
	{
		this.innerErrorXmlPath = innerErrorXmlPath;
	}

	public String getUnkonwnVerbXmlPath()
	{
		return unkonwnVerbXmlPath;
	}

	public void setUnkonwnVerbXmlPath(String unkonwnVerbXmlPath)
	{
		this.unkonwnVerbXmlPath = unkonwnVerbXmlPath;
	}

	public String getBadRequestXmlPath()
	{
		return badRequestXmlPath;
	}

	public void setBadRequestXmlPath(String badRequestXmlPath)
	{
		this.badRequestXmlPath = badRequestXmlPath;
	}

	public String getHttpServicePath()
	{
		return httpServicePath;
	}

	public void setHttpServicePath(String httpServicePath)
	{
		this.httpServicePath = httpServicePath;
	}

}
