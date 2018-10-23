var dhxWindow;
var dhxLayout,dhxToolbar,mygrid,statusBar,dhxAccord,dhxToolbarPage,menu,myDataProcessor,dhxTabbar;
var dhxWins,vault,mygridattach,dhxLayout_attach,dhxTabbar_attach,myDataProcessor_attach,dhxToolbarChild,childtable;
var menuattach, dhxToolbarAttachPage,layout_attach,layout;
var edit_mode = true;
var entityurl = "";
var fieldurl = "";
var queryFormWhereFilter = "";
var treeid = "";
var curPage = 1;
var pageSize = 15;
var recordNum = 0;
var pageNum = 1;

//var selectedCol = -1;
//var selectedRow = -1;
var selrowno = -1;
var selcolno = -1;

var copy_row_start = -1;
var copy_row_end = -1;
var copy_col_start = -1;
var copy_col_end = -1;
var row_copy_flag = -1;

var curPageChild = 1;
var recordNumChild = 0;
var pageNumChild = 1;

var curPageAttach = 1;
var recordNumAttach = 0;
var pageNumAttach = 1;

var colName = "";
var cacheline = 0;
var jsoQuery = null;
var goupStatToolBar;
var formIsChanged = false;
var relationFlag = "";

//以下是几个高度全局变量
var topHeight;
var footTopHeight;
var footHeight;
var treeTitleHeight;
var positionHeight;

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

function killErrors() {
	//document.location.href = document.location.href;
	return true;
}

window.onresize = function(){ 
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	layout.setSizes();   
	document.getElementById("statsContent").style.height=dhxLayout.cells('b').getHeight()-positionHeight;
	dhxLayout.setSizes();  
	document.getElementById("tree").style.height = layout.cells('a').getHeight()-treeTitleHeight;
} 

function setEditMode(state)
{
	if(state)
	{
		mygrid.setEditable(true);
	}
	else
	{
		mygrid.setEditable(false);
	}
}

