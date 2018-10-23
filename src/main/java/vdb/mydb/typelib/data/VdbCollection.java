package vdb.mydb.typelib.data;

import java.util.List;

import vdb.mydb.bean.AnyBean;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SdefNode;
import vdb.mydb.typelib.sdef.SimpleSdef;
import cn.csdb.commons.jsp.Pageable;

/*
 * @author bluejoe
 */
public class VdbCollection extends AbstractData
{
	private Pageable<AnyBean> _beans;

	public Sdef getAsSdef()
	{
		SimpleSdef sd = new SimpleSdef();
		SdefNode value = sd.addChild("value");
		SdefNode items = value.addChild("items");
		try
		{
			List<AnyBean> list = list();
			for (AnyBean bean : list)
			{
				SdefNode item = items.addChild("item");
				item.setNodeValue(bean.getUri().toString());
			}
		}
		catch (Exception e)
		{
		}
		return sd;
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
	}

	public Pageable<AnyBean> getBeans()
	{
		return _beans;
	}

	public void setBeans(Pageable<AnyBean> beans)
	{
		_beans = beans;
	}

	public List<AnyBean> list() throws Exception
	{
		if (_beans != null)
			return _beans.list(1, -1);
		return null;
	}

	/**
	 * @deprecated
	 */
	public List<AnyBean> listAll() throws Exception
	{
		return list();
	}

	public String getAsText()
	{
		return getAsSdef().getValue();
	}

	public void setAsText(String text)
	{
	}

	public boolean isEmpty() throws Exception
	{
		// 集合类型不能用getValue()来判断是否为空
		// getValue是得到value节点的内容
		// 而集合类型value节点下还有items节点
		if (_beans == null || _beans.list(1, -1) == null
				|| _beans.list(1, -1).size() == 0)
			return true;
		else
			return false;
	}

	// 覆盖父类方法，集合类型字段的长度为0
	public long getBytes()
	{
		return 0;
	}
}
