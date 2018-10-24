package cn.csdb.commons.jsp;

import java.util.List;
import java.util.Vector;

/**
 * 该类用于描述树节点。
 * 
 * <pre>
 *      TreeItem设计成与TreeCtrl无关性，TreeItem不依赖于TreeCtrl而存在。
 *      创建节点时须指定该节点的显示文字，必要时还必须指定该节点的ID，
 *      如果没有指定，系统将自动生成一个唯一ID。
 *      创建节点之后还可以调用相应方法设置该节点的链接，LOGO图片，提示文字；
 *      也可以设置该节点的选择、标记、展开状态。
 * </pre>
 * 
 * @author bluejoe
 */
public class TreeItem
{
	private boolean checked;

	private boolean radioed;

	private String itemImage;

	private String itemTitle;

	private String itemID;

	private Object itemData;

	// 该节点的类型
	private boolean isFolder;

	// 显示文字
	private String itemText;

	// 链接地址
	private String href;

	private String target;

	// 父节点
	private TreeItem parent;

	// 子节点列表
	private List children;

	private boolean expanded = false;

	private static long ITEM_UID = 0x1224;

	public TreeItem(String itemText)
	{
		init(null, itemText, null, true);
	}

	public TreeItem(String itemText, String itemID)
	{
		init(null, itemText, itemID, true);
	}

	private TreeItem(TreeItem parent, String itemText, String itemID,
			boolean isFolder)
	{
		init(parent, itemText, itemID, isFolder);
	}

	/**
	 * 完成节点的初始化
	 * 
	 * @param tree
	 * @param parent
	 * @param itemText
	 * @param href
	 * @param isFolder
	 */
	private void init(TreeItem parent, String itemText, String itemID,
			boolean isFolder)
	{
		this.parent = parent;
		this.itemText = itemText;

		if (itemID != null)
		{
			this.itemID = itemID;
		}
		else
		{
			this.itemID = "" + ITEM_UID;
			ITEM_UID++;
		}

		this.isFolder = isFolder;
		children = new Vector();
	}

	/**
	 * 设置链接字符串。
	 * 
	 * 可以在链接字符串使用变量，变量列表如下： $ITEM_ID 该节点的ID $ITEM_TEXT 该节点的文字 $ITEM_TITLE
	 * 该节点的提示文字
	 * 
	 * @param href
	 */
	public void setItemURL(String href)
	{
		this.href = href;
	}

	public void setItemTarget(String target)
	{
		this.target = target;
	}

	public void setItemURL(String href, String target)
	{
		setItemURL(href);
		setItemTarget(target);
	}

	public void setItemImage(String image)
	{
		this.itemImage = image;
	}

	public void append(TreeItem ti)
	{
		ti.parent = this;
		children.add(ti);
	}

	/**
	 * 增加一个目录节点
	 * 
	 * @param itemText
	 *            显示文字
	 * @param href
	 *            链接地址，null则不显示链接
	 */
	public TreeItem appendFolder(String itemText, String itemID)
	{
		TreeItem ti = new TreeItem(this, itemText, itemID, true);
		children.add(ti);

		return ti;
	}

	public TreeItem appendFolder(String itemText)
	{
		return appendFolder(itemText, null);
	}

	/**
	 * 增加一个叶节点
	 * 
	 * @param itemText
	 *            显示文字
	 * @param href
	 *            链接地址，null则不显示链接
	 */
	public TreeItem appendLeaf(String itemText, String itemID)
	{
		TreeItem ti = new TreeItem(this, itemText, itemID, false);
		children.add(ti);

		return ti;
	}

	public TreeItem appendLeaf(String itemText)
	{
		return appendLeaf(itemText, null);
	}

	/**
	 * 获取子节点列表
	 */
	public List getChildren()
	{
		return children;
	}

	public boolean hasChildren()
	{
		return (children.size() != 0);
	}

	public void expand(boolean expand)
	{
		this.expanded = expand;

		// 同时展开父节点
		if (expand && (parent != null))
		{
			parent.expand(expand);
		}
	}

	public String getItemImage(TreeCtrl tree)
	{
		if (itemImage != null && !"".equals(itemImage))
			return itemImage;

		return tree.getDefaultItemImage(isFolder);
	}

	public String toHTML(TreeCtrl tree)
	{
		return toHTML(tree, true);
	}

