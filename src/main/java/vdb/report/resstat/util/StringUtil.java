package vdb.report.resstat.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * 字符处理公共类
 * 
 * @author suxianming
 * 
 */
public class StringUtil
{

	/**
	 * 替换XML文档中的字符串
	 * 
	 * @param filePath
	 *            XML文档路径
	 * @param oldStr
	 *            原始字符串
	 * @param newStr
	 *            目标字符串
	 */
	public static void replaceInFileWithString(String filePath, String oldStr,
			String newStr)
	{
		SAXBuilder builder = new SAXBuilder();
		try
		{
			Document doc = builder.build(new File(filePath));
			Element e = doc.getRootElement();
			Element data = (Element) XPath.selectSingleNode(e, oldStr);
			data.removeContent();
			Element ee = (Element) builder.build(new StringReader(newStr))
					.getRootElement().clone();
			data.addContent(ee);
			XMLOutputter xmlOut = new XMLOutputter();
			xmlOut.output(doc, new FileOutputStream(filePath));
		}
		catch (JDOMException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 将长整型分数转变为百分比字符串
	 * 
	 * @param numerator
	 * @param denominator
	 * @return String 66%
	 */
	public String getPercentRate(long numerator, long denominator)
	{
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(0);
		nf.setMaximumIntegerDigits(3);
		return nf.format(((float) numerator) / denominator);
	}
}
