package vdb.metacat.fs.page;

import java.util.List;

public class Navigator
{

	public Navigator(String id, String text, String pid)
	{
		super();
		this.id = id;
		this.text = text;
		this.pid = pid;
	}

	private String id;// 当前节点id

	private String text;// 显示文本

	private String pid;// 父节点id

	private List<Navigator> navigators;

	public List<Navigator> getNavigators()
	{
		return navigators;
	}

	public void setNavigators(List<Navigator> navigators)
	{
		this.navigators = navigators;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getPid()
	{
		return pid;
	}

	public void setPid(String pid)
	{
		this.pid = pid;
	}

}
