<%@ page language="java"
	import="java.io.File,java.io.FileInputStream,java.io.InputStream,java.util.Iterator,java.util.List,jxl.Cell,jxl.Sheet,jxl.*,jxl.Workbook,org.apache.commons.fileupload.FileItem,org.apache.commons.fileupload.disk.DiskFileItemFactory,org.apache.commons.fileupload.servlet.ServletFileUpload,vdb.metacat.Entity,vdb.mydb.VdbManager,vdb.mydb.jdbc.JdbcSourceManager,cn.csdb.commons.sql.JdbcSource,cn.csdb.commons.sql.jdbc.sql.StringSql"%>
<%@page pageEncoding="utf-8"%>
<%@page import="vdb.tool.log.AccessLoggerTool"%>
<%@page import="vdb.tool.auth.AuthTool"%>
<%@page import="vdb.tool.generic.FormatTool"%>
<%@page import="java.util.Properties"%>

<%@page import="vdb.mydb.files.FileManager"%>
<%@page import="vdb.mydb.files.FileMetaData"%>
<%@page import="vdb.mydb.files.impl.FileMetaDataImpl"%>
<%@page import="cn.csdb.commons.util.StringUtils"%>
<%@page import="vdb.mydb.metacat.VdbDataSet"%>
<%@page import="vdb.mydb.metacat.VdbField"%>
<%@page import="vdb.mydb.files.FileOwner"%>
<%@page import="java.util.*"%>
<%@page import="vdb.mydb.files.impl.JdbcFileManager"%>
<%@page import="vdb.metacat.idgen.IdGenerator"%>

<%!
	void localFileHandler(String filePath, VdbDataSet dset, VdbField _field, String rowId) throws Exception
	{
		//如果路径为空，则不处理 
		if(filePath == null || filePath.equals(""))
			return ;
		String filedata = filePath;
		if (filedata.startsWith("\\") || filedata.startsWith("/"))
			filedata = filedata.substring(1);

		FileManager manager = VdbManager.getEngine().getFileManager();
		VdbDataSet ds = dset;
		FileMetaData fi = manager.createNewFile(ds);
		File file = ((JdbcFileManager)manager).getIoFile(ds,filePath);
		
		if(file.isDirectory())
		{
			for (File f : file.listFiles())
			{
				String name = f.getName();
				if (name.startsWith("#"))
					continue;
	
				if (name.startsWith("_"))
					continue;
	
				if (name.startsWith("."))
					continue;
				localFileHandler(filePath+"/"+name, ds, _field, rowId);
			}
			return;
		}
		
		String filepath = "";
		String filename = "";
		long length = 0;
		String contentType = "";
		String title = "";
		String ext = "";

		if(filedata.indexOf("(") < 0)
			filedata += "(0)";
		
		filepath = filedata.substring(0, filedata.indexOf("("));
		filename = filepath.substring(filepath.lastIndexOf("/") + 1);
		length = Long.parseLong(filedata.substring(
				filedata.indexOf("(") + 1, filedata.indexOf(")")));
		contentType = "";
		title = filename.substring(0, filename.indexOf("."));
		ext = filename.substring(filename.indexOf("."));

		String id = StringUtils.getGuid();//
		fi.setTitle(title);
		fi.setFileSize(length);
		fi.setContentType(contentType);
		fi.setId(id);
		fi.setExtension(ext);
		((FileMetaDataImpl) fi).setFilePath(filepath);
		
		if(file.exists())
			fi.setFileSize(file.length());
		else
			return;
			
		manager.insert(fi);
		
		java.io.Serializable pk = rowId;
		List<FileMetaData> files = new java.util.ArrayList<FileMetaData>();
		files.add(fi);
		// insert new files
		FileOwner owner = new FileOwner("" + pk, _field);
		manager.grant(owner, files);
	}
