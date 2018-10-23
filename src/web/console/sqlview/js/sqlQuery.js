//alert("sdfsdf");
var dhxLayout,dhxLayout2UcellsB2E,dhxToolbar,mygrid,statusBar,dhxAccord,dhxToolbarPage,menu,myDataProcessor,dhxTabbar;
var dhxWins,vault,mygridattach,dhxLayout_attach,activetable,dhxTabbar_attach,myDataProcessor_attach,dhxToolbarChild,childtable;
var menuattach, dhxToolbarAttachPage,layout_attach;
var entityurl = "";
var fieldurl = "";
var queryFormWhereFilter = "";
var whereFilter="";
var treeid = "";
//第几页
var curPage = 1;
var pageSize = 15;
var recordNum = 0;
//共几页
var pageNum = 1;

var curPageChild = 1;
var recordNumChild = 0;
var pageNumChild = 1;

var curPageAttach = 1;
var recordNumAttach = 0;
var pageNumAttach = 1;

var colName = "";
var cacheline = 0;
var jsoQuery = null;

//以下是几个高度全局变量
var topHeight;
var footTopHeight;
var footHeight;
var treeTitleHeight;
var positionHeight;
var datasetSelectHeight;

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

window.onresize = function(){ 
		//重新调整2U的高度和宽度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	dhxLayout.setSizes(); 
	
	//重新调整2E的高度和宽度
	document.getElementById("contentMain").style.height=dhxLayout.cells('b').getHeight()-positionHeight;
	dhxLayout2UcellsB2E.setSizes();
	
	//重新调整左侧树DIV的高度
	document.getElementById("tree").style.height = dhxLayout.cells('a').getHeight()-treeTitleHeight-datasetSelectHeight-70;
} 