function doOnLoad() {
	//得到头DIV和脚部DIV的高度
	topHeight = document.getElementById("topDiv").clientHeight;
	footTopHeight = document.getElementById("footTopDiv").clientHeight;
	footHeight = document.getElementById("footDiv").clientHeight;
	positionHeight = document.getElementById("position").clientHeight;
	//设置主体DIV的高度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	layout = new dhtmlXLayoutObject("parentId", "2U");
    layout.cells("a").setWidth("220"); 
	layout.cells("a").hideHeader();

	treeTitleHeight = document.getElementById("treeTitle").clientHeight;
	document.getElementById("tree").style.height = layout.cells('a').getHeight()-treeTitleHeight;
    dhxTree = new C("tree","100%","100%",0);
    dhxTree.setImagePath("/console/shared/plugins/dhtmlx/tree/imgs/csh_bluefolders/");
    dhxTree.ss("iconText.gif", "folderOpen.gif", "folderClosed.gif");
    //dhxTree.setOnClickHandler(tondblclick);
    dhxTree.ck(tondblclick);
    
    dhxTree.eG("getDynamicTree.vpage");//设置目录树的动态加载
    dhxTree.enableSmartXMLParsing(true);
    //dhxTree.loadXML("_menu.vpage");
    layout.cells("a").attachObject("treeDiv");
    
    dhtmlxAjax.get("_menu.vpage", menuhandle);

	//layout.cells("b").attachObject("statsContent");
	layout.cells("b").hideHeader();
	dhxLayout = new dhtmlXLayoutObject(layout.cells("b"), "4E");
    dhxLayout.cells("a").hideHeader();
	dhxLayout.cells("a").progressOn();
	dhxLayout.cells("a").attachObject("statsContent");
	//layout.cells("b").hideHeader();
	dhxLayout.cells("a").setHeight(51);
	dhxLayout.cells("a").fixSize(false,true);
	//dhxLayout.cells("a").setHeight(document.body.clientHeight*0.42);
    dhxToolbarPage = new bb("pageButton");//dhxLayout.cells("a").attachToolbar();
    dhxToolbar = new bb("funcButton");// dhxLayout.cells("a").attachToolbar();
	
    dhxToolbar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
    dhxToolbarPage.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
    
    dhxToolbar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/dhxtoolbar_button2.xml");
	dhxToolbarPage.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/dhxtoolbar_page.xml");
	dhxToolbarPage.setAlign('right');
	dhxToolbar.setSkin("dhx_skyblue1");
	dhxToolbarPage.setSkin("dhx_skyblue1");
	//dhxToolbar.addSpacer("paste");
    menu = new dhtmlXMenuObject();
	menu.renderAsContextMenu();
	menu.attachEvent("onClick", onMenu);
	menu.loadXML("/console/shared/plugins/dhtmlx/grid/dhxmenu.xml");
	menu.setIconsPath("/console/shared/plugins/dhtmlx/menu/imgs/");
	menuattach = new dhtmlXMenuObject();
	menuattach.renderAsContextMenu();
	menuattach.attachEvent("onClick", onMenuAttach);
	menuattach.loadXML("/console/shared/plugins/dhtmlx/grid/dhxmenuattach.xml");
	//dhxLayout.cells("b").attachObject("areaMain");
    //mygrid = new D("tableMain");
    mygrid = dhxLayout.cells("b").attachGrid();
    mygrid.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	mygrid.setSkin("dhx_skyblue1");
	mygrid.enableMultiselect(true);
	mygrid.enableColumnMove(true);
	mygrid.setDateFormat("%Y-%m-%d");
	//mygrid.enableBlockSelection();
	mygrid.enableDragAndDrop(true);
	//mygrid.enableMarkedCells();
	mygrid.init();
	
	mygrid.enableContextMenu(menu);
	mygrid.attachEvent("onBeforeContextMenu", onShowMenu);
    mygrid.enableUndoRedo();
    mygrid.enableHeaderMenu();
    //mygrid.enableHeaderImages(true);
    mygrid.attachEvent("onRowSelect", doOnGridSelected);
    mygrid.attachEvent("onHeaderClick", onHeaderClick);
    mygrid.attachEvent("onMouseOver", function(id,ind){
    	//alert(id);
    	if(ind==0)
    	{
	    	var rowindex = mygrid.getRowIndex(id);
	    	var cellObj = mygrid.cells(id, ind);
	    	cellObj.cell.style.cursor = "url(/console/shared/images/rowsel.cur)";
    	}
    });
    mygrid.attachEvent("onEditCell",function(stage,id,index,new_value,old_value){
    	var col_info = mygrid.getColumnId(index).split("#");
    	var isId = col_info[2];
    	//alert(isId);
        if (stage == 2 && isId == "true")
        {
        	if(new_value==old_value||!check(new_value))
        	{
        		return false;
        	}
			dhtmlxAjax.get("/console/editor/validateIdExisted.vpage?entityUri=" + entityurl + "&idValue=" + new_value,
			function(loader) {
				var validateResult = Trim(loader.ai.responseText);
				if(validateResult == '1')
				{
					alert("输入的主键值在数据库在已经存在");
					mygrid.cells(id, index).setValue(old_value);
					return false;
				}
			});
			function check(value)
			  {
			  	if(value == '')
			  	{
			  		alert("主键不能为空");
			  		return false;
			  	}
			  	if(col_info[1] == "Long")
			  	{
			  		var reg = /^(0|[1-9]\d*)$/;
			  	
					if(value.match(reg)==null)
					{
						alert("主键是整数类型，请输入一个整数");
						return false;
					}
			  	}

			  	return true;
			  }
        	//return false; //deny edit operation
        }
        return true;
    });
  mygrid.attachEvent("onDrag", function(sId,tId,sObj,tObj,sInd,tInd){
	initGridBgcolor();
	
	var r_start = mygrid.getRowIndex(sId);
	var r_end = mygrid.getRowIndex(tId);
	var tmp;
	if(r_start>r_end)
	{
		tmp=r_start;r_start=r_end;r_end=tmp;
	}
	if(sInd>tInd)
	{
		tmp=sInd;sInd=tInd;tInd=tmp;
	}
	if(sInd==tInd&&tInd==0)
	{
		sInd = 1;
		tInd = mygrid.So()-1;
	}
	for(var i=r_start; i<=r_end; i++)
	{
		for(var j=sInd; j<=tInd; j++)
		{
			mygrid.cells(mygrid.bB(i), j).cell.style.background = "#9ac2e5";
		}
	}

	copy_row_start = r_start;
	copy_row_end = r_end;
	copy_col_start = sInd;
	copy_col_end = tInd;
	
	return true;
});
   
    myDataProcessor = new Iq("doGridUpdate.vpage?entity="+entityurl);
	myDataProcessor.JI("POST",true);
	myDataProcessor.mq("off");

	myDataProcessor.init(mygrid);
	
	myDataProcessor.alx("error",function(tag)
	{
		alert(tag.firstChild.nodeValue);
		dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage+"&size="+pageSize+"&t="+new Date(), outputResponse);       
	});
    
    dhxLayout.cells("c").setHeight("26");
	dhxLayout.cells("c").fixSize(false,true);
	dhxLayout.cells("c").attachObject("attachPage");
	dhxLayout.setAutoSize("b;d");
	
    dhxLayout.cells("b").hideHeader(); 
	dhxLayout.cells("c").hideHeader(); 
    dhxToolbarAttachPage = new bb("pageButtonAttach");//layout_attach.cells("b").attachToolbar();
	dhxToolbarAttachPage.setSkin('dhx_skyblue1');
	dhxToolbarAttachPage.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
    dhxToolbarAttachPage.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/dhxtoolbar_page_attach.xml");
	dhxToolbarAttachPage.attachEvent("onClick", doPageAttach);
    dhxToolbarAttachPage.setAlign('right');
    dhxTabbar_attach = dhxLayout.cells("d").attachTabbar();
    dhxTabbar_attach.setSkin('dhx_skyblue');
    dhxTabbar_attach.setImagePath("/console/shared/plugins/dhtmlx/tabbar/imgs/");
    dhxTabbar_attach.enableAutoReSize();
    dhxTabbar_attach.attachEvent("onSelect",doOnTabbarSelect);

    dhxToolbarPage.attachEvent("onEnter", function(id, value) {
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
    	if(parseInt(value)<1)
        {
        	alert("输入值超出了最小范围");
        	return;
        	curPage = 1;
        }
        else if(parseInt(value)>pageNum)
        {
        	alert("输入值超出了最大范围");
        	return;
        	curPage = pageNum;
        }
        else 
        	curPage = parseInt(value);
        sendRequestGet(entityurl);
    });
    
    dhxToolbarAttachPage.attachEvent("onEnter", function(id, value) {
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
        	curPageAttach = 1;
        }
        else if(parseInt(value) > pageNumAttach)
        {
        	alert("输入值超出了最大范围");
        	return;
        	curPageAttach = pageNumAttach;
        }
        else 
        	curPageAttach = parseInt(value);
    	var fieldUri = dhxTabbar_attach.wX().split("@CNIC#@")[0];
		var params = { field:fieldUri, id:mygrid.bI(),start:curPageAttach,size:pageSize,t:new Date() };
		var strparams = jQuery.param(params); 
		$.get("getTableChildData.vpage",strparams,outputResponseAttach);
        //dhtmlxAjax.get("getTableChildData.vpage?field="+fieldUri+"&id="+escape(mygrid.bI())+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
    });
    
    dhxToolbarPage.attachEvent("onClick", doPage);
    
    dhxToolbar.attachEvent("onClick", function(id) {
        if(id == "new")
        {
			add_row();
        }
        
        if(id == "new1")
        {
            if(isGridUpdated())
            {
            	if(!confirm("表格编辑后没有保存,是否继续？"))
            		return;
            }
        	dhxWindow = dhxWins.createWindow("w1", 0, 0, 800, parseInt(document.body.clientHeight*0.8));
		    dhxWindow.setModal(true);
		    dhxWindow.tI("新增数据");
		    
		    dhxWins.window("w1").button("close").attachEvent("onClick", function(){
		    	dhxWins.window("w1").close();
		    });
        	dhxWindow.attachURL("addItem.vpage?uri=" + entityurl+"&t="+new Date());
        	dhxWindow.centerOnScreen();
        }
        
        if(id == "browne")
        {
			setEditMode(false);
        }
        
        if(id == "editor")
        {
			setEditMode(true);
        }
        
        if(id == "save")
        {
        	myDataProcessor.sendData();
        	edit_mode = "true";
        }
        
        if(id == "delete")
        {
        	deleteGridRows();
        }
        
        if(id == "copy")
        {
        	copyFromGrid("@tn#", "@tt#");
        }
        if(id == "paste")
        {
        	//mygrid.ug();
        	pasteGrid("@tn#", "@tt#");
        }
        if(id == "copy2")
		{
			copyFromGrid("\n", "\t");
		}
        if(id == "paste2")
		{
			pasteGrid("\n", "\t");
		}         
        if(id == "undo")
        {
        	mygrid.alN();
        }
        if(id == "redo")
        {
        	mygrid.alO();
        }
        
        if(id == "query")
        {
            if(isGridUpdated())
            {
            	if(!confirm("表格编辑后没有保存,是否继续？"))
            		return;
            }
        	var oDiv=document.createElement("div");   
			oDiv.id="QueryForm";   
			document.body.appendChild(oDiv);
			if(jsoQuery == null||jsoQuery.entity!=entityurl)
			{
				jsoQuery = new Query(entityurl);
			}
			var jsoQueryForm = new QueryForm(jsoQuery, entityurl, $('#QueryForm')[0], new function()
				{
					this.onsubmit = function()
					{
						//jsoDataGrid${seqid}.refresh();
					}
				}
			); 

			queryWindow = dhxWins.createWindow("queryWindow", 400, 200, 450, 285);
		    queryWindow.setModal(true);
		    queryWindow.tI("查询");
		    queryWindow.attachObject('QueryForm');
		    queryWindow.button("minmax1").hide();
		    dhxWins.window("queryWindow").centerOnScreen();
		}
        
        if(id == "logout")
        {
        	document.location.href = "../sqlview/sqlQuery.jsp";
        }
        
    });
    
	dhxWins = new dhtmlXWindows();
    dhxWins.enableAutoViewport(true);
    dhxWins.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
}

