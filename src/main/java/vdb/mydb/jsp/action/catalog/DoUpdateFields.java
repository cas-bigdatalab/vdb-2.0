package vdb.mydb.jsp.action.catalog;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.vtl.action.ServletActionProxy;

public class DoUpdateFields extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{

		// 验证URI是否重复，只验证新描述的字段
		Enumeration<?> e = request.getParameterNames();

		// 参数中新增的字段列表[列表仅存储字段的名字]，名字以N_开始
		ArrayList<String> paramList = new ArrayList<String>();

		while (e.hasMoreElements())
		{
			String paraStr = e.nextElement().toString();
			if (paraStr.startsWith("N_name"))
			{
				paramList.add(paraStr);
			}
		}

		String tid = request.getParameter("tid");
		Entity entity = VdbManager.getEngine().getCatalog().fromId(tid);

		boolean flag = true;

		ArrayList<String> valueList = new ArrayList<String>();
		for (String param : paramList)
		{
			String value = request.getParameter(param);
			if (!valueList.contains(value))
				valueList.add(value);
		}

		if (valueList.size() == paramList.size())
		{
			for (String value : valueList)
			{
				for (Field field : entity.getFields())
				{
					if (value.equalsIgnoreCase(field.getName()))
					{
						flag = false;
						break;
					}
				}
			}
		}
		else
		{
			flag = false;
		}

		if (!flag)
		{
			response
					.getWriter()
					.println(
							"<script>window.alert('您输入的字段URI重复或者与已描述字段的URI重复！');history.go(-1);</script>");
			response.getWriter().flush();
			return;
		}

		VdbEntity entityEx = ((VdbEntity) entity);
		// 批量更新提交
		entityEx.attachBatchUpdate(request);
	}
}