function doOnLoad() {
	//得到头DIV和脚部DIV的高度
	topHeight = document.getElementById("topDiv").clientHeight;
	footTopHeight = document.getElementById("footTopDiv").clientHeight;
	footHeight = document.getElementById("footDiv").clientHeight;
	//设置主体DIV的高度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	dhxLayout = new dhtmlXLayoutObject("parentId", "2U");
	dhxLayout.cells("a").setWidth("300");
	dhxLayout.cells("a").hideHeader();
	
	//左侧导航树
	treeTitleHeight = document.getElementById("treeTitle").clientHeight;
	datasetSelectHeight = document.getElementById("slt").clientHeight;
	document.getElementById("tree").style.height = dhxLayout.cells('a').getHeight()-treeTitleHeight-datasetSelectHeight-70;
	
    //dhxLayout = new dhtmlXLayoutObject("parentId", "2U");
     
    //dhxGrid = dhxLayout.cells("a").attachGrid();
    //dhxLayout.cells("a").attachObject("menu");
    dhxLayout.cells('a').attachObject('treeDiv');
    dhxLayout.cells("a").setWidth("220"); 
    dhxLayout.cells("b").setHeight("178");
    //dhxLayout.cells("b").hideHeader(); 
    //dhxLayout.cells("b").tI("SQL语句");
    dhxLayout.cells('b').attachObject('sqlContent');
    dhxLayout.cells('b').hideHeader();
    //dhxLayout.cells("c").tI("查询结果");
    //xcch
    //dhxLayoutLeft = new dhtmlXLayoutObject(dhxLayout.cells("a"), "2E");
    //dhxLayoutLeft.cells("a").attachObject("slt");
	//dhxLayoutLeft.cells("a").setHeight("50");
	//dhxLayoutLeft.cells("a").tI("SQL View  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href='/console/shared/logout.jsp'>注销</a>");
	//dhxLayoutLeft.cells("a").fixSize(false, true);
    dhxTree = new C("tree","100%","100%",0);//dhxLayoutLeft.cells("b").attachTree();
    //dhxLayoutLeft.cells("b").hideHeader();
	positionHeight = document.getElementById("position").clientHeight;
	document.getElementById("contentMain").style.height=dhxLayout.cells('b').getHeight()-positionHeight;
	
    dhxLayout2UcellsB2E = new dhtmlXLayoutObject("contentMain", "2E");
    dhxLayout2UcellsB2E.cells("a").hideHeader();
	dhxLayout2UcellsB2E.cells("a").setHeight("153");
	dhxLayout2UcellsB2E.cells("b").hideHeader();
	dhxLayout2UcellsB2E.cells("a").fixSize(false, true);
    dhxToolbar = dhxLayout2UcellsB2E.cells("a").attachToolbar();
    dhxToolbar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
    dhxToolbar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/sqlview.xml");
	
    dhxToolbarPage = dhxLayout2UcellsB2E.cells("b").attachToolbar();
    dhxToolbarPage.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
    dhxToolbarPage.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/sqlview1.xml");
    dhxToolbarPage.attachEvent("onEnter", function(id, value) {
        //alert("<b>onEnter event</b> input(" + id + ")'s value was changed to " + value);
        if(parseInt(value)<1)
        	curPage = 1;
        else if(parseInt(value)>pageNum)
        	curPage = pageNum;
        else 
        	curPage = parseInt(value);
        sendRequestGet(entityurl);
    });
    dhxToolbarPage.attachEvent("onClick", doPage);
    dhxLayout2UcellsB2E.cells("b").progressOn();
	//initPageBar();
     
    dhxTree.setImagePath("/console/shared/plugins/dhtmlx/tree/imgs/csh_vista/");
    //dhxTree.setOnClickHandler(tondblclick);
    dhxTree.ck(tondblclick);
    dhtmlxAjax.get("_select.vpage", selecthandle);
//  dhxTree.loadXML("/console/sqlview/test.xml");
   
    mygrid = dhxLayout2UcellsB2E.cells("b").attachGrid();
    mygrid.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	mygrid.setSkin("dhx_skyblue");

	mygrid.enableColumnMove(true);
	mygrid.setDateFormat("%Y-%m-%d");

	mygrid.init();

    dhxLayout2UcellsB2E.cells("a").attachObject("text");

    mygrid.setEditable(false);
 
    dhxToolbar.attachEvent("onClick", function(id) {
	    if(id == "run")
	    {
			dhxLayout2UcellsB2E.cells("b").progressOff();
	    	whereFilter = document.getElementById("sqlstr").value;		
			
			if(whereFilter.match(/select.*from/i))
			{
				curPage = 1;
				dhxToolbarPage.setValue("input1", curPage);
				dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage+"&size="+pageSize+"&whereFilter="+whereFilter, outputResponse);
			}
			else
			{
				alert("SQL syntax error!");
			}
	    }
    });
    
    dhxToolbarPage.attachEvent("onClick", function(id) {
	    if(id == "export")
	    {
	    	var sql = document.getElementById("sqlstr").value;
	    	var start = curPage*pageSize;
	    	window.open("/doDownloadSql.do?entity="+entityurl+"&start="+start+"&size="+pageSize+"&whereFilter="+sql);
	    }
    });
   
    setTimeout("dhxTree.Jo(0);", 10);
	dhxWins = new dhtmlXWindows();
    dhxWins.enableAutoViewport(true);
    dhxWins.attachViewportTo("winVP");
    dhxWins.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
    
}
//call everytime 'select' changing
function treehandle(loader)
{
	var text = Trim(loader.ai.responseText);
	//clear the tree
	dhxTree.fa(0);
	dhxTree.loadXMLString(text);
	//
	var opt = $("#select_ds").find("option:selected");
	entityurl = opt.attr("uri");	
	//
	curPage = 1;
	//dhxToolbarPage.setValue("input1", curPage);
}
//call once , when loading page
function selecthandle(loader)
{
	var text = loader.ai.responseText;
	$(text).find("option").each
	(
		function()
		{
			var value = $(this).attr("value");
			var uri = $(this).attr("uri");
			var text = $(this).text();
			$("#select_ds").append("<option value=" +value+ " uri=" +uri+ ">" +text+ "</option>");
		}
	);
	
	//initial
	var opt = $("#select_ds option:first-child");
	dhtmlxAjax.get("_tree.vpage?dsid="+ opt.attr("value"), treehandle);
}
//xcch///
function onDsChange()
{
	var opt = $("#select_ds").find("option:selected");
	dhtmlxAjax.get("_tree.vpage?dsid="+opt.attr("value"), treehandle);
}

function doOnGridSelected(id) {

	//alert((new String(id)).indexOf("#"));
	
	//if((new String(id)).indexOf("#")<0)
	//	return true;
	//var rId = mygrid.getRowIndex(id);
	//mygrid.eQ(rId+3,3);
	entityurl = treeid;
	doOnTabbarSelect(dhxTabbar_attach.wX());
	switchActiveGrid(mygrid,'false');
	return true;
}