function menuhandle(loader)
{
	dhxTree.loadXMLString(Trim(loader.ai.responseText));
}

function doOnGridSelected(id, ind) {

	initGridBgcolor();
	
	var rId = mygrid.getRowIndex(id);
	
	if(ind ==0&&rId!=-1)
	{
		for(var i=1; i<mygrid.So(); i++)
			mygrid.cells(mygrid.bB(rId), i).cell.style.background = "#9ac2e5";
		
		
		copy_row_start = rId;
		copy_row_end = rId;
		copy_col_start = 1;
		copy_col_end = mygrid.So()-1;
	}
	else
	{
		if(rId!=-1)
		{
			var cellObj = mygrid.cells(mygrid.bB(rId), ind);
			cellObj.cell.style.background = "#9ac2e5";
		}

		copy_row_start = rId;
		copy_row_end = rId;
		copy_col_start = ind;
		copy_col_end = ind;
	}
	selrowno = rId;
	selcolno = ind;
	
	entityurl = treeid;
	doOnTabbarSelect(dhxTabbar_attach.wX());
	return true;
}

function doOnAttachGridSelected(id) {
	return true;
}

function doOnTabbarSelect(id) {
	
	
	if(id!=null && mygrid.bI() != null)
	{
		var selid = arguments[0];//.split("_");
		var oDiv=document.createElement("div")   
		oDiv.id="divToolAttach"   
	    document.body.appendChild(oDiv) 
		
	  	mygridattach = dhxTabbar_attach.cells(selid).attachGrid();
	    mygridattach.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	
		mygridattach.setSkin("dhx_skyblue");
		mygridattach.enableColumnMove(true);
		mygridattach.setEditable(false);
		mygridattach.init();
		mygridattach.enableContextMenu(menuattach);
		
		var uri = escape(mygrid.bI());
		
		var params = { field:selid.split("@CNIC#@")[0], id:mygrid.bI(),start:curPageAttach,size:pageSize,t:new Date() };
		var strparams = jQuery.param(params); 
		$.get("getTableChildData.vpage",strparams,outputResponseAttach);
	
		dhxToolbarPage.setItemText("text3", "第"+curPage+"页 共"+pageNum+"页 "+"共<B>"+recordNum+"</B>条记录" );
	}else
	{
		recordNumAttach = 0;
		pageNumAttach = 1;
		curPageAttach = 1;
		initPageBarAttach();
		dhxToolbarAttachPage.setItemText("text3", "第"+curPageAttach+"页 共"+pageNumAttach+"页 "+"共<B>"+recordNumAttach+"</B>条记录" );
	}

    return true;
}
function onMenu(menuitemId, type) {
	
	var rId = mygrid.bI();
	var rowindex = selrowno;
	var cInd = selcolno;
	
    if(menuitemId=="colHide")
    {
    	if(selcolno == -1)
    	{
    		alert("还没有选择列，请选择一列");
    		return false;
    	}
    	mygrid.setColumnHidden(cInd,true);
    	//var colIndex=mygrid.getColIndexById('1');
    	var colLabel=mygrid.adE(cInd);
    	//alert(colLabel);
    	menu.addNewChild("colShow", 0, "show_"+cInd, colLabel, false, "item.gif");
    	selcolno = -1;
    	for(var i=0; i<mygrid.iD(); i++)
    	{
    		if(i%2==0)
				mygrid.cells(mygrid.bB(i), cInd).cell.style.background = "#FFFFFF";
			else
				mygrid.cells(mygrid.bB(i), cInd).cell.style.background = "#E3EFFF";
    	}
			//mygrid.cells(mygrid.bB(i), cInd).cell.style.background = "#FFFFFF";
    	return;
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
		        		mygrid.setColumnHidden(id.replace("show_",""),false);
    					menu.removeItem(id);
		        	}
		        }
		    });
		    
		    return false;
    		
    	}
    	mygrid.setColumnHidden(menuitemId.replace("show_",""),false);
    	menu.removeItem(menuitemId);
    	return;
    }
    
    if(menuitemId=="rowColor")
    {
    	mygrid.EA(rId,"#cdcdcd");
    }
    
    if(menuitemId=="copy")
    {
    	//copyFromGrid("@tn#", "@tt#");
    	copyFromGrid("\n", "\t");
    	return;
    }	//NA('srows','\t');
    
    if(menuitemId=="paste")
    {
    	pasteGrid("\n", "\t");
    	return;
    }
    
    if(menuitemId=="delete")
    {
    	deleteGridData();
    	return;
    }
    
    if(menuitemId=="rowdelete")
    {
    	//mygrid.iu();
    	deleteGridRows();
    	return;
    }
    
    if(menuitemId=="upload")
    	upload(rowindex,cInd);
    	
    if(menuitemId=="link")
    {
   		 dhxWindow = dhxWins.createWindow("w1", 400, 200, 450, 285);
		    dhxWindow.setModal(true);
		    //dhxWindow.setText("文件上传");
		    dhxWindow.button("minmax1").hide();
		    //dhxWindow.hide();
		    dhxWindow.attachHTMLString("链接名称:<input name='namelink'/><br>链接地址:<input name='urllink'/><br><input type='button' value='确定' onclick=\"mygrid.cells2("+rowindex+","+cInd+").setValue(document.getElementById('namelink').value+'^'+document.getElementById('urllink').value);dhxWins.window('dhxWindow').close();\"/>");
		    dhxWindow.centerOnScreen();
		    dhxWindow.denyResize();
		    
    }
    if(menuitemId=="htmleditor")
    {
    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 800, parseInt(document.body.clientHeight*0.8));
		    dhxWindow.setModal(true);
		    dhxWindow.tI("HTML文本编辑");
		    dhxWindow.button("minmax1").hide();
		    //dhxWindow.hide();
		    dhxWindow.attachURL("htmlEditor/htmlEditor.htm?uploadgroup=test&bindingfield=${field.name}&layout=advanced&parameters=${field.entity.id}");
		    dhxWindow.centerOnScreen();
		    dhxWindow.denyResize();
    	return;
	}
	
	if(menuitemId=="showcount")
    {
    	var type = mygrid.abv(cInd);
    	if(type!='edn')
    		return;
    	dhtmlxAjax.get("getTableStat.jsp?entity=" + entityurl + "&field="+mygrid.getColumnId(cInd).split("#")[0], function(loader){
    		//alert(loader.ai.responseText);
    		var str = loader.ai.responseText;
    		//alert(Trim(str));
    		var arraystat = str.split(",");
    		document.getElementById("sum"+cInd).innerHTML = arraystat[0];
    		document.getElementById("avr"+cInd).innerHTML = arraystat[1];
    		document.getElementById("max"+cInd).innerHTML = arraystat[2];
    		document.getElementById("min"+cInd).innerHTML = arraystat[3];
    	});
    	return;
    }
	
	if(menuitemId=="grpcount")
    {
    	if(selcolno == -1)
    	{
    		alert("还没有选择列，请选择一列");
    		return false;
    	}
    	
    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 800, parseInt(document.body.clientHeight*0.8));
	    dhxWindow.setModal(true);
	    dhxWindow.tI("分组统计");
	    dhxWindow.button("minmax1").hide();
	    
	    goupStatToolBar = dhxWindow.attachToolbar();
		goupStatToolBar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
		
    	var type = mygrid.abv(cInd);
    	var fieldType = mygrid.getColumnId(cInd).split("#")[1];
       	if(fieldType=='Long'||fieldType=='Double')
       	{
       		goupStatToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/groupStat2.xml");
       	}
       	else if(fieldType=='Date')
       	{
       		goupStatToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/groupStat1.xml");
       	}
       	else
       	{
       		goupStatToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/groupStat.xml");
       	}
       	var queryFilters = queryFormWhereFilter.split("*&^%$#@!!@#$%^&**");
	    
	    var whereFilterTemp = "";
        if(queryFilters.length>1&&entityurl==queryFilters[0])
    		whereFilterTemp = queryFilters[1];
	    goupStatToolBar.attachEvent("onClick", function(id) {
	    	if(id=="pie")
	    		dhxWindow.attachURL("groupStatPie.vpage?entity=" + encodeURI(encodeURI(entityurl)) + "&field="+encodeURI(encodeURI(mygrid.getColumnId(cInd).split("#")[0]))+"&whereFilter="+whereFilterTemp);
	    	if(id=="column")
	    		dhxWindow.attachURL("groupStatColumn.vpage?entity=" + encodeURI(encodeURI(entityurl)) + "&field="+encodeURI(encodeURI(mygrid.getColumnId(cInd).split("#")[0]))+"&whereFilter="+whereFilterTemp);
	    	if(id=="line")
	    		dhxWindow.attachURL("groupStatLine.vpage?entity=" + encodeURI(encodeURI(entityurl)) + "&field="+encodeURI(encodeURI(mygrid.getColumnId(cInd).split("#")[0]))+"&whereFilter="+whereFilterTemp);
		    if(id=="section")
	    		dhxWindow.attachURL("setSection.vpage?entity=" + encodeURI(encodeURI(entityurl)) + "&field="+encodeURI(encodeURI(mygrid.getColumnId(cInd).split("#")[0]))+"&whereFilter="+whereFilterTemp);
			if(id=="ballon")
	    		dhxWindow.attachURL("groupStatXy.vpage?entity=" + encodeURI(encodeURI(entityurl)) + "&field="+encodeURI(encodeURI(mygrid.getColumnId(cInd).split("#")[0]))+"&whereFilter="+whereFilterTemp);
		    if(id=="close")
		    	dhxWindow.close();
	    });

	    dhxWindow.attachURL("groupStatColumn.vpage?entity=" + encodeURI(encodeURI(entityurl)) + "&field="+encodeURI(encodeURI(mygrid.getColumnId(cInd).split("#")[0]))+"&whereFilter="+whereFilterTemp);
	    dhxWindow.centerOnScreen();
		dhxWindow.denyResize();
		return;
    }
	
	if(menuitemId=="impData")
    {
    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 500, 400);
	    dhxWindow.setModal(true);
	    dhxWindow.tI("excel导入");
	    dhxWindow.button("minmax1").hide();
    	dhxWindow.attachURL("importSet.vpage?entityUri=" + entityurl);
	    dhxWindow.centerOnScreen();
	    return;
    }
	if(menuitemId=="expDataSel")
	{
	
    	//var ids=mygrid.bI();
		var ids = "";
    	for(var i=copy_row_start; i<=copy_row_end; i++)
    	{
    		if(ids == "")
    			ids += mygrid.bB(i);
    		else
    			ids += ";" + mygrid.bB(i);
    	}
    	if(ids==null||ids==""||ids.length<1)
    		alert("未选择记录，请先选择记录！");
    	if(ids!=null&&ids!=""&&ids.length>0)
    	{
	    	ids=ids.replaceAll(ids.substring(0,ids.indexOf("#")+1),"");
	    	ids=ids.replaceAll(",",";");
	    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 500, 400);
		    dhxWindow.setModal(true);
		    dhxWindow.tI("导出所选数据");
		    dhxWindow.button("minmax1").hide();
	    	dhxWindow.attachURL("exportSet.vpage?entityUri=" + entityurl+"&exportType=2&ids="+ids);
		    dhxWindow.centerOnScreen();
	    }
	    return;
    }
    
    if(menuitemId=="expDataAll")
    {
    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 500, 400);
	    dhxWindow.setModal(true);
	    dhxWindow.tI("导出所有数据");
	    dhxWindow.button("minmax1").hide();
	    var queryFilters = queryFormWhereFilter.split("*&^%$#@!!@#$%^&**");
	    
	    var whereFilterTemp = "";
        if(queryFilters.length>1&&entityurl==queryFilters[0])
    		whereFilterTemp = queryFilters[1];
    		
    	dhxWindow.attachURL("exportSet.vpage?entityUri=" + entityurl+"&exportType=3&ids="+"&whereFilter="+whereFilterTemp);
	    dhxWindow.centerOnScreen();
	    return;
    }
    if(menuitemId=="expTep")
    {
    	//window.open("exportExcel.vpage?tid=${entity.uri}&exportType=3&exportItem="+encodeURI(encodeURI(exportItem)),"iftarget");
    	window.open("/doDownload.do?tid=" + treeid + "&exportType=1","iftarget");
    	return;
    }
    //mygrid.setRowTextStyle(data[0], "color:" + menuitemId.split("_")[1]);
     	
	if(menuitemId=="detail")
    {	if(rId == null)  return;
	    //dhxWindow.hide(); updateBean.vpage?oper=update&uri=" + uri + "&id=" + id);
		if(rId.indexOf('isNewRecord')<0)
		{
			//alert()
			dhxWindow = dhxWins.createWindow("w1", 0, 0, 900, parseInt(document.body.clientHeight*0.8));
		    dhxWindow.setModal(true);
		    dhxWindow.tI("数据编辑");
		    //dhxWins.attachEvent("onClose", function(win){
		    //    // code here
		    //});
		    dhxWins.window("w1").button("close").attachEvent("onClick", function(){
		    	//alert(formIsChanged);
		    	dhxWins.window("w1").close();
		    });
		    // detaching event
		    //dhxWins.detachEvent(evId);
		    //dhxWindow.button("minmax1").hide();
	    	dhxWindow.attachURL("updateItem.vpage?uri=" + entityurl + "&id="+escape(rId.split("#")[1])+ "&t=" + new Date());
	   		dhxWindow.centerOnScreen();
	    	//dhxWindow.denyResize();
	    }
	    else
	    	alert("输入完数据后，请点击保存");//dhxWindow.attachURL("updateBean.vpage?oper=add&uri=" + entityurl + "&id="+rId.split("#")[1]);
	    //alert('sdf');
	    
	    //dhxWindow.stick();
	    return;
    	
	}
    
    if(selrowno==-1)
   	{
   		alert("还没有选择行，请选择一行");
   		return false;
   	}
    
    if(menuitemId.indexOf("attachtable@CNIC#@")>=0)
    {
    	//mygrid.setColumnHidden(menuitemId.replace("show_",""),false);
    	//menu.removeItem(menuitemId);
    	
    	var mid = menuitemId.split("@CNIC#@");
    	if(mid[2]=="O2M")
    	{
    		dhxWindow = dhxWins.createWindow("w1", 0, 0, 800, parseInt(document.body.clientHeight*0.8));
		    dhxWindow.setModal(true);
		    dhxWindow.tI("数据编辑");
		    dhxWindow.button("minmax1").hide();
		    dhxWins.window("w1").button("close").attachEvent("onClick", function(){
		    	dhxWins.window("w1").close();
		    });
		    //dhxWindow.hide(); updateBean.vpage?oper=update&uri=" + uri + "&id=" + id);
			
		    //dhxWindow.attachURL("updateBean.vpage?oper=insert&uri=" + mid[1] + "&rid="+rId.split("#")[1]);
		    dhxWindow.attachURL("addChildItem.vpage?uri=" + mid[1]+ "&rid="+rId.split("#")[1]);
		    dhxWindow.centerOnScreen();
    	}
    	else
    	{
    		//if(mid[2]=="Ref")
    			//return false;
	    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 800, parseInt(document.body.clientHeight*0.8));
			    dhxWindow.setModal(true);
		    dhxWindow.tI("关联信息添加");
		    dhxWindow.button("minmax1").hide();
		    dhxWins.window("w1").button("close").attachEvent("onClick", function(){
		    	dhxWins.window("w1").close();
		    });
		    //dhxWindow.hide();
		    //dhxWindow.attachURL("htmlEditor/htmlEditor.htm?uploadgroup=test&bindingfield=${field.name}&layout=advanced&parameters=${field.entity.id}");
	   		
	   		dhxToolbarChild = dhxWindow.attachToolbar();
	    	dhxToolbarChild.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
	    	dhxToolbarChild.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/dhxtoolbar_page_child.xml");
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
	   	    	//var fieldUri = dhxTabbar_attach.wX().split("@CNIC#@")[0];
	   	        //dhtmlxAjax.get("getTableChildData.vpage?field="+fieldUri+"&id="+escape(mygrid.bI())+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
	   	    });
	   		
	   		childtable = dhxWindow.attachGrid();
	   		childtable.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
			childtable.setSkin("dhx_skyblue");
			if(mid[2]=="M2M")
			{
				relationFlag = "M2M";
				childtable.enableMultiselect(true);
			}else
			{
				relationFlag = "REF";
			}
			childtable.setEditable(false);
			childtable.init();
			
			fieldurl = mid[1];
		    //childtable.loadXML("getTableChildData.vpage?field="+mid[1]+"&id="+"&start="+0+"&size="+pageSize);
			dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+0+"&size="+pageSize, outputResponseChild);
		//alert(fieldurl);
		    dhxWindow.centerOnScreen();
		    //dhxWindow.denyResize();
	    }
    }
    
    
    return true;
}

