package vdb.metacat.fs.page;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.util.CatalogUtil;

import com.thoughtworks.xstream.XStream;

/**
 * pages辅助类,操作xml文件
 * 
 * @author gzb
 * 
 */
public class PagesHelper
{

	public static final String CHART_CONFIG_FILE_NAME = "pages.xml";// 配置文件名称

	private static PagesHelper helper = new PagesHelper();

	private XStream xstream = null;

	private PagesHelper()
	{

	}

	/**
	 * 单例模式,保证一个对象读写配置文件
	 * 
	 * @return
	 */
	public static PagesHelper getInstance()
	{
		return helper;
	}

	/**
	 * 获得配置文件完整路径
	 * 
	 * @param dataSetUri
	 *            数据集uri
	 * @return
	 */
	public File getFile(String dataSetUri)
	{
		return new File(CatalogUtil.getDataSetRoot(dataSetUri),
				CHART_CONFIG_FILE_NAME);
	}

	/**
	 * 获得配置文件完整路径
	 * 
	 * @param dataSet
	 *            数据集
	 * @return
	 */
	public File getFile(DataSet dataSet)
	{
		return new File(CatalogUtil.getDataSetRoot(dataSet),
				CHART_CONFIG_FILE_NAME);
	}

	/**
	 * 加载page页面对象
	 * 
	 * @return Document
	 * @throws DocumentException-文件没有找到或恶意修改配置文件
	 */
	public Document load(DataSet dataSet) throws DocumentException
	{

		SAXReader reader = new SAXReader();
		return reader.read(this.getFile(dataSet));

	}

