var dhxLayout2U;//统计分析模块2U布局
var dhxLayout2UcellsB2E;//统计分析模块右侧2E布局
var dhxToolbar;//统计分析模块的“按钮”，包括“立即抽取”和“显示云图”

var dhxLayout2UATree;//统计分析模块左侧树
var dhxTabbarQuality;//资源量统计区域的页签
var dhxTabbarAccess;//数据访问统计区域的页签

var dataSetQuality2U;//选中数据集时，资源量统计区域的”数据量“、”利用率“、”记录数“等页签下的布局对象
var dataSetAccess1C;//选中数据集时，数据访问统计区域的“今日访问统计”、“分时段访问统计”以及“访问来源统计”等页签的布局对象
var tableQuality2U;//选中数据实体时，资源量统计区域的“利用率”页签下的布局对象
var tableAccess1C;//选中数据实体时，数据访问统计区域的“今日访问统计”、“分时段访问统计”以及“访问来源统计”等页签的布局对象

//选中数据集时，“访问关键词统计”、“来访IP统计”、“被访实体统计”以及“操作类型统计”等页签的数据处理Action链接
var dataSetAccessDataUrl = "dataSetAccessData.vpage?dsuri=";
//选中数据实体时，“访问关键词统计”、“来访IP统计”以及“操作类型统计”等页签的数据处理Action链接
var entityAccessDataUrl = "entityAccessData.vpage?dsuri=";
//选中数据集时，“分时段访问情况统计”页签调用的页面
var dataSetPeriodAccess = "dataSetPeriodAccess.vpage?dsuri=";
//选中数据实体时，“分时段访问情况统计”页签调用的页面
var entityPeriodAccess = "entityPeriodAccess.vpage?dsuri=";
//选中数据集时，“今日访问情况统计”页面的数据处理Action链接
var dataSetTodayAccess="dataSetTodayAccess.vpage?dsuri=";
//选中数据集时，“访问来源情况统计”页面的数据处理Action链接
var dataSetAccessSource="dataSetAccessSource.vpage?dsuri=";
//选中数据实体时，“今日访问情况统计”页面的数据处理Action链接
var entityTodayAccess="entityTodayAccess.vpage?dsuri=";
//选中数据实体时，“访问来源情况统计”页面的数据处理Action链接
var entityAccessSource="entityAccessSource.vpage?dsuri=";

//选中数据集时，表示选中的资源量统计页签类型，共有四个页签，分别为dataOnAbstract,dataOnDataVolume,dataOnRecords,dataOnUtilization
var selectDSQualityType;
//选中数据集时，表示选中的访问统计页签类型，共有七个页签
var selectDSAccessType;
//选中数据实体时，表示选中的资源量统计页签类型，共有两个页签，分别为tableOnAbstract,tableOnUtilization
var selectTableQualityType;
//选中数据实体时，表示选中的访问统计页签类型，共有六个页签
var selectTableAccessType;

//临时变量，用来标识资源量统计部分FLASH图形所处的HTML页面(包括数据集和数据实体)
//此HTML页面被attach到相应的布局中，每次attach之前需要先删除相应布局原有的HTML页面
var qualityGraphicsURL;
//临时变量，用来标识访问统计部分FLASH图形所处的HTML页面(包括数据集和数据实体)
//此HTML页面被attach到相应的布局中，每次attach之前需要先删除相应布局原有的HTML页面
var accessGraphicsURL;

//获得数据集和数据实体数据量表格信息的URL
var getQualityDataUrl = "getQualityData.vpage?dsuri=";
//获得数据集和数据实体数据量图形信息的URL
var getQualityGraphicsUrl = "getQualityGraphics.vpage?dsuri=";

var dsuri = "";//左侧树选中的数据集的URI
var tableId = "";//左侧树选中的数据表的Id
var dsTitleName = "";//当前选中数据集的TITLE
var tableTitleName="";//当前选中数据实体的TITLE

//以下是几个高度全局变量
var topHeight;
var footTopHeight;
var footHeight;
var buttonHeight;
var positionHeight;
var treeTitleHeight;
	
function Trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.LTrim = function() {
	return this.replace(/(^\s*)/g, "");
};
String.prototype.RTrim = function() {
	return this.replace(/(\s*$)/g, "");
};

