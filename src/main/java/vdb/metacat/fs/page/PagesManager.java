package vdb.metacat.fs.page;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;

public class PagesManager
{

	public static final String PAGE_TYPE_ADD = "addItem";// 新增页面

	public static final String PAGE_TYPE_LIST = "listItems";// 列表页面

	public static final String PAGE_TYPE_SHOW = "showItem";// 显示页面

	public static final String PAGE_TYPE_LISTEDIT = "listEditItems";// 列表编辑页面

	public static final String PAGE_TYPE_UPDATE = "updateItem";// 更新页面

	public static final String[] pageTypes = { PAGE_TYPE_ADD, PAGE_TYPE_LIST,
			PAGE_TYPE_SHOW, PAGE_TYPE_LISTEDIT, PAGE_TYPE_UPDATE };

	private PagesHelper helper = null;

	public PagesManager()
	{

		this.helper = PagesHelper.getInstance();
	}

	public PagesManager(PagesHelper helper)
	{

		this.helper = helper;
	}

	/**
	 * 删除指定页面
	 * 
	 * @param name
	 *            页面id
	 */
	@SuppressWarnings("unchecked")
	public void deletePage(String name, String entityUri)
			throws DocumentException, Exception
	{
		DataSet ds = ((Entity) VdbManager.getEngine().getCatalog().fromUri(
				entityUri)).getDataSet();// 根据实体uri获得当前数据集
		Document doc = helper.load(ds);// 加载所有page页面

		List<Node> node = doc.selectNodes("/pages/entity[@uri='" + entityUri
				+ "']/pageType/page/name");
		for (Node n : node)
		{
			if (name.equals(n.getText()))
			{
				Element page = n.getParent();// page节点
				page.getParent().remove(page);// 删除page节点
				break;
			}
		}
		helper.save(doc, helper.getFile(ds));// 保存结果
	}

	/**
	 * 更新指定页面
	 * 
	 * @param page
	 *            页面
	 */
	public void updatePage(Page page) throws DocumentException, Exception
	{
		DataSet ds = page.getEntity().getDataSet();// 获得数据集

		File file = helper.getFile(ds);// 获得当前页面配置文件存储路径

		if (file.exists())
		{// page页面加载
			this.deletePage(page.getName(), page.getEntity().getUri());// 删除节点
		}
		this.savePage(page);// 保存
	}

	/**
	 * 保存指定页面
	 * 
	 * @param page
	 *            页面
	 * @param entityUri
	 *            实体uri
	 * @param type
	 *            页面类型
	 */

	public void savePage(Page page) throws DocumentException, Exception
	{

		DataSet ds = page.getEntity().getDataSet();// 获得数据集

		String entityUri = page.getEntity().getUri();// 获得实体uri

		File file = helper.getFile(ds);// 获得当前页面配置文件存储路径

		Document doc = null;
		if (file.exists())
		{// page页面加载
			doc = helper.load(file);

			Node sEntity = doc.selectSingleNode("/pages/entity[@uri='"
					+ entityUri + "']");// 获得entity节点

			if (sEntity == null)
			{// entity不存在

				Element root = doc.getRootElement();// 获得根节点
				Element aEntity = root.addElement("entity");// 实体
				aEntity.addAttribute("uri", entityUri);
				Element aPageType = aEntity.addElement("pageType");// 页面类型
				aPageType.addAttribute("type", page.getType());
				// 增加page对象
				aPageType.addText(helper.getXmlFromObj(page));

			}
			else
			{

				Element sPageType = (Element) sEntity
						.selectSingleNode("pageType[@type='" + page.getType()
								+ "']");
				if (sPageType == null)
				{// pageType不存在

					((Element) sEntity).addElement("pageType").addAttribute(
							"type", page.getType()).addText(
							helper.getXmlFromObj(page));

				}
				else
				{
					sPageType.addText(helper.getXmlFromObj(page));
				}

			}
		}
		else
		{// page页面初始化
			doc = helper.init();
			Element pages = doc.addElement("pages");// 根节点
			Element entity = pages.addElement("entity");// 实体
			entity.addAttribute("uri", entityUri);
			Element pageType = entity.addElement("pageType");// 页面类型
			pageType.addAttribute("type", page.getType());
			// 增加page对象
			pageType.addText(helper.getXmlFromObj(page));
		}
		helper.save(doc, file);
	}

