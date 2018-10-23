function chooseMany2ManyForUpdate(turi,furi,beanId,title,datagrid,model,thisEntityUri,fieldName,sep){
	//openModalDialog("./many2manyTotal.jsp?turi=" + escape(turi) + "&furi=" + escape(furi) + "&beanId=" + escape(beanId) + "&title=" + escape(title) + "&mode=update&i=" + Math.random(),title,title);
	//location.href="./many2manyTotal.jsp?turi=" + escape(turi) + "&furi=" + escape(furi) + "&beanId=" + escape(beanId) + "&title=" + escape(title) + "&mode=update&i=" + Math.random();
	//grid_selected.refresh();
	window.open("./many2manyTotal.vpage?turi=" + escape(turi) + "&furi=" + escape(furi) + "&beanId=" + escape(beanId) + "&title=" + escape(title) +  "&datagrid=" + escape(datagrid) + "&mode=update&model=" + escape(model) + "&thisEntityUri=" + escape(thisEntityUri) + "&fieldName=" + escape(fieldName) + "&sep=" + sep + "&i=" + Math.random(),"","modal=yes,width=800,height=650,resizable=yes,scrollbars=yes");
	//window.location.href=window.location.href;
}

function addMany2ManyForUpdate(thisEntityUri,thisFieldUri,thisId,thatId){
	httpPost("DoM2M.action",{'thisTableUri':thisEntityUri,'thisFieldUri':thisFieldUri,'thisId':thisId,'thatId':thatId,'mode':'linkAll'}
		,function(data){
 			dhxWins1 = new dhtmlXWindows();
			dhxWins1.enableAutoViewport(true);
			dhxWins1.attachViewportTo("winVP");
			dhxWins1.setImagePath("dhtmlx/windows/imgs/");
			document.body.scrollTop=0;
			win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
			win1.setModal(true);	
			win1.tI("操作结果");
			var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;添加成功!</div>";
			win1.attachHTMLString(htmlString);
			win1.centerOnScreen();
  			//dhxWins1.window("w1").close();
  			//grid.refresh();
  			//refreshgrid();
  			//parent.refreshgrid();
  			doOnGridSelected(thisId);
	});
}

function addMany2OneForUpdate(thisEntityUri,thisFieldUri,thisId,thatId){
	httpPost("DoM2O.action",{'thisTableUri':thisEntityUri,'thisFieldUri':thisFieldUri,'thisId':thisId,'thatId':thatId,'mode':'link'}
		,function(data){
  			dhxWins1 = new dhtmlXWindows();
			dhxWins1.enableAutoViewport(true);
			dhxWins1.attachViewportTo("winVP");
			dhxWins1.setImagePath("dhtmlx/windows/imgs/");
			document.body.scrollTop=0;
			win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
			win1.setModal(true);	
			win1.tI("操作结果");
			var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;添加成功!</div>";
			win1.attachHTMLString(htmlString);
			win1.centerOnScreen();
			doOnGridSelected(thisId);
			refreshgrid();
			dhxWins.window("w1").close();			
	});
}


function deleteMany2ManyForUpdate(thisEntityUri,thisFieldUri,thisId,thatId,seqid){
	if(confirm("您确定要删除关联信息:\n" + thatId )==true){
		httpPost("DoM2M.action",{'thisTableUri':thisEntityUri,'thisFieldUri':thisFieldUri,'thisId':thisId,'thatId':thatId,'mode':'unlink'}
			, function(data){
				dhxWins1 = new dhtmlXWindows();
				dhxWins1.enableAutoViewport(true);
				dhxWins1.attachViewportTo("winVP");
				dhxWins1.setImagePath("dhtmlx/windows/imgs/");
				document.body.scrollTop=0;
				win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
				win1.setModal(true);	
				win1.tI("操作结果");
				var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;删除成功!</div>";
				win1.attachHTMLString(htmlString);
				win1.centerOnScreen();
	 			doOnGridSelected(thisId);
	 			//var grid = eval("jsoDataGrid" + seqid);	
				//grid.refresh();
				//grid.refresh();
			});

	}
}


function showMany2ManyMainTableDetails(thisEntityUri,thatId){
	openDialog("/~showBean[" + thisEntityUri + "].vpage?id=" + thatId);
}


function addAllMany2ManyForUpdate(thisEntityUri,thisFieldUri,thisId){
	var thatIds =ref_total.jsoDataGrid.getCheckedRecords().ids;
	
	httpPost("DoM2M.action",
		"thisTableUri=" + thisEntityUri 
		+ "&thisFieldUri=" + thisFieldUri 
		+ "&thisId=" + thisId
		+ "&thatId =" + thatIds
		+ "&mode = linkAll"
		, function(data){
  			alert("成功增加选中关联信息");
	});
}