%>
<%
	request.setCharacterEncoding("utf-8");

	String importType = "0";
	// 上传文件路径相关信息
	String responseText = "";
	int exceptionType = 100; // 标记出错类型
	File fullFile = null;
	String path = getServletContext().getRealPath("WEB-INF/tmp");
	Iterator itr;
	FileItem fi = null;
	String fileName = "";
	try
	{
		// 创建磁盘文件工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 设置缓冲区大小
		factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
		// 创建文件上传句柄
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB
		List items = upload.parseRequest(request);// 得到所有的文件
		itr = items.iterator();
		fi = (FileItem) itr.next();
		fileName = fi.getName();
	}
	catch (Exception e)
	{

	}

	importType = request.getParameter("importType");

	if (importType.equals("1"))
	{
		try
		{
			if (fileName != null)
			{
				fullFile = new File(fi.getName());
				String fullFileName=fullFile.getName();
				//截取文件名
				Properties   prop   =   new   Properties(System.getProperties());
				String   sep=prop.getProperty("file.separator"); 
				int i = fullFileName.lastIndexOf(sep);
				
				if (i > 0) 
				{
					fullFileName = fullFileName.substring(i + 1, fullFileName.length());
				}
				File filePath = new File(path);
				if(!filePath.exists()) filePath.mkdirs();//如果目录不存在，则创建此目录
				File savedFile = new File(path, fullFileName);
				if (savedFile.exists())
				{
					savedFile.delete();
				}
				fi.write(savedFile);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			exceptionType = 1;
		}
	}

	if (importType.equals("2"))
	{

		String filename = java.net.URLDecoder.decode(request.getParameter("filename"),"UTF-8");
		// 读取excel数据，生成sql语句
		try
		{
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			// 从输入流创建Workbook
			InputStream is = null;
			int iCount=0;
			while (true)
			{
				iCount++;
				try
				{
					is = new FileInputStream(getServletContext()
					.getRealPath("WEB-INF/tmp")
					+ "/" + filename);// 创建输入
					break;
				}
				catch (Exception e)
				{
					try
					{
				   		Thread.sleep(1000);
				  	} catch (InterruptedException ie)
				  	{
				   		e.printStackTrace();
				  	}
					if(iCount>20)
					{
						break;
					}
				}
			}
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet rs = rwb.getSheet(0); // 读取第一个sheet
			int colNum = rs.getColumns();// 列数
			int rowNum = rs.getRows();// 行数
			String strValue;
			int colTemp = colNum;
			int rowTemp = rowNum > 3 ? 3 : rowNum;

			for (int i = 0; i < rowTemp; i++)
			{
				for (int j = 0; j < colTemp; j++)
				{
					Cell c = rs.getCell(j, i);
					strValue = c.getContents();
					if(strValue.length()>10)
					{
						strValue=strValue.substring(0,10)+"...";
					}
					responseText = responseText + strValue + "^^**";
		
				}
		
				responseText = responseText.substring(0, responseText
						.length() - 4)
						+ "@@||";
			}

			responseText = responseText.substring(0, responseText
			.length() - 4);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			//out.println(new String(responseText.getBytes("utf-8"),
			//"GB2312"));
			out.println(responseText);
			out.flush();
			//out.close();
			rwb.close();
			is.close();

		}
		catch (Exception e)
		{
			exceptionType = 2;
		}
	}

	if (importType.equals("3"))
	{
		FormatTool formatTool = new FormatTool();
		String tid = request.getParameter("tid");
		String resultStr = formatTool.decodeUrl(formatTool.decodeUrl(request.getParameter("resultStr")));
		Entity thisTable = (Entity) VdbManager.getEngine()
		.getCatalog().fromId(tid);
		JdbcSource jdbcSource = JdbcSourceManager
					.getInstance().getJdbcSource(
							thisTable.getDataSet());
		String useBig = request.getParameter("useBig");
		String insertSql;
		String valuesSql="values(";
		
		String[] resultStrs = resultStr.split(";");
		insertSql = "insert into " + thisTable.getTableName() + "(";
		for (int i = 0; i < resultStrs.length; i++)
		{
			insertSql = insertSql
			+ resultStrs[i].substring(0, resultStrs[i]
			.indexOf("(")) + ",";
			valuesSql=valuesSql+"?,";
		}
		StringSql insertStringSql = new StringSql(insertSql.substring(0, insertSql.length() - 1)+ ") "+valuesSql.substring(0, valuesSql.length() - 1)+ ")");
		// 读取excel数据，生成sql语句
		try
		{
			// 构建Workbook对象, 只读Workbook对象
			// 直接从本地文件创建Workbook
			// 从输入流创建Workbook
			//InputStream is = new FileInputStream(getServletContext()
			//.getRealPath("WEB-INF/tmp")
			//+ "/" + fileName);// 创建输入
			InputStream is = new FileInputStream(getServletContext()
			.getRealPath("WEB-INF/tmp")
			+ "/" + fileName.substring(fileName.lastIndexOf("\\")+1));
			Workbook rwb = Workbook.getWorkbook(is);
			Sheet rs = rwb.getSheet(0); // 读取第一个sheet
			int colNum = rs.getColumns();// 列数
			int rowNum = rs.getRows();// 行数
			String strValue;
			int tempi = 0;
			if (useBig.equals("y"))
			tempi = 1;
			for (int i = tempi; i < rowNum; i++)
			{
				strValue = "";
				int k = 0;
				String fieldTypeName = "";
				int flag = 0;
				for(int n=0;n<colNum; n++)
				{
					Cell c = rs.getCell(n, i);
					strValue = c.getContents();
					if(null!=strValue&&strValue.trim().length()>0)
						flag =1;
				}
				if(flag == 0)
					continue;
				Map fileMap = new HashMap();
				for (int j = 0; j < colNum; j++)
				{
					if (resultStr.indexOf("(" + j + ")") > 1)
					{
						Cell c = rs.getCell(j, i);
						strValue = c.getContents();
						if(c.getType()==CellType.NUMBER || c.getType()==CellType.NUMBER_FORMULA){
						       NumberCell nc=(NumberCell)c;
					    	   strValue = "" + nc.getValue();
					   	if(strValue.endsWith(".0"))
					   		strValue = strValue.substring(0,strValue.length()-2);
						}
						//setParameter第一个参数是从1开始
						k++;
						VdbField field = (VdbField)thisTable.getField(resultStrs[k-1].substring(0,resultStrs[k-1].indexOf("(")));
						fieldTypeName = field.getTypeName();
						
						if("Files".equalsIgnoreCase(fieldTypeName))
						{
							fileMap.put(field,strValue);
							insertStringSql.setParameter(k,"");
						}
						else
							insertStringSql.setParameter(k,strValue);
						//为空的数值型数据导入时自动赋值为0
						if(null==strValue||""==strValue)
						{	
							if ("integer".equalsIgnoreCase(fieldTypeName)|| "double".equalsIgnoreCase(fieldTypeName)|| "long".equalsIgnoreCase(fieldTypeName))
							{
								insertStringSql.setParameter(k,0);
							}
							if ("date".equalsIgnoreCase(fieldTypeName))
							{
								insertStringSql.setParameter(k,"1900-01-01");
							}
						}
					}
				}

				try
				{
					jdbcSource.executeUpdate(insertStringSql);
					
					exceptionType = 0;
					//EXCEL导入算是一种增加操作，将此操作记入日志记录
					AccessLoggerTool loggerTool = new AccessLoggerTool();
					String ip = request.getRemoteAddr();
					String user = new AuthTool().getUserName();
					loggerTool.logAccess(user,ip,thisTable,"insert",null,null,false);
					
					
					Integer lastId = 0;
					String lastIdString = null;
					if(fileMap.keySet().size()>0)
					{
						IdGenerator identity = thisTable.getIdentifier().getIdGenerator();
						if(identity == null || !identity.getName().equals("autoinc")){
							//默认第一列为主键
							lastIdString = rs.getCell(0, i).getContents();
							for(Object field: fileMap.keySet()){
								localFileHandler((String)fileMap.get(field),  (VdbDataSet)thisTable.getDataSet(), (VdbField)field,lastIdString);			
							}
						}
						else{
							String idField = thisTable.getIdentifier().getField().getName();//SELECT last_insert_id();")
							java.util.Map map = jdbcSource.queryForObject(new StringSql("select max(" + idField + ") as m from " + thisTable.getTableName()));
							lastId = (Integer)map.get("m");
						
							for(Object field: fileMap.keySet()){
								localFileHandler((String)fileMap.get(field),  (VdbDataSet)thisTable.getDataSet(), (VdbField)field,String.valueOf(lastId));			
							}
						}
					}
				}
				catch (Exception e)
				{
					exceptionType = 2;
					e.printStackTrace();
				}
			}
				rwb.close();
				is.close();
		}
		catch (Exception e)
		{
			exceptionType = 2;
			e.printStackTrace();
		}

		File savedFile = new File(path, fileName);
		if (savedFile.exists())
		{
			savedFile.delete();
		}
	}
	// 中文编码

	//response.setCharacterEncoding("utf-8");
	response.setContentType("text/html; charset=UTF-8");
	if (exceptionType == 0)
	{
		out
		.println("<script language='javascript'>alert('excel导入成功！');top.refreshgrid();top.dhxWins.window('w1').close();	</script>");
	}
	if (exceptionType == 1)
	{
		out
		.println("<script language='javascript'>alert('excel上传失败！请检查excel文件格式是否正确！');</script>");
	}
	if (exceptionType == 2)
	{
		out
		.println("<script language='javascript'>alert('excel导入数据库失败！请检查主键是否冲突、列名以及记录信息是否正确！');</script>");
	}
	out.flush();
%>