	/**
	 * 获取该节点的HTML代码
	 */
	public String toHTML(TreeCtrl tree, boolean showMe)
	{
		String HTML = "";
		String spacing = "";
		String si = tree.getSpacingImage();

		if (si != null)
			spacing = " background=\"" + si + "\"";

		if (showMe)
		{
			// 显示节点图标和Item

			// 显示节点图标
			HTML += "<table class=\"tree\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
					+ "<tr>\r\n"
					+ "<td width=\"16\""
					+ spacing
					+ " style=\"padding-top: "
					+ tree.getLineSpacing()
					+ "; padding-bottom: " + tree.getLineSpacing() + "\">\r\n";

			if (!hasChildren())
			{
				HTML += "<img src=\"" + tree.getNodeImage(TreeCtrl.EMPTY_NODE)
						+ "\">";
			}
			else
			{
				HTML += "<img id=\"$NODE_"
						+ getItemID()
						+ "\" src=\""
						+ tree.getNodeImage(expanded ? TreeCtrl.EXPANDED_NODE
								: TreeCtrl.COLLAPSED_NODE)
						+ "\" style=\"cursor: hand\" onClick=\"Toggle('"
						+ getItemID() + "')\">";
			}

			HTML += "</td>\r\n";

			// 显示checkbox
			if (tree.hasCheckBox())
			{
				HTML += "<td width=\"16\">\r\n" + "<input name=\""
						+ tree.getCheckBoxName() + getItemID()
						+ "\" type=\"checkbox\""
						+ (isChecked() ? " checked" : "") + " value=\""
						+ getItemID() + "\">\r\n" + "</td>\r\n";
			}
			// 显示checkbox
			if (tree.hasRadioBox())
			{
				HTML += "<td width=\"16\">\r\n" + "<input name=\""
						+ tree.getRadioBoxName() + "\" type=\"radio\""
						+ (isRadioed() ? " checked" : "") + " value=\""
						+ getItemID() + "\">\r\n" + "</td>\r\n";
			}
			HTML += "<td width=\"16\">\r\n";

			// 显示item图标
			String ii = getItemImage(tree);
			if (hasChildren())
			{
				HTML += (ii == null ? "" : "<img src=\"" + ii + "\">");
			}
			else
			{
				HTML += "<a>" + (ii == null ? "" : "<img src=\"" + ii + "\">")
						+ "</a>";
			}

			HTML += "</td>\r\n";

			// 显示item内容
			// 间隔
			HTML += "<td width=\"2\">\r\n" + "</td>\r\n";

			HTML += "<td align=\"left\" nowrap>\r\n" + getItemHTML(tree)
					+ "</td>\r\n";

			HTML += "</tr>\r\n" + "</table>\r\n";
		}

		// 显示子节点
		if (hasChildren())
		{
			if (showMe)
			{
				String display = expanded ? "block" : "none";
				HTML += "\r\n<div id=\"$BLOCK_" + getItemID()
						+ "\" style=\"display:" + display + "\">\r\n";

				// 缩进
				HTML += "<table class=\"tree\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"table-layout:fixed;\">\r\n"
						+ "<col width=\"16\">\r\n"
						+ "<tr>\r\n"
						+ "<td width=\"16\""
						+ spacing
						+ ">\r\n"
						+ "</td>\r\n"
						+ "<td>\r\n";
			}

			for (int i = 0; i < children.size(); i++)
			{
				TreeItem ti = (TreeItem) children.get(i);
				HTML += ti.toHTML(tree, true);
			}

			if (showMe)
			{
				HTML += "</td></tr></table>\r\n";
				HTML += "</div>\r\n";
			}
		}

		return HTML;
	}

	/**
	 * 返回该Item的HTML代码
	 */
	public String getItemHTML(TreeCtrl tree)
	{

		if (href == null)
			return getItemText();

		String it = href;

		it = it.replaceAll("\\$ITEM_ID", getItemID());
		it = it.replaceAll("\\$ITEM_TEXT", getItemText());
		it = it.replaceAll("\\$ITEM_TITLE", getItemTitle());

		String title = getItemTitle();
		String target = (this.target == null ? tree.getBaseTarget()
				: this.target);

		return "<a id=\"$ITEM_" + getItemID() + "\" onclick=\"Hilight('"
				+ getItemID() + "')\""
				+ (title == null ? "" : " title=\"" + title + "\"")
				+ " href=\"" + it + "\""
				+ (target == null ? "" : " target=\"" + target + "\"") + ">"
				+ itemText + "</a>";
	}

	/**
	 */
	public boolean isFolder()
	{
		return isFolder;
	}

	/**
	 */
	public boolean isChecked()
	{
		return checked;
	}

	public boolean isRadioed()
	{
		return radioed;
	}

	public void checkItem(boolean checked)
	{
		this.checked = checked;
	}

	public void radioItem(boolean radioed)
	{
		this.radioed = radioed;
	}

	/**
	 */
	public String getItemID()
	{
		return itemID;
	}

	/**
	 */
	public String getItemText()
	{
		return itemText;
	}

	/**
	 */
	public String getItemTitle()
	{
		return itemTitle;
	}

	/**
	 * @param string
	 */
	public void setItemTitle(String itemTitle)
	{
		this.itemTitle = itemTitle;
	}

	/**
	 */
	public Object getItemData()
	{
		return itemData;
	}

	/**
	 * @param object
	 */
	public void setItemData(Object object)
	{
		itemData = object;
	}
}