function returnMany2ManyForUpdate(datagrid){
	//window.returnValue = "";
	window.close();
	//window.opener.location.href=window.opener.location.href;
	var grid = eval("window.opener." + datagrid);
	grid.refresh();
	grid.refresh();
}


function deleteAllMany2ManyForUpdate(thisEntityUri,thisFieldUri,thisId,seqid){
	var div = eval("ref_selected"+seqid);
	var thatIds = div.jsoDataGrid.getCheckedRecords().ids;
	if(confirm("您确定要删除关联信息：\n" + thatIds)==true){
		httpPost("DoM2M.action",
			"thisTableUri=" + thisEntityUri
			+ "&thisFieldUri=" + thisFieldUri 
			+ "&thisId=" + thisId
			+ "&thatId =" + thatIds
			+ "&mode = unlinkAll"
			, function(data){
	 			dhxWins1 = new dhtmlXWindows();
				dhxWins1.enableAutoViewport(true);
				dhxWins1.attachViewportTo("winVP");
				dhxWins1.setImagePath("dhtmlx/windows/imgs/");
				document.body.scrollTop=0;
				win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
				win1.setModal(true);	
				win1.tI("操作结果");
				var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;删除成功!</div>";
				win1.attachHTMLString(htmlString);
				win1.centerOnScreen();
	 			var grid = eval("jsoDataGrid" + seqid);	
				grid.refresh();
				grid.refresh();
			});

	}
}

function deleteMany2ManyForTitle(thisEntityUri,thisFieldUri,thisId,thatId,divName,fieldName,thatEntityUri,title,sep){
	if(confirm("您确定要删除此条关联信息:\n" )==true){
		httpPost("DoM2M.action",
			"thisTableUri=" + thisEntityUri
			+ "&thisFieldUri=" + thisFieldUri 
			+ "&thisId=" + thisId
			+ "&thatId =" + thatId
			+ "&mode = unlink"
			, function(data){
	 			dhxWins1 = new dhtmlXWindows();
				dhxWins1.enableAutoViewport(true);
				dhxWins1.attachViewportTo("winVP");
				dhxWins1.setImagePath("dhtmlx/windows/imgs/");
				document.body.scrollTop=0;
				win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
				win1.setModal(true);	
				win1.tI("操作结果");
				var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;删除成功!</div>";
				win1.attachHTMLString(htmlString);
				win1.centerOnScreen();
	 			var div = eval("document.all." + divName);
				loadHTML1("/console/editor/many2manySelectedForTitle.vpage","thisEntityUri=" + escape(thatEntityUri) + "&thisFieldUri=" + escape(thisFieldUri) + "&fieldname=" + escape(fieldName) +"&thatEntityUri=" + escape(thisEntityUri) + "&thisId=" + escape(thisId) + "&title=" + escape(title) + "&div=" + escape(divName)  + "&sep=" + escape(sep),div);
				//location.href="/console/editor/many2manySelectedForTitle.vpage?thisEntityUri=" + thatEntityUri + "&thisFieldUri=" + thisFieldUri + "&fieldname=" + fieldName +"&thatEntityUri=" + thisEntityUri + "&thisId=" + thisId + "&title=" + title + "&div=" + divName;
			});
	}
}

function returnMany2ManyForTitle(divName,thisEntityUri,thisFieldUri,fieldName,thatEntityUri,thisId,title,sep){
	httpPost("/console/editor/many2manySelectedForTitle.vpage", 
		"thisEntityUri=" + escape(thisEntityUri) 
		+ "&thisFieldUri=" + escape(thisFieldUri) 
		+ "&fieldname=" + escape(fieldName )
		+"&thatEntityUri=" + escape(thatEntityUri)
		+ "&thisId=" + escape(thisId )
		+ "&title=" + escape(title) 
		+ "&div=" + escape(divName)
		+ "&sep=" + escape(sep)
		, function(data){
			var div = eval("window.opener.document.all." + divName);
			div.innerHTML=data;
			window.close();
		})
	//window.open("/console/editor/many2manySelectedForTitle.vpage?thisEntityUri=" + thisEntityUri + "&thisFieldUri=" + thisFieldUri + "&fieldname=" + fieldName +"&thatEntityUri=" + thatEntityUri + "&thisId=" + thisId + "&title=" + title + "&div=" + divName);
}