function doOnAttachGridSelected(id) {
	//alert(id);
	//doOnTabbarSelect(dhxTabbar_attach.wX());
	//var selid = dhxTabbar_attach.wX().split("_"); 
	//entityurl = selid;
	//switchActiveGrid(mygridattach,'true');
	return true;
}

function doOnTabbarSelect(id) {
	
	
	if(id!=null && mygrid.bI() != null)
	{
		//alert(dhxTabbar_attach.wX());
		var selid = arguments[0];//.split("_");
		//if(dhxToolbarAttachPage!=null)
		//	dhxToolbarAttachPage.unload();
		
		var oDiv=document.createElement("div")   
		oDiv.id="divToolAttach"   
	    document.body.appendChild(oDiv) 
		
		//dhxToolbarAttachPage = new bb("divToolAttach");
		//dhxTabbar_attach.cells(selid).attachObject("divToolAttach");

	  	mygridattach = dhxTabbar_attach.cells(selid).attachGrid();
	    mygridattach.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	
		mygridattach.setSkin("dhx_skyblue");
	
		mygridattach.enableColumnMove(true);
		mygridattach.init();
		mygridattach.enableContextMenu(menuattach);
		mygridattach.setEditable(false);
	    //mygridattach.loadXML("/console/shared/plugins/dhtmlx/grid/grid.xml");
	    //alert(mygrid.bI().split("#")[1]);
	    //mygridattach.loadXML("getTableChildData.jsp?field="+selid+"&id="+mygrid.bI()+"&start="+0+"&size="+pageSize);
	    dhtmlxAjax.get("getTableChildData.jsp?field="+selid+"&id="+mygrid.bI()+"&start="+0+"&size="+pageSize, outputResponseAttach);
	    mygridattach.enableUndoRedo();
	    mygridattach.attachEvent("onRowSelect", doOnAttachGridSelected);
	    //activeChildGrid = mygridattach;
	    //myDataProcessor_attach = new Iq("/DoUpdateBean?entity="+selid+"&attach=true");
		//myDataProcessor_attach.JI("GET",true);
		//myDataProcessor_attach.mq("off");
		//myDataProcessor_attach.init(mygridattach);

	
		dhxToolbarPage.setItemText("text3", "第"+curPage+"-"+pageNum+"页 "+"共<B>"+recordNum+"</B>条记录" );
	}
	//activetable = mygridattach;
   		//return false;
    return true;
}

function switchActiveGrid(grid,attach)
{
	activetable = grid;
	//alert(entityurl);
	//myDataProcessor = new Iq("/DoUpdateBean?entity="+entityurl+"&attach="+attach);
	//myDataProcessor.JI("GET",true);
	//myDataProcessor.mq("off");
	//myDataProcessor.init(grid);
}