function onMenuAttach(menuitemId, type) {

	//alert(mygrid.bI());
	if(mygridattach.bI()==null)
   	{
   		alert("还没有选择行，请选择一行");
   		return false;
   	}
	
	var tmp = dhxTabbar_attach.wX().split("@CNIC#@");
    if(menuitemId=="delete")
    {	
    	if(tmp[1] == "M2M")
    		deleteMany2ManyForUpdate(entityurl,tmp[0],mygrid.bI().split("#")[1],mygridattach.bI(),"");
    	else if(tmp[1] == "O2M")
        	deleteOne2ManyForUpdate(entityurl,tmp[0],mygrid.bI().split("#")[1],mygridattach.bI(),"");
    	
    }
    
    if(menuitemId=="detail")
    {
    	dhxWindow = dhxWins.createWindow("w1", 0, 0, 800, parseInt(document.body.clientHeight*0.8));
	    dhxWindow.setModal(true);
	    dhxWindow.tI("数据编辑");
	    dhxWins.window("w1").button("close").attachEvent("onClick", function(){
	    	dhxWins.window("w1").close();
	    });
	    dhxWindow.button("minmax1").hide();
	    //alert(dhxTabbar_attach.wX());
    	dhxWindow.attachURL("updateChildBean.vpage?oper=update&uri=" + dhxTabbar_attach.wX().split("@CNIC#@")[0] + "&id="+mygridattach.bI());
   		dhxWindow.centerOnScreen();
    	dhxWindow.denyResize();
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
   	menu.clearAll();
	menu.loadXML("/console/shared/plugins/dhtmlx/grid/dhxmenu.xml");
    var str = loader.ai.responseText;
    str = Trim(str);
    
    if(str.indexOf("j_spring_security_check")>=0)
    {
    	document.location.href = document.location.href;
    	return;
    }
    var strArr = str.split("@CNIC@");
    mygrid.parse(strArr[0]);
    
    //mygrid.dX(0);
    recordNum = strArr[1];
    pageNum = strArr[2];
    if(pageNum=='0')
		pageNum = 1 ;
    
	dhxTabbar_attach.clearAll();
	if(strArr[3]!='no'&&strArr[3]!=undefined)
	{
		//alert(strArr[3]);
		var atachtable = strArr[3].split("@;@");
   		for(var i=1; i<atachtable.length; i++)
   		{
   			if(i==1)
   				menu.addNewChild(null, 5, "children", "关联信息", false, "edit.gif");
   			var atachtabattr = atachtable[i].split("@child#@");
   			
   			if(atachtabattr[2]=="Ref")
   			{
   				//setTimeOut("menu.addNewChild(\"children\", 0, \"attachtable@CNIC#@\"+atachtabattr[1]+\"@CNIC#@\"+atachtabattr[2], \"修改'\"+atachtabattr[0]+\"'\", false, \"edit.gif'\");",100);
   				menu.addNewChild("children", 0, "attachtable@CNIC#@"+atachtabattr[1]+"@CNIC#@"+atachtabattr[2], "修改'"+atachtabattr[0]+"'", false, "edit.gif");
   			}else{
   			//setTimeOut("menu.addNewChild('children', 0, 'attachtable@CNIC#@"+atachtabattr[1]+"@CNIC#@"+atachtabattr[2]+"', '添加"+atachtabattr[0]+"', false, 'addcol.gif')",100);
   				menu.addNewChild("children", 0, "attachtable@CNIC#@"+atachtabattr[1]+"@CNIC#@"+atachtabattr[2], "添加'"+atachtabattr[0]+"'", false, "addcol.gif");
   				dhxTabbar_attach.hG(atachtabattr[1]+"@CNIC#@"+atachtabattr[2],atachtabattr[0],"150px");		
   			}
   		}	
   		dhxTabbar_attach.fP(atachtabattr[1]+"@CNIC#@"+atachtabattr[2]);
	}
 
	//alert(pageNum);
	initPageBar();
	dhxToolbarPage.setItemText("text3", "第"+curPage+"页 共"+pageNum+"页 "+"共<B>"+recordNum+"</B>条记录" );

	//setEditMode(edit_mode);
	//mygrid.enableHeaderMenu();
	dhxLayout.progressOff();
	//mygrid.setColValidators("NotEmpty,NotEmpty");
	mygrid.dX(mygrid.bB(0));
	doOnGridSelected(mygrid.bB(0), 0);
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

	initPageBarChild();
	dhxToolbarChild.setItemText("text3", "第"+curPageChild+"页 共"+pageNumChild+"页 "+"共<B>"+recordNumChild+"</B>条记录" );

	dhxLayout.progressOff();
}

function outputResponseAttach(loader) {

   	mygridattach.clearAll(true);
    var str = loader;
    str = Trim(str);
    
    if(str.indexOf("j_spring_security_check")>=0)
    {
    	document.location.href = document.location.href;
    	return;
    }
    
    var strArr = str.split("@CNIC@");
    mygridattach.parse(strArr[0]);
    recordNumAttach = strArr[1];
    pageNumAttach = strArr[2];
    if(pageNumAttach=='0')
		pageNumAttach = 1 ;
	
	initPageBarAttach();
	
	dhxToolbarAttachPage.setItemText("text3", "第"+curPageAttach+"页 共"+pageNumAttach+"页 "+"共<B>"+recordNumAttach+"</B>条记录" );
}

function outputResponseStat(loader) {

}

function onShowMenu(rowId, celInd, grid) 
{
    return true;
}
function initPageBar()
{
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
	selrowno = -1;
	selcolno = -1;

	var wf = queryFormWhereFilter.split("*&^%$#@!!@#$%^&**");
	if(wf.length>1&&entityurl==wf[0])
		dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage+"&size="+pageSize+"&whereFilter="+wf[1]+"&t="+new Date(), outputResponse);
	else
   		dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage+"&size="+pageSize+"&t="+new Date(), outputResponse);
}