//窗口放大时调用的方法
window.onresize = function(){
	//重新调整2U的高度和宽度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	dhxLayout2U.setSizes();
	
	//重新调整2E的高度和宽度
	document.getElementById("contentMain").style.height=dhxLayout2U.cells('b').getHeight()-buttonHeight-positionHeight;
	dhxLayout2UcellsB2E.setSizes();
	dhxLayout2UcellsB2E.cells("a").setHeight((dhxLayout2U.cells('b').getHeight()-buttonHeight-positionHeight)/2);
	
	//重新调整左侧树DIV的高度
	document.getElementById("tree").style.height=dhxLayout2U.cells('a').getHeight()-treeTitleHeight;
	
	setLayoutSizes();
};
//窗口大小变化时调用的方法
function setLayoutSizes(){
	
	var aWidth = dhxLayout2UcellsB2E.cells("a").getWidth();
	var aHeight = dhxLayout2UcellsB2E.cells("a").getHeight();
	var bWidth = dhxLayout2UcellsB2E.cells("b").getWidth();
	var bHeight = dhxLayout2UcellsB2E.cells("b").getHeight();
	
	//重新调整2E－a中的DIV的高度和宽度
	var dqDiv = document.getElementById("dqdiv");
	if(dqDiv!==null){
		dqDiv.style.width=aWidth;
		dqDiv.style.height=aHeight;
	}
	//重新调整2E-b中的DIV的高度和宽度
	var accessDiv = document.getElementById("accessdiv");
	if(accessDiv!==null){
		accessDiv.style.width=bWidth;
		accessDiv.style.height=bHeight;
	}
	
	//点击数据集时，重新调整数据量、记录数、利用率部分2U的大小
	if(dataSetQuality2U && dataSetQuality2U!=="undefined"){
		dataSetQuality2U.setSizes();
		dataSetQuality2U.cells("a").setHeight(aHeight);
		dataSetQuality2U.cells("b").setWidth(aWidth-250);
		dataSetQuality2U.cells("b").setHeight(aHeight);
	}
	
	//点击数据实体时，重新调整利用率部分2U的大小
	if(tableQuality2U && tableQuality2U!=="undefined"){
		tableQuality2U.setSizes();
		tableQuality2U.cells("a").setHeight(aHeight);
		tableQuality2U.cells("b").setWidth(aWidth-250);
		tableQuality2U.cells("b").setHeight(aHeight);
	}
	
	//点击数据集时，重新调整数据访问部分1C的布局大小
	if(dataSetAccess1C && dataSetAccess1C!=="undefined"){
		dataSetAccess1C.setSizes();
	}
	
	//点击数据实体时，重新调整数据访问部分1C的布局大小 
	if(tableAccess1C && tableAccess1C!=="undefined"){
		tableAccess1C.setSizes();
	}

}

//加载页面时调用的方法
function doOnLoad() {
	//得到头DIV和脚部DIV的高度
	topHeight = document.getElementById("topDiv").clientHeight;
	footTopHeight = document.getElementById("footTopDiv").clientHeight;
	footHeight = document.getElementById("footDiv").clientHeight;
	//设置主体DIV的高度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	dhxLayout2U = new dhtmlXLayoutObject("parentId", "2U");
	
	//左侧导航树结构
	dhxLayout2U.cells("a").setWidth("220");
	dhxLayout2U.cells("a").hideHeader();
	
	//左侧导航树
	treeTitleHeight = document.getElementById("treeTitle").clientHeight;
	document.getElementById("tree").style.height = dhxLayout2U.cells('a').getHeight()-treeTitleHeight;
	dhxLayout2UATree = new C("tree","100%","100%",0);
	dhxLayout2UATree.setImagePath("/console/shared/plugins/dhtmlx/tree/imgs/csh_vista/");
	dhxLayout2UATree.ck(dhxLayout2UATreeOnClick);
	dhtmlxAjax.get("_menu.vpage", menuhandle);
	
	//将左侧导航树所属div添加到布局中
	dhxLayout2U.cells('a').attachObject('treeDiv');
	
	//用来控制左侧树的展开与否
	//setTimeout("dhxLayout2UATree.Jo(0);", 10);
	
	//将右侧统计分析内容所属的div添加到布局中
	dhxLayout2U.cells('b').attachObject('statsContent');
	dhxLayout2U.cells('b').hideHeader();
	
	//计算右侧主体布局的高度
	buttonHeight = document.getElementById("button").clientHeight;
	positionHeight = document.getElementById("position").clientHeight;
	document.getElementById("contentMain").style.height=dhxLayout2U.cells('b').getHeight()-buttonHeight-positionHeight;
	dhxLayout2UcellsB2E = new dhtmlXLayoutObject("contentMain", "2E");
	dhxLayout2UcellsB2E.cells("a").hideHeader();
	dhxLayout2UcellsB2E.cells("b").hideHeader();
	
	dhxLayout2U.attachEvent("onPanelResizeFinish", function() {
		dhxLayout2U.setSizes();
		dhxLayout2UcellsB2E.setSizes();
		setLayoutSizes();
    });
    dhxLayout2UcellsB2E.attachEvent("onPanelResizeFinish",function(){
    	dhxLayout2UcellsB2E.setSizes();
    	setLayoutSizes();
    });
    
    //将两个按钮置为不可用状态
    document.getElementById("retrieveButton").disabled=true;
    document.getElementById("showCloudButton").disabled=true;
}

