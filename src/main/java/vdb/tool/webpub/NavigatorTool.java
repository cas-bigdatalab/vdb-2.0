package vdb.tool.webpub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Query;
import vdb.metacat.fs.page.Navigator;
import vdb.mydb.VdbManager;
import vdb.tool.da.DataAccessTool;

public class NavigatorTool
{

	public static final String TYPE_TEXT = "text";

	public static final String TYPE_SQL = "sql";

	private static int deepth = 1;

	/**
	 * 根据文本，sql生成三元组树形菜单
	 * 
	 * @param type
	 *            类型
	 * @param query
	 * @param uri
	 *            数据集uri
	 * @return
	 * @throws Exception
	 */
	public String getQueryXml(String type, String query, String uri)
			throws Exception
	{

		if (TYPE_TEXT.equals(type))
		{// 文本

			String[] items = query.split(";");// 获得每个节点
			List<Navigator> list = buildNavigatorTree(this.setNavigators(items));

			StringBuffer sb = new StringBuffer();
			// sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			// sb.append("<tree id='0'>");
			// 树结构的xml
			for (Navigator nav : list)
			{
				sb.append("<item id='" + nav.getId() + "' text='"
						+ nav.getText() + "'>");
				this.parseTreeXml(nav, sb);
				sb.append("</item>");
			}
			// sb.append("</tree>");
			return sb.toString();

		}
		else if (TYPE_SQL.equals(type))
		{// sql

			String[] subItems = this.parseSqlFields(query).split(",");// 获取查询字段
			if (subItems.length != 3)
			{
				return null;
			}

			List<Map<String, Serializable>> list = new DataAccessTool()
					.execute(uri, query);
			List<Navigator> ln = new ArrayList<Navigator>();
			for (Map<String, Serializable> map : list)
			{// 封装数据库记录,三元组导航对象
				// String tmp
				// for(Object obj:map.keySet())
				// {
				// String
				// }
				// ln.add(new
				// Navigator(map.get(subItems[0]).toString(),map.get(subItems[1]).toString(),map.get(subItems[2]).toString()));
				Object[] obj = map.keySet().toArray();
				ln.add(new Navigator(map.get(obj[0]).toString(), map
						.get(obj[1]).toString(), map.get(obj[2]).toString()));
			}
			List<Navigator> li = buildNavigatorTree(ln);

			StringBuffer sb = new StringBuffer();
			// sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			// sb.append("<tree id='0'>");
			// 树结构的xml
			for (Navigator nav : li)
			{
				sb.append("<item id='" + nav.getId() + "' text='"
						+ nav.getText() + "'>");
				this.parseTreeXml(nav, sb);
				sb.append("</item>");
			}
			// sb.append("</tree>");
			return sb.toString();

		}
		else
		{
			return "";
		}
	}

	/**
	 * 递归遍历生成三元组树形对象
	 * 
	 * @param navs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Navigator> buildNavigatorTree(List<Navigator> navs)
	{
		Navigator topNav = null;
		List<Navigator> list = new ArrayList<Navigator>();
		for (Iterator iterator = navs.iterator(); iterator.hasNext();)
		{
			Navigator nav = (Navigator) iterator.next();
			if ("0".equals(nav.getPid()))
			{// 根节点
				topNav = nav;
				topNav.setNavigators(new ArrayList<Navigator>());
				// iterator.remove();
				buildTree(topNav, navs);
				list.add(topNav);
			}
		}
		return list;

	}

	/**
	 * 递归生成三元组树形菜单xml格式文件,dhtmlx
	 * 
	 * @param nav
	 * @param sb
	 * @return
	 */
	private String parseTreeXml(Navigator nav, StringBuffer sb)
	{
		if (nav.getNavigators() != null && nav.getNavigators().size() > 0)
		{
			// sb.append("<item id='" + nav.getId() + "' text='" + nav.getText()
			// + "'>");
			for (Navigator n : nav.getNavigators())
			{
				sb.append("<item id='" + n.getId() + "' text='" + n.getText()
						+ "'>");
				parseTreeXml(n, sb);
				sb.append("</item>");
			}
			// sb.append("</item>");
			return sb.toString();
		}
		else
		{
			return sb.toString();
		}
	}