function tondblclick(id) {
	if(id.indexOf('directory@')>=0)
		return false;
	if(entityurl=='')
    {
		dhxToolbar.enableItem("delete");
		dhxToolbar.enableItem("save");
    }
	dhxLayout.cells("a").progressOff();
    if(isGridUpdated())
    {
    	if(!confirm("表格编辑后没有保存,是否继续？"))
    		return;
    }
	curPage = 1;
	curPageChild = 1;
	curPageAttach = 1;
	treeid = id;
    entityurl = id;
	queryFormWhereFilter="";
    sendRequestGet(id);
}

function refreshgrid() {
	sendRequestGet(entityurl);
}

function doPage(id) 
{
    if(isGridUpdated())
    {
    	if(!confirm("表格编辑后没有保存,是否继续？"))
    		return;
    }
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
    	dhtmlxAjax.get("getTableData.vpage?entity="+entityurl+"&start="+curPage+"&size="+pageSize+"&t="+new Date(), outputResponse);       
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
    	//if(dhxTabbar_attach.wX()!=null && dhxTabbar_attach.wX().split("@CNIC#@")[0]==fieldurl)
		if(relationFlag=="M2M")
    		addMany2ManyForUpdate(entityurl,fieldurl,mygrid.bI().split("#")[1],childtable.bI());
    	else
    		addMany2OneForUpdate(entityurl,fieldurl,mygrid.bI().split("#")[1],childtable.bI());

    }
	if(id == "cancel")
    {	
    	//alert(childtable.bI());
    	if(childtable.bI() != null)
    	{
    		if(!confirm('选择数据没有保存，是否继续？'))
			{
				return;
			}
    	}
    	dhxWins.window("w1").close();
    }
}