//左侧树加载并解析XML文档
function menuhandle(loader) {
	dhxLayout2UATree.loadXMLString(Trim(loader.ai.responseText));
}

//左侧树的点击事件
function dhxLayout2UATreeOnClick(id) {
	
	//删除attachURL里面的内容
	deleteIframe(qualityGraphicsURL);
	deleteIframe(accessGraphicsURL);
	
	//每次点击，都删除相应的对应的DIV，否则创建会出现错误
	var div1 = document.getElementById("accessdiv");
	var div2 = document.getElementById("dqdiv");
	if(div1!==null){
		div1.parentNode.removeChild(div1);
	}
	if(div2!==null){
		div2.parentNode.removeChild(div2);
	}
	
	var arr;//临时变量，存放节点Id分割后的数组
	
	if(id == 'directory@') {
		//将相应的变量置空
		dsuri = "";
		tableId = "";
		dsTitleName = "";
		tableTitleName="";
		
		//将两个按钮置为不可用状态
    	document.getElementById("retrieveButton").disabled=true;
    	document.getElementById("showCloudButton").disabled=true;
    
		//改变当前位置
		document.getElementById("currentPosition").innerHTML = "<strong>首页 -> 统计分析</strong>";
	}
	else if(id.indexOf('libraryName@')>=0) {
		arr = id.replace("libraryName@", "").split("-");
		dsuri = arr[0];
		dsTitleName = arr[1];
		
		//将两个按钮置为可用状态
    	document.getElementById("retrieveButton").disabled=false;
    	document.getElementById("showCloudButton").disabled=false;
    	
		//改变当前位置
		document.getElementById("currentPosition").innerHTML = "<strong>首页 -> 统计分析 -> "+dsTitleName+"</strong>";
		
		sendRequestGetLibrary();//调用方法响应数据集的点击事件
	}
	else if(id.indexOf('tableId@')>=0) {
		arr = id.replace("tableId@", "").split("-");
		tableId = arr[0];
		dsuri = arr[1];
		tableTitleName = arr[2];
		dsTitlename = arr[3];
		
		//将两个按钮置为不可用状态
    	document.getElementById("retrieveButton").disabled=true;
    	document.getElementById("showCloudButton").disabled=true;
    	
		//改变当前位置
		document.getElementById("currentPosition").innerHTML = "<strong>首页 -> 统计分析 -> "+dsTitlename+" -> "+tableTitleName+"</strong>";
		sendRequestGetTable(dsuri);//调用方法响应数据实体的点击事件
	}
}

//点击数据集时触发的方法
function sendRequestGetLibrary() {
	//得到右侧父窗口的宽度
	var bWidth = dhxLayout2U.cells("b").getWidth();
	
	//创建资源量统计区域DIV
	var dqDiv=document.createElement("div");
	dqDiv.id="dqdiv";
	dqDiv.style.width=bWidth;
	dqDiv.style.height=dhxLayout2UcellsB2E.cells("a").getHeight();
	document.body.appendChild(dqDiv);

	dhxTabbarQuality = new aj("dqdiv", "top");
	dhxTabbarQuality.setSkin('dhx_skyblue');
	dhxTabbarQuality.setImagePath("/console/shared/plugins/dhtmlx/tabbar/imgs/");
	dhxTabbarQuality.hG("dataOnAbstract","摘要","150px");
	dhxTabbarQuality.hG("dataOnDataVolume","数据量","150px");
	dhxTabbarQuality.hG("dataOnRecords","记录数","150px");
	dhxTabbarQuality.hG("dataOnUtilization","利用率","150px");
	dhxTabbarQuality.enableAutoReSize(true);
	dhxTabbarQuality.attachEvent("onSelect",doOnDSQualitySelect);
	
	//定时执行attachObject方法，不能直接执行attachObject方法
	//创建DIV需要时间，如果直接执行attachObject方法，则无法将DIV附加到相应的cell中
	setTimeout("dhxLayout2UcellsB2E.cells('a').attachObject('dqdiv')",100);
	
	//创建数据访问统计区域DIV
	var accessDiv=document.createElement("div");
	accessDiv.id="accessdiv";
	accessDiv.style.width=bWidth;
	accessDiv.style.height=dhxLayout2UcellsB2E.cells("b").getHeight();
	document.body.appendChild(accessDiv);
	
	dhxTabbarAccess = new aj("accessdiv","top");
	dhxTabbarAccess.setSkin('dhx_skyblue');
	dhxTabbarAccess.setImagePath("/console/shared/plugins/dhtmlx/tabbar/imgs/");
	dhxTabbarAccess.hG("dataTodayAccess","今天访问情况统计","150px");
	dhxTabbarAccess.hG("dataPeriodAccess","分时段访问情况统计","150px");
	dhxTabbarAccess.hG("dataSourceAccess","访问来源情况统计","150px");
	dhxTabbarAccess.hG("dataKeywordAccess","访问关键字统计top(50)","150px");
	dhxTabbarAccess.hG("dataVisitIPAccess","来访IP统计top(50)","150px");
	dhxTabbarAccess.hG("dataVisitedAccess","被访实体统计","100px");
	dhxTabbarAccess.hG("dataOperationAccess","操作类型统计","100px");
	dhxTabbarAccess.enableAutoReSize(true);
	dhxTabbarAccess.attachEvent("onSelect",doOnDSAccessSelect);
	
	setTimeout("dhxLayout2UcellsB2E.cells('b').attachObject('accessdiv')",100);
	
	//将数据集的“摘要”和“今日访问统计”置为激活状态
	dhxTabbarQuality.fP("dataOnAbstract");
	dhxTabbarAccess.fP("dataTodayAccess");
}