	/**
	 * 根据页面类型,实体uri获得页面
	 * 
	 * @param type
	 *            页面类型
	 * @param entityUri
	 *            实体uri
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Page> getPagesByType(String type, String entityUri)
			throws DocumentException
	{

		Entity entity = (Entity) VdbManager.getEngine().getCatalog().fromUri(
				entityUri);// 根据实体uri获得当前实体
		DataSet ds = entity.getDataSet();// 根据实体获得当前数据集

		File file = helper.getFile(ds);
		List<Page> list = new ArrayList<Page>();
		if (!file.exists())
		{// 页面不存在
			list.add(helper.getDefaultPagesFromEntity(entity, type));
			return list;
		}

		Document doc = helper.load(file);// 加载所有page页面

		List<Node> node = doc.selectNodes("/pages/entity[@uri='" + entityUri
				+ "']/pageType[@type='" + type + "']/page");

		if (node.size() > 0)
		{
			for (Node n : node)
			{
				list.add(helper.getObjFromXml(n.asXML(), type));
			}
			// 判断页面列表中是否存在默认页面
			boolean flag = false;
			for (Page p : list)
			{
				if (p.isDefault())
				{
					flag = true;
					break;
				}
			}
			if (!flag) // 如果不存在默认页面，则添加默认页面到列表中
				list.add(helper.getDefaultPagesFromEntity(entity, type));// 将默认页面也添加到列表中
		}
		else
		{// 返回默认页面对象
			list.add(helper.getDefaultPagesFromEntity(entity, type));
			return list;
		}
		return list;
	}

	/**
	 * 获得默认页面对象
	 * 
	 * @param type
	 * @param entityUri
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page getDefaultPageByType(String entityUri, String type)
			throws DocumentException
	{
		Entity entity = (Entity) VdbManager.getEngine().getCatalog().fromUri(
				entityUri);// 根据实体uri获得当前实体
		DataSet ds = entity.getDataSet();// 根据实体获得当前数据集

		File file = helper.getFile(ds);
		if (file.exists())
		{
			Document doc = helper.load(file);// 加载所有page页面
			List<Node> node = doc.selectNodes("/pages/entity[@uri='"
					+ entityUri + "']/pageType[@type='" + type
					+ "']/page/isDefault");
			if (node.size() > 0)
			{// 默认页面已经保存
				for (Node n : node)
				{
					if ("true".equals(n.getText()))
					{// 配置文件中的默认页面
						return helper
								.getObjFromXml(n.getParent().asXML(), type);
					}
				}
			}

			return helper.getDefaultPagesFromEntity(entity, type);
		}
		else
		{// 配置文件不存在,返回默认该类型页面
			return helper.getDefaultPagesFromEntity(entity, type);
		}
	}

	/**
	 * 返回该实体下所有页面,包含默认页面
	 * 
	 * @return
	 */
	public Map<String, List<Page>> getPagesByEntityUri(String entityUri)
			throws DocumentException
	{
		Entity entity = (Entity) VdbManager.getEngine().getCatalog().fromUri(
				entityUri);// 根据实体uri获得当前实体
		DataSet ds = entity.getDataSet();// 根据实体获得当前数据集

		File file = helper.getFile(ds);
		Map<String, List<Page>> map = new HashMap<String, List<Page>>();
		if (file.exists())
		{
			for (String s : pageTypes)
			{
				map.put(s, this.getPagesByType(s, entityUri));
			}
		}
		else
		{// 配置文件不存在返回,一组默认页面
			for (String s : pageTypes)
			{
				List<Page> list = new ArrayList<Page>();
				list.add(helper.getDefaultPagesFromEntity(entity, s));// 获取页面对象
				map.put(s, list);

			}
		}
		return map;
	}

