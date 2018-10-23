package vdb.mydb.jsp.action.editor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Catalog;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;
import cn.csdb.commons.util.StringKeyMap;

public class DoGetSqlTree extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String fid = request.getParameter("fid");
		String sql = request.getParameter("sql");
		Catalog catalog = VdbManager.getInstance().getCatalog();
		Entity entity = catalog.fromId(fid);
		AnyBeanDao dao = new AnyBeanDao(entity);
		JdbcSource js = dao.getJdbcSource();
		StringSql strsql = new StringSql(sql);
		Map<String, String> options = new StringKeyMap<String>();
		try
		{
			List<Map<String, Serializable>> list = js.queryForObjects(strsql);
			for (int i = 0; i < list.size(); i++)
			{
				Map<String, Serializable> map = list.get(i);
				Object[] o = map.values().toArray();
				if (o.length >= 2)
				{
					String temp = "";
					for (int j = 1; j < o.length; j++)
					{
						temp = temp
								+ (o[j].toString() == null ? "" : o[j]
										.toString()) + ",";
					}
					temp = temp.substring(0, temp.length() - 1);
					options.put(o[0].toString(), temp);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		Iterator it = options.entrySet().iterator();
		StringBuilder html = new StringBuilder();
		html.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		html.append("<tree id=\"0\" >\n");
		List<Map.Entry<String, String>> optionsList = new ArrayList<Map.Entry<String, String>>();
		while (it.hasNext())
		{
			Map.Entry<String, String> option = (Map.Entry<String, String>) it
					.next();
			optionsList.add(option);
		}
		for (int i = 0; i < optionsList.size(); i++)
		{
			html.append("<item text='" + optionsList.get(i).getValue()
					+ "' id='" + optionsList.get(i).getKey() + "'>\n");
			if (i < optionsList.size() - 1
					&& optionsList.get(i + 1).getValue().indexOf(
							optionsList.get(i).getKey()) < 0)
			{
				int length = (optionsList.get(i).getKey().length()) / 4;
				int length1 = (optionsList.get(i + 1).getKey().length()) / 4;
				int count = length - length1;
				for (int j = 0; j < count + 1; j++)
				{
					html.append("</item>\n");
				}
			}
			if (i == optionsList.size() - 1)
			{
				int lth = (optionsList.get(i).getKey().length()) / 4;
				int ct = lth;
				for (int j = 0; j < ct; j++)
				{
					html.append("</item>\n");
				}
			}
		}

		html.append("</tree>");
		request.setAttribute("tree", html.toString());
	}
}
