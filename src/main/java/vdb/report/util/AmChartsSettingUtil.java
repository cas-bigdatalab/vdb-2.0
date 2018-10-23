package vdb.report.util;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import vdb.mydb.VdbManager;

public class AmChartsSettingUtil
{

	/** 分时段访问统计中调用的参数文件 */
	public static String PERIOD_HISTOGRAM_SETTING_FILEPATH = "/console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Histogram_edit.xml";

	/**
	 * 功能说明：用于动态修改FusionCharts图形的参数配置文件
	 * 
	 * @param filePath
	 *            参数配置文件的路径<br>
	 *            从根路径开始写，以"/"开头
	 * @param paramName
	 *            需要修改的参数名字，需要写XPath路径，从根节点开始写
	 * @param paramValue
	 *            参数值
	 * @return
	 */
	public static String modifyValue(String filePath, String paramName,
			String paramValue)
	{

		// 如果配置文件参数为空，则提供默认的配置文件地址
		if (filePath == null || filePath.equals(""))
		{
			return null;// 不做任何操作
		}

		// 构造参数文件的路径
		String path = VdbManager.getEngine().getServletContext().getRealPath(
				filePath);

		File f = new File(path);

		// 如果指定路径的配置文件不存在，则提供默认的配置文件路径
		if (!f.exists())
		{
			return null;// 不做任何操作
		}

		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try
		{
			doc = builder.build(f);
			Element paramElement = (Element) XPath.selectSingleNode(doc
					.getRootElement(), paramName);
			paramElement.setText(paramValue);

		}
		catch (JDOMException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// 返回更改后的DOC文档
		XMLOutputter outputter = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setEncoding("UTF-8");
		format.setIndent(null);
		outputter.setFormat(format);
		String returnStr = outputter.outputString(doc);
		returnStr = returnStr.substring(returnStr.indexOf("<settings>"))
				.replaceAll("\"", "\'").trim();
		return returnStr;
	}
}
