/*
 * Created on 2003-5-15
 * 
 */
package cn.csdb.commons.jsp;

import java.util.List;

/**
 * 用以实现数据记录分页显示。
 * 
 * @author bluejoe
 */
public class BeanPageViewer extends PageViewer
{
	private List _beans;

	public BeanPageViewer(PageUrlBuilder url, Pageable query, int gotoPage,
			int pageSize) throws Exception
	{
		super(url, query.size(), gotoPage, pageSize);
		_beans = query.list(getBeginning(), pageSize);
	}

	/**
	 * 获取所有记录
	 * 
	 * @return
	 */
	public List getBeans()
	{
		return _beans;
	}
}