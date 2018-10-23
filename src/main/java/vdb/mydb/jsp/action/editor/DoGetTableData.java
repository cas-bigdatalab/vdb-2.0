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

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.fs.page.EditField;
import vdb.metacat.fs.page.ListEditItemsPage;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.impl.AnyQueryImpl;
import vdb.mydb.query.impl.JsoQuery;
import vdb.mydb.typelib.data.VdbDate;
import vdb.mydb.typelib.data.VdbDouble;
import vdb.mydb.typelib.data.VdbFiles;
import vdb.mydb.typelib.data.VdbRef;
import vdb.mydb.typelib.type.VdbEnumType;
import vdb.mydb.util.XmlUtil;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;
import cn.csdb.commons.jsp.BeanPageViewer;

public class DoGetTableData extends ServletActionProxy
{

	String typeFormat(VdbField field)
	{
		String str = field.getTypeName();

		String type = "txt";
		String sort = "str";
		String width = "100";
		String validator = "";

		String title = field.getTitle();
		if (field.get("nullable") != null
				&& field.get("nullable").equals("false"))
		{
			title += "&lt;font color='red'&gt;*&lt;/font&gt;";
			validator = "NotEmpty";
		}

		String option = "";
		if (str.equals("Collection"))
		{
			return "";
		}
		else if (str.equals("ChemStructure"))
		{
			type = "ro";
			sort = "str";
			width = "80";
			// title = title + formatTitle("");
		}
		else if (str.equals("Date"))
		{
			type = "dhxCalendar";
			sort = "date";
			width = "80";
			if ((field.getEntity().getTimeStampField() != null && field
					.getEntity().getTimeStampField().getTitle()
					.equalsIgnoreCase(field.getTitle())))
			{
				type = "ro";
			}
			String editorStyle = field.meta().get("editorStyle");
			if (editorStyle != null
					&& editorStyle.trim().equals("DateTimeCtrl"))
				type = "ro";
		}
		else if (str.equals("Double"))
		{
			type = "edn";
			sort = "int";
			width = "80";
			// title = title + formatTitle("");
		}
		else if (str.equals("Enum"))
		{
			type = "coro";
			sort = "str";
			width = "50";// return "co";
			// title = title + formatTitle("");
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
		else if (str.equals("Expression"))
		{
			type = "ro";
			sort = "str";
			width = "80";
		}
		else if (str.equals("Files"))
		{
			type = "ro";
			sort = "str";
			width = "250";
			title = title + formatTitle("/console/shared/images/file.gif");// "&lt;img
		}
		else if (str.equals("GpsLocation"))
		{
			type = "ro";
			sort = "str";
			width = "50";
			// title = title + formatTitle("");
		}
		else if (str.equals("HyperLink"))
		{
			// type = "link";
			sort = "str";
			width = "250";
			title = title + formatTitle("/console/shared/images/hyperlink.gif");// "&lt;img
		}
		else if (str.equals("Long"))
		{
			type = "edn";// type = "calck";
			sort = "int";
			width = "50";
			// title = field.getTitle() + formatTitle("");
		}
		else if (str.equals("Reference"))
		{
			type = "ro";
			width = "150";
			title = title + formatTitle("/console/shared/images/reference.gif");// "&lt;img
		}
		else if (str.equals("RichText"))
		{
			type = "ro";
			width = "200";
			title = title + formatTitle("/console/shared/images/html.gif");// "&lt;img
		}
		else if (str.equals("String"))
		{
			String editorStyle = field.meta().get("editorStyle");
			if (editorStyle != null && editorStyle.trim().equals("StringEdit"))
				type = "ed";

			if (editorStyle != null && editorStyle.trim().equals("PassEdit"))
			{
				type = "ro";
			}

			if ((field.getEntity().getEditorField() != null && field
					.getEntity().getEditorField().getTitle().equalsIgnoreCase(
							field.getTitle()))
					|| (field.getEntity().getGroupField() != null && field
							.getEntity().getGroupField().getTitle()
							.equalsIgnoreCase(field.getTitle())))
			{
				type = "ro";
			}
			// title = field.getTitle() + formatTitle("");
		}
		else
		{
			type = "ro";
			width = "50";
			// title = field.getTitle();
		}

		try
		{
			if (field.isIdentifier())
			{
				// if (((VdbEntity) field.getEntity()).isAutoPrimaryKey())
				type = "ro";
				title = field.getTitle()
						+ formatTitle("/console/shared/images/key.gif");
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		return "<column id='" + field.getUri() + "#" + field.getTypeName()
				+ "#" + field.isIdentifier() + "' sort='na' validator='"
				+ validator + "' align='left' width='" + width + "' type='"
				+ type + "'>" + title + option + "</column>";

	}

	String formatTitle(String image)
	{
		return "&lt;span style=\"width: 16px; line-height: 16px; height:16px; background-repeat:no-repeat; background-image:url('"
				+ image
				+ "');\"&gt;&amp;nbsp;&amp;nbsp;&amp;nbsp;&amp;nbsp;&lt;/span&gt;";
	}

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext)
	{
		AuthTool auth = new AuthTool();
		String user = auth.getUserName();
		String entityStr = request.getParameter("entity");
		vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
		Entity entity = (Entity) VdbManager.getInstance().getCatalog().fromUri(
				entityStr);
		ListEditItemsPage page = (ListEditItemsPage) auth
				.getGrantedPageByTypeAndRes(user, "listEditItems", entity
						.getId());
		String start = request.getParameter("start");
		String size = request.getParameter("size");
		String whereFilter = null;
		String html = "";
		int recordNum = 0;
		int pageNum = 0;

		String attachTable = "no";

		try
		{
			if (request.getParameter("whereFilter") != null)
				whereFilter = java.net.URLDecoder.decode(request
						.getParameter("whereFilter"), "UTF-8");

			AnyQuery query = null;
			BeanPageViewer pageList = null;

			if (whereFilter == null || whereFilter.trim().length() <= 0)
			{
				query = new AnyQueryImpl(entity);
				if (entity.getOrderField() != null && entity.getOrder() != null)
				{
					query.orderBy(entity.getOrderField(), entity.getOrder());
				}
			}
			else
			{
				JsoQuery jsoquery = tool.parseJsoQuery(whereFilter);
				query = tool.createQuery(jsoquery);
				tool.mergeQuery(query, jsoquery);
			}

			List<JdbcExpr> jel = new ArrayList();

			String filter = page.getGrantFilter();
			filter = auth.mergeFilter(filter, entity.getId());
			AnyQueryImpl queryImpl = new AnyQueryImpl(entity);
			if (filter != null && filter.trim().length() > 0)
				jel.add(queryImpl.in(
						entity.getIdentifier().getField().getUri(), "select "
								+ entity.getIdentifier().getField()
										.getColumnName() + " from "
								+ entity.getTableName() + " where " + filter));

			AnyQueryImpl queryTemp = new AnyQueryImpl(entity);
			for (int i = 0; i < jel.size(); i++)
			{
				queryTemp.where(queryTemp.or(queryTemp.where(), jel.get(i)));
			}

			query.where(query.and(query.where(), queryTemp.where()));
			List<Field> lf = new ArrayList<Field>();

			if (page.getOrderFields() != null
					&& page.getOrderFields().size() > 0)
			{
				for (int i = 0; i < page.getOrderFields().size(); i++)
					lf.add(page.getOrderFields().get(i).getField());
				query.orderBy(lf, page.getOrderFields().get(0).getOrder());
			}

			pageList = (BeanPageViewer) tool.createPageViewer(query, Integer
					.parseInt(start), Integer.parseInt(size));

			XmlUtil xmlutil = new XmlUtil((HttpServletRequest) request,
					(HttpServletResponse) response);
			java.util.List list = pageList.getBeans();

			List<EditField> ef = page.getEditFields();

			String strNum = "<column id='-1' align='center' sort='na' type='cntr' width='35' color='ededed'>"
					+ " " + "</column>";
			xmlutil.setXmlHead(strNum);

			for (int i = 0; i < ef.size(); i++)
			{
				VdbField f = (VdbField) ef.get(i).getField();
				String str = typeFormat(f);
				xmlutil.setXmlHead(str);
			}

			for (int i = 0; i < list.size(); i++)
			{
				vdb.mydb.bean.AnyBeanImpl bean = (vdb.mydb.bean.AnyBeanImpl) list
						.get(i);
				xmlutil.setId(entityStr + "#"
						+ String.valueOf(bean.getId().getValue()));
				xmlutil.setXmlData(String.valueOf(i + 1));
				for (int j = 0; j < ef.size(); j++)
				{
					Field field = (Field) ef.get(j).getField();

					String str = "";
					String colType = field.getTypeName();
					if (bean.get(field.getName()) != null)
					{
						if (colType.equals("ChemStructure"))
						{
							str = bean.get(field.getName()).getValue();
						}
						else if (colType.equals("Collection"))
						{
							String refType = "";
							if (field.getRelationKey().isManyToMany())
								refType = "M2M";
							else
								refType = "O2M";

							if (i == 0)
								attachTable += "@;@" + field.getTitle()
										+ "@child#@" + field.getUri()
										+ "@child#@" + refType;
							continue;
						}
						else if (colType.equals("Date"))
						{
							VdbDate data = (VdbDate) bean.get(field.getName());
							// str = sd.format(new Date());
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
						else if (colType.equals("Double"))
						{
							str = ((VdbDouble) bean.get(field.getName()))
									.format();
						}
						else if (colType.equals("Enum"))
						{
							str = bean.get(field.getName()).getValue();
							if (str.equalsIgnoreCase("$data.value"))
							{
								str = "";
							}
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
						else if (colType.equals("GpsLocation"))
						{
							str = bean.get(field.getName()).getTitle();
						}
						else if (colType.equals("HyperLink"))
						{
							str = bean.get(field.getName()).getValue();
						}
						else if (colType.equals("Long"))
							str = bean.get(field.getName()).getValue();
						else if (colType.equals("Reference"))
						{
							VdbRef ref = (VdbRef) bean.get(field.getName());
							if (ref.getBeanRef().getBean() != null)
								str = ref.getBeanRef().getBean().getTitle()
										.getTitle();
							if (i == 0)
								attachTable += "@;@" + field.getTitle()
										+ "@child#@" + field.getUri()
										+ "@child#@Ref";
						}
						else if (colType.equals("RichText"))
						{
							str = bean.get(field.getName()).getValue();
						}
						else if (colType.equals("String"))
						{
							str = bean.get(field.getName()).getValue();
							String editorStyle = field.meta()
									.get("editorStyle");
							if (editorStyle != null
									&& editorStyle.trim().equals("PassEdit"))
							{
								if (str != null && str.trim().length() > 0)
									str = str.format("********", null);
							}
						}
						else if (colType.equals("Tree"))
						{
							str = bean.get(field.getName()).getTitle();
						}
						else if (colType.equals("LocalFiles"))
						{
							str = bean.get(field.getName()).getTitle();
						}
						else
						{
							str = bean.get(field.getName()).getTitle();
						}

					}
					str = str.replaceAll("&", "&amp;");
					str = str.replaceAll(">", "&amp;gt;");
					str = str.replaceAll("<", "&amp;lt;");
					str = str.replaceAll("'", "&amp;apos;");
					str = str.replaceAll("\"", "&amp;quot;");
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
				xmlutil.setXmlData("Sorry!Unexcepted exception!请检查数据库连接！");
				html = xmlutil.getXmlFile();
			}
		}
		finally
		{
			html += "@CNIC@" + recordNum + "@CNIC@" + pageNum + "@CNIC@"
					+ attachTable;
			request.setAttribute("data", html);
		}

	}
}
