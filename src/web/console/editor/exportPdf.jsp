<%@ page
	import="java.io.*,com.lowagie.text.Document,java.net.URLDecoder,com.lowagie.text.Element,com.lowagie.text.Paragraph,com.lowagie.text.pdf.BaseFont,com.lowagie.text.pdf.PdfPCell,com.lowagie.text.pdf.PdfPTable,com.lowagie.text.pdf.PdfWriter"%>
<%@ page language="java"
	import="vdb.metacat.Entity,vdb.metacat.Field,java.util.Calendar,java.util.List,vdb.mydb.VdbManager,vdb.mydb.bean.AnyBean,vdb.mydb.bean.AnyBeanDao,vdb.mydb.typelib.VdbData,vdb.mydb.query.AnyQuery"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%
		String exportItem = "";
		String tid = request.getParameter("tid");
		exportItem = new String(request.getParameter("exportItem").getBytes("iso-8859-1"),("gb2312"));
		exportItem = URLDecoder.decode(exportItem,"UTF-8");
		String exportType = request.getParameter("exportType");
		String[] exportItems=exportItem.split(";");;
		Entity thisTable = (Entity) VdbManager.getInstance().getCatalog()
		.fromId(tid);
		String titles = thisTable.getTitle();
		String id = request.getParameter("id");
		String[] ids = null;
		if (null != id) {
			ids = id.split(";");
		}
%>

<%
	response.setContentType("application/pdf");
	Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		String dateString = "";
		if (month < 10) {
			String months = "0" + String.valueOf(month);
			if (date < 10) {
				String dates = "0" + String.valueOf(date);
				dateString = "" + year + months + dates;
			} else {
				dateString = "" + year + months + date;
			}
		} else {
			dateString = "" + year + month + date;
		}
	response.setHeader("Content-Disposition", "attachment;filename="+new String(titles.getBytes("gb2312"),"iso8859-1")
			+ dateString+".pdf");
	try{		
	// 定义中文字体
  	BaseFont bfChinese = BaseFont.createFont("STSong-Light",
    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
  	com.lowagie.text.Font fontCN = new com.lowagie.text.Font(bfChinese, 12,
    com.lowagie.text.Font.NORMAL);
			
	Document document = new Document();
	ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	PdfWriter writer = PdfWriter.getInstance(document, buffer);
	document.open();
	Paragraph temp=new Paragraph(titles, fontCN);
	temp.setAlignment(Element.ALIGN_CENTER);
	document.add(temp);
	document.add(new Paragraph("   \n"));
	// 创建一个有3列的表格
   	PdfPTable table = new PdfPTable(2);
   	float[] widths = { 0.3f,0.7f };
   	
   	table.setWidths(widths);
   // 定义一个表格单元
 	PdfPCell cell =new PdfPCell(new Paragraph(""));
 	if(exportType.equals("6"))
	{
		AnyBean anyBean = new AnyBeanDao(thisTable).lookup(ids[0]);
		// 循环添加值
		for (int i = 0; i < ids.length; i++) 
		{
			anyBean = new AnyBeanDao(thisTable).lookup(ids[i]);
			table = new PdfPTable(2);
			table.setWidths(widths);
			for(int k=0;k<exportItems.length;k++)
			{
				for (Field field : thisTable.getFields())
				{
					if(field.getTitle().equals(exportItems[k]))
					{
						try{
								VdbData data = anyBean.get(field);
								cell = new PdfPCell(new Paragraph(field.getTitle(),fontCN));
								table.addCell(cell);
								if(null!=data)
								{
									cell = new PdfPCell(new Paragraph(data.getValue().toString(),fontCN));
									table.addCell(cell);
								}else
								{
									table.addCell("");
								}
							}catch(Exception e)
							{
							}
						}
					}
				}
				document.add(table);
				document.add(new Paragraph("   \n "));
			}			
		
		}
 
 
	if(exportType.equals("7"))
		{
			AnyBeanDao dao = new AnyBeanDao(thisTable);
			AnyQuery query = dao.createQuery();
			List drs = dao.execute(query).list();
			// 循环添加值
			for (int i = 0; i < drs.size(); i++) 
			{
				table = new PdfPTable(2);
				table.setWidths(widths);
				for(int k=0;k<exportItems.length;k++)
				{
					for (Field field : thisTable.getFields())
					{
						if(field.getTitle().equals(exportItems[k]))
						{
							try{
									String data = ((AnyBean) drs.get(i)).get(field)==null?" ":((AnyBean) drs.get(i)).get(field).getValue().toString();
									cell = new PdfPCell(new Paragraph(field.getTitle(),fontCN));
									table.addCell(cell);
									if(null!=data)
									{
										cell = new PdfPCell(new Paragraph(data.toString(),fontCN));
										table.addCell(cell);
									}else
									{
										table.addCell("");
									}
							}catch(Exception e)
							{
							}
						}
					}
				}
				document.add(table);
				document.add(new Paragraph("   \n"));
			}			
		
		}
   
   
	
		document.close();
		out.clear();
		out = pageContext.pushBody();

		DataOutput output = new DataOutputStream(response.getOutputStream());
		byte[] bytes = buffer.toByteArray();
		try{
		response.setContentLength(bytes.length);
		}catch(Exception e)
		{
		}
		for (int i = 0; i < bytes.length; i++) {
			output.writeByte(bytes[i]);
		}
	
	}catch(Exception e)
	{
	}
%>