function doPageAttach(id) 
{	
	var fieldUri = dhxTabbar_attach.wX().split("@CNIC#@")[0];
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
		var params = { field:fieldUri, id:mygrid.bI(),start:curPageAttach,size:pageSize,t:new Date() };
		var strparams = jQuery.param(params); 
		$.get("getTableChildData.vpage",strparams,outputResponseAttach);
    	//dhtmlxAjax.get("getTableChildData.vpage?field="+fieldUri+"&id="+escape(mygrid.bI())+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);	
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
		var params = { field:fieldUri, id:mygrid.bI(),start:curPageAttach,size:pageSize,t:new Date() };
		var strparams = jQuery.param(params); 
		$.get("getTableChildData.vpage",strparams,outputResponseAttach);
		//dhtmlxAjax.get("getTableChildData.vpage?field="+fieldUri+"&id="+escape(mygrid.bI())+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
    }
    
    if(id == "first")
    {
    	curPageAttach=1;
    	//outputResponse(entityurl,curPage,pageSize);
		var params = { field:fieldUri, id:mygrid.bI(),start:curPageAttach,size:pageSize,t:new Date() };
		var strparams = jQuery.param(params); 
		$.get("getTableChildData.vpage",strparams,outputResponseAttach);
    	//dhtmlxAjax.get("getTableChildData.vpage?field="+fieldUri+"&id="+escape(mygrid.bI())+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
    	//dhtmlxAjax.get("getTableChildData.vpage?field="+fieldurl+"&id=&start="+curPageChild+"&size="+pageSize, outputResponseChild);
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
		var params = { field:fieldUri, id:mygrid.bI(),start:curPageAttach,size:pageSize,t:new Date() };
		var strparams = jQuery.param(params); 
		$.get("getTableChildData.vpage",strparams,outputResponseAttach);
    	//dhtmlxAjax.get("getTableChildData.vpage?field="+fieldUri+"&id="+escape(mygrid.bI())+"&start="+curPageAttach+"&size="+pageSize, outputResponseAttach);
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
	alert("dff");
	dhxWins.window("w1").close();
}

