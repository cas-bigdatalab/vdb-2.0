package vdb.mydb.jsp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.mydb.VdbManager;
import vdb.mydb.security.VdbGroup;
import vdb.mydb.security.VdbSecurityManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;

public class DoGetSelectUsers extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		String id = request.getParameter("id");
		Map<String, String> users = new HashMap<String, String>();
		VdbSecurityManager vsm = VdbManager.getInstance().getSecurityManager();

		try
		{

			if (id != null && !id.equalsIgnoreCase("all"))
			{
				VdbGroup group = vsm.getGroupByCode(id);
				if (group != null)
				{
					String gid = group.get("GROUPID").toString();
					List<String> list = vsm.getUserNamesOfGroup(gid);
					for (String l : list)
					{
						users.put(l, gid);
					}
				}
			}

			if (id != null && id.equalsIgnoreCase("all"))
			{
				List<String> list = vsm.getAllUserNames();
				for (String l : list)
				{
					List<VdbGroup> groups = vsm.getGroupsOfUser(l);

					if (groups != null && groups.size() > 0)
					{
						users.put(l, groups.get(0).get("GROUPID").toString());
					}
					else
					{
						users.put(l, "");
					}
				}
			}

			String usersData = "<rows>";
			int rowId;

			for (int i = 0; i < users.size(); i++)
			{
				rowId = i + 1;
				usersData += "<row id='" + users.keySet().toArray()[i] + "'>";
				usersData += "<cell>" + rowId + "</cell>";
				usersData += "<cell>" + users.keySet().toArray()[i] + "</cell>";
				if (null != users.get(users.keySet().toArray()[i])
						&& !"".equalsIgnoreCase(users.get(users.keySet()
								.toArray()[i])))
				{
					usersData += "<cell>"
							+ new AuthTool().getGroup(
									users.get(users.keySet().toArray()[i]))
									.get("GROUPNAME").toString() + "</cell>";
				}
				usersData += "</row>";
			}
			usersData += "</rows>";
			request.setAttribute("usersData", usersData);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
