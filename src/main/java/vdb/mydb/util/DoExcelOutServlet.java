package vdb.mydb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.RelationKey;
import vdb.mydb.VdbManager;
import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.AnyBeanDao;
import vdb.mydb.metacat.VdbEntity;
import vdb.mydb.query.AnyQuery;
import vdb.mydb.query.JdbcExpr;
import vdb.mydb.query.QueryExecutor;
import vdb.mydb.query.VarQuery;
import vdb.mydb.query.impl.JsoQuery;
import vdb.mydb.query.impl.QueryFromSqlImpl;
import vdb.mydb.typelib.VdbData;
import vdb.mydb.typelib.data.VdbCollection;
import vdb.mydb.typelib.data.VdbDate;
import cn.csdb.commons.sql.jdbc.sql.StringSql;

//FIXME: incorrect package name!!!
//FIXME: why other exportXxx.jsp files keep existing?
public class DoExcelOutServlet extends HttpServlet
{
	private String _errorPage;

	@Override
	public void init(ServletConfig arg0) throws ServletException
	{
		super.init(arg0);

		_errorPage = arg0.getInitParameter("errorPage");
		if (_errorPage == null)
			_errorPage = "/console/shared/error.vpage";
	}

	public void yuanExcels(OutputStream os, String whereFilter,
			String exportType, String tid, String id, String exportItem)
			throws IOException
	{
		{

			// File file = new File(arg2.getRealPath("WEB-INF") + "/tmp");
			// if (!file.exists())
			// {
			// file.mkdir();
			// }
			// String path = arg2.getRealPath("WEB-INF/tmp");
			WritableWorkbook book = null;
			// String exportType = request.getParameter("exportType");
			// String exportItem = "";
			
			String[] exportItems = new String[0];//初始化
			// String tid = request.getParameter("tid");
			// Entity thisTable = (Entity)
			// VdbManager.getInstance().getCatalog().fromUri(tid);
			VdbEntity thisTable = (VdbEntity) VdbManager.getInstance()
					.getCatalog().fromUri(tid);
			// 查找外键
			RelationKey[] collectionKeys = thisTable.getCollectionKeys();
			RelationKey[] referenceKeys = thisTable.getReferenceKeys();
			List<VdbEntity> collectionVdbEntitys = new ArrayList<VdbEntity>();
			List<VdbEntity> referenceVdbEntitys = new ArrayList<VdbEntity>();
			// 全部实体列表
			List<VdbEntity> allVdbEntitys = new ArrayList<VdbEntity>();
			String collectionItems = "";
			String referenceItems = "";
			String comCollectionItems = "";
			String comReferenceItems = "";
			allVdbEntitys.add(thisTable);
			for (int i = 0; i < collectionKeys.length; i++)
			{
				RelationKey ck = collectionKeys[i];
				VdbEntity cv = (VdbEntity) ck.getTarget();
				collectionVdbEntitys.add(cv);
			}
			for (int i = 0; i < referenceKeys.length; i++)
			{
				RelationKey rk = referenceKeys[i];
				VdbEntity rv = (VdbEntity) rk.getTarget();
				referenceVdbEntitys.add(rv);
			}
			for (Iterator<VdbEntity> ir = referenceVdbEntitys.iterator(); ir
					.hasNext();)
			{
				VdbEntity vr = ir.next();
				Field[] fs = vr.getFields();
				for (int j = 0; j < fs.length; j++)
				{

					referenceItems += (fs[j].getTitle() + ";");
					comReferenceItems += (fs[j].getEntity().getName() + "-"
							+ fs[j].getTitle() + ";");
				}
				allVdbEntitys.add(vr);
			}
			for (Iterator<VdbEntity> ic = collectionVdbEntitys.iterator(); ic
					.hasNext();)
			{
				VdbEntity vc = ic.next();
				Field[] fs = vc.getFields();
				for (int j = 0; j < fs.length; j++)
				{
					collectionItems += (fs[j].getTitle() + ";");
					comCollectionItems += (fs[j].getEntity().getName() + "-"
							+ fs[j].getTitle() + ";");
				}
				allVdbEntitys.add(vc);
			}

			String ttitle = thisTable.getTitle();
			if (exportType.equals("1"))
			{
				ttitle = ttitle + "模板";
			}
			// 记录的id组合
			// String id = request.getParameter("id");
			String[] ids = null;
			if (null != id)
			{
				ids = id.split(";");
			}
			String[] thisName = null;
			String thisTableFieldNames = "";
			// 导出模板
			if (!exportType.equals("1"))
			{
				// exportItem = new String(exportItem)
				// .getBytes("iso-8859-1"), ("gb2312"));
				exportItem = URLDecoder.decode(exportItem, "UTF-8");
				// 将表名加入主表字段名列 表名-字段名
				thisName = exportItem.split(";");
				for (int i = 0; i < thisName.length; i++)
				{
					thisTableFieldNames += thisTable.getName() + "-"
							+ thisName[i] + ";";
				}
				exportItem = exportItem + ";" + referenceItems
						+ collectionItems;
				exportItems = exportItem.split(";");
			}
			// 比较字段时所有字段，与显示的字段顺序一致
			String compareExportItem = thisTableFieldNames + comReferenceItems
					+ comCollectionItems;
			String[] compareExportItems = compareExportItem.split(";");
			try
			{

				book = Workbook.createWorkbook(os);
				WritableSheet sheet = book.createSheet(ttitle, 0);
				try
				{
					int j = 0;
					if (exportType.equals("1"))
					{
						for (Field field : thisTable.getFields())
						{
							Label label = new Label(j, 0, field.getTitle());
							sheet.addCell(label);
							j++;
						}
					}
					else
					{
						for (int i = 0; i < exportItems.length; i++)
						{
							Label label = new Label(j, 0, exportItems[i]);
							sheet.addCell(label);
							j++;
						}
					}
				}
				catch (Exception e)
				{
					e.getStackTrace();
				}
				// 导出所选 i列号 j行号
				/*
				 * if (exportType.equals("2")) { AnyBean anyBean = null; for
				 * (int i = 0; i < ids.length; i++) { try { int j = 0; anyBean =
				 * new AnyBeanDao(thisTable).lookup(ids[i]); for(int k=0;k<exportItems.length;k++) {
				 * for (Field field : thisTable.getFields()) {
				 * if(field.getTitle().equals(exportItems[k])) { VdbData data =
				 * anyBean.get(field); Label label = new Label(j, i + 1,
				 * data.getValue().toString());
				 * if(field.getType().getName().toString().equalsIgnoreCase("date"))
				 * label = new Label(j, i + 1, ((VdbDate)data).format(null));
				 * sheet.addCell(label); j++; break; } } } } catch (Exception e) {
				 *  } } }
				 */

				if (exportType.equals("2"))
				{
					int startLine = 1;
					int endLine = 0;
					// 选择的记录循环
					for (int i = 0; i < ids.length; i++)
					{
						List<AnyBean> allBeans = new ArrayList<AnyBean>();
						try
						{

							AnyBean thisTableBean = new AnyBeanDao(thisTable)
									.lookup(ids[i]);
							allBeans.add(thisTableBean);
							for (Field field : thisTable.getFields())
							{
								if (field.isReference())
								{
									// 主表中每个引用字段对应一个结果
									// 引用字段在主表中的值
									VdbData thisTabledata = thisTableBean
											.get(field);
									// 引用字段在引用表中 按主表值查询的结果
									AnyBean refBean = new AnyBeanDao(field
											.getRelationKey().getTarget())
											.lookup(thisTabledata.getValue());
									allBeans.add(refBean);
								}
								if (field.isCollection())
								{
									// 主表中每个集合字段对应多个结果

									// 取出全部集合
									VdbCollection vc = (VdbCollection) thisTableBean
											.get(field);
									List<AnyBean> colBeans = vc.listAll();
									for (Iterator<AnyBean> it = colBeans
											.iterator(); it.hasNext();)
									{
										// 每个集合中引用字段的结果
										AnyBean colBean = it.next();
										allBeans.add(colBean);
									}
								}
							}
							// 取同一实体中记录最大数
							int[] a = new int[allVdbEntitys.size()];
							for (Iterator<AnyBean> it = allBeans.iterator(); it
									.hasNext();)
							{
								Entity e1 = it.next().getEntity();
								for (int k = 0; k < allVdbEntitys.size(); k++)
								{
									Entity e2 = allVdbEntitys.get(k);
									if (e1 == e2)
									{
										a[k]++;
									}
								}
							}
							Arrays.sort(a);
							int max = a[a.length - 1];
							endLine = startLine + max - 1;
							// 应输出max条记录
							for (int j = startLine; j <= endLine; j++)
							{
								// 已经查询出结果的列表，下次不需要显示
								List<AnyBean> toDeleteBeans = new ArrayList<AnyBean>();
								// 根据字段名输出 k为列数 全部循环一次，输出一行结果
								for (int k = 0; k < exportItems.length; k++)
								{

									// 按照实体数量的顺序输出
									for (Iterator<AnyBean> ia = allBeans
											.iterator(); ia.hasNext();)
									{
										AnyBean outBean = ia.next();
										if (outBean != null)
										{
											for (Field field : outBean
													.getEntity().getFields())
											{
												// 过滤掉表达式字段
												if (field.getTypeName()
														.equalsIgnoreCase(
																"Expression"))
												{
													continue;
												}
												// 与标题字段比较 表名-字段名
												// exportItems与compareExportItems字段顺序一致
												if ((field.getEntity()
														.getName()
														+ "-" + field
														.getTitle())
														.equals(compareExportItems[k]))
												{
													VdbData data = outBean
															.get(field);
													String value = data
															.getValue()
															.toString();

													Label label = null;
													// 时间需要处理
													if (field.getType()
															.getName()
															.toString()
															.equalsIgnoreCase(
																	"date"))
													{
														label = new Label(
																k,
																j,
																((VdbDate) data)
																		.format(null));
													}
													else
													{
														// 过滤 列数 行数
														label = new Label(k, j,
																value);
													}
													sheet.addCell(label);
													break;
												}
											}
										}
									}
								}
								startLine++;
								toDeleteBeans.addAll(allBeans);
								List<Integer> delIndexs = new ArrayList<Integer>();
								// 遍历toDeleteBeans，删除线写入的1相同数据库名 2最后写入的bean对象
								for (Iterator<AnyBean> itd = toDeleteBeans
										.iterator(); itd.hasNext();)
								{
									AnyBean a1 = itd.next();
									int count = 0;
									int maxIndex = toDeleteBeans.indexOf(a1);
									for (Iterator<AnyBean> itd2 = toDeleteBeans
											.iterator(); itd2.hasNext();)
									{
										AnyBean a2 = itd2.next();
										// 相同表名
										if ((a1.getEntity().getName())
												.equals(a2.getEntity()
														.getName()))
										{
											count++;
											if (toDeleteBeans.indexOf(a2) > maxIndex)
											{
												maxIndex = toDeleteBeans
														.indexOf(a2);
												delIndexs.add(maxIndex);
											}
										}
									}
									if (count == 1)
									{
										// 加入删除数组
										int delI = toDeleteBeans.indexOf(a1);
										delIndexs.add(delI);
									}
								}
								// 遍历删除数组，删除
								for (int m = 0; m < delIndexs.size(); m++)
								{
									int idx = delIndexs.get(m).intValue();
									allBeans.set(idx, null);
								}
								// startLine++;
							}

						}
						catch (Exception e)
						{
							e.getStackTrace();
						}
					}
				}
				// 导出所有
				if (exportType.equals("3"))
				{
					int startLine = 1;
					int endLine = 0;
					try
					{
						vdb.mydb.vtl.toolbox.VdbTool tool = new vdb.mydb.vtl.toolbox.VdbTool();
						JdbcExpr je = null;

						if (whereFilter != null
								&& whereFilter.trim().length() > 0)
						{
							JsoQuery jsoQuery = tool.parseJsoQuery(whereFilter);
							AnyQuery query = tool.createQuery(jsoQuery);
							tool.mergeQuery(query, jsoQuery);
							je = query.where();
						}

						VarQuery vq = new QueryFromSqlImpl(thisTable);
						String sqlStrSta = "select * from "
								+ thisTable.getTableName();

						if (null != je)
						{
							sqlStrSta = sqlStrSta + " where "
									+ je.toStringSql().toString();
						}

						StringSql sqlSta = new StringSql(sqlStrSta);

						if (null != je)
						{
							sqlSta.addParameters(je.toStringSql()
									.getParameters());
						}

						((QueryFromSqlImpl) vq).setSql(new StringSql(sqlSta));

						QueryExecutor qe = null;
						try
						{
							qe = new AnyBeanDao(thisTable).execute(vq);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}

						List<AnyBean> list = null;
						try
						{
							list = qe.list();
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
						// AnyBeanDao dao = new AnyBeanDao(thisTable);
						// AnyQuery query = dao.createQuery();
						// List<AnyBean> drs = dao.execute(query).list();
						for (int i = 0; i < list.size(); i++)
						{
							List<AnyBean> allBeans = new ArrayList<AnyBean>();
							try
							{
								AnyBean thisTableBean = list.get(i);
								allBeans.add(thisTableBean);

								for (Field field : thisTable.getFields())
								{
									if (field.isReference())
									{
										// 主表中每个引用字段对应一个结果
										// 引用字段在主表中的值
										VdbData thisTabledata = thisTableBean
												.get(field);
										// 引用字段在引用表中 按主表值查询的结果
										AnyBean refBean = new AnyBeanDao(field
												.getRelationKey().getTarget())
												.lookup(thisTabledata
														.getValue());
										allBeans.add(refBean);
									}
									if (field.isCollection())
									{
										// 主表中每个集合字段对应多个结果

										// 取出全部集合
										VdbCollection vc = (VdbCollection) thisTableBean
												.get(field);
										List<AnyBean> colBeans = vc.listAll();
										for (Iterator<AnyBean> it = colBeans
												.iterator(); it.hasNext();)
										{
											// 每个集合中引用字段的结果
											AnyBean colBean = it.next();
											allBeans.add(colBean);
										}
									}
								}
								// 取同一实体中记录最大数
								int[] a = new int[allVdbEntitys.size()];
								for (Iterator<AnyBean> it = allBeans.iterator(); it
										.hasNext();)
								{
									Entity e1 = it.next().getEntity();
									for (int k = 0; k < allVdbEntitys.size(); k++)
									{
										Entity e2 = allVdbEntitys.get(k);
										if (e1 == e2)
										{
											a[k]++;
										}
									}
								}
								Arrays.sort(a);
								int max = a[a.length - 1];
								endLine = startLine + max - 1;
								// 应输出max条记录
								for (int j = startLine; j <= endLine; j++)
								{
									// 已经查询出结果的列表，下次不需要显示
									List<AnyBean> toDeleteBeans = new ArrayList<AnyBean>();
									// 根据字段名输出 k为列数 全部循环一次，输出一行结果
									for (int k = 0; k < exportItems.length; k++)
									{

										// 按照实体数量的顺序输出
										for (Iterator<AnyBean> ia = allBeans
												.iterator(); ia.hasNext();)
										{
											AnyBean outBean = ia.next();
											if (outBean != null)
											{
												for (Field field : outBean
														.getEntity()
														.getFields())
												{
													// 过滤掉表达式字段
													if (field
															.getTypeName()
															.equalsIgnoreCase(
																	"Expression"))
													{
														continue;
													}
													// 与标题字段比较 表名-字段名
													// exportItems与compareExportItems字段顺序一致
													if ((field.getEntity()
															.getName()
															+ "-" + field
															.getTitle())
															.equals(compareExportItems[k]))
													{
														VdbData data = outBean
																.get(field);
														String value = data
																.getValue()
																.toString();

														Label label = null;
														// 时间需要处理
														if (field
																.getType()
																.getName()
																.toString()
																.equalsIgnoreCase(
																		"date"))
														{
															label = new Label(
																	k,
																	j,
																	((VdbDate) data)
																			.format(null));
														}
														else
														{
															// 过滤 列数 行数
															label = new Label(
																	k, j, value);
														}
														sheet.addCell(label);
														break;
													}
												}
											}
										}

									}
									startLine++;
									toDeleteBeans.addAll(allBeans);
									List<Integer> delIndexs = new ArrayList<Integer>();
									// 遍历toDeleteBeans，删除线写入的1相同数据库名
									// 2最后写入的bean对象
									for (Iterator<AnyBean> itd = toDeleteBeans
											.iterator(); itd.hasNext();)
									{
										AnyBean a1 = itd.next();
										int count = 0;
										int maxIndex = toDeleteBeans
												.indexOf(a1);
										for (Iterator<AnyBean> itd2 = toDeleteBeans
												.iterator(); itd2.hasNext();)
										{
											AnyBean a2 = itd2.next();
											// 相同表名
											if ((a1.getEntity().getName())
													.equals(a2.getEntity()
															.getName()))
											{
												count++;
												if (toDeleteBeans.indexOf(a2) > maxIndex)
												{
													maxIndex = toDeleteBeans
															.indexOf(a2);
													delIndexs.add(maxIndex);
												}
											}
										}
										if (count == 1)
										{
											// 加入删除数组
											int delI = toDeleteBeans
													.indexOf(a1);
											delIndexs.add(delI);
										}
									}
									// 遍历删除数组，删除
									for (int m = 0; m < delIndexs.size(); m++)
									{
										int idx = delIndexs.get(m).intValue();
										allBeans.set(idx, null);
									}
								}

							}
							catch (Exception e)
							{
								e.getStackTrace();
							}
						}

					}
					catch (Exception e)
					{
						e.getStackTrace();
					}

				}
				book.write();
				// book.close();
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
			}
			finally
			{
				try
				{
					book.close();
				}
				catch (WriteException e)
				{

					e.printStackTrace();
				}
				catch (IOException e)
				{

					e.printStackTrace();
				}
			}
		}
	}

	public void service(ServletRequest req, ServletResponse resp)
			throws ServletException, IOException
	{
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpServletRequest request = (HttpServletRequest) req;
		String whereFilter = request.getParameter("whereFilter") == null ? ""
				: request.getParameter("whereFilter");
		request.setCharacterEncoding("UTF-8");
		String exportType = request.getParameter("exportType");
		String tid = request.getParameter("tid");
		String id = request.getParameter("id");
		String exportItem = request.getParameter("exportItem");

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
		String showFileName = "EXCEL" + dateString + ".xls";
		// 文件名称的编码

		showFileName = new String(showFileName.getBytes("ISO-8859-1"), "utf-8");

		// 设置页面弹出下载窗口

		response.addHeader("Content-disposition", "attachment; filename=\""
				+ showFileName + "\"");
		response.setContentType("application/unknown;charset=utf-8");
		try
		{
			OutputStream outstream = response.getOutputStream();
			yuanExcels(outstream, whereFilter, exportType, tid, id, exportItem);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