function chooseOne2ManyForUpdate(turi,mtid,mfid,fkv,title,datagrid,model,thisEntityUri,thisFieldUri,fieldName,sep){
	//openModalDialog("~updateBean[" + turi + "].vpage?mtid=" + mtid + "&mfid=" + mfid + "&fkv="+ fkv,title,top);
	//location.href="/console/editor/updateBean(" + turi + ").vpage?mtid=" + mtid + "&mfid=" + mfid + "&fkv="+ fkv;
	//window.location.href=window.location.href;
	window.open("~updateBean[" + turi + "].vpage?mtid=" + mtid + "&mfid=" + mfid + "&fkv="+ fkv + "&datagrid=" + datagrid + "&model=" + model + "&thisEntityUri=" + thisEntityUri + "&thisFieldUri=" + thisFieldUri + "&fieldName=" + fieldName + "&title=" + title + "&sep=" + sep ,"","modal=yes,width=800,height=650,resizable=yes,scrollbars=yes");
}

function deleteOne2ManyForUpdate(thisEntityUri,thisFieldUri,thisId,thatId,seqid){

	if(confirm("您确定要删除此条关联信息:\n" + thatId )==true){
		httpPost("DoO2M.action",{'thisTableUri':thisEntityUri,'thisFieldUri':thisFieldUri,'thisId':thisId,'thatId':thatId,'mode':'unlink'}
			, function(data){
				dhxWins1 = new dhtmlXWindows();
				dhxWins1.enableAutoViewport(true);
				dhxWins1.attachViewportTo("winVP");
				dhxWins1.setImagePath("dhtmlx/windows/imgs/");
				document.body.scrollTop=0;
				win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
				win1.setModal(true);	
				win1.tI("操作结果");
				var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;删除成功!</div>";
				win1.attachHTMLString(htmlString);
				win1.centerOnScreen();
				doOnGridSelected(thisId);
			});
	}
}

function deleteAllOne2ManyForUpdate(thisEntityUri,thisFieldUri,thisId,seqid){
	var div = eval("ref_selected"+seqid);
	var thatIds = div.jsoDataGrid.getCheckedRecords().ids;
	if(confirm("您确定要删除选中关联信息：\n" + thatIds)==true){
		httpPost("DoO2M.action",
			"thisTableUri=" + thisEntityUri
			+ "&thisFieldUri=" + thisFieldUri 
			+ "&thisId=" + thisId
			+ "&thatId =" + thatIds
			+ "&mode = unlinkAll"
			, function(data){
	 			dhxWins1 = new dhtmlXWindows();
				dhxWins1.enableAutoViewport(true);
				dhxWins1.attachViewportTo("winVP");
				dhxWins1.setImagePath("dhtmlx/windows/imgs/");
				document.body.scrollTop=0;
				win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
				win1.setModal(true);	
				win1.tI("操作结果");
				var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;删除成功!</div>";
				win1.attachHTMLString(htmlString);
				win1.centerOnScreen();
	 			var grid = eval("window.jsoDataGrid" + seqid);	
				grid.refresh();
				grid.refresh();
			});
	}
}

function modifyOne2ManyForUpdate(thisEntityUri,thatId,seqid){
	openModalDialog("~updateBean[" + thisEntityUri + "].vpage?id=" + thatId ,"",top);
	var grid = eval("window.jsoDataGrid" + seqid);	
	grid.refresh();
	grid.refresh();
}

function deleteOne2ManyForTitle(thisEntityUri,thisFieldUri,thisId,thatId,divName,fieldName,thatEntityUri,title,sep){
	if(confirm("您确定要删除选中关联信息:\n" )==true){
		httpPost("DoO2M.action",
			"thisTableUri=" + thisEntityUri
			+ "&thisFieldUri=" + thisFieldUri 
			+ "&thisId=" + thisId
			+ "&thatId =" + thatId
			+ "&mode = unlink"
			, function(data){
	 			dhxWins1 = new dhtmlXWindows();
				dhxWins1.enableAutoViewport(true);
				dhxWins1.attachViewportTo("winVP");
				dhxWins1.setImagePath("dhtmlx/windows/imgs/");
				document.body.scrollTop=0;
				win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
				win1.setModal(true);	
				win1.tI("操作结果");
				var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>&nbsp;删除成功!</div>";
				win1.attachHTMLString(htmlString);
				win1.centerOnScreen();
	 			var div = eval("document.all." + divName);
				loadHTML1("/console/editor/one2manySelectedForTitle.vpage","thisEntityUri=" + escape(thatEntityUri) + "&thisFieldUri=" + escape(thisFieldUri) + "&fieldname=" + escape(fieldName) +"&thatEntityUri=" + escape(thisEntityUri) + "&thisId=" + escape(thisId) + "&title=" + escape(title) + "&div=" + escape(divName)  + "&sep=" + escape(sep),div);
				//location.href="/console/editor/many2manySelectedForTitle.vpage?thisEntityUri=" + thatEntityUri + "&thisFieldUri=" + thisFieldUri + "&fieldname=" + fieldName +"&thatEntityUri=" + thisEntityUri + "&thisId=" + thisId + "&title=" + title + "&div=" + divName;
			});
	}
}