//左侧树选中数据集时，数据资源量显示区域的TAB页签的触发方法 
function doOnDSQualitySelect(id) {
	//将选中类型保存
	selectDSQualityType = id;
	//删除掉原有URL里面的内容
	deleteIframe(qualityGraphicsURL);
	
	dhxLayout2U.progressOn();
	dhtmlxAjax.get(getQualityDataUrl + dsuri + "&key=" + id, handleDSQualityData);
	return true;
}

//数据集的数据资源量显示区域的TAB页签的数据处理方法
function handleDSQualityData(loader) {
	
	var dataSetValue = loader.ai.responseText;
	
	//如果选中的是摘要信息
	if(selectDSQualityType == 'dataOnAbstract'){
		var dataSetAbstract = dhxTabbarQuality.cells('dataOnAbstract').attachGrid();
		dataSetAbstract.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
		dataSetAbstract.setEditable(false);
		dataSetAbstract.setHeader("序 号,实体名称,大小,记录数,空间利用率,统计日期");
		dataSetAbstract.setInitWidths("50,230,100,100,100,200");
		dataSetAbstract.setColAlign("left,left,left,left,left,left");
		dataSetAbstract.setColTypes("txt,txt,txt,txt,txt,txt");
		dataSetAbstract.setColSorting("str,str,str,str,str,str");
		dataSetAbstract.enableCollSpan(true);
		dataSetAbstract.init();
		
		dataSetValue = Trim(dataSetValue);
		if (dataSetValue !== "") {
			dataSetAbstract.parse(dataSetValue);
		}
		
		dhxLayout2U.progressOff();
	}else{//如果选中的是摘要以外的其他页签
	
		dataSetQuality2U = dhxTabbarQuality.cells(selectDSQualityType).attachLayout("2U");
		dataSetQuality2U.cells("a").setWidth("250");//将表格的宽度设置为250
		dataSetQuality2U.cells("a").hideHeader();
		dataSetQuality2U.cells("b").hideHeader();
		
		var dataSetGrid = dataSetQuality2U.cells('a').attachGrid();
		dataSetGrid.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
		dataSetGrid.clearAll(true);
		dataSetGrid.setEditable(false);
		
		if (selectDSQualityType == 'dataOnDataVolume') {
			dataSetGrid.setHeader("实体名称,数据量");
		}
		if (selectDSQualityType == 'dataOnRecords') {
			dataSetGrid.setHeader("实体名称,记录数");
		}
		if (selectDSQualityType == 'dataOnUtilization') {
			dataSetGrid.setHeader("实体名称,利用率");
		}
		dataSetGrid.setColAlign("left,left");
		dataSetGrid.setColTypes("txt,txt");
		dataSetGrid.setColSorting("str,str");
		dataSetGrid.init();
		
		dataSetValue = Trim(dataSetValue);
		if (dataSetValue !== "") {
			dataSetGrid.parse(dataSetValue);
			
			var dataSetToolBar = dataSetQuality2U.cells('b').attachToolbar();
			dataSetToolBar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
			
			//数据集的“记录数”、“利用率”以及“数据量”等页签的按钮加载
			if (selectDSQualityType == 'dataOnUtilization') {
				dataSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndLine.xml");
			} else {
				dataSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndLineAndPie.xml");
			}
			
			if (selectDSQualityType == 'dataOnDataVolume') {
				qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=volumeHistogram";
			}
			else if (selectDSQualityType == 'dataOnRecords') {
				qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=recordsHistogram";
			}
			else if (selectDSQualityType == 'dataOnUtilization') {
				qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=utilizationHistogram";
			}
			dataSetQuality2U.cells('b').attachURL(qualityGraphicsURL);
			
			//响应按钮的点击事件
			dataSetToolBar.attachEvent("onClick", function(id) {
				if(id=="pie") {
					if (selectDSQualityType == 'dataOnDataVolume') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=volumePie";
					}
					if (selectDSQualityType == 'dataOnRecords') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=recordsPie";
					}
				}
				if(id=="histogram") {
					if (selectDSQualityType == 'dataOnDataVolume') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=volumeHistogram";
					}
					if (selectDSQualityType == 'dataOnRecords') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=recordsHistogram";
					}
					if (selectDSQualityType == 'dataOnUtilization') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=utilizationHistogram";
					}
				}
				if(id=="line") {
					if (selectDSQualityType == 'dataOnDataVolume') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=volumeLine";
					}
					if (selectDSQualityType == 'dataOnRecords') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=recordsLine";
					}
					if (selectDSQualityType == 'dataOnUtilization') {
						qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=utilizationLine";
					}
				}
				
				dataSetQuality2U.cells('b').attachURL(qualityGraphicsURL);
			});
		}
		dhxLayout2U.progressOff();
	}
}
//点击数据表(数据实体)时触发的方法
function sendRequestGetTable(id) {

	//得到右侧父窗口的宽度
	var bWidth = dhxLayout2U.cells("b").getWidth();
	
	//创建资源量区域的页签
	var dqDiv=document.createElement("div");
	dqDiv.id="dqdiv";
	dqDiv.style.width=bWidth;
	dqDiv.style.height=dhxLayout2UcellsB2E.cells("a").getHeight();
	document.body.appendChild(dqDiv);
	
	dhxTabbarQuality = new aj("dqdiv", "top");
	dhxTabbarQuality.setSkin('dhx_skyblue');
	dhxTabbarQuality.setImagePath("/console/shared/plugins/dhtmlx/tabbar/imgs/");
	dhxTabbarQuality.hG("tableOnAbstract","摘要","150px");
	dhxTabbarQuality.hG("tableOnUtilization","利用率","150px");
	dhxTabbarQuality.enableAutoReSize(true);
	dhxTabbarQuality.attachEvent("onSelect",doOnTableQualitySelect);
	
	setTimeout("dhxLayout2UcellsB2E.cells('a').attachObject('dqdiv')",100);
	
	//创建访问统计区域的页签	
	var accessDiv=document.createElement("div");
	accessDiv.id="accessdiv";
	accessDiv.style.width=bWidth;
	accessDiv.style.height=dhxLayout2UcellsB2E.cells("b").getHeight();
	document.body.appendChild(accessDiv);
	
	dhxTabbarAccess = new aj("accessdiv","top");
	dhxTabbarAccess.setSkin('dhx_skyblue');
	dhxTabbarAccess.setImagePath("/console/shared/plugins/dhtmlx/tabbar/imgs/");
	dhxTabbarAccess.hG("tableTodayAccess","今天访问情况统计","150px");
	dhxTabbarAccess.hG("tablePeriodAccess","分时段访问情况统计","150px");
	dhxTabbarAccess.hG("tableSourceAccess","访问来源情况统计","150px");
	dhxTabbarAccess.hG("tableKeywordAccess","访问关键字统计top(50)","150px");
	dhxTabbarAccess.hG("tableVisitIPAccess","来访IP统计top(50)","150px");
	dhxTabbarAccess.hG("tableOperationAccess","操作类型统计","100px");
	dhxTabbarAccess.enableAutoReSize(true);
	dhxTabbarAccess.attachEvent("onSelect",doOnTableAccessSelect);
	
	setTimeout("dhxLayout2UcellsB2E.cells('b').attachObject('accessdiv')",100);
	
	dhxTabbarQuality.fP("tableOnAbstract");
	dhxTabbarAccess.fP("tableTodayAccess");
}
//数据实体，资源量页签的触发方法
function doOnTableQualitySelect(id) {

	selectTableQualityType = id;
	deleteIframe(qualityGraphicsURL);
	
	dhxLayout2U.progressOn();
	dhtmlxAjax.get(getQualityDataUrl + dsuri + "&key=" + id + "&id=" + tableId, handleTableQualityData);
	return true;
}

