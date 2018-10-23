package vdb.mydb.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.vtl.action.ServletActionProxy;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class DoPDFOutAction extends ServletActionProxy
{

	public void doAction(ServletRequest req, ServletResponse resp,
			ServletContext arg2) throws Exception
	{
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;

		String exportItem = "";
		String tid = request.getParameter("tid");
		exportItem = request.getParameter("exportItem");
		String exportType = request.getParameter("exportType");
		String[] exportItems = exportItem.split(";");
		Entity thisTable = (Entity) VdbManager.getInstance().getCatalog()
				.fromId(tid);
		String titles = request.getParameter("ttitle");
		String id = request.getParameter("id");
		String[] ids = null;
		if (null != id)
		{
			ids = id.split(";");
		}

		response.setContentType("application/pdf");
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
				+ dateString + ".pdf");
		try
		{
			BaseFont bfChinese = BaseFont.createFont("STSong-Light",
					"UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			com.lowagie.text.Font fontCN = new com.lowagie.text.Font(bfChinese,
					12, com.lowagie.text.Font.NORMAL);

			Document document = new Document();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			document.open();
			Paragraph temp = new Paragraph(titles, fontCN);
			temp.setAlignment(Element.ALIGN_CENTER);
			document.add(temp);
			document.add(new Paragraph("   \n"));
			PdfPTable table = new PdfPTable(2);
			float[] widths = { 0.3f, 0.7f };

			table.setWidths(widths);
			PdfPCell cell = new PdfPCell(new Paragraph(""));
			if (exportType.equals("6"))
			{
				AnyBean anyBean = new AnyBeanDao(thisTable).lookup(ids[0]);
				for (int i = 0; i < ids.length; i++)
				{
					anyBean = new AnyBeanDao(thisTable).lookup(ids[i]);
					table = new PdfPTable(2);
					table.setWidths(widths);
					for (int k = 0; k < exportItems.length; k++)
					{
						for (Field field : thisTable.getFields())
						{
							if (field.getTitle().equals(exportItems[k]))
							{
								try
								{
									VdbData data = anyBean.get(field);
									cell = new PdfPCell(new Paragraph(field
											.getTitle(), fontCN));
									table.addCell(cell);
									if (null != data)
									{
										cell = new PdfPCell(new Paragraph(data
												.toString(), fontCN));
										table.addCell(cell);
									}
									else
									{
										table.addCell("");
									}
								}
								catch (Exception e)
								{
								}
							}
						}
					}
					document.add(table);
					document.add(new Paragraph("   \n "));
				}

			}

			if (exportType.equals("7"))
			{
				AnyBeanDao dao = new AnyBeanDao(thisTable);
				AnyQuery query = dao.createQuery();
				List drs = dao.execute(query).list();
				for (int i = 0; i < drs.size(); i++)
				{
					table = new PdfPTable(2);
					table.setWidths(widths);
					for (int k = 0; k < exportItems.length; k++)
					{
						for (Field field : thisTable.getFields())
						{
							if (field.getTitle().equals(exportItems[k]))
							{
								try
								{
									String data = ((AnyBean) drs.get(i))
											.get(field) == null ? " "
											: ((AnyBean) drs.get(i)).get(field)
													.toString();
									cell = new PdfPCell(new Paragraph(field
											.getTitle(), fontCN));
									table.addCell(cell);
									if (null != data)
									{
										cell = new PdfPCell(new Paragraph(data
												.toString(), fontCN));
										table.addCell(cell);
									}
									else
									{
										table.addCell("");
									}
								}
								catch (Exception e)
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

			DataOutput output = new DataOutputStream(response.getOutputStream());
			byte[] bytes = buffer.toByteArray();
			try
			{
				response.setContentLength(bytes.length);
			}
			catch (Exception e)
			{
			}
			for (int i = 0; i < bytes.length; i++)
			{
				output.writeByte(bytes[i]);
			}
		}
		catch (Exception e)
		{
		}
	}
}