	/**
	 * 加载page页面对象
	 * 
	 * @return Document
	 * @throws DocumentException-文件没有找到或恶意修改配置文件
	 */
	public Document load(File file) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		return reader.read(file);
	}

	/**
	 * 初始化page页面对象
	 * 
	 * @return
	 */
	public Document init()
	{
		return DocumentHelper.createDocument();
	}

	/**
	 * 格式化输出并保存配置文件
	 * 
	 * @param doc
	 * @param dataSetUri
	 * @throws Exception
	 */
	public void save(Document doc, File file) throws Exception
	{
		// 格式化配置文件(浏览器模式)
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 将document中的内容输出
		XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);

		writer.setEscapeText(false);// 关闭特殊字符转换
		writer.write(doc);
		writer.close();
	}

	/**
	 * 根据相应的页面对象生成xml
	 * 
	 * @param page
	 * @return
	 */
	public String getXmlFromObj(Page page)
	{
		if (page instanceof AddItemPage)
		{// 增加页面
			xstream = new XStream();
			xstream.alias("page", AddItemPage.class);
			xstream.alias("editField", EditField.class);
			return xstream.toXML((AddItemPage) page);

		}
		else if (page instanceof ListEditItemsPage)
		{// 编辑列表页面
			xstream = new XStream();
			xstream.alias("page", ListEditItemsPage.class);
			xstream.alias("orderField", OrderField.class);
			xstream.alias("editField", EditField.class);
			return xstream.toXML((ListEditItemsPage) page);

		}
		else if (page instanceof ListItemsPage)
		{// 列表页面
			xstream = new XStream();
			xstream.alias("page", ListItemsPage.class);
			xstream.alias("orderField", OrderField.class);
			xstream.alias("viewField", ViewField.class);
			return xstream.toXML((ListItemsPage) page);

		}
		else if (page instanceof ShowItemPage)
		{// 显示页面
			xstream = new XStream();
			xstream.alias("page", ShowItemPage.class);
			xstream.alias("viewField", ViewField.class);
			return xstream.toXML((ShowItemPage) page);

		}
		else if (page instanceof UpdateItemPage)
		{// 更新页面
			xstream = new XStream();
			xstream.alias("page", UpdateItemPage.class);
			xstream.alias("editField", EditField.class);
			return xstream.toXML((UpdateItemPage) page);

		}
		else
		{
			return null;
		}
	}

	/**
	 * 从xml文件获得相应page对象
	 * 
	 * @param xml
	 * @param type
	 * @return
	 */
	public Page getObjFromXml(String xml, String type)
	{
		if (PagesManager.PAGE_TYPE_ADD.equals(type))
		{// 增加页面
			xstream = new XStream();
			xstream.alias("page", AddItemPage.class);
			xstream.alias("editField", EditField.class);
			return (AddItemPage) xstream.fromXML(xml);

		}
		else if (PagesManager.PAGE_TYPE_LIST.equals(type))
		{// 列表页面
			xstream = new XStream();
			xstream.alias("page", ListItemsPage.class);
			xstream.alias("orderField", OrderField.class);
			xstream.alias("viewField", ViewField.class);
			return (ListItemsPage) xstream.fromXML(xml);

		}
		else if (PagesManager.PAGE_TYPE_LISTEDIT.equals(type))
		{// 编辑列表页面
			xstream = new XStream();
			xstream.alias("page", ListEditItemsPage.class);
			xstream.alias("orderField", OrderField.class);
			xstream.alias("editField", EditField.class);
			return (ListEditItemsPage) xstream.fromXML(xml);

		}
		else if (PagesManager.PAGE_TYPE_SHOW.equals(type))
		{// 显示页面
			xstream = new XStream();
			xstream.alias("page", ShowItemPage.class);
			xstream.alias("viewField", ViewField.class);
			return (ShowItemPage) xstream.fromXML(xml);

		}
		else if (PagesManager.PAGE_TYPE_UPDATE.equals(type))
		{// 更新页面
			xstream = new XStream();
			xstream.alias("page", UpdateItemPage.class);
			xstream.alias("editField", EditField.class);
			return (UpdateItemPage) xstream.fromXML(xml);
		}
		else
		{
			return null;
		}

	}

	/**
	 * 获得数据集下所有实体的默认页面(一个实体对应5个页面)
	 * 
	 * @return
	 */
	public List<Page> getDefaultPagesFromDataSet(DataSet ds)
	{
		Entity[] entities = ds.getEntities();
		List<Page> list = new ArrayList<Page>();
		for (Entity e : entities)
		{// 遍历实体
			List<Page> temp = getDefaultPagesFromEntity(e);
			for (Page p : temp)
			{// 遍历每一实体下的5类页面
				list.add(p);
			}
		}
		return list;
	}

	/**
	 * 获得一个实体的默认页面
	 * 
	 * @param entity
	 * @return
	 */
	public List<Page> getDefaultPagesFromEntity(Entity entity)
	{
		List<Page> list = new ArrayList<Page>();

		// 新增页面
		AddItemPage add = new AddItemPage();
		add.setName(PagesManager.PAGE_TYPE_ADD + "[" + entity.getUri() + "]");
		add.setDefault(true);
		add.setTitle(entity.getTitle() + "-新增页面");
		add.setEntityUri(entity.getUri());
		add.setType(PagesManager.PAGE_TYPE_ADD);
		List<EditField> field1 = new ArrayList<EditField>();
		for (Field f : entity.getFields())
		{
			if (!f.getTypeName().equalsIgnoreCase("Expression"))
				field1.add(new EditField(f.getUri(), this
						.parseFieldDefaultValue(f.getDefaultValue()), false));
		}
		add.setEditFields(field1);
		list.add(add);

		// 列表编辑页面
		ListEditItemsPage listEdit = new ListEditItemsPage();
		listEdit.setName(PagesManager.PAGE_TYPE_LISTEDIT + "["
				+ entity.getUri() + "]");
		listEdit.setDefault(true);
		listEdit.setTitle(entity.getTitle() + "-列表编辑页面");
		listEdit.setEntityUri(entity.getUri());
		listEdit.setType(PagesManager.PAGE_TYPE_LISTEDIT);
		listEdit.setGrantFilter("");
		List<EditField> field2 = new ArrayList<EditField>();
		for (Field f : entity.getFields())
		{
			field2.add(new EditField(f.getUri(), this.parseFieldDefaultValue(f
					.getDefaultValue()), false));
		}
		listEdit.setEditFields(field2);
		list.add(listEdit);

		// 列表页面
		ListItemsPage listItem = new ListItemsPage();
		listItem.setName(PagesManager.PAGE_TYPE_LIST + "[" + entity.getUri()
				+ "]");
		listItem.setDefault(true);
		listItem.setTitle(entity.getTitle() + "-列表页面");
		listItem.setEntityUri(entity.getUri());
		listItem.setType(PagesManager.PAGE_TYPE_LIST);
		listItem.setGrantFilter("");
		List<ViewField> field3 = new ArrayList<ViewField>();
		for (Field f : entity.getFields())
		{
			field3.add(new ViewField(f.getUri()));
		}
		listItem.setOrderFields(null);
		listItem.setViewFields(field3);
		list.add(listItem);

		// 显示页面
		ShowItemPage showItem = new ShowItemPage();
		showItem.setName(PagesManager.PAGE_TYPE_SHOW + "[" + entity.getUri()
				+ "]");
		showItem.setDefault(true);
		showItem.setTitle(entity.getTitle() + "-显示页面");
		showItem.setEntityUri(entity.getUri());
		showItem.setType(PagesManager.PAGE_TYPE_SHOW);

		List<ViewField> field4 = new ArrayList<ViewField>();
		for (Field f : entity.getFields())
		{
			field4.add(new ViewField(f.getUri()));
		}

		showItem.setViewFields(field4);
		list.add(showItem);

		// 更新页面
		UpdateItemPage update = new UpdateItemPage();
		update.setName(PagesManager.PAGE_TYPE_UPDATE + "[" + entity.getUri()
				+ "]");
		update.setDefault(true);
		update.setTitle(entity.getTitle() + "-更新页面");
		update.setEntityUri(entity.getUri());
		update.setType(PagesManager.PAGE_TYPE_UPDATE);
		List<EditField> field5 = new ArrayList<EditField>();
		for (Field f : entity.getFields())
		{
			if (!f.getTypeName().equalsIgnoreCase("Expression"))
				field5.add(new EditField(f.getUri(), this
						.parseFieldDefaultValue(f.getDefaultValue()), false));
		}
		update.setEditFields(field5);
		list.add(update);

		return list;
	}

	/**
	 * 处理字段默认属性
	 * 
	 * @param obj
	 * @return
	 */
	private String parseFieldDefaultValue(Object obj)
	{
		if (obj == null)
		{
			return "";
		}
		else
		{
			return obj.toString();
		}
	}

	/**
	 * 根据页面类型获得默认页面对象
	 * 
	 * @param entity
	 * @param type
	 * @return
	 */
	public Page getDefaultPagesFromEntity(Entity entity, String type)
	{
		if (PagesManager.PAGE_TYPE_ADD.equals(type))
		{// 增加页面

			AddItemPage add = new AddItemPage();
			add.setName(PagesManager.PAGE_TYPE_ADD + "[" + entity.getUri()
					+ "]");
			add.setDefault(true);
			add.setTitle(entity.getTitle() + "-新增页面");
			add.setEntityUri(entity.getUri());
			add.setType(PagesManager.PAGE_TYPE_ADD);
			List<EditField> field1 = new ArrayList<EditField>();
			for (Field f : entity.getFields())
			{
				if (!f.getTypeName().equalsIgnoreCase("Expression"))
					field1
							.add(new EditField(f.getUri(),
									this.parseFieldDefaultValue(f
											.getDefaultValue()), false));
			}
			add.setEditFields(field1);
			return add;

		}
		else if (PagesManager.PAGE_TYPE_LIST.equals(type))
		{// 列表页面

			ListItemsPage listItem = new ListItemsPage();
			listItem.setName(PagesManager.PAGE_TYPE_LIST + "["
					+ entity.getUri() + "]");
			listItem.setDefault(true);
			listItem.setTitle(entity.getTitle() + "-列表页面");
			listItem.setEntityUri(entity.getUri());
			listItem.setType(PagesManager.PAGE_TYPE_LIST);
			listItem.setGrantFilter("");
			List<ViewField> field3 = new ArrayList<ViewField>();
			List<OrderField> field4 = new ArrayList<OrderField>();
			for (Field f : entity.getFields())
			{
				field3.add(new ViewField(f.getUri()));
				OrderField of = new OrderField();
				of.setFieldUri(f.getUri());
				of.setOrder("desc");
				field4.add(of);
			}
			// listItem.setOrderFields(field4);
			listItem.setViewFields(field3);
			return listItem;

		}
		else if (PagesManager.PAGE_TYPE_LISTEDIT.equals(type))
		{// 编辑列表页面

			ListEditItemsPage listEdit = new ListEditItemsPage();
			listEdit.setName(PagesManager.PAGE_TYPE_LISTEDIT + "["
					+ entity.getUri() + "]");
			listEdit.setDefault(true);
			listEdit.setTitle(entity.getTitle() + "-列表编辑页面");
			listEdit.setEntityUri(entity.getUri());
			listEdit.setType(PagesManager.PAGE_TYPE_LISTEDIT);
			listEdit.setGrantFilter("");
			List<EditField> field2 = new ArrayList<EditField>();
			List<OrderField> field4 = new ArrayList<OrderField>();
			for (Field f : entity.getFields())
			{
				field2.add(new EditField(f.getUri(), this
						.parseFieldDefaultValue(f.getDefaultValue()), false));
				OrderField of = new OrderField();
				of.setFieldUri(f.getUri());
				of.setOrder("desc");
				field4.add(of);
			}
			listEdit.setEditFields(field2);
			// listEdit.setOrderFields(field4);
			return listEdit;

		}
		else if (PagesManager.PAGE_TYPE_SHOW.equals(type))
		{// 显示页面

			ShowItemPage showItem = new ShowItemPage();
			showItem.setName(PagesManager.PAGE_TYPE_SHOW + "["
					+ entity.getUri() + "]");
			showItem.setDefault(true);
			showItem.setTitle(entity.getTitle() + "-显示页面");
			showItem.setEntityUri(entity.getUri());
			showItem.setType(PagesManager.PAGE_TYPE_SHOW);

			List<ViewField> field4 = new ArrayList<ViewField>();
			for (Field f : entity.getFields())
			{
				field4.add(new ViewField(f.getUri()));
			}

			showItem.setViewFields(field4);
			return showItem;

		}
		else if (PagesManager.PAGE_TYPE_UPDATE.equals(type))
		{// 更新页面

			UpdateItemPage update = new UpdateItemPage();
			update.setName(PagesManager.PAGE_TYPE_UPDATE + "["
					+ entity.getUri() + "]");
			update.setDefault(true);
			update.setTitle(entity.getTitle() + "-更新页面");
			update.setEntityUri(entity.getUri());
			update.setType(PagesManager.PAGE_TYPE_UPDATE);
			List<EditField> field5 = new ArrayList<EditField>();
			for (Field f : entity.getFields())
			{
				if (!f.getTypeName().equalsIgnoreCase("Expression"))
					field5
							.add(new EditField(f.getUri(),
									this.parseFieldDefaultValue(f
											.getDefaultValue()), false));
			}
			update.setEditFields(field5);
			return update;

		}
		else
		{
			return null;
		}

	}

}