	/**
	 * 返回该实体下所有页面,包含默认页面
	 * 
	 * @return
	 */
	public List<Page> getPagesByEntity(Entity entity) throws DocumentException
	{
		DataSet ds = entity.getDataSet();// 根据实体获得当前数据集

		File file = helper.getFile(ds);
		List<Page> pages = new ArrayList<Page>();
		if (file.exists())
		{
			for (String s : pageTypes)
			{
				List<Page> ps = this.getPagesByType(s, entity.getUri());
				for (int i = 0; i < ps.size(); i++)
				{
					pages.add(ps.get(i));
				}
			}
		}
		else
		{// 配置文件不存在返回,一组默认页面
			for (String s : pageTypes)
			{
				pages.add(helper.getDefaultPagesFromEntity(entity, s));// 获取页面对象
			}
		}
		return pages;
	}

	/**
	 * 根据页面名字获得页面
	 * 
	 * @param name
	 *            页面id
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public Page getPageByName(String name, String entityUri)
			throws DocumentException, UnsupportedEncodingException
	{
		Entity entity = (Entity) VdbManager.getEngine().getCatalog().fromUri(
				entityUri);// 根据实体uri获得当前实体
		DataSet ds = entity.getDataSet();// 根据实体获得当前数据集

		File file = helper.getFile(ds);
		if (!file.exists())
		{// 返回默认页面
			Page page = helper.getDefaultPagesFromEntity(entity, name
					.substring(0, name.indexOf("[")));
			if (page.getName().equalsIgnoreCase(name))
				return page;
		}

		Document doc = helper.load(ds);// 加载所有page页面

		List<Node> node = doc.selectNodes("/pages/entity/pageType/page/name");

		if (node == null || node.size() == 0)
		{// 返回默认页面
			Page page = helper.getDefaultPagesFromEntity(entity, name
					.substring(0, name.indexOf("[")));
			if (page.getName().equalsIgnoreCase(name))
				return page;
		}

		for (Node n : node)
		{
			if (name.equals(n.getText()))
			{
				String type = n.getParent().element("type").getText();// 获得页面类型
				return helper.getObjFromXml(n.getParent().asXML(), type);
			}
		}

		Page page = helper.getDefaultPagesFromEntity(entity, name.substring(0,
				name.indexOf("[")));
		if (page == null)
			page = helper.getDefaultPagesFromEntity(entity, name);
		if (page != null && page.getName().equalsIgnoreCase(name))
			return page;

		return null;
	}

	/**
	 * 获得所有页面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Page> getAllPages(DataSet ds) throws DocumentException
	{
		List<Page> pages = new ArrayList<Page>();
		for (Entity entity : ds.getEntities())
		{
			for (Page page : this.getPagesByEntity(entity))
			{
				pages.add(page);
			}
		}
		return pages;
	}

	/**
	 * 检测页面名称是否重复
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkPageName(String name, DataSet ds)
			throws DocumentException
	{
		File file = helper.getFile(ds);
		if (file.exists())
		{
			Document doc = helper.load(file);// 加载所有page页面
			List<Node> node = doc
					.selectNodes("/pages/entity/pageType/page/name");
			for (Node n : node)
			{
				if (name.equals(n.getText()))
				{// 页面名称重复
					return false;
				}
			}
		}
		else
		{// 文件不存在
			return true;
		}
		return true;
	}

	/**
	 * 获得所有页面类型
	 * 
	 * @return
	 */
	public List<String> getPageTypes() throws DocumentException
	{
		List<String> list = new ArrayList<String>();
		for (String s : pageTypes)
		{
			list.add(s);
		}
		return list;
	}

}
