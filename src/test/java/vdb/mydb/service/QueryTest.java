package vdb.mydb.service;

import vdb.mydb.VdbManagerTest;
import vdb.mydb.bean.AnyBean;
import vdb.tool.da.DataAccessTool;
import cn.csdb.commons.jsp.Pageable;

public class QueryTest extends VdbManagerTest
{

	public void test() throws Exception
	{
		DataAccessTool tool = new DataAccessTool();
		String uri = "cn.csdb.vdb.bookstore.book";
		String queryString = "cn.csdb.vdb.bookstore.book.title = a";
		Pageable<AnyBean> beans = tool.query(uri, queryString);
		System.err.println(beans.size());
		assert (true);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
}
