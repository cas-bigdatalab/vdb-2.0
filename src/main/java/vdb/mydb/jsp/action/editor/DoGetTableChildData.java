package vdb.mydb.jsp.action.editor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.metacat.Field;
import vdb.metacat.fs.page.EditField;
import vdb.metacat.fs.page.ListEditItemsPage;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.impl.AnyQueryImpl;
import vdb.mydb.query.impl.CollectionQueryCreator;
import vdb.mydb.typelib.data.VdbDate;
import vdb.mydb.typelib.data.VdbFiles;
import vdb.mydb.typelib.data.VdbRef;
import vdb.mydb.typelib.type.VdbEnumType;
import vdb.mydb.util.XmlUtil;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;
import cn.csdb.commons.jsp.BeanPageViewer;

public class DoGetTableChildData extends ServletActionProxy
{

	String typeFormat(int index, VdbField field)
	{
		String str = field.getTypeName();

		String type = "txt";
		String sort = "str";
		String width = "100";
		if (str.equals("String"))
		{
		}
		if (str.equals("Long"))
		{
			type = "ed";
			sort = "int";
			width = "50";
		}
		if (str.equals("Double"))
		{
			type = "ed";
			sort = "int";
			width = "50";
		}
		if (str.equals("LocalFiles"))
		{
			type = "ro";
			sort = "str";
			width = "250";
		} // return "txt";
		if (str.equals("RichText"))
		{

		}
		if (str.equals("Date"))
		{
			type = "dhxCalendar";
			sort = "date";
			width = "80";
			type = "ro";
		}
		String option = "";
		if (str.equals("Enum"))
		{
			type = "co";
			sort = "str";
			width = "50";// return "co";

			try
			{
				Map<String, String> map = ((VdbEnumType) field.getTypeDriver())
						.getOptions();
				for (Entry entry : map.entrySet())
				{
					option += "<option value='" + entry.getKey().toString()
							+ "'>" + entry.getValue().toString() + "</option>";
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (str.equals("Collection"))
		{
			return "";
		}
		if (str.equals("HyperLink"))
		{
			// type = "link";
			sort = "str";
			width = "250";
		}
		if (str.equals("Expression"))
		{
			type = "ro";
			sort = "str";
			width = "80";
		}
		if (str.equals("Files"))
		{
			type = "ro";
			sort = "str";
			width = "250";// return "co";return "img";
		}
		if (str.equals("HyperLink"))
		{
		}
		if (str.equals("Reference"))
		{

		}
		if (str.equals("GpsLocation"))
		{
		}

		return "<column id='" + index + "' sort='na' align='left' width='"
				+ width + "' type='" + type + "'>" + field.getTitle() + option
				+ "</column>";
	}

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{

		String fieldStr = request.getParameter("field");
		String start = request.getParameter("start");
		String size = request.getParameter("size");
		// String id = request.getParameter("id");
		String id = java.net.URLDecoder.decode(request.getParameter("id"),
				"UTF-8");
		String html = "";
		int pageNum = 0;
		int recordNum = 0;
		String attachTable = "no";

		try
		{
			vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();

			Field fieldParent = tool.getField(fieldStr);

			AuthTool auth = new AuthTool();
			String user = auth.getUserName();

			XmlUtil xmlutil = new XmlUtil((HttpServletRequest) request,
					(HttpServletResponse) response);
			// java.util.List list = pageList.getBeans();//
			// tool.getEntity(entity).getDao().execute(query).list();

			VdbEntity entity = (VdbEntity) fieldParent.getRelationKey()
					.getTarget();
			ListEditItemsPage page = (ListEditItemsPage) auth
					.getGrantedPageByTypeAndRes(user, "listEditItems", entity
							.getId());

			List<JdbcExpr> jel = new ArrayList();

			String filter = page.getGrantFilter();
			filter = auth.mergeFilter(filter, entity.getId());
			AnyQueryImpl queryImpl = new AnyQueryImpl(entity);
			if (filter != null && filter.trim().length() > 0)
				jel.add(queryImpl.in(
						entity.getIdentifier().getField().getUri(), "select "
								+entity.getTableName()+"."+ entity.getIdentifier().getField()
										.getColumnName() + " from "
								+ entity.getTableName() + " where " + filter));

			AnyQueryImpl queryTemp = new AnyQueryImpl(entity);
			for (int i = 0; i < jel.size(); i++)
			{
				queryTemp.where(queryTemp.or(queryTemp.where(), jel.get(i)));
			}

			//
			// vdb.mydb.bean.AnyBean bean1 =
			// tool.getBean("cn.csdb.vdb.vdbstu.teacher",pid);
			// query.where(query.eq("cn.csdb.vdb.vdbstu.course.teacher",pid));
			BeanPageViewer pageList = null;
			if (id == null || id.trim().length() <= 0)
			{
				vdb.mydb.query.AnyQuery query = new AnyQueryImpl(fieldParent
						.getRelationKey().getTarget());
				//query.where(query.and(query.where(), queryTemp.where()));
				pageList = (BeanPageViewer) tool.createPageViewer(query,
						Integer.parseInt(start), Integer.parseInt(size));
			}
			else
			{
				String pid = id.split("#")[1];
				CollectionQueryCreator s = new CollectionQueryCreator(
						fieldParent, pid);
				vdb.mydb.query.AnyQuery query = s.createQuery();
				//query.where(query.and(query.where(), queryTemp.where()));
				pageList = (BeanPageViewer) tool.createPageViewer(query,
						Integer.parseInt(start), Integer.parseInt(size));
			}
			//
			//
			java.util.List list = pageList.getBeans();

			// CatalogObject[] col = entity.getEx().getView("updateBean")
			// .getItems();
			List<EditField> col = page.getEditFields();
			String strNum = "<column id='-1' align='center' sort='na' type='cntr' width='28' color='cdcdcd'>"
					+ " " + "</column>";
			xmlutil.setXmlHead(strNum);

			String colStr = "";
			String typeStr = "";
			for (int i = 0; i < col.size(); i++)
			{
				VdbField f = (VdbField) col.get(i).getField();

				String str = typeFormat(i, f);
//				if (i == col.size() - 1 && str.length() > 0
//						&& str.indexOf("width='") != -1)
//				{
//					String tmp = str.substring(str.indexOf("width='"), str
//							.indexOf("'", str.indexOf("width='") + 7));
//					str = str.replace(tmp, "width='*");
//				}
				xmlutil.setXmlHead(str);
			}

			for (int i = 0; i < list.size(); i++)
			{
				vdb.mydb.bean.AnyBeanImpl bean = (vdb.mydb.bean.AnyBeanImpl) list
						.get(i);

				xmlutil.setId(String.valueOf(bean.getId().getValue()));
				xmlutil.setXmlData(String.valueOf(i + 1));
				for (int j = 0; j < col.size(); j++)
				{
					Field field = (Field) col.get(j).getField();

					String str = "";
					String colType = field.getTypeName();

					if (bean.get(field.getName()) != null)
					{
						if (colType.equals("String"))
						{
							str = bean.get(field.getName()).getValue();
							String editorStyle = field.meta()
									.get("editorStyle");
							if (editorStyle != null
									&& editorStyle.trim().equals("PassEdit"))
							{
								if (str != null && str.trim().length() > 0)
									//FIXME: remove null argument
									str = str.format("********", null);
							}
						}
						else if (colType.equals("Long"))
							str = bean.get(field.getName()).getValue();
						else if (colType.equals("Double"))
							str = bean.get(field.getName()).getValue();
						else if (colType.equals("LocalFiles"))
						{
						}
						else if (colType.equals("RichText"))
						{
							str = bean.get(field.getName()).getValue();
						}
						else if (colType.equals("Tree"))
						{
							str = bean.get(field.getName()).getTitle();
						}
						else if (colType.equals("Date"))
						{
							VdbDate data = (VdbDate) bean.get(field.getName());
							if (data.getDate() != null)
							{
								str = data.dateFormat("yyyy-MM-dd");
								String editorStyle = field.meta().get(
										"editorStyle");
								if (editorStyle != null
										&& editorStyle.trim().equals(
												"DateTimeCtrl"))
								{
									str = data
											.dateFormat("yyyy-MM-dd HH:mm:ss");// sd.format(data.getDate());
								}
							}
						}
						else if (colType.equals("Enum"))
						{
							str = bean.get(field.getName()).getTitle();
						}
						else if (colType.equals("Collection"))
						{
							continue;
						}
						else if (colType.equals("Expression"))
						{
						}
						else if (colType.equals("Files"))
						{
							VdbFiles file = (VdbFiles) bean
									.get(field.getName());
							for (int m = 0; m < file.getFiles().size(); m++)
								str = str + ";"
										+ file.getFiles().get(m).getTitle();
							if (str.length() > 0)
								str = str.substring(1, str.length());
						}
						else if (colType.equals("HyperLink"))
						{
							str = bean.get(field.getName()).getValue();
						}
						else if (colType.equals("Reference"))
						{
							VdbRef ref = (VdbRef) bean.get(field.getName());
							if (ref.getBeanRef().getBean() != null)
								str = ref.getBeanRef().getBean().getTitle()
										.getTitle();
						}
						else if (colType.equals("GpsLocation"))
						{
							str = bean.get(field.getName()).getTitle();
						}
						else
						{
							str = bean.get(field.getName()).getTitle();
						}
					}
					if (null == str)
						str = "";
					str = str.replaceAll("&", "&amp;amp;");
					str = str.replaceAll(">", "&amp;gt;");
					str = str.replaceAll("<", "&amp;lt;");
					// str = str.replaceAll("<","&amp;lt;");
					str = str.replaceAll("'", "&apos;");
					str = str.replaceAll("\"", "&quot;");
					if ("null".equals(str))
						str = "";
					xmlutil.setXmlData(str);
				}

			}
			recordNum = pageList.getRecordCount();
			pageNum = pageList.getPageCount();
			html = xmlutil.getXmlFile();
		}// try-end
		catch (Exception e)
		{
			e.printStackTrace();
			XmlUtil xmlutil = new XmlUtil((HttpServletRequest) request,
					(HttpServletResponse) response);

			if (e instanceof SQLException)
			{
				SQLException sqlException = (SQLException) e;
				String col_1 = "<column id='1' sort='txt' align='left' width='250' type='ro'>ErrorCode</column>";
				xmlutil.setXmlHead(col_1);
				String col_2 = "<column id='2' sort='txt' align='left' width='250' type='ro'>Message</column>";
				xmlutil.setXmlHead(col_2);
				String col_3 = "<column id='3' sort='txt' align='left' width='250' type='ro'>SQLState</column>";
				xmlutil.setXmlHead(col_3);

				xmlutil.setId("1");
				xmlutil.setXmlData(sqlException.getErrorCode());
				xmlutil.setXmlData(sqlException.getMessage());
				xmlutil.setXmlData(sqlException.getSQLState());

				html = xmlutil.getXmlFile();
			}
			else
			{
				String col_1 = "<column id='1' sort='txt' align='left' width='250' type='ro'>message</column>";
				xmlutil.setXmlHead(col_1);
				xmlutil.setId("1");
				xmlutil.setXmlData("Sorry!Unexcepted exception!请检查数据库连接!");
				html = xmlutil.getXmlFile();
			}
		}
		finally
		{
			html += "@CNIC@" + recordNum + "@CNIC@" + pageNum + "@CNIC@"
					+ attachTable;
			request.setAttribute("data", html);
			// response.setContentType("text/xml; charset=UTF-8");
			// response.setHeader("Cache-Control", "no-cache");
			// out.write(html);
			// out.flush();
		}
	}
}