//左侧树点击数据实体时，利用率页签的数据处理方法
function handleTableQualityData(loader) {

	var tableValue = loader.ai.responseText;
	
	if (selectTableQualityType == 'tableOnAbstract') {
		var tableAbstract = dhxTabbarQuality.cells('tableOnAbstract').attachGrid();
		tableAbstract.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
		tableAbstract.setEditable(false);
		tableAbstract.setInitWidths("230,100,100");
		tableAbstract.setHeader("实体名称,属性名称,库中字段利用率");
		tableAbstract.setColAlign("left,left,left");
		tableAbstract.setColTypes("txt,txt,txt");
		tableAbstract.setColSorting("str,str,str");
		tableAbstract.init();
		
		tableValue = Trim(tableValue);
		if (tableValue !== "") {
			tableAbstract.parse(tableValue);
		}
		dhxLayout2U.progressOff();
	}
	else if(selectTableQualityType == 'tableOnUtilization'){
	
		tableQuality2U = dhxTabbarQuality.cells('tableOnUtilization').attachLayout("2U");
		tableQuality2U.cells("a").setWidth("250");
		tableQuality2U.cells("a").hideHeader();
		tableQuality2U.cells("b").hideHeader();
		
		var tableGrid = tableQuality2U.cells('a').attachGrid();
		tableGrid.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
		tableGrid.clearAll(true);
		tableGrid.setEditable(false);
		tableGrid.setHeader("属性名称,库中字段利用率");
		tableGrid.setColAlign("left,left");
		tableGrid.setColTypes("txt,txt");
		tableGrid.setColSorting("str,str");
		tableGrid.init();
		
		tableValue = Trim(tableValue);
		if (tableValue !== "") {
			//解析表格数据
			tableGrid.parse(tableValue);
			//装载按钮
			var dataSetToolBar = tableQuality2U.cells('b').attachToolbar();
			dataSetToolBar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
			dataSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndLine.xml");
			qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=tableUtilizationHistogram" + "&id=" + tableId;
			tableQuality2U.cells("b").attachURL(qualityGraphicsURL);
			
			//响应按钮的点击事件
			dataSetToolBar.attachEvent("onClick", function(id) {
				if(id=="histogram") {
					qualityGraphicsURL = getQualityGraphicsUrl  + dsuri + "&key=tableUtilizationHistogram" + "&id=" + tableId;
				}
				if(id=="line") {
					qualityGraphicsURL = getQualityGraphicsUrl + dsuri + "&key=tableUtilizationLine" + "&id=" + tableId;
				}
				tableQuality2U.cells("b").attachURL(qualityGraphicsURL);
			});
			
		}
		dhxLayout2U.progressOff();
	}
}

