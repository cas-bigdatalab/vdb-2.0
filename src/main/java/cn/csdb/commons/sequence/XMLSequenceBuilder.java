/*
 * 创建日期 2003-11-12
 * 
 */
package cn.csdb.commons.sequence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * 完成从XML文件中存取sequence集合。
 * 
 * <pre>
 *      使用getInstance()获取SequenceBuilder之后，可以使用loadFromXML()方法
 *      从指定XML文件中读取所有的sequence。
 *      &lt;?xml version=&quot;1.0&quot; encoding=&quot;gb2312&quot;?&gt;
 *      &lt;root&gt;
 *       &lt;sequences&gt;
 *        &lt;sequence name=&quot;hello&quot;&gt;
 *         &lt;beginning&gt;140&lt;/beginning&gt;
 *         &lt;interval&gt;20&lt;/interval&gt;
 *        &lt;/sequence&gt;
 *       &lt;/sequences&gt;
 *      &lt;/root&gt;
 * </pre>
 * 
 * @author wkc,bluejoe
 */
public class XMLSequenceBuilder implements SequenceBuilder
{
	private Map sequenceMap = new HashMap();

	private String sequenceXMLPath = null;

	static XMLSequenceBuilder sb = null;

	/**
	 * 单例模式，禁止外部通过构造子实例化
	 */
	private XMLSequenceBuilder()
	{
	}

	public static XMLSequenceBuilder getInstance()
	{
		if (sb == null)
			sb = new XMLSequenceBuilder();

		return sb;
	}

	public int loadFromXML(String xmlPath) throws Exception
	{
		sequenceXMLPath = xmlPath;

		// 读取xml文件
		SAXBuilder parser = new SAXBuilder();
		Document document = parser.build(xmlPath);
		Element domRoot = document.getRootElement();

		Element pse = domRoot.getChild("sequences");

		List sequences = pse.getChildren("sequence");
		int count = 0;
		for (int i = 0; i < sequences.size(); i++)
		{
			Element element = (Element) sequences.get(i);
			Map properties = new HashMap();

			// 属性
			List attributes = element.getAttributes();
			for (int m = 0; m < attributes.size(); m++)
			{
				try
				{
					Attribute attribute = (Attribute) attributes.get(m);
					properties.put(attribute.getName().toUpperCase(), attribute
							.getValue());
				}
				catch (Exception e)
				{
					continue;
				}
			}

			// 元素
			List elements = element.getChildren();
			for (int m = 0; m < elements.size(); m++)
			{
				try
				{
					Element node = (Element) elements.get(m);
					String nodeName = node.getName();
					// 剔除无效元素
					if (nodeName != null && !(nodeName.trim().equals("")))
					{
						properties.put(nodeName.toUpperCase(), node.getText());
					}
				}
				catch (Exception e)
				{
					continue;
				}
			}

			String sequenceName = (String) properties.get("NAME");
			if (sequenceName != null)
			{
				Sequence sequence = new Sequence(this, sequenceName, Long
						.parseLong((String) properties.get("BEGINNING")), Long
						.parseLong((String) properties.get("INTERVAL")));

				addSequence(sequence);
				count++;
			}
		}

		return count;
	}

	/**
	 * @param sequence
	 */
	public void setSequence(Sequence sequence)
	{
		sequenceMap.put(sequence.getName(), sequence);
	}

	public void removeSequence(Sequence sequence)
	{
		sequenceMap.remove(sequence.getName());
	}

	/**
	 * 获取一个sequence
	 * 
	 * @param name
	 */
	public Sequence getSequence(String sequenceName)
	{
		Sequence sequence = (Sequence) sequenceMap.get(sequenceName);
		return sequence;
	}

	/**
	 * 清除所有的Sequence
	 * 
	 */
	public void removeAll()
	{
		sequenceMap.clear();
	}

	private void serialize(Sequence o, Element e)
	{
		// 设置属性
		e.setAttribute("name", o.getName());

		// 设置元素
		Element ine = new Element("beginning");
		ine.setText("" + o.getBeginning());
		e.addContent(ine);

		ine = new Element("interval");
		ine.setText("" + o.getInterval());
		e.addContent(ine);
	}

	/**
	 * 将所有sequence对象写回文件
	 * 
	 * @throws Exception
	 */
	public void serialize() throws IOException
	{
		if (sequenceXMLPath == null)
			return;

		Document document = new Document();
		Element domRoot = new Element("root");
		document.setRootElement(domRoot);

		Element pse = new Element("sequences");
		domRoot.addContent(pse);

		Iterator it = sequenceMap.keySet().iterator();
		while (it.hasNext())
		{
			Sequence sequence = getSequence((String) it.next());
			Element ne = new Element("sequence");
			serialize(sequence, ne);
			pse.addContent(ne);
		}

		File file = new File(sequenceXMLPath);
		Format f = Format.getPrettyFormat();
		f.setEncoding("gb2312");
		f.setIndent("\t");
		XMLOutputter xo = new XMLOutputter(f);
		OutputStream os = new FileOutputStream(file);
		xo.output(document, os);
	}

	/*
	 * @see cn.csdb.commons.SequenceBuilder#addSequence(cn.csdb.commons.Sequence)
	 */
	public void addSequence(Sequence sequence)
	{
		sequenceMap.put(sequence.getName(), sequence);
	}
}
