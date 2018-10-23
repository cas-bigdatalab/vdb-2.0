function showUpdateResult(id) {
	var dhxWins1 = new dhtmlXWindows();
	dhxWins1.enableAutoViewport(true);

	dhxWins1.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
	document.body.scrollTop = 0;
	win1 = dhxWins1.createWindow("win1", 200, 200, 300, 200);
	win1.setModal(true);
	win1.tI("编辑结果");
	win1.button("close").hide();
	var htmlString = "<div align='center' style='height:100%;background-Color:#f7f9fa;font-size:10pt' ><br><br><img src='/console/shared/images/onSuccess.gif' border=0 align=absbottom>修改成功！<br><br><br><button type='button' onclick='successFun(3)'><img src='/console/shared/images/gb.gif' border=0 align=absbottom>关闭页面</button></div>";
	win1.attachHTMLString(htmlString);
}

function updateRecord(uri, id) {
	openDialog("updateBean.vpage?oper=update&uri=" + uri + "&id=" + id);
}
function deleteRecord(tid, id) {
	openModalDialog("delete.jsp?tid=" + tid + "&id=" + id,
			"\u5220\u9664\u8bb0\u5f55", window);
}
function successFun(id) {
	if (id == 1) {
		window.location.href = window.location.href;
	}
	if (id == 3) {
		parent.refreshgrid();
		parent.dhxWindow.close();
	}
}
function successRefFun(datagrid, model, thisEntityUri, thisFieldUri, fieldName,
		thatEntityUri, thisId, title, sep) {
	$("#updateResult").hide();
	document.getElementById("content").style.visibility = "hidden";
	var grid = eval("window.opener." + datagrid);
	if (model == "tableModel") {
		grid.refresh();
		grid.refresh();
		window.close();
	} else {
		if (model == "titleModel") {
			var grid = eval("window.opener." + datagrid);
			httpPost(
					"/console/editor/one2manySelectedForTitle.vpage",
					"thisEntityUri=" + escape(thisEntityUri) + "&thisFieldUri="
							+ escape(thisFieldUri) + "&fieldname="
							+ escape(fieldName) + "&thatEntityUri="
							+ escape(thatEntityUri) + "&thisId="
							+ escape(thisId) + "&title=" + escape(title)
							+ "&div=" + escape(datagrid) + "&sep="
							+ escape(sep),
					function(data) {
						var div = eval("window.opener.document.all." + datagrid);
						div.innerHTML = data;
						window.close();
					});
		}
	}
}
var dhxWins,dhxToolbarChild,childtable;
var curPageChild = 1;
var recordNumChild = 0;
var pageNumChild = 1;
var pageSize = 15;
var txtid = '';
var entityuri = '';
var fieldurl = '';
function updateRef(fieldurl1,textid,entityurl,height) {
    txtid = textid;
    entityuri = entityurl;
    fieldurl = fieldurl1;
	dhxWins = new dhtmlXWindows();
    dhxWins.enableAutoViewport(true);
    dhxWins.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
	var dhxWindow = dhxWins.createWindow("w1",parseInt((parseInt(document.body.clientWidth*0.3))/2), parseInt(height-parseInt(document.body.clientHeight*0.7)/2),parseInt(document.body.clientWidth*0.7),parseInt(document.body.clientHeight*0.7));
	dhxWindow.setModal(false);
	dhxWindow.tI("从表添加");
	dhxWindow.button("minmax1").hide();
    dhxWins.window("w1").button("close").attachEvent("onClick", function(){
     	dhxWins.window("w1").close();
     	dhxWins.window("w1").hide();
    });
	// dhxWindow.hide();
	// dhxWindow.attachURL("htmlEditor/htmlEditor.htm?uploadgroup=test&bindingfield=${field.name}&layout=advanced&parameters=${field.entity.id}");

	dhxToolbarChild = dhxWindow.attachToolbar();
	dhxToolbarChild
			.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
	dhxToolbarChild
			.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/dhxtoolbar_page_child.xml");
	dhxToolbarChild.attachEvent("onClick", doPageChild);
	
	dhxToolbarChild.attachEvent("onEnter", function(id, value) {
	        //alert("<b>onEnter event</b> input(" + id + ")'s value was changed to " + value);
	        if(value == '')
	        {
	        	alert("输入值不能为空");
	        	return;
	        }
	        if(value.search("^-?\\d+$")!=0)
	        {
	        	alert("请输入一个整数");
	        	return;
	        }
			if(parseInt(value) < 1)
	        {
	        	alert("输入值超出了最小范围");
	        	return;
	        	curPageChild = 1;
	        }
	        else if(parseInt(value) > pageNumChild)
	        {
	        	alert("输入值超出了最大范围");
	        	return;
	        	curPageChild = pageNumChild;
	        }
	        else 
	        	curPageChild = parseInt(value);
	        dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);	
	});
	
	childtable = dhxWindow.attachGrid();
	childtable.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	childtable.setSkin("dhx_skyblue");
	
	childtable.setEditable(false);
	childtable.init();

	//fieldurl = mid[1];
	dhtmlxAjax.get("/console/editor/getTableChildData.vpage?field=" + fieldurl1 + "&id=&start="
			+ 0 + "&size=" + pageSize, outputResponseChild);
	//dhxWindow.centerOnScreen();
}


