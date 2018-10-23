<%@ page language="java"
	import="vdb.metacat.Entity,vdb.metacat.Field,java.util.Calendar,java.net.URLDecoder,java.util.List,vdb.mydb.VdbManager,vdb.mydb.bean.AnyBean,vdb.mydb.bean.AnyBeanDao,vdb.mydb.typelib.VdbData,vdb.mydb.query.AnyQuery"%>
<%@ page contentType="application/msword; charset=UTF-8"%>
<html>
	<head>
		<title></title>
		<style type="text/css">
body,td,tr {
	margin: 0px;
	font-size: 14px;
	color: #4b4b4b;
	background-color: #ffffff;
	font-family: "宋体";
}

table {
	border-collapse: collapse;
	width: 600px;
}

table td {
	border: 1px solid #666666;
	font-family: "宋体";
	font-size: 14px;
}

.td1 {
	width: 160px
}

.td2 {
	width: 440px
}
</style>
	</head>
	<body>
		<%
			response.setCharacterEncoding("utf-8");
			String exportItem = "";
			String tid = request.getParameter("tid");
			exportItem = new String(request.getParameter("exportItem")
					.getBytes("iso-8859-1"), ("gb2312"));
			exportItem = URLDecoder.decode(exportItem, "utf-8");
			String exportType = request.getParameter("exportType");
			String[] exportItems = exportItem.split(";");
			;
			Entity thisTable = (Entity) VdbManager.getInstance().getCatalog()
					.fromId(tid);
			String titles = thisTable.getTitle();
			String id = request.getParameter("id");
			String[] ids = null;
			if (null != id)
			{
				ids = id.split(";");
			}
		%>

		<%
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int date = cal.get(Calendar.DATE);
			String dateString = "";
			if (month < 10)
			{
				String months = "0" + String.valueOf(month);
				if (date < 10)
				{
					String dates = "0" + String.valueOf(date);
					dateString = "" + year + months + dates;
				}
				else
				{
					dateString = "" + year + months + date;
				}
			}
			else
			{
				dateString = "" + year + month + date;
			}
			response.setHeader("Content-Disposition", "attachment;filename="
					+ new String(titles.getBytes("gb2312"), "iso8859-1")
					+ dateString + ".doc");
		%>

		<div align="center" width="800px">
			<b><%=titles%> </b>
		</div>

		<%
			if (exportType.equals("4"))
			{
				AnyBean anyBean = new AnyBeanDao(thisTable).lookup(ids[0]);
				// 循环添加值
				for (int i = 0; i < ids.length; i++)
				{
		%>
		<div align="center">
			<table>
				<%
					anyBean = new AnyBeanDao(thisTable).lookup(ids[i]);
							for (int k = 0; k < exportItems.length; k++)
							{
				%>
				<tr>
					<%
						for (Field field : thisTable.getFields())
									{
										if (field.getTitle().equals(exportItems[k]))
										{
											try
											{
												VdbData data = anyBean.get(field);
					%>
					<td class="td1">
						<%=field.getTitle()%>
					</td>
					<td class="td2">
						<%
							if (null != data)
													{
						%>
						&nbsp;<%=data.getValue()%>&nbsp;
						<%
							}
													else
													{
						%>
						&nbsp;&nbsp;
					</td>
					<%
						}
											}
											catch (Exception e)
											{
											}
										}
									}
					%>
				</tr>
				<%
					}
				%>
			</table>
		</div>
		<br>
		<br>
		<%
			}
			}
			if (exportType.equals("5"))
			{
				AnyBeanDao dao = new AnyBeanDao(thisTable);
				AnyQuery query = dao.createQuery();
				List drs = dao.execute(query).list();

				// 循环添加值
				for (int i = 0; i < drs.size(); i++)
				{
		%>
		<div align="center">
			<table>
				<%
					for (int k = 0; k < exportItems.length; k++)
							{
				%>
				<tr>
					<%
						for (Field field : thisTable.getFields())
									{
										if (field.getTitle().equals(exportItems[k]))
										{
											try
											{
												String data = ((AnyBean) drs.get(i))
														.get(field) == null ? " "
														: ((AnyBean) drs.get(i)).get(field)
																.getValue().toString();
					%>

					<td class="td1">
						<%=field.getTitle()%>
					</td>
					<td class="td2">
						<%
							if (null != data)
													{
						%>
						<%=data%>
						<%
							}
													else
													{
						%>
						&nbsp;&nbsp;
					</td>
					<%
						}
											}
											catch (Exception e)
											{
											}
										}
									}
					%>
				</tr>
				<%
					}
				%>
			</table>
		</div>
		<br>
		<br>
		<%
			}
			}
		%>
	</body>
</html>
