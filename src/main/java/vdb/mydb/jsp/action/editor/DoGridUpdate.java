package vdb.mydb.jsp.action.editor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.fs.page.EditField;
import vdb.metacat.fs.page.ListEditItemsPage;
import vdb.metacat.idgen.IdGeneratorContext;
import vdb.mydb.VdbManager;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.metacat.VdbField;
import vdb.mydb.security.VdbGroup;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SimpleSdef;
import vdb.mydb.vtl.action.ServletActionProxy;
import vdb.tool.auth.AuthTool;
import vdb.tool.log.AccessLoggerTool;

public class DoGridUpdate extends ServletActionProxy
{

	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		request.setCharacterEncoding("utf-8");// 处理乱码
		String entity = ""; // request.getParameter("entity");
		String ids = request.getParameter("ids");
		String attach = request.getParameter("attach");

		String[] idsa = ids.split(",");

		if (idsa.length > 0)
			entity = idsa[0].split("#")[0];

		vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
		Entity ent = null;

		if (attach != null && attach.trim().equals("true"))
			ent = ((Field) tool.getField(entity)).getRelationKey().getTarget();
		else
			ent = tool.getEntity(entity);

		StringBuffer sf = new StringBuffer();
		sf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?> ");
		sf.append("<data>");
		final Identifier primaryKey = ent.getIdentifier();
		vdb.mydb.bean.AnyBeanDao dao = new vdb.mydb.bean.AnyBeanDao(ent);
		vdb.mydb.bean.AnyBean bean = null;
		try
		{
			AccessLoggerTool loggerTool = new AccessLoggerTool();
			AuthTool auth = new AuthTool();
			String user = auth.getUserName();
			String ip = request.getRemoteAddr();
			ListEditItemsPage page = (ListEditItemsPage) auth
					.getGrantedPageByTypeAndRes(user, "listEditItems", ent
							.getId());
			List<EditField> col = page.getEditFields();
			for (String id : idsa)
			{
				String operType = request.getParameter(id
						+ "_!nativeeditor_status");
				String beanid = id.split("#")[1];// request.getParameter(id+"_c0");

				try
				{
					if (operType.equals("deleted"))
					{
						bean = dao.lookup(beanid);
						dao.delete(bean);
						
						//记入操作日志
						loggerTool.logAccess(user, ip, bean.getEntity(), "delete", null, bean.getId().getValue(), false);
						
						sf.append("<action type=\"delete\" sid=\"" + id
								+ "\" tid=\"" + id + "\" /> ");
					}
					else
					{
						if (operType.equals("inserted"))
							bean = dao.create();
						else
							bean = dao.lookup(beanid);

						int dataid = 0;

						for (int i = 0; i < col.size(); i++)
						{
							// VdbField field = (VdbField) col.get(i);
							VdbField field = (VdbField) col.get(i).getField();
							String colType = field.getTypeName();

							if (colType.equals("RichText"))
								continue;
							if (colType.equals("Collection"))
							{
								continue;
							}
							if (colType.equals("Expression"))
								continue;
							if (colType.equals("Files"))
							{
								continue;
							}
							if (colType.equals("Date"))
							{
								// continue;
							}
							if (colType.equals("String")
									&& field.meta().get("editorStyle") != null
									&& field.meta().get("editorStyle").trim()
											.equals("PassEdit"))
								continue;
							if (colType.equals("Reference"))
								continue;
							if (colType.equals("GpsLocation"))
								continue;

							if (field.getEntity().getTimeStampField() != null
									&& field.getEntity().getTimeStampField()
											.getTitle().equalsIgnoreCase(
													field.getTitle()))
							{
								continue;
							}

							if (field.isIdentifier()
									&& ((VdbEntity) field.getEntity())
											.isAutoPrimaryKey())
							{
								continue;
							}

							// try
							// {
							VdbData data = bean.get(field);
							Object jso = (request.getParameter(id + "_c"
									+ (i + 1)));

							if (jso != null)// &&
							// jso.toString().trim().length()>0)
							{
								Sdef ddl = new SimpleSdef();
								String value = jso.toString();
								value = value
										.replaceAll("&nbsp;", "&amp;nbsp;");
								value = value.replaceAll("<", "&lt;");
								value = value.replaceAll(">", "&gt;");
								String xml = "<sdef><title></title><value>"
										+ value + "</value></sdef>";
								if (colType.equals("Date") && value != null
										&& value.length() > 0)
								{
									DateFormat format = new SimpleDateFormat(
											"yyyy-MM-dd");
									Date date = format.parse(value);
									xml = "<sdef><title></title><value>"
											+ date.getTime()
											+ "</value></sdef>";// continue;
								}
								if (colType.equals("Files"))
								{
									//
									// continue;
									xml = "<sdef><files>";
									if (jso.toString().trim().length() > 0) // 此条记录没有文件列表
									{
										String[] files = value.split(",");
										for (String file : files)
										{
											xml += "<file><id>" + file
													+ "</id></file>";
										}
									}
									xml += "</files></sdef>";
								}

								ddl.setXml(xml);
								data.setAsSdef(ddl);
								if (field.get("nullable") != null
										&& field.get("nullable")
												.equals("false"))
								{
									if (data.getValue() == null
											|| data.getValue().trim()
													.equals("")
											|| data.getValue().trim()
													.toLowerCase().equals(
															"null"))
									{
										throw new RuntimeException(field
												.get("title")
												+ "不能为空");
									}
								}
							}

							dataid++;
						}

						if (operType.equals("inserted"))
						{
							if (((VdbEntity) ent).isAutoPrimaryKey())
							{
								bean.setId(primaryKey.getIdGenerator()
										.generateId(new IdGeneratorContext()
										{
											public Identifier getPrimaryKey()
											{
												return primaryKey;
											}
										}));
							}
							Field editorField = bean.getEntity()
									.getEditorField();
							if (editorField != null)
							{
								bean.get(editorField).setAsSdef(
										new SimpleSdef(VdbManager.getEngine()
												.getSecurityManager()
												.getUserName()));
							}

							Field groupField = bean.getEntity().getGroupField();
							if (groupField != null)
							{
								String userName = VdbManager.getEngine()
										.getSecurityManager().getUserName();
								List<VdbGroup> groups = VdbManager
										.getEngine().getSecurityManager()
										.getGroupsOfUser(userName);
								if (groups != null && groups.size() > 0)
								{
									bean.get(groupField).setAsSdef(
											new SimpleSdef(groups.get(0).get(
													"GROUPCODE").toString()));
								}
							}
							dao.insert(bean);
							/**
							 * *****************BEGIN
							 * LOGGING********************
							 */
							
							loggerTool.logAccess(user, ip, bean.getEntity(), "insert", null, bean.getId().getValue(), false);
							
							/**
							 * ******************End
							 * LOGGING***********************
							 */
							sf.append("<action type=\"insert\" sid=\"" + id
									+ "\" tid=\"" + id + "\" /> ");
						}
						else
						{

							dao.update(bean);
							
							loggerTool.logAccess(user, ip, bean.getEntity(), "update", null, bean.getId().toString(), false);
							sf.append("<action type=\"update\" sid=\"" + id
									+ "\" tid=\"" + id + "\" /> ");
						}
					}

				}
				catch (Exception e)
				{
					e.printStackTrace();
					dao.removeFromCache(bean);
					sf.append("<action type=\"error\" sid=\"" + id
							+ "\" tid=\"" + id + "\" > ");
					sf.append("保存数据错误！   详细错误信息如下：" + e.getMessage()
							+ "</action>");

				}

			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		sf.append("</data>");
		request.setAttribute("data", sf.toString());
	}

}