//左侧点击数据集时，数据访问统计区域页签的触发方法
function doOnDSAccessSelect(id) {
	selectDSAccessType = id;
	deleteIframe(accessGraphicsURL);
	if (id == 'dataTodayAccess' || id == 'dataPeriodAccess' || id == 'dataSourceAccess') {
		accessSelectGraphics();
	}
	if (id == 'dataKeywordAccess' || id == 'dataVisitIPAccess' || id == 'dataVisitedAccess' || id == 'dataOperationAccess') {
		accessSelectGrid();
	}
	return true;
}

//左侧点击数据集时，数据访问统计区域点击“今天访问统计”、“分时段访问统计”以及“数据来源访问统计”的触发方法
function accessSelectGraphics() {
	//这里不能使用attachLayout的形式，报“参数无效”的错误
	dataSetAccess1C = new dhtmlXLayoutObject(dhxTabbarAccess.cells(selectDSAccessType), "1C");
	dataSetAccess1C.cells("a").hideHeader();
	
	//加载按钮
	var dataAccessSetToolBar = dataSetAccess1C.cells("a").attachToolbar();
	dataAccessSetToolBar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
	
	//不同类型图形加载的按钮不一样
	if (selectDSAccessType == 'dataSourceAccess') {
		dataAccessSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndPie.xml");
	} else {
		dataAccessSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndLine.xml");
	}
	
	if (selectDSAccessType == 'dataTodayAccess') {
		accessGraphicsURL = dataSetTodayAccess + dsuri + "&key=todayHistogram";
	}
	if (selectDSAccessType == 'dataPeriodAccess') {
		accessGraphicsURL = dataSetPeriodAccess + dsuri + "&key=periodHistogram";
	}
	if (selectDSAccessType == 'dataSourceAccess') {
		accessGraphicsURL = dataSetAccessSource+ dsuri + "&key=sourceHistogram";
	}
	dataSetAccess1C.cells("a").attachURL(accessGraphicsURL );
	
	//按钮点击的响应事件
	dataAccessSetToolBar.attachEvent("onClick", function(id) {
		deleteIframe(accessGraphicsURL);
		if(id=="pie") {
			if (selectDSAccessType == 'dataSourceAccess') {
				accessGraphicsURL = dataSetAccessSource+ dsuri + "&key=sourcePie";
			}
		}
		if(id=="histogram") {
			if (selectDSAccessType == 'dataTodayAccess') {
				accessGraphicsURL = dataSetTodayAccess+ dsuri + "&key=todayHistogram";
			}
			if (selectDSAccessType == 'dataPeriodAccess') {
				accessGraphicsURL = dataSetPeriodAccess+ dsuri + "&key=periodHistogram";
			}
			if (selectDSAccessType == 'dataSourceAccess') {
				accessGraphicsURL = dataSetAccessSource+ dsuri + "&key=sourceHistogram";
			}
		}
		if(id=="line") {
			if (selectDSAccessType == 'dataTodayAccess') {
				accessGraphicsURL = dataSetTodayAccess + dsuri + "&key=todayLine";
			}
			if (selectDSAccessType == 'dataPeriodAccess') {
				accessGraphicsURL = dataSetPeriodAccess + dsuri + "&key=periodLine";
			}
		}
		
		dataSetAccess1C.cells("a").attachURL(accessGraphicsURL);
	});
}