function outputResponseChild(loader) {

   	childtable.clearAll(true);
    var str = loader.ai.responseText;
    str = Trim(str);
    
    if(str.indexOf("j_spring_security_check")>=0)
    {
    	document.location.href = document.location.href;
    	return;
    }
    
    var strArr = str.split("@CNIC@");
    childtable.parse(strArr[0]);
    //mygrid.dX(0);
    recordNumChild = strArr[1];
    pageNumChild = strArr[2];
	if(pageNumChild=='0')
		pageNumChild = 1 ;
    	//alert("ddd");
    //myDataProcessor = new Iq("/DoUpdateBean?entity="+entityurl);
	//myDataProcessor.JI("GET",true);
	//myDataProcessor.mq("off");
	//myDataProcessor.init(mygrid);
	
	//alert(pageNum);
	initPageBarChild();
	//dhxToolbarPage.setItemText("text3", "第"+curPage+"页 共"+pageNum+"页 "+"共<B>"+recordNum+"</B>条记录" );
	dhxToolbarChild.setItemText("text3", "第"+curPageChild+"页 共"+pageNumChild+"页 "+"共<B>"+recordNumChild+"</B>条记录" );
}

function initPageBarChild()
{
	//alert(pageNum);
	//alert(curPageChild);
	if(curPageChild<pageNumChild||curPageChild>1)
   	{
   		dhxToolbarChild.enableItem("prev");
   		dhxToolbarChild.enableItem("first");
   		dhxToolbarChild.enableItem("last");
   		dhxToolbarChild.enableItem("next");
   	}	
	
   	if(curPageChild==1)
   	{
   		dhxToolbarChild.disableItem("prev");
   		dhxToolbarChild.disableItem("first");
   	}
	
	if(curPageChild==pageNumChild)
   	{
   		dhxToolbarChild.disableItem("last");
   		dhxToolbarChild.disableItem("next");
   	}	
}
function doPageChild(id) 
{
    if(id == "prev")
    {
    	curPageChild--;
    	if(curPageChild==1)
    	{
    		dhxToolbarChild.disableItem("prev");
    		dhxToolbarChild.disableItem("first");
    	}
    	
    	if(curPageChild!=pageNumChild)
    	{
    		dhxToolbarChild.enableItem("last");
    		dhxToolbarChild.enableItem("next");
    	}
    	dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);	
    	//sendRequestGet(entityurl);
    }
    
    if(id == "next")
    {
    	curPageChild++;
    	if(curPageChild>=pageNumChild)
    	{
    		dhxToolbarChild.disableItem("last");
    		dhxToolbarChild.disableItem("next");
    	}
    	
    	if(curPageChild!=1)
    	{
    		dhxToolbarChild.enableItem("prev");
    		dhxToolbarChild.enableItem("first");
    	}
		dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
    }
    
    if(id == "first")
    {
    	curPageChild=1;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
    	dhxToolbarChild.disableItem("prev");
    	dhxToolbarChild.disableItem("first");
    	if(curPageChild!=pageNumChild)
    	{
    		dhxToolbarChild.enableItem("last");
    		dhxToolbarChild.enableItem("next");
    	}

    }
    
    if(id == "last")
    {
    	curPageChild=pageNumChild;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
    	dhxToolbarChild.disableItem("last");
    	dhxToolbarChild.disableItem("next");
    	if(curPageChild!=1)
    	{
    		dhxToolbarChild.enableItem("prev");
    		dhxToolbarChild.enableItem("first");
    	}
    }
    
    if(id == "ok")
    {
    	//modifyOne2ManyForUpdate(entityurl,mygrid.bI().split("#")[1],childtable.bI());
    	if(childtable.bI()==null)
    	{
			alert("还没有选择从表信息，请选择一条或多条记录");  
			return true;  	
    	}
    	document.getElementById(txtid).value = entityuri + "/" + childtable.bI();
    	//document.getElementById("sp_"+txtid).innerHTML = childtable.bI();
    	dhtmlxAjax.get("getReferenceTitle.vpage?field="+fieldurl+"&key="+childtable.bI(), outputResult);
    	dhxWins.window("w1").close();

    }
	if(id == "cancel")
    {	
    	dhxWins.window("w1").close();
    }
}

function outputResult(loader) {

    var str = loader.ai.responseText;
    str = Trim(str);
    
    if(str.indexOf("j_spring_security_check")>=0)
    {
    	document.location.href = document.location.href;
    	return;
    }
    document.getElementById("sp_"+txtid).innerHTML = str;
}