function initGridBgcolor()
{
	for(var i=0; i<(mygrid.iD()>pageSize?pageSize:mygrid.iD()); i++)
	{
		if(mygrid.bB(i) != undefined)
		{
			mygrid.cells(mygrid.bB(i), 0).cell.style.background = "#ededed";
			for(var j=1; j<mygrid.So(); j++)
			{
				if(i%2==0)
					mygrid.cells(mygrid.bB(i), j).cell.style.background = "#FFFFFF";
				else
					mygrid.cells(mygrid.bB(i), j).cell.style.background = "#E3EFFF";
			}
		}
	}
}

function onHeaderClick(ind,obj)
{
	if(ind ==0)
		return;
	
	initGridBgcolor();
	
	for(var i=0; i<(mygrid.iD()>pageSize?pageSize:mygrid.iD()); i++)
	{
		var cellObj = mygrid.cells(mygrid.bB(i), ind); 
    	cellObj.cell.style.background = "#9ac2e5";
	}
	
	
	selcolno = ind;

	return true;
}

function copyToClipboard(txt) 
{
	if (window.netscape) {        
        try {        
            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");        
        } catch (e) {        
            alert("被浏览器拒绝！\n请在浏览器地址栏输入'about:config'并回车\n然后将'signed.applets.codebase_principal_support'设置为'true'");        
        }        
		var clip = Components.classes['@mozilla.org/widget/clipboard;1'].createInstance(Components.interfaces.nsIClipboard);        
		if (!clip)        
			return;        
		var trans = Components.classes['@mozilla.org/widget/transferable;1'].createInstance(Components.interfaces.nsITransferable);        
		if (!trans)        
			return;        
        trans.addDataFlavor('text/unicode');        
		var str = new Object();        
		var len = new Object();        
		var str = Components.classes["@mozilla.org/supports-string;1"].createInstance(Components.interfaces.nsISupportsString);        
		var copytext = txt;        
		str.data = copytext;        
		trans.setTransferData("text/unicode",str,copytext.length*2);        
		var clipid = Components.interfaces.nsIClipboard;        
		if (!clip)        
            return false;        
        clip.setData(trans,null,clipid.kGlobalClipboard);        
        alert("复制成功!");
    }
	else if(window._isChrome)
	{
		alert("对不起，此功能目前不支持Chrome!");
	}
	else if(window.clipboardData) 
	{
        //window.clipboardData.clearData();        
        window.clipboardData.setData("Text", txt);  
    } else if(navigator.userAgent.indexOf("Opera") != -1) {        
        window.location = txt;        
    }	
}
function getFromClipboard()
{
	var text="";
	if(window.clipboardData)
	{
		text=window.clipboardData.getData("Text");
		//document.getElementById("ser_1").value = text;
	}
	return text;
}
function copyFromGrid(row_del, col_del) {
	var data = "";
	if(copy_col_start == 1 && copy_col_end == mygrid.So()-1)
		row_copy_flag = 1;
	else
		row_copy_flag = 0;
	for(var i=copy_row_start; i<=copy_row_end; i++)
	{
		for(var j=copy_col_start; j<=copy_col_end; j++)
		{
			if(j>0)
				data += mygrid.cells(mygrid.bB(i), j).getValue();
			if(j<copy_col_end)
				data += col_del;
		}	
		data += row_del;
	}
	mygrid.setCSVDelimiter("\t");
	copyToClipboard(data);
}