function updateOne2ManyForTitle(thisEntityUri,thisFieldUri,thisId,thatId,divName,fieldName,thatEntityUri,title){
	openModalDialog("~updateBean[" + thisEntityUri + "].vpage?id=" + thatId ,"",top);
}

/////////////////////deprecated///////////////

function JsoCollectionCtrl(jsoForm, jsoField, htmlBinder)
{
	this.newMethod = JsoCtrl;
	this.newMethod(jsoForm, jsoField, htmlBinder);

	var ctrl = this;
	this.collections = new ActiveXObject("Scripting.Dictionary");
	
	
	this.getJsoValue = function()
	{
		return new VBArray(this.collections.Keys()).toArray();
	}
	
	this.add = function(id)
	{
		this.collections.Add(id,id);
	}
	
	this.addIds = function(ids){
		for(var i=0;i<ids.length;i++){
			this.collections.Add(ids[i],ids[i]);
		}
	}
	
	this.remove = function(id)
	{
		this.collections.Remove(id);
	}
	
	this.removeAll = function()
	{
		this.collections = new ActiveXObject("Scripting.Dictionary");
	}
	
	
	this.get = function(id){
		return this.collections.Item(id);
	}
	
}



function JsoFile()
{
}

//锟侥硷拷锟截硷拷
function JsoFilesCtrl(jsoForm, jsoField, htmlBinder, flag, url)
{
	//this.newMethod = JsoCtrl;
	//this.newMethod(jsoForm, jsoField, htmlBinder);

	this.jsoForm = jsoForm;
	this.jsoField = jsoField;
	this.htmlBinder = htmlBinder;
	htmlBinder.jsoCtrl = this;
	this.url = url;
	//this.files = new ActiveXObject("Scripting.Dictionary");
	this.files = new Object();
	this.add = function(dsUri, id, title, filePath, extension, fileSize, isImage, imageWidth, imageHeight)
	{
		var file = new JsoFile();
		file.dsUri = dsUri;
		file.id = id;
		file.title = title;
		file.filePath = filePath;
		file.extension = extension;
		file.fileSize = fileSize;
		file.isImage = isImage;
		file.imageWidth = imageWidth;
		file.imageHeight = imageHeight;
		
		//this.files.Add(file.id, file);
		this.files[file.id] = file;
	}
	
	this.remove = function(id)
	{
		//this.files.Remove(id);
		delete this.files[id];
	}
	
	this.refresh = function()
	{
		var html, li, bytes;
		li = "";
		bytes = 0;
		//var files = (new VBArray(this.files.Items())).toArray();
		var i;
		//for(i = 0; i < files.length; i++)
		//{
		//	var file = files[i];
		//	li += "<li>" + file.title + file.extension + " [" + (file.isImage? "锟竭达拷" + file.imageWidth + "x" + file.imageHeight + " ": "") + file.fileSize + "锟街斤拷] <a title='锟介看' href='/file?uri=" + file.dsUri + "&id=" + file.id + "' target='_blank'><img src='/console/shared/images/view.gif' border=0 align='absbottom'></a> <a title='删锟斤拷' href='#' onclick=\"btnDeleteFile(this.jsoCtrl, '" + file.id + "');\"><img src='/console/shared/images/unlink.gif' border=0 align='absbottom'></a></li>";
		//	li += "\r\n";
		//	bytes += file.fileSize;
		//}
		i=0;
		for(var k in this.files){
			var file = this.files[k];
			li += "<li>" + file.title + file.extension + " [" + (file.isImage? "文件" + file.imageWidth + "x" + file.imageHeight + " ": "") + file.fileSize + "字节 <button type='button' title='查看' onclick=\"window.open('" + file.filePath + "?type=" + flag + "')\" target='_blank'><img src='/console/shared/images/view.gif' border=0 align='absbottom'></button> <button type='button' title='删除' onclick=\"btnDeleteFile(this.jsoCtrl, '" + file.id + "');\"><img src='/console/shared/images/unlink.gif' border=0 align='absbottom'></button></li>";
			li += "\r\n";
			bytes += file.fileSize;
			i++;
		}
		
		var click_txt = "";
		if(flag=="localfiles")
		{
			click_txt = "&nbsp;<button type='button' onclick=\"btnAddFile('"+this.url+"',this.jsoCtrl);\"><img src='/console/shared/images/asc.gif' border=0 align=absbottom></button>"
		}else if(flag=="files")
		{
			click_txt = "&nbsp;<button type='button' onclick='btnAddFile1(this.jsoCtrl);'><img src='/console/shared/images/asc.gif' border=0 align=absbottom></button>"
		}
		//html = "<span style='border-style: inset; border-width: 1px;padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px' style='width:90%;height:21'>锟侥硷拷锟斤拷目:" + this.files.count + " 锟街斤拷锟斤拷:" + bytes + "</span><button onclick='btnAddFile(this.jsoCtrl);'><img src='/console/shared/images/drop.gif' border=0 align=absbottom></button>";
		html = "<span style='border-style: inset; border-width: 1px;padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px' style='width:90%;height:21'>文件数量：" + i + " 文件大小：" + bytes + "</span>"+click_txt;
		
		if(bytes > 0)
			html += "<br><br>";
			
		html += li;
		var span = this.htmlBinder;
		span.innerHTML = html;
		var jsoCtrl = this;
		$('button', span).each(function(i){this.jsoCtrl = jsoCtrl;});
		//$('button', span).each(function(i){this.jsoCtrl = jsoCtrl;});
	}
}