	/**
	 * 递归遍历生成三元组树形对象
	 * 
	 * @param pnav
	 * @param navs
	 */
	@SuppressWarnings("unchecked")
	private void buildTree(Navigator pnav, List<Navigator> navs)
	{
		for (Iterator iterator = navs.iterator(); iterator.hasNext();)
		{
			Navigator nav = (Navigator) iterator.next();
			if (nav.getPid().equals(pnav.getId()))// &&
			// !nav.getId().equals(pnav.getId()))
			// {
			{
				if (null == pnav.getNavigators())
				{
					pnav.setNavigators(new ArrayList<Navigator>());
				}
				pnav.getNavigators().add(nav);
				// iterator.remove();
			}
		}
		if (navs.size() == 0)
		{
			return;
		}
		if (null != pnav.getNavigators())
		{
			for (Navigator nav2 : pnav.getNavigators())
			{
				// if (!"0".equals(nav2.getPid())) {
				buildTree(nav2, navs);
				// }
			}
		}

	}

	/**
	 * 解析页面输入的三元组,生成对象列表
	 * 
	 * @param items
	 * @return
	 */
	private List<Navigator> setNavigators(String[] items)
	{
		List<Navigator> list = new ArrayList<Navigator>();
		for (String s1 : items)
		{
			String[] subItems = s1.split(",");
			if (subItems.length != 3)
			{
				return null;
			}
			else
			{
				Navigator nav = new Navigator(subItems[0], subItems[1],
						subItems[2]);

				list.add(nav);
			}
		}
		return list;
	}

	/**
	 * 数据集导航
	 * 
	 * @param dataseturi
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getDataSetXml(String dataseturi)
	{
		StringBuffer sb = new StringBuffer();
		if ("all".equals(dataseturi))
		{// 全部数据集
			DataSet[] ds = VdbManager.getInstance().getDomain().getDataSets();
			for (DataSet d : ds)
			{
				sb.append("<item id='" + d.getId() + "' text='" + d.getTitle()
						+ "'>");
				for (Entity e : d.getEntities())
				{
					if (e instanceof Query)
					{// 查询
						sb.append("<item id='" + e.getId() + "' text='"
								+ e.getTitle() + "|"
								+ VdbManager.getEngine().getWebpub()
								+ "/showQuery.vpage?uri=" + e.getUri()
								+ "'></item>");
					}
					else
					{// 实体
						sb.append("<item id='" + e.getId() + "' text='"
								+ e.getTitle() + "|"
								+ VdbManager.getEngine().getWebpub()
								+ "/showEntity.vpage?uri=" + e.getUri()
								+ "'></item>");
					}
				}
				sb.append("</item>");
			}
		}
		else
		{
			DataSet ds = VdbManager.getInstance().getCatalog().fromUri(
					dataseturi);
			sb.append("<item id='" + ds.getId() + "' text='" + ds.getTitle()
					+ "'>");
			for (Entity e : ds.getEntities())
			{
				if (e instanceof Query)
				{// 查询
					sb.append("<item id='" + e.getId() + "' text='"
							+ e.getTitle() + "|"
							+ VdbManager.getEngine().getWebpub()
							+ "/showQuery.vpage?uri=" + e.getUri()
							+ "'></item>");
				}
				else
				{// 实体
					sb.append("<item id='" + e.getId() + "' text='"
							+ e.getTitle() + "|"
							+ VdbManager.getEngine().getWebpub()
							+ "/showEntity.vpage?uri=" + e.getUri()
							+ "'></item>");
				}
			}
			sb.append("</item>");

		}
		return sb.toString();
	}

	/**
	 * 生成导航目录树xml
	 * 
	 * @param xml
	 *            导航目录树模板xml
	 * @param datasetUri
	 *            数据集uri
	 * @param ternarytype
	 *            三元组类型(text,sql)
	 * @param ternaryuri
	 *            三元组sql数据集uri
	 * @param query
	 *            三元组数据
	 * @return
	 */
	public String getNavXml(String xml) throws Exception
	{
		String temp = xml;
		String temp1 = "";
		while (temp.indexOf("dataset") != -1)
		{// 数据集

			String type_dataset = temp.substring(temp.indexOf("dataset") - 10,
					temp.indexOf("</item>", temp.indexOf("dataset")))
					+ "</item>";// "<item id='dataset' select='1'
			// text='数据集目录节点'></item>";
			String datasetUri = type_dataset.substring(type_dataset
					.indexOf("text") + 6, // type_dataset.indexOf("text",
					type_dataset.indexOf("^"));
			// String type_dataset = "";
			// if(xml.indexOf(TYPE_DATASET_SELECTED) != -1){
			temp = temp.replace(type_dataset, this.getDataSetXml(datasetUri));
			// }
			// if(xml.indexOf(TYPE_DATASET) != -1){
			// temp = xml.replace(TYPE_DATASET, this.getDataSetXml(datasetUri));
			// }
		}

		temp1 = temp;
		while (temp1.indexOf("ternary") != -1)
		{// 三元组
			// if(temp.indexOf(TYPE_TERNARY_SELECTED) != -1){
			String type_ternary = temp1.substring(
					temp1.indexOf("ternary") - 10, temp1.indexOf("</item>",
							temp1.indexOf("ternary")))
					+ "</item>";// "<item id='dataset' select='1'
			// text='数据集目录节点'></item>";
			String ternaryText = type_ternary.substring(type_ternary
					.indexOf("text") + 6, type_ternary.indexOf("'>",
					type_ternary.indexOf("text")));
			String[] ternList = ternaryText.split("\\^");
			String ternaryType = ternList[0];
			String ternaryUri = ternList[1];
			String query = ternList[3];
			String queryString = this.getQueryXml(ternaryType, query,
					ternaryUri);
			if (queryString != null)
			{
				temp1 = temp1.replace(type_ternary, queryString);
			}
			else
			{
				temp1 = temp1.replace(type_ternary, "<item id='t_"
						+ System.currentTimeMillis()
						+ "'  text='三元组树形结构数据错误'></item>");
			}
		}

		return temp1.replace("\"", "\'");
	}

