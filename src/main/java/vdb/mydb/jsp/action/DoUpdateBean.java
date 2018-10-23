/*
 * Created on 2006-8-28
 */
package vdb.mydb.jsp.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSONObject;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.fs.page.AddItemPage;
import vdb.metacat.fs.page.EditField;
import vdb.metacat.fs.page.Page;
import vdb.metacat.fs.page.UpdateItemPage;
import vdb.metacat.idgen.IdGeneratorContext;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.files.FileManager;
import vdb.mydb.files.FileMetaData;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.security.VdbGroup;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.data.VdbFiles;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SimpleSdef;
import vdb.mydb.vtl.toolbox.EscapeUnescape;
import vdb.tool.auth.AuthTool;
import vdb.tool.log.AccessLoggerTool;
import vdb.tool.pages.PagesManagerTool;
import cn.csdb.commons.action.JspAction;

public class DoUpdateBean implements JspAction
{
	public void doAction(ServletRequest request, ServletResponse response,
			ServletContext servletContext) throws Exception
	{
		// json
		JSONObject form = JSONObject.fromObject(EscapeUnescape.unescape(request
				.getParameter("form")));
		String tableId = form.getString("tableId");
		String beanId = null;
		String pageName = request.getParameter("page");

		try
		{
			beanId = form.getString("beanId");
		}
		catch (Exception e)
		{
		}

		Entity thisEntity = (Entity) VdbManager.getEngine().getCatalog()
				.fromId(tableId);
		Page page = new PagesManagerTool().getPageByName(pageName, thisEntity
				.getUri());

		boolean isNewRecord = (beanId == null || beanId.length() == 0);
		AnyBeanDao dao = new AnyBeanDao(thisEntity);
		AnyBean bean;

		List<EditField> editFields = new ArrayList<EditField>();

		if (isNewRecord)
		{
			bean = dao.create();
			editFields = ((AddItemPage) page).getEditFields();
		}
		else
		{
			editFields = ((UpdateItemPage) page).getEditFields();
			bean = dao.lookup(beanId);
		}

		JSONObject jsoBean = form.getJSONObject("bean");

		final Identifier primaryKey = thisEntity.getIdentifier();
		VdbEntity te = (VdbEntity) thisEntity;

		if (isNewRecord && te.isAutoPrimaryKey())
		{
			bean.setId(primaryKey.getIdGenerator().generateId(
					new IdGeneratorContext()
					{
						public Identifier getPrimaryKey()
						{
							return primaryKey;
						}
					}));
		}

		for (EditField ef : editFields)
		{
			Field field = ef.getField();

			if (field == thisEntity.getIdentifier().getField()
					&& te.isAutoPrimaryKey())
				continue;

			if (field == thisEntity.getEditorField())
				continue;

			if (field == thisEntity.getTimeStampField())
				continue;

			if (field == thisEntity.getGroupField())
				continue;

			if (field.getTypeName().equals("Collection"))
				continue;

			try
			{
				VdbData data = bean.get(field);
				Sdef ddl = new SimpleSdef();

				Object obj = jsoBean.get(field.getName());

				if ("Files".equals(field.getTypeName()))
				{
					List<FileMetaData> oldFiles = new ArrayList<FileMetaData>();
					oldFiles.addAll(((VdbFiles) data).getFiles());
					ddl.setXml((String) obj);
					data.setAsSdef(ddl);
					VdbFiles newFiles = (VdbFiles) data;
					if (oldFiles.size() > newFiles.getFiles().size())
					{
						oldFiles.removeAll(newFiles.getFiles());
						FileManager manager = VdbManager.getEngine()
								.getFileManager();
						for (FileMetaData fi : oldFiles)
						{
							manager.delete(fi);
						}
						manager.ungrant(oldFiles);
					}
				}
				String result = (String) obj;
				if (result.indexOf("<value>") > -1)
				{
					String value = result.substring(
							result.indexOf("<value>") + 7, result
									.indexOf("</value>"));
					String replace = value.replaceAll("<", "&lt;").replaceAll(
							">", "&gt;");
					result = result.replace(value, replace);
				}
				ddl.setXml(result);
				data.setAsSdef(ddl);
				if (field.get("nullable") != null
						&& field.get("nullable").equals("false"))
				{
					if (data.getValue() == null
							|| data.getValue().trim().equals("")
							|| data.getValue().trim().toLowerCase().equals(
									"null"))
					{
						if ("Files".equals(field.getTypeName()))
						{

							if (((VdbFiles) data).getFiles().size() == 0)
								throw new RuntimeException(field.get("title")
										+ "不能为空");
						}
						else
						{
							throw new RuntimeException(field.get("title")
									+ "不能为空");
						}
					}

					if ("RichText".equals(field.getTypeName()))
					{
						String r_value = data.getValue();
						r_value = r_value.replaceFirst("<P>", "");
						r_value = r_value.replaceFirst("</P>", "");
						r_value = r_value.replaceAll("&nbsp;", "");
						if (r_value.trim().trim().length() <= 0)
							throw new RuntimeException(field.get("title")
									+ "不能为空");
					}
				}
			}
			catch (Exception e)
			{
				JSONObject failed = new JSONObject();
				failed.put("code", 400);
				failed.put("source", field.getId());
				failed.put("message", "保存数据错误！<br><br>"
						+ "<div style='line-height:20px'>错误字段："
						+ field.getColumnName() + "<br>详细错误信息如下：<br>"
						+ e.getMessage() + "</div>");
				failed.write(response.getWriter());
				dao.removeFromCache(bean);
				return;
			}
		}

		if (isNewRecord)
		{
			Field editorField = thisEntity.getEditorField();
			if (editorField != null)
			{
				bean.get(editorField).setAsSdef(
						new SimpleSdef(new AuthTool().getUserName()));
			}

			Field groupField = thisEntity.getGroupField();
			if (groupField != null)
			{
				String userName = new AuthTool().getUserName();
				List<VdbGroup> groups = VdbManager.getEngine()
						.getSecurityManager().getGroupsOfUser(userName);
				if (groups != null && groups.size() > 0)
				{
					bean.get(groupField).setAsSdef(
							new SimpleSdef(groups.get(0).get("GROUPCODE")
									.toString()));
				}
			}

		}

		Field timeStampField = thisEntity.getTimeStampField();
		if (timeStampField != null)
		{
			bean.get(timeStampField).setAsSdef(new SimpleSdef(new Date()));
		}

		try
		{
			if (isNewRecord)
			{
				dao.insert(bean);
			}
			else
			{
				dao.update(bean);
			}
		}
		catch (Exception e)
		{
			JSONObject updateFailed = new JSONObject();
			updateFailed.put("code", 400);
			updateFailed.put("message", "保存数据错误！<br><br>"
					+ "<div style='line-height:20px'>具体错误信息如下：<br>"
					+ e.getMessage() + "</div>");
			updateFailed.write(response.getWriter());
			dao.removeFromCache(bean);
			return;
		}

		/** *****************BEGIN LOGGING******************** */
		String ip = request.getRemoteAddr();
		String user = new AuthTool().getUserName();
		AccessLoggerTool loggerTool = new AccessLoggerTool();
		if (isNewRecord)
		{
			loggerTool.logAccess(user, ip, thisEntity, "insert", null, bean
					.getId().getValue(), false);
		}
		else
		{
			loggerTool.logAccess(user, ip, thisEntity, "update", null, beanId,
					false);
		}
		/** ******************End LOGGING*********************** */

		JSONObject ok = new JSONObject();
		ok.put("code", 200);
		ok.put("id", bean.getId() == null ? "" : bean.getId().toString());

		ok.write(response.getWriter());
	}

}