function btnAddFile(url,filesCtrl1)
	{
		filesCtrl = filesCtrl1;

		w1 = dhxWins.createWindow("w1", 0, 0, 450, 550);
	    w1.tI("本地文件");
	    w1.button("minmax1").hide();
	    w1.attachURL("/console/editor/jquery/addLocalFile.jsp?dsid="+url);
   		w1.centerOnScreen();
		w1.denyResize();
	}

function btnAddFile1(filesCtrl1)
{
	filesCtrl = filesCtrl1;
	upload();
	//showModalDialog("/console/editor/addFile.jsp?tid=" + filesCtrl.jsoForm.tableId + "&fid=" + filesCtrl.jsoField.id + "&fname=" + filesCtrl.jsoField.name + "&ftitle=" + filesCtrl.jsoField.title, new Array(window, filesCtrl), "dialogWidth:520px;dialogHeight:200px;help:no;scroll:no;status:no");
}
var dhxWins;
function onLoad()
{
	dhxWins = new dhtmlXWindows();
    dhxWins.enableAutoViewport(true);
    //dhxWins.setViewport(0, 0, 900, 700);
    dhxWins.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
}

function upload()
{
	vault = new dhtmlXVaultObject();
    vault.setImagePath("/console/shared/plugins/dhtmlx/vault/imgs/");
    vault.setServerHandlers("uploadHandler.vpage?entity="+eurl, "getInfoHandler.vpage", "getIdHandler.vpage");
    vault.onUploadComplete = function(files) {
    	dhxWins.window("w1").close();
    };
    vault.strings.remove = "删除";
  	vault.strings.done = "完成";
   	vault.strings.error = "错误";

    vault.strings.btnAdd = "添加";
    vault.strings.btnUpload = "上传";
    vault.strings.btnClean = "清空";
          
    var oDiv=document.createElement("div")   
	oDiv.id="divVault"   
	document.body.appendChild(oDiv) 
    vault.create("divVault");

	w1 = dhxWins.createWindow("w1", 400, 500, 450, 285);
	w1.setModal(true);
    w1.tI("上传文件");
    w1.attachObject('divVault');
    w1.button("minmax1").hide();
    w1.center();
    w1.denyResize();
    scrollTo(0,0);
}

function JsoField(id, name, title, hint)
{
	this.id = id;
	this.name = name;
	this.title = title;
	this.hint = hint;
}

function dofile(ds,fid,title,path,ext,size,image,width,height)
{
	filesCtrl.add(ds,fid,title,path,ext,size,image,width,height);
	filesCtrl.refresh();
}

function btnDeleteFile(filesCtrl, id)
{
	httpPost("/console/editor/doDeleteFile.action", "id=" + id, function(data)
		{
			filesCtrl.remove(id);
			filesCtrl.refresh();
		});
}

function JsoRichTextCtrl(jsoForm, jsoField, htmlBinder,htmlFrame)
{
	
	this.newMethod = JsoCtrl;
	this.newMethod(jsoForm, jsoField, htmlBinder);

	var ctrl = this;
	
	this.getJsoValue = function()
	{
		return getEditorHTMLContents(htmlFrame);
	}
	
	this.jsValidate = function()
	{
		
	}
}