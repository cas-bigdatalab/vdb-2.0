package vdb.mydb.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XmlUtil
{
	StringBuffer xmlBuffer = new StringBuffer();

	HttpServletRequest request = null;

	HttpServletResponse response = null;

	public XmlUtil(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
		xmlBuffer.append("<?xml version='1.0' encoding='UTF-8'?>");
		xmlBuffer.append("<rows>");
		xmlBuffer.append("<head>");
		xmlBuffer.append("</head>");
		xmlBuffer.append("</rows>");
	}

	public void setId(String id)
	{
		xmlBuffer.insert(xmlBuffer.lastIndexOf("</rows>"), "<row id='" + id
				+ "'></row>");
	}

	public void setXmlHead(String col)
	{
		xmlBuffer.insert(xmlBuffer.lastIndexOf("</head>"), col);
	}

	public void insertEnumOption(String colName, String optValue, String optKey)
	{
		// xmlBuffer.i
		xmlBuffer.insert(xmlBuffer.indexOf("</column>", xmlBuffer
				.indexOf(colName)), "<option value='" + optValue + "'>"
				+ optKey + "</option>");
	}

	public void setXmlData(Object cell)
	{
		if (xmlBuffer.lastIndexOf("</row>") > 0)
			xmlBuffer.insert(xmlBuffer.lastIndexOf("</row>"), "<cell>" + cell
					+ "</cell>");
	}

	public String getXmlFile()
	{
		return new String(xmlBuffer);
	}

	public List getXmlData()
	{
		List l = new ArrayList();
		try
		{
			SAXBuilder xmlbuiilder = new SAXBuilder();
			BufferedInputStream buinst = new BufferedInputStream(request
					.getInputStream());
			Document document = xmlbuiilder.build(buinst);
			Element root = document.getRootElement();
			for (Iterator it = root.getChildren("row").iterator(); it.hasNext();)
			{
				Element row = (Element) it.next();
				Object[] o = new Object[row.getChildren().size() + 1];
				o[0] = row.getAttributeValue("id");
				int i = 1;
				if (!row.getChildren().isEmpty())
					for (Iterator itcell = row.getChildren("cell").iterator(); itcell
							.hasNext();)
					{
						Element cell = (Element) itcell.next();
						;
						o[i++] = cell.getText();
					}
				l.add(o);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return l;
	}

	public void returnResultXml(String resultxml)
	{
		try
		{
			response.setContentType("text/xml; charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultxml);
			response.getWriter().flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try
			{
				response.getWriter().write("<flag>0</flag>");
			}
			catch (Exception fe)
			{
			}
		}
	}
}
