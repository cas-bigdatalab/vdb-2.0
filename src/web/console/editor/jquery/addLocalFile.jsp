<%@ page language="java" import="java.util.*"%>
<jsp:directive.page import="vdb.metacat.DataSet"/>
<jsp:directive.page import="vdb.metacat.Catalog"/>
<%@page import="vdb.mydb.*,vdb.mydb.metacat.VdbDataSet"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<title>本地文件</title>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<script type="text/javascript" src="fileTree.js"></script>
		<script src="jquery.easing.js" type="text/javascript"></script>
		<script src="jqueryFileTree.js" type="text/javascript"></script>
		<link href="jqueryFileTree.css" rel="stylesheet" type="text/css"
			media="screen" />
		<script type="text/javascript">
			
			$(document).ready( function() {
				<%
					//Catalog catalog = (Catalog)request.getAttribute("catalog");
				   VdbDataSet ds = VdbManager.getInstance().getCatalog().fromId(request.getParameter("dsid"));
				   String path = ds.getRepository().getLocalFilePath();//(String)request.getAttribute("path");
				   String path_real = ds.getDataSetXml().getParentFile().getCanonicalPath();//(String)request.getAttribute("path_real");
				   path = path.replace("$dsRoot",path_real);
				   path = path.replace("\\","/");			
				%>
				
				$('#fileTreeDemo_3').fileTree({ root: "<%=path%>", script: 'jqueryFileTree.jsp', folderEvent: 'click', expandSpeed: 750, collapseSpeed: 750, expandEasing: 'easeOutBounce', collapseEasing: 'easeOutBounce', loadMessage: 'Un momento...' }, function(file) { 
				var ret_txt = file;
				ret_txt = ret_txt.replace("<%=path%>","");
				document.getElementById('file_name').value=ret_txt;
				});
				
				
			});
			
			function onSubmit()
			{
				var dsid = "<%=request.getParameter("dsid")%>";
				if(document.getElementById('file_name').value=="")
				{
					alert("还没有选择文件,请选择");
					return false;
				}
				document.location.href="../complexEditor/localFilesMetaSave.vpage?dsid="+dsid+"&value="+document.getElementById('file_name').value;
				//parent.dhxWins.window("w1").close();
			}
			
		</script>


		<style type="text/css">
			BODY {
				font-family: Verdana, Arial, Helvetica, sans-serif;
				font-size: 11px;
				background: #EEE;
				padding: 15px;
			}
			
			.demo {
				width: 300px;
				height: 300px;
				border-top: solid 1px #BBB;
				border-left: solid 1px #BBB;
				border-bottom: solid 1px #FFF;
				border-right: solid 1px #FFF;
				background: #FFF;
				overflow: scroll;
				padding: 10px;
				padding-left: 10px;
			}
	
		.example {
				padding-left: 10px;
			}
		</style>
	</head>
	<body>
		<div class="example">
			已选文件:
			<input type="text" name="file_name" id="file_name" size="35" value="">
			<br>
			<label style="word-break:break-all;" disabled>
				数据集本地文件路径：
				<br>
				<%=path%>
			</label>
			<br>
			<br>
			<div id="fileTreeDemo_3" class="demo"></div>
			<br>
			<div style="padding-left:130px">
				<input type="button" value=" 确 认 "	onclick="onSubmit();" />
			</div>
		</div>
	</body>
</html>