function pasteGrid(row_del, col_del) 
{
	var data = getFromClipboard();
	var rows = data.split(row_del);
	var r_start = selrowno;	
	
	
	if(row_copy_flag == 1)
		selcolno = 1;
	var c_start = selcolno;
	
	var cur_row_size = mygrid.iD();
	var cur_col_size = mygrid.So()-1;
	//防止行溢出
	var r_num = (r_start + rows.length-1) > cur_row_size ? cur_row_size - r_start: rows.length-1;

	for(var i=0; i<r_num; i++)
	{
		var cellValue = rows[i].split(col_del);
		myDataProcessor.setUpdated(mygrid.bB(r_start),true,null);
		//防止列溢出
		var c_num = (c_start + cellValue.length) > cur_col_size ? cur_col_size - c_start+1: cellValue.length;
		for(var j=0; j<c_num; j++)
		{
			var cell = mygrid.cells(mygrid.bB(r_start), c_start++);
			if(cell != null)
			{
				cell.setValue(cellValue[j]);
				cell.cell.style.color = "#ff0000"; 
			}
		}

		r_start++;
		c_start = selcolno;	
	}
	
}

function deleteGridData() {

	for(var i=copy_row_start; i<=copy_row_end; i++)
	{
		myDataProcessor.setUpdated(mygrid.bB(i),true,null);
		for(var j=copy_col_start; j<=copy_col_end; j++)
			mygrid.cells(mygrid.bB(i), j).setValue("");
	}
}

function deleteGridRows() {

	for(var i=copy_row_start; i<=copy_row_end; i++)
	{
		mygrid.deleteRow(mygrid.bB(i));
	}
}

function add_row()
{
	var col = "[";
	for (var i=0; i<mygrid.So(); i++){
	 //do_something_with_row(index);
		if(i!=0)
			col += ",";
		var type = mygrid.abv(i);
		//alert(type);
		if(type=='edn')
			col += "0";
		else if(type=='dhxCalendar')
			col += "'1890-01-01'";
		else
			col += "''";   	
	}
	col += "]";
	var nid = mygrid.uid();
	//alert(nid);
	mygrid.hY(entityurl+"#isNewRecord#"+nid, eval(col), nid);	
}

function isGridUpdated()
{
	for(var i=0; i<(mygrid.iD()>pageSize?pageSize:mygrid.iD()); i++)
	{
		var cellObj = mygrid.cells(mygrid.bB(i), 0); 
		if(cellObj.cell.style.fontWeight.indexOf("bold")>=0)
			return true;
	}
	return false;
}