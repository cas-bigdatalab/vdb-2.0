package vdb.mydb.jsp.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.log.dao.DataAccessLogDao;
import vdb.log.vo.DataAccessLog;
import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.mydb.VdbManager;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.ui.PagerTool;
import cn.csdb.commons.jsp.BeanPageViewer;
import cn.csdb.commons.jsp.PageViewer;
import cn.csdb.commons.jsp.Pageable;

public class DoVisitLog extends ServletActionProxy
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		try
		{
			DataAccessLogDao dao = new DataAccessLogDao();
			String datasetId = request.getParameter("datasetId");
			String username = request.getParameter("username");
			DataSet dataset = VdbManager.getInstance().getCatalog().fromId(
					datasetId);
			Pageable pageable = dao.getUserAccessLogsInDataset(dataset,
					username);
			int size = pageable.size();

			if (size > 0)
			{
				PagerTool tool = new PagerTool();
				PageViewer pageViewer = tool.create(pageable, 1, size);

				// 登陆者名称
				String name;
				// 登陆者时间
				String time;
				// 登陆者地址
				String ip;
				// 事件
				String event, str;

				String nameTimeIpEvent;
				String visitLogData = "<rows>";

				int i, rowId;
				for (i = 0; i < size; i++)
				{
					rowId = i + 1;
					visitLogData += "<row id='" + rowId + "'>";
					visitLogData += "<cell>" + rowId + "</cell>";
					name = ((DataAccessLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getUser();
					time = ((DataAccessLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getOpTime();
					ip = ((DataAccessLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getIp();
					event = ((DataAccessLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getOperation();
					String entity = "";
					if (((DataAccessLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getEntity() != null)
					{
						entity = ((DataAccessLog) ((BeanPageViewer) pageViewer)
								.getBeans().get(i)).getEntity();
						entity = ((Entity) VdbManager.getEngine().getCatalog()
								.fromId(entity)).getTitle();

					}

					String id = "";
					if (((DataAccessLog) ((BeanPageViewer) pageViewer)
							.getBeans().get(i)).getParam2() != null)
						id = ((DataAccessLog) ((BeanPageViewer) pageViewer)
								.getBeans().get(i)).getParam2();

					if ("update".equals(event))
					{
						str = " 修改了" + entity + "的记录(" + id + ")";
					}
					else if ("insert".equals(event))
					{
						str = " 添加了" + entity + "的记录(" + id + ")";
					}
					else if ("delete".equals(event))
					{
						str = " 删除了" + entity + "的记录(" + id + ")";
					}
					else if ("showEntity".equals(event))
					{
						str = " 查看了该数据集的表";
					}
					else if ("showBean".equals(event))
					{
						str = " 查看了该数据集的记录";
					}
					else
					{
						str = " 查询了该数据集的记录";
					}
					nameTimeIpEvent = name + " 在 " + time + " 于 " + ip + str;
					visitLogData += "<cell>" + nameTimeIpEvent
							+ "</cell></row>";
					str = null;
				}
				visitLogData += "</rows>";
				request.setAttribute("visitLogData", visitLogData);
			}
			else
			{
				request.setAttribute("visitLogData", "<rows></rows>");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