//左侧点击数据集时，右侧点击“关键词统计”、“访问IP统计”等页签的触发方法
function accessSelectGrid() {
	dhxLayout2U.progressOn();
	dhtmlxAjax.get(dataSetAccessDataUrl + dsuri + "&key=" + selectDSAccessType, handleDSAccessData);
}

//左侧点击数据集时，右侧点击“关键词统计”、“访问IP统计”等页签的数据处理方法
function handleDSAccessData(loader) {
	
	var accessValue = loader.ai.responseText;
	
	var accessGrid = dhxTabbarAccess.cells(selectDSAccessType).attachGrid();
	accessGrid.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	accessGrid.setEditable(false);
	
	if (selectDSAccessType == 'dataKeywordAccess') {
		accessGrid.setHeader("序 号,关键字,被访实体,访问次数");
		accessGrid.setInitWidths("50,150,230,100");
		accessGrid.setColAlign("left,left,left,left");
		accessGrid.setColTypes("txt,txt,txt,txt");
		accessGrid.setColSorting("str,str,str,str");
	}
	if (selectDSAccessType == 'dataVisitIPAccess') {
		accessGrid.setHeader("序 号,来访IP,IP所属地区,访问次数");
		accessGrid.setInitWidths("50,150,100,100");
		accessGrid.setColAlign("left,left,left,left");
		accessGrid.setColTypes("txt,txt,txt,txt");
		accessGrid.setColSorting("str,str,str,str");
	}
	if (selectDSAccessType == 'dataVisitedAccess') {
		accessGrid.setHeader("序 号,被访实体,访问次数,最后更新时间");
		accessGrid.setInitWidths("50,230,100,200");
		accessGrid.setColAlign("left,left,left,left");
		accessGrid.setColTypes("txt,txt,txt,txt");
		accessGrid.setColSorting("str,str,str,str");
	}
	if (selectDSAccessType == 'dataOperationAccess') {
		accessGrid.setHeader("序 号,访问操作,访问次数");
		accessGrid.setInitWidths("50,100,100");
		accessGrid.setColAlign("left,left,left");
		accessGrid.setColTypes("txt,txt,txt");
		accessGrid.setColSorting("str,str,str");
	}
	accessGrid.init();
	accessGrid.parse(accessValue);
	dhxLayout2U.progressOff();
}

