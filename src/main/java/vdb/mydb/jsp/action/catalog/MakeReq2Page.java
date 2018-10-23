package vdb.mydb.jsp.action.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import vdb.metacat.fs.page.AddItemPage;
import vdb.metacat.fs.page.EditField;
import vdb.metacat.fs.page.ListEditItemsPage;
import vdb.metacat.fs.page.ListItemsPage;
import vdb.metacat.fs.page.OrderField;
import vdb.metacat.fs.page.ShowItemPage;
import vdb.metacat.fs.page.UpdateItemPage;
import vdb.metacat.fs.page.ViewField;

public class MakeReq2Page
{
	/**
	 * ListItemsPage
	 * 
	 * @param request
	 * @return
	 */
	public ListItemsPage makeListItemsPage(ServletRequest request)
	{
		// 组装页面对象
		ListItemsPage lip = new ListItemsPage();

		boolean flag = false;
		if (request.getParameter("isDefault").equals("true"))
			flag = true;
		else
			flag = false;
		lip.setDefault(flag);
		lip.setName(request.getParameter("name"));
		try
		{
			lip.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		lip.setGrantFilter(request.getParameter("grantFilter"));
		lip.setTitle(request.getParameter("title"));
		lip.setStyle(request.getParameter("style"));
		String type = request.getParameter("type");
		lip.setType(type);
		String entityUri = request.getParameter("eui");
		lip.setEntityUri(entityUri);
		// 显示字段
		List<ViewField> viewFields = new ArrayList<ViewField>();
		String sfs = request.getParameter("sfs");
		String[] fields = sfs.split(";");
		for (int j = 0; j < fields.length; j++)
		{
			ViewField viewField = new ViewField(fields[j]);
			viewFields.add(viewField);
		}
		lip.setViewFields(viewFields);

		// 排序字段
		List<OrderField> orderFields = new ArrayList<OrderField>();
		String ofs = request.getParameter("ofs");
		String[] fields2 = ofs.split(";");
		for (int j = 0; j < fields2.length; j++)
		{
			String orderValue = request.getParameter("sort" + fields2[j]);
			OrderField of = new OrderField();
			of.setFieldUri(fields2[j]);
			of.setOrder(orderValue);
			orderFields.add(of);

		}
		lip.setOrderFields(orderFields);

		return lip;
	}

	public ShowItemPage makeShowItemPage(ServletRequest request)
	{
		// 组装页面对象
		ShowItemPage sip = new ShowItemPage();
		boolean display = true;
		boolean hideCollectionFieldTitle = false;
		boolean flag = false;
		if (request.getParameter("isDefault").equals("true"))
			flag = true;
		else
			flag = false;

		if (request.getParameter("hideCollectionFieldTitle") == null)
			hideCollectionFieldTitle = false;
		else
			hideCollectionFieldTitle = true;

		sip.setHideCollectionFieldTitle(hideCollectionFieldTitle);

		if (request.getParameter("isDisplay") == null)
			display = false;
		else
			display = true;
		sip.setDefault(flag);
		sip.setDisplay(display);
		sip.setName(request.getParameter("name"));
		sip.setTitle(request.getParameter("title"));
		sip.setStyle(request.getParameter("style"));
		sip.setType(request.getParameter("type"));
		sip.setEntityUri(request.getParameter("eui"));
		// 显示字段
		List<ViewField> viewFields = new ArrayList<ViewField>();
		String sfs = request.getParameter("sfs");
		String[] fields = sfs.split(";");
		for (int j = 0; j < fields.length; j++)
		{
			ViewField viewField = new ViewField(fields[j]);
			viewFields.add(viewField);
		}
		sip.setViewFields(viewFields);
		return sip;
	}

	public AddItemPage makeAddItemPage(ServletRequest request)
	{

		// 编辑字段
		List<EditField> editFields = new ArrayList<EditField>();

		String efs = request.getParameter("efs");
		String[] fields = efs.split(";");
		for (int j = 0; j < fields.length; j++)
		{
			String dfvParm = "defaultValue" + fields[j];
			String defaultValue = request.getParameter(dfvParm);
			String readParm = "read" + fields[j];
			String readValue = request.getParameter(readParm);
			boolean isReadonly = false;
			if (readValue == null)
			{
				isReadonly = false;
			}
			if ((readValue != null) && (readValue.equals("readonly")))
			{
				isReadonly = true;
			}
			EditField ef = new EditField(fields[j], defaultValue, isReadonly);
			editFields.add(ef);
		}

		// 组装页面对象
		AddItemPage aip = new AddItemPage();

		boolean flag = false;
		if (request.getParameter("isDefault").equals("true"))
			flag = true;
		else
			flag = false;
		aip.setDefault(flag);
		aip.setName(request.getParameter("name"));
		aip.setTitle(request.getParameter("title"));
		aip.setStyle(request.getParameter("style"));

		aip.setEditFields(editFields);
		aip.setType(request.getParameter("type"));
		aip.setEntityUri(request.getParameter("eui"));

		aip.setEditFields(editFields);
		return aip;
	}

	public ListEditItemsPage makeListEditItemsPage(ServletRequest request)
	{
		// 组装页面对象
		ListEditItemsPage leip = new ListEditItemsPage();

		// 编辑字段
		List<EditField> editFields = new ArrayList<EditField>();

		String efs = request.getParameter("efs");
		String[] fields = efs.split(";");
		for (int j = 0; j < fields.length; j++)
		{
			String dfvParm = "defaultValue" + fields[j];
			String defaultValue = request.getParameter(dfvParm);
			String readParm = "read" + fields[j];
			String readValue = request.getParameter(readParm);
			boolean isReadonly = false;
			if (readValue == null)
			{
				isReadonly = false;
			}
			if ((readValue != null) && (readValue.equals("readonly")))
			{
				isReadonly = true;
			}
			EditField ef = new EditField(fields[j], defaultValue, isReadonly);
			editFields.add(ef);
		}

		// 排序字段
		List<OrderField> orderFields = new ArrayList<OrderField>();
		String ofs = request.getParameter("ofs");
		String[] fields2 = ofs.split(";");
		for (int j = 0; j < fields2.length; j++)
		{
			String orderValue = request.getParameter("sort" + fields2[j]);
			OrderField of = new OrderField();
			of.setFieldUri(fields2[j]);
			of.setOrder(orderValue);
			orderFields.add(of);

		}

		boolean flag = false;
		if (request.getParameter("isDefault").equals("true"))
			flag = true;
		else
			flag = false;
		leip.setDefault(flag);

		leip.setOrderFields(orderFields);
		leip.setEditFields(editFields);
		leip.setName(request.getParameter("name"));
		leip.setGrantFilter(request.getParameter("grantFilter"));
		leip.setTitle(request.getParameter("title"));
		leip.setStyle(request.getParameter("style"));
		leip.setType(request.getParameter("type"));
		leip.setEntityUri(request.getParameter("eui"));
		return leip;
	}

	public UpdateItemPage makeUpdateItemPage(ServletRequest request)
	{

		// 编辑字段
		List<EditField> editFields = new ArrayList<EditField>();

		String efs = request.getParameter("efs");
		String[] fields = efs.split(";");
		for (int j = 0; j < fields.length; j++)
		{
			String dfvParm = "defaultValue" + fields[j];
			String defaultValue = request.getParameter(dfvParm);
			String readParm = "read" + fields[j];
			String readValue = request.getParameter(readParm);
			boolean isReadonly = false;
			if (readValue == null)
			{
				isReadonly = false;
			}
			if ((readValue != null) && (readValue.equals("readonly")))
			{
				isReadonly = true;
			}
			EditField ef = new EditField(fields[j], defaultValue, isReadonly);
			editFields.add(ef);
		}

		// 组装页面对象
		UpdateItemPage uip = new UpdateItemPage();

		boolean flag = false;
		if (request.getParameter("isDefault").equals("true"))
			flag = true;
		else
			flag = false;
		uip.setDefault(flag);
		uip.setEditFields(editFields);
		uip.setName(request.getParameter("name"));
		uip.setTitle(request.getParameter("title"));
		uip.setStyle(request.getParameter("style"));

		uip.setType(request.getParameter("type"));
		uip.setEntityUri(request.getParameter("eui"));
		return uip;
	}
}