function onMenu(menuitemId, type) {
	
	
	var tmp = activetable.contextID.split("#");
    var data = tmp[1].split("_");
    var rId = tmp[0]+"#"+data[0];
    var cInd = data[1];
	var rowindex = activetable.getRowIndex(rId);
	//mygrid.dX(mygrid.bB(1));
	//mygrid.moveRow(mygrid.bI(),"up");
	//activetable.dX(rId);
	//activetable.Th(rowindex);
    if(menuitemId=="colHide")
    {
    	activetable.setColumnHidden(cInd,true);
    	//var colIndex=mygrid.getColIndexById('1');
    	var colLabel=mygrid.adE(cInd);
    	//alert(colLabel);
    	menu.addNewChild("colShow", 0, "show_"+cInd, colLabel, false, "item.gif");
    }
    
    if(menuitemId.indexOf("show_")>=0)
    {
    	if(menuitemId=='show_allitem')
    	{
    		menu.forEachItem(function(id) {
		        if (menu.getItemType(id) != "separator" && menu._getItemLevelType(id) != "TopLevel")
		        {
		        	if(id.indexOf("show_")>=0 && id != 'show_allitem')
		        	{
		        		activetable.setColumnHidden(id.replace("show_",""),false);
    					menu.removeItem(id);
		        	}
		        }
		    });
		    
		    return false;
    		
    	}
    	activetable.setColumnHidden(menuitemId.replace("show_",""),false);
    	menu.removeItem(menuitemId);
    }
	//alert(dhxTabbar_attach.wX());
    if(menuitemId.indexOf("attachtable@CNIC#@")>=0)
    {
    	//activetable.setColumnHidden(menuitemId.replace("show_",""),false);
    	//menu.removeItem(menuitemId);
    	
    	var mid = menuitemId.split("@CNIC#@");
    	if(mid[2]=="O2M")
    	{
    		w1 = dhxWins.createWindow("w1", 0, 0, 800, 600);
		    w1.setModal(true);
		    w1.tI("数据编辑");
		    w1.button("minmax1").hide();
		    //w1.hide(); updateBean.vpage?oper=update&uri=" + uri + "&id=" + id);
			
		    //w1.attachURL("updateBean.vpage?oper=insert&uri=" + mid[1] + "&rid="+rId.split("#")[1]);
		    w1.attachURL("updateBean.vpage?oper=insert&uri=" + mid[1] + "&rid="+rId.replace("#","/"));
		    w1.centerOnScreen();
    	}
    	else
    	{
    		//if(mid[2]=="Ref")
    			//return false;
	    	w1 = dhxWins.createWindow("w1", 0, 0, 800, 600);
			    w1.setModal(true);
		    w1.tI("从表添加");
		    w1.button("minmax1").hide();
		    //w1.hide();
		    //w1.attachURL("htmlEditor/htmlEditor.htm?uploadgroup=test&bindingfield=${field.name}&layout=advanced&parameters=${field.entity.id}");
	   		
	   		dhxToolbarChild = w1.attachToolbar();
	    	dhxToolbarChild.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
	    	dhxToolbarChild.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/dhxtoolbar_page_child.xml");
	   		dhxToolbarChild.attachEvent("onClick", doPageChild);
	   		childtable = w1.attachGrid();
	   		childtable.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
			childtable.setSkin("dhx_skyblue");
			if(mid[2]=="M2M")
			{
				childtable.enableMultiselect(true);
			}
			childtable.setEditable(false);
			childtable.init();
			
			fieldurl = mid[1];
		    //childtable.loadXML("getTableChildData.jsp?field="+mid[1]+"&id="+"&start="+0+"&size="+pageSize);
			dhtmlxAjax.get("getTableChildData.jsp?field="+fieldurl+"&id=&start="+0+"&size="+pageSize, outputResponseChild);
		//alert(fieldurl);
		    w1.centerOnScreen();
		    //w1.denyResize();
	    }
    }
    
    if(menuitemId=="rowColor")
    {
    	activetable.EA(rId,"#cdcdcd");
    	//mygrid.Sr();
    }
    
    if(menuitemId=="rowscopy")
    {
        //cacheline = activetable.
    	activetable.setCSVDelimiter('\t');
    	activetable.Sr();
    }	//NA('srows','\t');
    
    if(menuitemId=="rowspaste")
    {
    	activetable.dX(rId);
    	activetable.ug();
    }
    
    if(menuitemId=="upload")
    	upload(rowindex,cInd);
    	
    if(menuitemId=="link")
    {
   		 w1 = dhxWins.createWindow("w1", 400, 200, 450, 285);
		    w1.setModal(true);
		    //w1.setText("文件上传");
		    w1.button("minmax1").hide();
		    //w1.hide();
		    w1.attachHTMLString("链接名称:<input name='namelink'/><br>链接地址:<input name='urllink'/><br><input type='button' value='确定' onclick=\"mygrid.cells2("+rowindex+","+cInd+").setValue(document.getElementById('namelink').value+'^'+document.getElementById('urllink').value);dhxWins.window('w1').close();\"/>");
		    w1.centerOnScreen();
		    w1.denyResize();
		    
    }
    if(menuitemId=="htmleditor")
    {
    	w1 = dhxWins.createWindow("w1", 0, 0, 800, 600);
		    w1.setModal(true);
		    w1.tI("HTML文本编辑");
		    w1.button("minmax1").hide();
		    //w1.hide();
		    w1.attachURL("htmlEditor/htmlEditor.htm?uploadgroup=test&bindingfield=${field.name}&layout=advanced&parameters=${field.entity.id}");
		    w1.centerOnScreen();
		    w1.denyResize();
    	
	}
	
	if(menuitemId=="detail")
    {
    	w1 = dhxWins.createWindow("w1", 0, 0, 800, 600);
	    w1.setModal(true);
	    w1.tI("数据编辑");
	    w1.button("minmax1").hide();
	    //w1.hide(); updateBean.vpage?oper=update&uri=" + uri + "&id=" + id);
		if(rId.indexOf('isNewRecord')<0)
	    	w1.attachURL("updateBean.vpage?oper=update&uri=" + entityurl + "&id="+rId.split("#")[1]);
	    else
	    	w1.attachURL("updateBean.vpage?oper=add&uri=" + entityurl + "&id="+rId.split("#")[1]);
	    //alert('sdf');
	    w1.centerOnScreen();
	    w1.denyResize();
	    //w1.stick();
    	
	}
	
	if(menuitemId=="showcount")
    {
    	var type = mygrid.abv(cInd);
    	if(type!='edn')
    		return;
    	dhtmlxAjax.get("getTableStat.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd), function(loader){
    		//alert(loader.ai.responseText);
    		var str = loader.ai.responseText;
    		//alert(Trim(str));
    		var arraystat = str.split(",");
    		document.getElementById("sum"+cInd).innerHTML = arraystat[0];
    		document.getElementById("avr"+cInd).innerHTML = arraystat[1];
    		document.getElementById("max"+cInd).innerHTML = arraystat[2];
    		document.getElementById("min"+cInd).innerHTML = arraystat[3];
    	});
    }
	
	if(menuitemId=="grpcount")
    {
    	w1 = dhxWins.createWindow("w1", 0, 0, 800, 600);
	    w1.setModal(true);
	    w1.tI("分组统计");
	    w1.button("minmax1").hide();
	    
	    //dhxToolbar = w1.attachToolbar();
		//dhxToolbar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
		//dhxToolbar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/groupStat.xml");
	    
	    //w1.attachURL("groupStat.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd));

	    var goupStatToolBar = w1.attachToolbar();
		goupStatToolBar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
		goupStatToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/groupStat.xml");
	    //goupStatToolBar.attachEvent("onClick", doGoupStatToolBar);
	    goupStatToolBar.attachEvent("onClick", function(id) {
    	if(id=="pie")
    		w1.attachURL("groupStatPie.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd));
    	if(id=="column")
    		w1.attachURL("groupStatColumn.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd));
    	if(id=="line")
    		w1.attachURL("groupStatLine.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd));
	    if(id=="close")
	    	w1.close();
	    });
	    w1.attachURL("groupStatPie.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd));

	    w1.centerOnScreen();
		w1.denyResize();
    }
	
	if(menuitemId=="impData")
    {
    	w1 = dhxWins.createWindow("w1", 0, 0, 500, 400);
	    w1.setModal(true);
	    w1.tI("excel导入");
	    w1.button("minmax1").hide();
    	w1.attachURL("importSet.vpage?entityUri=" + entityurl);
	    w1.centerOnScreen();
    }
	if(menuitemId=="expDataSel")
	{
	
    	var ids=mygrid.bI();
    	if(ids==null||ids==""||ids.length<1)
    		alert("未选择记录，请先选择记录！");
    	if(ids!=null&&ids!=""&&ids.length>0)
    	{
	    	ids=ids.replaceAll(ids.substring(0,ids.indexOf("#")+1),"");
	    	ids=ids.replaceAll(",",";");
	    	w1 = dhxWins.createWindow("w1", 0, 0, 500, 400);
		    w1.setModal(true);
		    w1.tI("导出所选数据");
		    w1.button("minmax1").hide();
	    	w1.attachURL("exportSet.vpage?entityUri=" + entityurl+"&exportType=2&ids="+ids);
		    w1.centerOnScreen();
	    }
    }
    
    if(menuitemId=="expDataAll")
    {
    	w1 = dhxWins.createWindow("w1", 0, 0, 500, 400);
	    w1.setModal(true);
	    w1.tI("导出所有数据");
	    w1.button("minmax1").hide();
    	w1.attachURL("exportSet.vpage?entityUri=" + entityurl+"&exportType=3&ids=");
	    w1.centerOnScreen();
    }
    if(menuitemId=="expTep")
    	window.open("exportExcel.vpage?tid=" + treeid + "&exportType=1","iftarget");
    //mygrid.setRowTextStyle(data[0], "color:" + menuitemId.split("_")[1]);
    return true;
}

function onMenuAttach(menuitemId, type) {
    //var data = activetable.contextID.split("_");
    //var rId = data[0];
    //var cInd = data[1];
	//var rowindex = activetable.getRowIndex(rId);
	//mygrid.dX(mygrid.bB(1));

    if(menuitemId=="delete")
    {
    	deleteMany2ManyForUpdate(entityurl,dhxTabbar_attach.wX(),mygrid.bI().split("#")[1],mygridattach.bI(),"");
    }
}

function setLink(rId,cInd) {
	//alert("sdfs");
	//mygrid.cells2(rId,cInd).setValue("C:/tt.jpg");
}

function sumColumn(ind)
{
	var out=0;
	for(var i=0;i<mygrid.iD();i++)
	{
		out+=parseInt(mygrid.cells2(i,ind).getValue());
	}
	return out;
}

function gridavr() {
	for (var i=0; i<mygrid.So(); i++){
    	var type = mygrid.abv(i);
    	if(type=='edn')
			document.getElementById("avr"+i).innerHTML = sumColumn(i)/mygrid.iD();
    }
    return true;
}

function gridsum() {
	for (var i=0; i<mygrid.So(); i++){
    	var type = mygrid.abv(i);
    	if(type=='edn')
			document.getElementById("sum"+i).innerHTML = sumColumn(i);
    }
    return true;
}


function outputResponse(loader) {

   	mygrid.clearAll(true);
    var str = loader.ai.responseText;
    str = Trim(str);
//	alert(str);
    
    if(str.indexOf("j_spring_security_check")>=0)
    {
    	document.location.href = document.location.href;
    	return;
    }
    var strArr = str.split("@CNIC@");
	
	{
		mygrid.parse(strArr[0]);
		
		//mygrid.dX(0);
		recordNum = strArr[1];
		pageNum = strArr[2];
		if(pageNum=='0')
			pageNum = 1 ;
		
		//dhxTabbar_attach.clearAll();
		
			//alert("ddd");
		//myDataProcessor = new Iq("/DoUpdateBean?entity="+entityurl);
		//myDataProcessor.JI("GET",true);
		//myDataProcessor.mq("off");
		//myDataProcessor.init(mygrid);
		
		//alert(pageNum);
		initPageBar();
		dhxToolbarPage.setItemText("text3", "第"+curPage+"-"+pageNum+"页 "+"共<B>"+recordNum+"</B>条记录" );
		//mygrid.attachFooter("Total quantity,#cspan,-,<div id='nr_q'>0</div>,-,<div id='sr_q'>0</div>",["text-align:left;"]);
	 
		var sumStr = "求和,";
		var avrStr = "平均值,";
		var maxStr = "最大值,";
		var minStr = "最小值,";
		for (var i=1; i<mygrid.So(); i++){
			if(i!=1)
			{
				sumStr += ",";
				avrStr += ",";
				maxStr += ",";
				minStr += ",";
			}
			var type = mygrid.abv(i);
			if(type=='edn')
			{
				sumStr += "<div id='sum"+i+"'>0</div>";
				avrStr += "<div id='avr"+i+"'>0</div>";
				maxStr += "<div id='max"+i+"'>0</div>";
				minStr += "<div id='min"+i+"'>0</div>";
			}
			else
			{
				sumStr += "";
				avrStr += "";
				maxStr += "";
				minStr += "";
			}
		}
		//alert(footerStr+"dd");
		//mygrid.attachFooter(sumStr);
		//mygrid.attachFooter(avrStr);
		//mygrid.attachFooter(maxStr);
		//mygrid.attachFooter(minStr);
		
		activetable = mygrid;
		//gridsum();
		//gridavr();	
		dhxLayout.progressOff();
	}
	
}

function outputResponseChild(loader) {

   	childtable.clearAll(true);
    var str = loader.ai.responseText;
    str = Trim(str);
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
	//dhxToolbarPage.setItemText("text3", "第"+curPage+"-"+pageNum+"页 "+"共<B>"+recordNum+"</B>条记录" );
	dhxToolbarChild.setItemText("text3", "第"+curPageChild+"-"+pageNumChild+"页 "+"共<B>"+recordNumChild+"</B>条记录" );

	dhxLayout.progressOff();
}

function outputResponseAttach(loader) {

   	mygridattach.clearAll(true);
    var str = loader.ai.responseText;
    str = Trim(str);
    var strArr = str.split("@CNIC@");
    mygridattach.parse(strArr[0]);
    recordNumAttach = strArr[1];
    pageNumAttach = strArr[2];
    if(pageNumAttach=='0')
		pageNumAttach = 1 ;
    //mygrid.dX(0);
    //recordNumChild = strArr[1];
    //pageNumChild = strArr[2];
	
    	//alert("ddd");
    //myDataProcessor = new Iq("/DoUpdateBean?entity="+entityurl);
	//myDataProcessor.JI("GET",true);
	//myDataProcessor.mq("off");
	//myDataProcessor.init(mygrid);
	
	//alert(pageNum);
	//initPageBar();
	//dhxToolbarChild.setItemText("text3", "第"+curPageChild+"-"+pageNumChild+"页 "+"共<B>"+recordNumChild+"</B>条记录" );
	initPageBarAttach();
	
	dhxToolbarAttachPage.setItemText("text3", "第"+curPageAttach+"-"+pageNumAttach+"页 "+"共<B>"+recordNumAttach+"</B>条记录" );
	layout_attach.cells("a").progressOff();
}

function outputResponseStat(loader) {

}

function onShowMenu(rowId, celInd, grid) {
    //var arr = ["upload", "localfile", "link", "htmleditor"];
    //for (var i = 0; i < arr.length; i++) {
    //    menu.setItemDisabled(arr[i]);
    //}
    
    //var type = mygrid.abv(celInd);
    //if(type=='img')
    //	menu.setItemEnabled("upload");
	//if(type=='link')
    //	menu.setItemEnabled("link");
    //if(type=='txttxt')
    //	menu.setItemEnabled("htmleditor");
    
    return true;
}
function initPageBar()
{
	//alert(pageNum);
	//alert(curPage);
	if(curPage<pageNum||curPage>1)
   	{
   		dhxToolbarPage.enableItem("prev");
   		dhxToolbarPage.enableItem("first");
   		dhxToolbarPage.enableItem("last");
   		dhxToolbarPage.enableItem("next");
   	}	
	
   	if(curPage==1)
   	{
   		dhxToolbarPage.disableItem("prev");
   		dhxToolbarPage.disableItem("first");
   	}
	
	if(curPage==pageNum)
   	{
   		dhxToolbarPage.disableItem("last");
   		dhxToolbarPage.disableItem("next");
   	}	
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

function initPageBarAttach()
{
	//alert(pageNum);
	//alert(curPageChild);
	if(curPageAttach<pageNumAttach||curPageAttach>1)
   	{
   		dhxToolbarAttachPage.enableItem("prev");
   		dhxToolbarAttachPage.enableItem("first");
   		dhxToolbarAttachPage.enableItem("last");
   		dhxToolbarAttachPage.enableItem("next");
   	}
	
   	if(curPageAttach==1)
   	{
   		dhxToolbarAttachPage.disableItem("prev");
   		dhxToolbarAttachPage.disableItem("first");
   	}
	
	if(curPageAttach==pageNumAttach)
   	{
   		dhxToolbarAttachPage.disableItem("last");
   		dhxToolbarAttachPage.disableItem("next");
   	}	
}

function sendRequestGet(id) {
	dhxLayout.progressOn();
//	alert(curPage + "-" + pageNum + "-" + whereFilter);
   	dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage+"&size="+pageSize+"&whereFilter="+whereFilter, outputResponse);
}

function tondblclick(id) {
	//if(id.indexOf('directory@')>=0)
	//	return false;
	//dhxLayout.cells("a").progressOff();
	//alert("tondblclick" + id);
	curPage = 0;
	treeid = id;
    entityurl = id;
    queryFormWhereFilter="";
}

function refreshgrid() {
	dhxWins.window("w1").close();
	sendRequestGet(entityurl);
}

function doPage(id) 
{
    if(id == "prev")
    {
    	curPage--;
    	if(curPage==1)
    	{
    		dhxToolbarPage.disableItem("prev");
    		dhxToolbarPage.disableItem("first");
    	}
    	
    	if(curPage!=pageNum)
    	{
    		dhxToolbarPage.enableItem("last");
    		dhxToolbarPage.enableItem("next");
    	}
    	
    	sendRequestGet(entityurl);
    }
    
    if(id == "next")
    {
    	curPage++;
    	if(curPage>=pageNum)
    	{
    		dhxToolbarPage.disableItem("last");
    		dhxToolbarPage.disableItem("next");
    	}
    	
    	if(curPage!=1)
    	{
    		dhxToolbarPage.enableItem("prev");
    		dhxToolbarPage.enableItem("first");
    	}
    	
    	sendRequestGet(entityurl);
    }
    
    if(id == "first")
    {
    	curPage=1;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhxToolbarPage.disableItem("prev");
    	dhxToolbarPage.disableItem("first");
    	if(curPage!=pageNum)
    	{
    		dhxToolbarPage.enableItem("last");
    		dhxToolbarPage.enableItem("next");
    	}
    	sendRequestGet(entityurl);
    }
    
    if(id == "last")
    {
    	curPage=pageNum;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhxToolbarPage.disableItem("last");
    	dhxToolbarPage.disableItem("next");
    	if(curPage!=1)
    	{
    		dhxToolbarPage.enableItem("prev");
    		dhxToolbarPage.enableItem("first");
    	}
    	sendRequestGet(entityurl);		
    }
    
    if(id == "refresh")
    {
    	queryFormWhereFilter = "";
    	dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage*pageSize+"&size="+pageSize, outputResponse);       
    }

	dhxToolbarPage.setValue("input1",curPage);	
    
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
    	dhtmlxAjax.get("getTableChildData.jsp?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);	
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
		dhtmlxAjax.get("getTableChildData.jsp?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
    }
    
    if(id == "first")
    {
    	curPageChild=1;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhtmlxAjax.get("getTableChildData.jsp?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
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
    	dhtmlxAjax.get("getTableChildData.jsp?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
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
    	if(dhxTabbar_attach.wX()!=null && dhxTabbar_attach.wX()==fieldurl)
    		addMany2ManyForUpdate(entityurl,fieldurl,mygrid.bI().split("#")[1],childtable.bI());
    	else
    		addMany2OneForUpdate(entityurl,fieldurl,mygrid.bI().split("#")[1],childtable.bI());

    }
	if(id == "cancel")
    {	
    	//alert(childtable.bI());
    	dhxWins.window("w1").close();
    }
}

function doPageAttach(id) 
{	
    if(id == "prev")
    {
    	curPageAttach--;
    	if(curPageAttach==1)
    	{
    		dhxToolbarAttachPage.disableItem("prev");
    		dhxToolbarAttachPage.disableItem("first");
    	}
    	
    	if(curPageAttach!=pageNumAttach)
    	{
    		dhxToolbarAttachPage.enableItem("last");
    		dhxToolbarAttachPage.enableItem("next");
    	}
    	dhtmlxAjax.get("getTableChildData.jsp?field="+dhxTabbar_attach.wX()+"&id="+mygrid.bI()+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);	
    	//sendRequestGet(entityurl);
    }
    
    if(id == "next")
    {
    	curPageAttach++;
    	if(curPageAttach>=pageNumAttach)
    	{
    		dhxToolbarAttachPage.disableItem("last");
    		dhxToolbarAttachPage.disableItem("next");
    	}
    	
    	if(curPageAttach!=1)
    	{
    		dhxToolbarAttachPage.enableItem("prev");
    		dhxToolbarAttachPage.enableItem("first");
    	}
		dhtmlxAjax.get("getTableChildData.jsp?field="+dhxTabbar_attach.wX()+"&id="+mygrid.bI()+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
    }
    
    if(id == "first")
    {
    	curPageAttach=1;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhtmlxAjax.get("getTableChildData.jsp?field="+dhxTabbar_attach.wX()+"&id="+mygrid.bI()+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
    	//dhtmlxAjax.get("getTableChildData.jsp?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
    	dhxToolbarAttachPage.disableItem("prev");
    	dhxToolbarAttachPage.disableItem("first");
    	if(curPageAttach!=pageNumAttach)
    	{
    		dhxToolbarAttachPage.enableItem("last");
    		dhxToolbarAttachPage.enableItem("next");
    	}

    }
    
    if(id == "last")
    {
    	curPageAttach=pageNumAttach;
    	//outputResponse(entityurl,curPage,pageSize);
    	dhtmlxAjax.get("getTableChildData.jsp?field="+dhxTabbar_attach.wX()+"&id="+mygrid.bI()+"&start="+curPageChild+"&size="+pageSize, outputResponseAttach);
    	dhxToolbarAttachPage.disableItem("last");
    	dhxToolbarAttachPage.disableItem("next");
    	if(curPageAttach!=1)
    	{
    		dhxToolbarAttachPage.enableItem("prev");
    		dhxToolbarAttachPage.enableItem("first");
    	}
    }
    
}
function closeWindow()
{
	dhxWins.window("w1").close();
}


function onHeaderClick(ind,obj)
{
	alert("ddssaa");
	//mygrid.enableMarkedCells();
	//mygrid.unmarkAll();
	//for(var i=1; i<=pageSize; i++)
	//	mygrid.mark(i,ind);
		
	mygrid.setColumnColor("#3e3e3e,#3e3e3e,#d5f1ff");
	//mygrid.init();
	return true;
}