//左侧点击数据实体时，数据访问统计区域页签的触发方法
function doOnTableAccessSelect(id) {
	selectTableAccessType = id;
	deleteIframe(accessGraphicsURL);
	if (id == 'tableTodayAccess' || id == 'tablePeriodAccess' || id == 'tableSourceAccess') {
		tableAccessSelectGraphics();
	}
	if (id == 'tableKeywordAccess' || id == 'tableVisitIPAccess' || id == 'tableOperationAccess') {
		tableAccessSelectGrid();
	}
	return true;
}
//左侧树选中数据表，访问统计部分TAB页签改变调用的函数（图形部分）
function tableAccessSelectGraphics() {
	tableAccess1C = new dhtmlXLayoutObject(dhxTabbarAccess.cells(selectTableAccessType), "1C");
	tableAccess1C.cells("a").hideHeader();
	
	var dataAccessSetToolBar = tableAccess1C.cells("a").attachToolbar();
	dataAccessSetToolBar.setIconsPath("/console/shared/plugins/dhtmlx/toolbar/common/imgs/");
	
	if (selectTableAccessType == 'tableSourceAccess') {
		dataAccessSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndPie.xml");
	} else {
		dataAccessSetToolBar.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/HistogramAndLine.xml");
	}
	
	if (selectTableAccessType == 'tableTodayAccess') {
		accessGraphicsURL = entityTodayAccess  + dsuri + "&key=todayHistogram" + "&id=" + tableId;
	}
	if (selectTableAccessType == 'tablePeriodAccess') {
		accessGraphicsURL = entityPeriodAccess + dsuri + "&key=periodHistogram" + "&id=" + tableId;
	}
	if (selectTableAccessType == 'tableSourceAccess') {
		accessGraphicsURL = entityAccessSource + dsuri + "&key=sourceHistogram" + "&id=" + tableId;
	}
	tableAccess1C.cells("a").attachURL(accessGraphicsURL);
	
	dataAccessSetToolBar.attachEvent("onClick", function(id) {
		deleteIframe(accessGraphicsURL);
		if(id=="pie") {
			if (selectTableAccessType == 'tableSourceAccess') {
				accessGraphicsURL = entityAccessSource + dsuri + "&key=sourcePie" + "&id=" + tableId;
			}
		}
		if(id=="histogram") {
			if (selectTableAccessType == 'tableTodayAccess') {
				accessGraphicsURL = entityTodayAccess + dsuri + "&key=todayHistogram" + "&id=" + tableId;
			}
			if (selectTableAccessType == 'tablePeriodAccess') {
				accessGraphicsURL = entityPeriodAccess + dsuri + "&key=periodHistogram" + "&id=" + tableId;
			}
			if (selectTableAccessType == 'tableSourceAccess') {
				accessGraphicsURL = entityAccessSource  + dsuri + "&key=sourceHistogram" + "&id=" + tableId;
			}
		}
		if(id=="line") {
			if (selectTableAccessType == 'tableTodayAccess') {
				accessGraphicsURL = entityTodayAccess  + dsuri + "&key=todayLine" + "&id=" + tableId;
			}
			if (selectTableAccessType == 'tablePeriodAccess') {
				accessGraphicsURL = entityPeriodAccess  + dsuri + "&key=periodLine" + "&id=" + tableId;
			}
		}
		tableAccess1C.cells("a").attachURL(accessGraphicsURL);
	});
}
//左侧树选中数据表，访问统计部分TAB页签改变调用的函数（表格部分）
function tableAccessSelectGrid() {
	dhxLayout2U.progressOn();
	dhtmlxAjax.get(entityAccessDataUrl + dsuri + "&key=" + selectTableAccessType + "&id=" + tableId, handleTableAccessData);
}
//左侧树选中数据表，访问统计部分TAB页签改变调用的数据处理方法（表格部分）
function handleTableAccessData(loader) {
	
	var tableAccessValue = loader.ai.responseText;
	
	var tableAccessGrid = dhxTabbarAccess.cells(selectTableAccessType).attachGrid();
	tableAccessGrid.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	tableAccessGrid.setEditable(false);
	
	if (selectTableAccessType == 'tableKeywordAccess') {
		tableAccessGrid.setHeader("序 号,关键字,被访实体,访问次数");
		tableAccessGrid.setInitWidths("50,150,230,100");
		tableAccessGrid.setColAlign("left,left,left,left");
		tableAccessGrid.setColTypes("txt,txt,txt,txt");
		tableAccessGrid.setColSorting("str,str,str,str");
	}
	if (selectTableAccessType == 'tableVisitIPAccess') {
		tableAccessGrid.setHeader("序 号,来访IP,IP所属地区,访问次数");
		tableAccessGrid.setInitWidths("50,150,100,100");
		tableAccessGrid.setColAlign("left,left,left,left");
		tableAccessGrid.setColTypes("txt,txt,txt,txt");
		tableAccessGrid.setColSorting("str,str,str,str");
	}
	if (selectTableAccessType == 'tableOperationAccess') {
		tableAccessGrid.setHeader("序 号,访问操作,访问次数");
		tableAccessGrid.setInitWidths("50,100,100");
		tableAccessGrid.setColAlign("left,left,left");
		tableAccessGrid.setColTypes("txt,txt,txt");
		tableAccessGrid.setColSorting("str,str,str");
	}
	tableAccessGrid.init();
	tableAccessGrid.parse(tableAccessValue);
	dhxLayout2U.progressOff();
}

//删除attachUrl里面的东西
function deleteIframe(formId) {
    if(formId === null) {
    	return;
    }
    var obj = document.getElementsByTagName('iframe');
	for (var i=0;i<obj.length;i=i+1) {
		var objsrc = document.getElementsByTagName('iframe')[i].src;
		if(objsrc.indexOf(formId)>=0) {
			document.getElementsByTagName('iframe')[i].src = "";
		}
	}
}

//点击“立即抽取调用的方法”
function retrieve(){
	dhxLayout2U.progressOn();
	dhtmlxAjax.get("retrieve.vpage?dsuri=" + dsuri + "&forward=getQualityData.vpage" + "&key="+selectDSQualityType, handleDSQualityData);
}
//点击显示云图调用的方法
function showCloud(){
	window.open("showCloud.vpage?dsuri="+dsuri+"&key=dataOnCloud","newwindow", "height=728, width=1024, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no");
}