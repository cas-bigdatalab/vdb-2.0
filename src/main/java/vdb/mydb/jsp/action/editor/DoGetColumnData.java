package vdb.mydb.jsp.action.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.jdbc.JdbcSourceManager;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.impl.JsoQuery;
import vdb.mydb.typelib.type.VdbEnumType;
import vdb.mydb.vtl.action.ServletActionProxy;
import cn.csdb.commons.sql.JdbcSource;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

public class DoGetColumnData extends ServletActionProxy {
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) {
		try {
			String entityStr = request.getParameter("entity") == null ? request
					.getAttribute("entity").toString() : request
					.getParameter("entity");
			String fieldStr = request.getParameter("field") == null ? request
					.getAttribute("field").toString() : request
					.getParameter("field");
			String whereFilter = request.getParameter("whereFilter") == null ? request
					.getAttribute("whereFilter").toString()
					: request.getParameter("whereFilter");
					
			entityStr = java.net.URLDecoder.decode(entityStr, "UTF-8");
			fieldStr = java.net.URLDecoder.decode(fieldStr, "UTF-8");
					
			vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();

			JdbcExpr je = null;

			if (whereFilter != null && whereFilter.trim().length() > 0) {
				JsoQuery jsoQuery = tool.parseJsoQuery(whereFilter);
				AnyQuery query = tool.createQuery(jsoQuery);
				tool.mergeQuery(query, jsoQuery);
				je = query.where();
			}

			Boolean isSector = null;

			String colors[] = { "0x0D8ECF", "0x04D215", "0xB0DE09", "0xF8FF01",
					"0xFF9E01", "0xFF6600", "0xFF1F11", "0x814EE6", "0xF234B0",
					"0x54F034" };
			HttpServletRequest servletRequest = (HttpServletRequest) request;
			HttpSession session = servletRequest.getSession();

			if (session.getAttribute(entityStr + fieldStr + "isSector") != null) {
				isSector = session.getAttribute(
						entityStr + fieldStr + "isSector").toString()
						.equalsIgnoreCase("true");
			}

			String sections = "";

			if (session.getAttribute(entityStr + fieldStr + "sections") != null) {
				sections = session.getAttribute(
						entityStr + fieldStr + "sections").toString();
			}

			String[] sectionList = sections.split(";");

			int count = 0;

			Entity entity = (Entity) VdbManager.getInstance().getCatalog()
					.fromUri(entityStr);
			Field field = tool.getField(fieldStr);
			Boolean isNum = field.getType().getName().equalsIgnoreCase("Long")
					|| field.getType().getName().equalsIgnoreCase("double");
			Boolean isDate = field.getType().getName().equalsIgnoreCase("Date");

			JdbcSource jds = JdbcSourceManager.getInstance().getJdbcSource(
					entity.getDataSet());
			String sqlCount = "select count(*) as c from "
					+ entity.getTableName();
			if (null != je) {
				sqlCount = sqlCount + " where " + je.toStringSql().toString();
			}
			StringSql qsql = new StringSql(sqlCount);
			if (null != je) {
				qsql.addParameters(je.toStringSql().getParameters());
			}

			List listCount = jds.queryForObjects(qsql);
			count += Integer.parseInt(((HashMap) listCount.get(0)).get("c")
					.toString());
			String fieldName = "";
			fieldName = field.getColumnName();

			String pieData = null;

			if ((isNum || isDate) && isSector != null && isSector) {
				pieData = "<chart>";
				pieData += "<series>";
				String sqlStr = "";
				String series = "";
				String graphs = "";
				// String sqlStr = "select "+fieldName+", count(*) as c from
				// "+entity.getTableName()+" group by "+fieldName;
				for (int i = 0; i < sectionList.length; i++) {
					if (sectionList[i] != "") {
						series += "<value xid='" + (i + 1) + "'>"
								+ sectionList[i] + "</value>";
						String[] sectionse = sectionList[i].split(" ------ ");
						if (isNum) {
							sqlStr = "select count(*) as c from "
									+ entity.getTableName() + " where "
									+ fieldName + " between " + sectionse[0]
									+ " and " + sectionse[1];
							if (null != je) {
								sqlStr = sqlStr + " and "
										+ je.toStringSql().toString();
							}
						} else {
							sqlStr = "select count(*) as c from "
									+ entity.getTableName() + " where "
									+ fieldName + " between '" + sectionse[0]
									+ "' and '" + sectionse[1] + "'";
							if (null != je) {
								sqlStr = sqlStr + " and "
										+ je.toStringSql().toString();
							}
						}
						StringSql sql = new StringSql(sqlStr);
						if (null != je) {
							sql.addParameters(je.toStringSql().getParameters());
						}
						List list = jds.queryForObjects(sql);
						graphs += "<value xid='" + (i + 1) + "' color='"
								+ colors[i % 9] + "'>"
								+ ((HashMap) list.get(0)).get("c") + "</value>";
					}
				}

				pieData += series;
				pieData += "</series>";
				pieData += "<graphs><graph gid='1'>";
				pieData += graphs;
				pieData += "</graph></graphs>";
				pieData += "</chart>";

			} else {
				String sqlStr = "select " + fieldName + ", count(*) as c from "
						+ entity.getTableName();
				if (null != je) {
					sqlStr = sqlStr + " where " + je.toStringSql().toString();
				}
				sqlStr = sqlStr + " group by " + fieldName;

				if (field.isReference()) {
					String tableA = entity.getTableName();
					String tableB = field.getRelationKey().getTarget()
							.getTableName();
					String fieldA = field.getColumnName();
					String fieldB = field.getRelationKey().getTarget()
							.getTitleField().getColumnName();
					String fieldC = field.getRelationKey().getTarget()
							.getIdentifier().getField().getColumnName();
					sqlStr = "select b." + fieldB + ", count(*) as c from "
							+ tableA + " a," + tableB + " b where a." + fieldA
							+ "=b." + fieldC;

					if (null != je) {
						sqlStr = sqlStr + " and " + je.toStringSql().toString();
					}
					sqlStr = sqlStr + " group by " + fieldB;
					fieldName = fieldB;
				}
				StringSql sql = new StringSql(sqlStr);
				if (null != je) {
					sql.addParameters(je.toStringSql().getParameters());
				}
				List list = jds.queryForObjects(sql);
				pieData = "<chart>";

				pieData += "<series>";
				for (int i = 0; i < list.size(); i++) {
					Map map = (HashMap) list.get(i);
					if (field.getType().getName().equalsIgnoreCase("enum")) {
						Map options = ((VdbEnumType) field.getTypeDriver())
								.getOptions();
						pieData += "<value xid='" + (i + 1) + "' color='"
								+ colors[i % 9] + "'>"
								+ options.get(map.get(fieldName)) + "</value>";
					} else {
						pieData += "<value xid='" + (i + 1) + "' color='"
								+ colors[i % 9] + "'>" + map.get(fieldName)
								+ "</value>";
					}
				}
				pieData += "</series>";

				pieData += "<graphs><graph gid='1'>";
				for (int i = 0; i < list.size(); i++) {
					Map map = (HashMap) list.get(i);
					pieData += "<value xid='" + (i + 1) + "' color='"
							+ colors[i % 9] + "'>" + map.get("c") + "</value>";
				}

				pieData += "</graph></graphs>";

				pieData += "</chart>";
			}
			String txt = "";

			if (isNum) {
				String sqlStrSta = "select sum(" + fieldName + ") as a, avg("
						+ fieldName + ") as b, max(" + fieldName
						+ ") as c, min(" + fieldName + ") as d from "
						+ entity.getTableName();

				if (null != je) {
					sqlStrSta = sqlStrSta + " where "
							+ je.toStringSql().toString();
				}

				StringSql sqlSta = new StringSql(sqlStrSta);

				if (null != je) {
					sqlSta.addParameters(je.toStringSql().getParameters());
				}

				List listSta = jds.queryForObjects(sqlSta);

				if (listSta.size() > 0) {
					Map map = (HashMap) listSta.get(0);
					Iterator it = map.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if (txt.equals(""))
							txt += entry.getValue();
						else
							txt += "," + entry.getValue();
					}
				}
			}

			String[] txts = txt.split(",");

			request.setAttribute("isSector", isSector);
			request.setAttribute("pieData", pieData);
			request.setAttribute("count", count);
			request.setAttribute("isDate", isDate);
			request.setAttribute("isNum", isNum);
			request.setAttribute("txts", txts);
			request.setAttribute("sectionList", sectionList);
			request.setAttribute("entityStr", entityStr);
			request.setAttribute("fieldStr", fieldStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
