package vdb.tool.webpub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlToHtml
{
	private HashMap<String, Integer> hash = new HashMap<String, Integer>(100);

	private ArrayList<Element> levelList = new ArrayList<Element>(100);

	private String resultHtml = "";

	private String blank = "";

	private int deepth = 1;

	private static XmlToHtml instance = null;

	/**
	 * 
	 * 
	 */
	public String getHtmlStr(String s)
	{
		Element root = null;

		try
		{
			root = getRootElement(s);
		}
		catch (DocumentException e)
		{

			e.printStackTrace();
		}

		storeNodeDeepthInfo(root);
		if (root != null)
			getHtmlCode(root, "");
		return getResultHtml();
	}

	public static void main(String args[]) throws DocumentException
	{
		XmlToHtml test = new XmlToHtml();
		String s = "<?xml version=\'1.0\'?>\n"
				+ "<tree id='0'>\n"
				+ "<item id='1272529174765'  text='\u9996\u9875|/page/index.vpage'></item>\n"
				+ "<item id='1274756937734' open='1' text='\u67E5\u627E\u6570\u636E\u5E93|\\#'>\n"
				+ "<item id='staticNode1277358774664'  open='1'  text='\u6570\u636E\u5E931|\\#'>"
				+ "<item id='staticNode1277358810047'  open='1'  text='\u6570\u636E\u5E931.1|\\#'>"
				+ "<item id='staticNode1277358829862'  open='1'  text='\u6570\u636E\u5E931.1.1|\\#'>"
				+ "<item id='staticNode1277358850542'  text='\u6570\u636E\u5E931.1.1.1|\\#'>"
				+ "</item>"
				+ "</item>"
				+ "</item>"
				+ "<item id='staticNode1277358974573'  open='1'  select='1' text='\u6570\u636E\u5E931.2|\\#'>"
				+ "<item id='staticNode1277358987397'  text='\u6570\u636E\u5E931.2.1|\\#'></item>"
				+ "<item id='staticNode1277359010884'  text='\u6570\u636E\u5E931.2.2|\\#'></item>"
				+ "</item>"
				+ "</item>"
				+ "<item id='staticNode1277358781071'  text='\u6570\u636E\u5E932|\\#'>"
				+ "</item>"
				+ "</item>"
				+ "<item id='1274756963796'  text='\u6570\u636E\u68C0\u7D22|\\#'></item>"
				+ "<item id='1274756984125'  text='\u6807\u51C6\u8F6F\u4EF6|\\#'></item>"
				+ "<item id='1274756981093'  text='\u6570\u636E\u670D\u52A1|\\#'></item>"
				+ "<item id='1274756979781'  text='\u670D\u52A1\u6848\u4F8B|\\#'></item>"
				+ "<item id='1272529182234'  text='\u6570\u636E\u7BA1\u7406|/console/editor'></item>"
				+ "<item id='1272529181812'  text='\u6CE8\u9500|/console/shared/logout.jsp'></item>"
				+ "</tree>";

		test.getHtmlStr(s);

	}

	public String getResultHtml()
	{
		return resultHtml;
	}

	public void setResultHtml(String resultHtml)
	{
		this.resultHtml = resultHtml;
	}

	/**
	 * 获取根元素
	 * 
	 * @return
	 * @throws DocumentException
	 */
	public Element getRootElement(String srcXml) throws DocumentException
	{
		Document srcdoc = DocumentHelper.parseText(srcXml);
		Element elem = srcdoc.getRootElement();
		return elem;
	}

	/**
	 * 递归遍历方法
	 * 
	 * @param element
	 *            为xml文档对应的根节点
	 */
	public void getHtmlCode(Element element, String s)
	{

		int nodeDeepth = 0;
		blank = " " + s;
		List elements = element.elements();
		if (elements.size() == 0)
		{

		}
		else
		{
			// 有子元素
			String id = element.attributeValue("id");
			nodeDeepth = getDeepthByElementId(id);
			if (0 == nodeDeepth)
			{
				resultHtml += "\n" + blank + "<ul id='nav'> ";
			}
			else if (1 == nodeDeepth)
			{
				resultHtml += "\n" + blank + "<ul class='sub'> ";
			}
			else
			{
				resultHtml += "\n" + blank + "<ul> ";
			}

			for (Iterator it = elements.iterator(); it.hasNext();)
			{

				Element elem = (Element) it.next();
				String text = elem.attributeValue("text");
				id = elem.attributeValue("id");
				nodeDeepth = getDeepthByElementId(id);

				String subStr[] = text.split("\\|");

				String title = subStr[0];
				String href = "#";
				if (subStr.length > 1)
					href = subStr[1];

				if (elem.elements().size() != 0)
				{

					if (nodeDeepth > 1)
					{
						// 如果一个节点还有子节点

						resultHtml += blank + "<li><a class=\"fly\"";

						resultHtml += " href=\"" + href + "\">" + title;
						resultHtml += "</a>";

						deepth++;
						getHtmlCode(elem, "  ");

						resultHtml += "</li>";
						resultHtml += "\n";

					}
					else
					{

						resultHtml += blank
								+ "<li class=\"top\"><a href=\""
								+ href
								+ "\" class=\"top_link\"><span class=\"down\" >";
						resultHtml += title;
						resultHtml += "</span></a>";

						deepth++;
						getHtmlCode(elem, "  ");

						resultHtml += "</li>" + "\n";
					}

				}
				else
				{
					if (nodeDeepth > 1)
					{

						resultHtml += blank + "<li><a href=\"" + href
								+ "\"><span>";
						resultHtml += title;
						resultHtml += "</span></a></li>";
						resultHtml += "\n";

					}
					else
					{
						resultHtml += blank + "<li class=\"top\"><a href=\""
								+ href + "\" class=\"top_link\"><span>";
						resultHtml += title;
						resultHtml += "</span></a></li>";
						resultHtml += "\n";

					}

				}

			}

			resultHtml += "</ul> ";
		}
	}

	/**
	 * 通过xml元素id属性得到元素的在文档树中的深度值
	 * 
	 * @author lxd
	 * @return 元素的在文档树中的深度值
	 * @param id
	 *            xml元素id属性
	 */
	private int getDeepthByElementId(String id)
	{
		return hash.get(id).intValue();
	}

	/**
	 * 通过xml元素的id属性得到元素的在文档树中的深度值
	 * 
	 * @author lxd
	 * @return 元素的在文档树中的深度值
	 * @param id
	 *            xml元素id属性
	 */
	private void storeNodeDeepthInfo(Element e)
	{
		deepth = -1;
		// String id = e.attributeValue("id");
		levelList.add(e);
		recordDeepthInfo(levelList);

	}

	/**
	 * 通过广度优先搜索记录每一层节点的深度
	 * 
	 * @author lxd
	 * @param al
	 */

	private void recordDeepthInfo(ArrayList<Element> al)
	{
		if (al.size() == 0)
			return;

		deepth++;
		for (Element e : al)
		{
			String id = e.attributeValue("id");
			hash.put(id, deepth);
		}

		ArrayList<Element> temp = new ArrayList<Element>(al);

		al.clear();

		for (Element e : temp)
		{
			if (e.elements().size() != 0)
			{
				Iterator iter = e.elements().iterator();
				while (iter.hasNext())
				{

					Element child = (Element) iter.next();
					al.add(child);
				}
			}
		}

		recordDeepthInfo(al);

	}

	/**
	 * 
	 */
	public static XmlToHtml getInstance()
	{
		if (null == instance)
		{
			return new XmlToHtml();
		}
		else
		{
			return instance;
		}
	}
}