	public static final String TYPE_DATASET_SELECTED = "<item id='dataset' select='1' text='数据集目录节点'></item>";

	public static final String TYPE_DATASET = "<item id='dataset'  text='数据集目录节点'></item>";

	public static final String TYPE_TERNARY_SELECTED = "<item id='ternary' select='1' text='动态树节点'></item>";

	public static final String TYPE_TERNARY = "<item id='ternary'  text='动态树节点'></item>";

	/**
	 * 获取sql语句中的字段名(3)
	 * 
	 * @param sql
	 * @return
	 */
	private String parseSqlFields(String sql)
	{
		if (sql.length() < 7)
			return "";
		String temp = sql.replace(" ", "");
		return temp.substring(6, temp.indexOf("from"));
	}

	/**
	 * 树形菜单xml生成水平导航菜单xml
	 * 
	 * @param treexml
	 *            树形菜单xml
	 * @return
	 */
	public String getMenuXml(String treexml)
	{
		return treexml.replace("tree", "menu");
	}

	/**
	 * 树形菜单xml生成菜单项列表
	 * 
	 * @param treexml
	 *            树形菜单xml
	 * @return
	 */
	public List<String> getMenusXml(String treexml) throws DocumentException
	{
		List<String> menus = new ArrayList<String>();
		Document treeDoc = DocumentHelper.parseText(treexml);
		List<Element> elems = treeDoc.getRootElement().elements();
		for (Element e : elems)
		{
			menus.add(e.attributeValue("text"));
		}
		return menus;
	}

	/**
	 * 由xml生成水平导航的html片段,用于代替getMenuXml(String treexml)方法
	 * 
	 * @author lxd
	 * @param String
	 *            treexml
	 */
	public String getMenuHtml(String treeXml)
	{
		XmlToHtml xTh = XmlToHtml.getInstance();
		String htmlStr = xTh.getHtmlStr(treeXml);
		return htmlStr;
	}

}
