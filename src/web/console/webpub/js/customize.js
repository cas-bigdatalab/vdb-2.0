var dhxWins;
var dhxWins1;
function onLoad()
{
	dhxWins = new dhtmlXWindows();
    dhxWins.enableAutoViewport(true);
    dhxWins.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
    initDrag("#column1, #column2, #column3");
    
    var menu = document.getElementById("one1");
	menu.className = "hover";
}

function Trim(str)
{
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.LTrim = function()
{
	return this.replace(/(^\s*)/g, "");
}

String.prototype.RTrim = function()
{
	return this.replace(/(\s*$)/g, "");
} 

function chgLayout(layout) {
	layoutSel = layout;
	var url = "changeLayout.vpage?layout=" + layout
			+ "&tmp=" + (new Date().getTime().toString(36));
	//setAllChkFalse();
	dhtmlxAjax.get(url, reLayout);
}

function changcss(name)
{
	$("#theme").val(name);
	document.getElementById("cssfile").href = "/console/webpub/js/themecss.vsp?themeName=" + name;
}

function reLayout(loader)
{
	var incomingCols = loader.ai.responseText.split(";");
	var outCols = 0;
	var s = document.getElementById("wrap").getElementsByTagName('div');
	for ( var i = 0; i < s.length; i++) 
	{
		if(s[i].id != null && s[i].id.indexOf("column")>=0)
		{
			outCols++;
		}
	}
	if(incomingCols.length == outCols+1) //布局变换后，列数不变的情况
	{
		if(document.getElementById("column1")!=null)
			document.getElementById("column1").style.width = Trim(incomingCols[0])+"px";
		if(document.getElementById("column2")!=null)
			document.getElementById("column2").style.width = Trim(incomingCols[1])+"px";
		if(document.getElementById("column3")!=null)
			document.getElementById("column3").style.width = Trim(incomingCols[2])+"px";
	}
	
	if(incomingCols.length==3 && outCols == 3)
	{
		if(!confirm('第三列内容会被删除，是否继续？'))
		{
			return;
		}
		
		var col3Children = document.getElementById("column3").childNodes;
		for(var i=0; i<col3Children.length; i++)
		{
			if(col3Children[i].id != null && 
					document.getElementById('select_' + col3Children[i].id) != null)
				document.getElementById('select_' + col3Children[i].id).checked = false;
		}
		
		document.getElementById("wrap").removeChild(document.getElementById("column3"));
		document.getElementById("column1").style.width = incomingCols[0]+"px";
		document.getElementById("column2").style.width = incomingCols[1]+"px";
	}
	if(incomingCols.length > outCols + 1)//Trim(incomingCols[2]) == '0')
	{
		document.getElementById("column1").style.width = incomingCols[0]+"px";
		var col2 = document.getElementById("column2");
		var col3 = document.getElementById("column3");
		
		var newNodeText = "";
		
		if(col2 != null)
		{
			col2.style.width = incomingCols[1]+"px";
		}else if(incomingCols.length >= 2)
		{
			newNodeText += "<div id='column2' style='width:"
				+ incomingCols[1] +"px;margin-right:"+(incomingCols.length===2?0:5)+"px;float:left; min-height:100px;'></div>";
		}
		if(col3 != null)
		{
			col3.style.width = incomingCols[2]+"px";
		}else if(incomingCols.length >= 3)
		{
			newNodeText += "<div id='column3' style='width:"
				+ incomingCols[2] +"px;margin-right:0px;float:left; min-height:100px;'></div>";
		}
	
		var txt = document.getElementById("wrap").innerHTML;
		txt = txt.replace(/jQuery\d{13}=\"\d{1,10}\"/g, "");
		document.getElementById('wrap').innerHTML = txt + newNodeText;
	}
	
	if(incomingCols.length==2 && outCols == 3)
	{
		if(!confirm('第二,三列内容会被删除，是否继续？'))
		{
			return;
		}
		
		var col3Children = document.getElementById("column3").childNodes;
		for(var i=0; i<col3Children.length; i++)
		{
			if(col3Children[i].id != null && 
					document.getElementById('select_' + col3Children[i].id) != null)
				document.getElementById('select_' + col3Children[i].id).checked = false;
		}
		document.getElementById("wrap").removeChild(document.getElementById("column3"));
		
		var col2Children = document.getElementById("column2").childNodes;
		for(var i=0; i<col2Children.length; i++)
		{
			if(col2Children[i].id != null && 
					document.getElementById('select_' + col2Children[i].id) != null)
				document.getElementById('select_' + col2Children[i].id).checked = false;
		}
		document.getElementById("wrap").removeChild(document.getElementById("column2"));
		
		document.getElementById("column1").style.width = incomingCols[0]+"px";
		//document.getElementById("column2").style.width = incomingCols[1]+"px";
	}
	
	if(incomingCols.length==2 && outCols == 2)
	{
		if(!confirm('第二列内容会被删除，是否继续？'))
		{
			return;
		}

		var col2Children = document.getElementById("column2").childNodes;
		for(var i=0; i<col2Children.length; i++)
		{
			if(col2Children[i].id != null && 
					document.getElementById('select_' + col2Children[i].id) != null)
				document.getElementById('select_' + col2Children[i].id).checked = false;
		}
		document.getElementById("wrap").removeChild(document.getElementById("column2"));
		
		document.getElementById("column1").style.width = incomingCols[0]+"px";
		//document.getElementById("column2").style.width = incomingCols[1]+"px";
	}
	
	initDrag("#column1, #column2, #column3");
}

function setTab(name, cursel, n) {
	for (i = 1; i <= n; i++) {
		var menu = document.getElementById(name + i);
		var con = document.getElementById("con_" + name + "_" + i);
		menu.className = i == cursel ? "hover" : "";
		con.style.display = i == cursel ? "block" : "none";
	}
}

function layoutWidget(widgetid,layout) {
	dhtmlxAjax.get("renderWidgetBody.vpage?widget=" + widgetid+"&layout="+layout,
	function(loader) {
		setInnerHTML(document.getElementById(widgetid), loader.ai.responseText);
		initDrag("#column1, #column2, #column3");
	});
}

function loadColumn(widgetid, id, chk,layout) {
	if (!chk.checked) {
		removeWidget(document.getElementById(widgetid));
		return false;
	}

	var col1 = document.getElementById("column1");
	if (col1 != null) {
		var oDiv = document.createElement("dl");
		oDiv.id = widgetid;
		oDiv.className = "baseball";
		col1.appendChild(oDiv);
		layoutWidget(widgetid,layout);
	}
}

function saveProfile3()
{
	setAllChkFalse();
}

function removeWidget(_element) {
	var _parentElement = _element.parentNode;
	if (_parentElement) {
		document.getElementById('select_' + _element.id).checked = false;
		_parentElement.removeChild(_element);
	}

}

function saveProfile() {
	var theme = $("#theme").val();
	var param = layoutSel + ";";
	param += theme + ";";
	var s = document.getElementById("wrap").getElementsByTagName('div');
	var widgetTxt = "";
	for ( var i = 0; i < s.length; i++) {
		if(s[i].id != null && s[i].id.indexOf("column")>=0)
		{
			if (widgetTxt != "")
				widgetTxt += ";";
			widgetTxt += s[i].id + ":";
			var widget = s[i].getElementsByTagName('dl');
	
			for ( var j = 0; j < widget.length; j++) {
				if (widget[j] != null && widget[j].id.length > 0) {
					if (j != 0)
						widgetTxt += ",";
					widgetTxt += widget[j].id;
				}
			}
		}
	}
	param += widgetTxt;
	var url = "doSaveLayout.vpage?theme=" + theme + "&param=" + param + "&page=" + widgetLayout
			+ "&tmp=" + (new Date().getTime().toString(36));
	dhtmlxAjax.get(url, function(loader) {
	
	dhxWins1 = new dhtmlXWindows();

	dhxWins1.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
	win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
	win1.centerOnScreen();
	win1.setModal(true);
	win1.tI("保存结果");
	var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt;text-align:center;' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 style='padding-bottom:-30px;' />"+loader.ai.responseText+"！<br><br><br><button type='button' onclick=dhxWins1.window('win1').close();><img src='/console/shared/images/gb.gif' border=0 align=absbottom>关闭</button></div>";
	win1.attachHTMLString(htmlString);
	});

}

function setAllChkFalse()
{
	var chkList = document.getElementById('thisform').getElementsByTagName('input');
	for(var i=0;i<chkList.length; i++)
	{
		chkList[i].checked = false;
	}
}


function updateWidget(id,height)
{
	// ,event.pageY || (event.clientY + (document.documentElement.scrollTop || document.body.scrollTop))
	var dhxWindow = dhxWins.createWindow("w1", (window.screen.width-220-720)/2,  parseInt(height-parseInt(document.body.clientHeight)/2), 720, 450);
    dhxWindow.setModal(false);
    dhxWindow.tI("组件修改");
	dhxWindow.attachURL("doUpdateWidget.vpage?reload=true&widgetlib=" + id);
	dhxWindow.centerOnScreen();
}

function initDrag(cols)
{
	try
	{
		$.baseball({
			accepter:cols,
			target:".baseball",	
			handle:".basebat"
		});
	}catch(e)
	{
	}
}

function changeLiClass(li,id,count)
{
	for(var i = 0;i<count;i++)
	{
		$("#"+li+i).removeClass("selected"); 
	}

	$("#"+li+id).addClass("selected"); 
	return false;
	
}
function openSet()
{
	document.getElementById("one1").style.display = "";
	document.getElementById("one2").style.display = "";
	document.getElementById("one3").style.display = "";
	document.getElementById("Contentbox").style.display = "";
	document.getElementById("closeSetBotton").style.display = "";
	document.getElementById("openSetBotton").style.display = "none";
	document.getElementById("pageTop").style.display = "none";
	document.getElementById("pageBottom").style.display = "none";
	
}

function closeSet()
{
	document.getElementById("one1").style.display = "none";
	document.getElementById("one2").style.display = "none";
	document.getElementById("one3").style.display = "none";
	document.getElementById("Contentbox").style.display = "none";
	document.getElementById("closeSetBotton").style.display = "none";
	document.getElementById("openSetBotton").style.display = "";
	document.getElementById("pageTop").style.display = "";
	document.getElementById("pageBottom").style.display = "";
}

function upload(type)
{
	dhxWins = new dhtmlXWindows();
	dhxWins.enableAutoViewport(true);
	vault = new dhtmlXVaultObject();
    vault.setImagePath("/console/shared/plugins/dhtmlx/vault/imgs/");
    vault.setServerHandlers("uploadHandler.vpage?pictype="+type, "getInfoHandler.vpage", "getIdHandler.vpage");
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
	oDiv.style.display="none";
	w1 = dhxWins.createWindow("w1", 400, 500, 450, 285);
	w1.attachObject('divVault');
	w1.center();
	w1.setModal(true);
    w1.tI("上传文件");
    
    w1.button("minmax1").hide();
 }
 
 function addPic(type,path)
{
	$("#imgUL").html($("#imgUL").html() + "<LI class=''><P><IMG src='/userfiles/" + type + "/"+ path + "'></P>");

	$('#next').removeClass('gray');
	
	pList = $('#slidePic ul li');
	pList.each(function(n,v){
			$(this).click(function(){
			$('#slidePic ul li.cur').removeClass('cur');
			$(this).addClass('cur');
			show(n);
			}).mouseover(function(){
			$(this).addClass('hover');
			}).mouseout(function(){
			$(this).removeClass('hover');
			})
		});
}


function show(i){
		pList = $('#slidePic ul li');
		var ad = pList[i];
		$('#dailyImage').attr('src',$('img', ad).attr("src"));
	}