var headerName = "Security Center";
var dhxLayout2U;
var dhxLayout2UcellsB2E;
var dhx2U = "2U";
var dhx3E = "3E";
var groupsEvent;
var usersEvent;
var param = 0;
var myDataProcessor;
var dhxgroups;
var dhxusers;
var usersRecordEntity;
var size, tabbarGrid, doUserLog1C, doUserLogGrid, userId, visitId, doVisitLog1C, doVisitLogGrid, flg;
var strName;
var strId;
var groupsList;
var groupsEntity;
var groupsStyle;
var groupsTools;
var usersList;
var usersEntity;
var usersStyle;
var usersTools;

//以下是几个高度全局变量
var topHeight;
var footTopHeight;
var footHeight;
var treeTitleHeight;
var positionHeight;

function doOnPanelResize() {
	if (flg == "doUserLog") {
		if (doUserLog1C != undefined && doUserLog1C != null) {
			doOnTabbarGrid(visitId);
		}
	}
	if (flg == "doVisitLog") {
		if (doVisitLog1C != undefined && doVisitLog1C != null) {
			doOnTabbarGrid(visitId);
		}
	}
}
function jq(menuitemId, type) 
{
	dhxLayout2UATree.hE(dhxLayout2UATree.contextID);
	dhxLayout2UATree.Jz(dhxLayout2UATree.contextID);
	groupsOnClick(dhxLayout2UATree.contextID);
	if (menuitemId == "auth") 
	{
		usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
		usersEntity.cells("a").tI("授权当前用户组");
		usersEntity.cells("a").attachURL("grantGroup.vpage?gid=" + dhxLayout2UATree.contextID + "&t=" + new Date());
	}
	if (menuitemId == "addGroup") 
	{
		var gcode = dhxLayout2UATree.contextID;
		jQuery.ajax({type:"POST", url:"getGroupCode.vpage", data:{gcode:gcode}, success:function (responseText) {
			usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
			usersEntity.cells("a").tI("添加子组");
			usersEntity.cells("a").attachURL("addGroup.vpage?gid=" + dhxLayout2UATree.contextID + responseText + "&t=" + new Date());
		}});
	}
	if (menuitemId == "edit") 
	{
		usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
		usersEntity.cells("a").tI("\u4fee\u6539\u7528\u6237\u7ec4");
		usersEntity.cells("a").attachURL("updateGroup.vpage?gcode=" + dhxLayout2UATree.contextID + "&t=" + new Date());
	}
	if (menuitemId == "addUser") 
	{
		usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
		usersEntity.cells("a").tI("\u6dfb\u52a0\u7528\u6237");
		usersEntity.cells("a").attachURL("addUser.vpage?id=" + dhxLayout2UATree.contextID + "&t=" + new Date());
	}
	if (menuitemId == "delete") 
	{
		if (confirm("\u60a8\u786e\u5b9a\u8981\u5220\u9664\u8be5\u7528\u6237\u7ec4\u53ca\u8be5\u7ec4\u6240\u6709\u5b50\u7ec4\u5417\uff1f")) 
		{
			dhxLayout2UATree.deleteItem(dhxLayout2UATree.contextID, true);
			usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
			usersEntity.cells("a").tI("\u5220\u9664\u7528\u6237\u7ec4");
			usersEntity.cells("a").attachURL("deleteGroups.vpage?gid=" + dhxLayout2UATree.contextID + "&t=" + new Date());
		}
	}
}
function addItem(fatherId, id, context) 
{
	dhxLayout2UATree.insertNewItem(fatherId, id, context, 0, 0, 0, 0, "SELECT");
}
function setMenu(id) 
{
	treeOnClick(id);
	dhxLayout2UATree.hE(id);
	dhxLayout2UATree.Jz(id);
	if (id == 1) 
	{
		menu.hideItem("delete");
		menu.hideItem("edit");
	} 
	else 
	{
		menu.showItem("delete");
		menu.showItem("edit");
	}
}
window.onresize = function(){

	//重新调整2U的高度和宽度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	dhxLayout2U.setSizes(); 
	
	//重新调整2E的高度和宽度
	document.getElementById("contentMain").style.height=dhxLayout2U.cells('b').getHeight()-positionHeight;
	dhxLayout2UcellsB2E.setSizes();
	
	//重新调整左侧树DIV的高度
	document.getElementById("tree").style.height=dhxLayout2U.cells('a').getHeight()-treeTitleHeight;
}
function doOnLoad() 
{
	//得到头DIV和脚部DIV的高度
	topHeight = document.getElementById("topDiv").clientHeight;
	footTopHeight = document.getElementById("footTopDiv").clientHeight;
	footHeight = document.getElementById("footDiv").clientHeight;
	//设置主体DIV的高度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	dhxLayout2U = new dhtmlXLayoutObject("parentId", "2U");
	dhxLayout2U.cells("a").setWidth("300");
	dhxLayout2U.cells("a").hideHeader();
	
	//左侧导航树
	treeTitleHeight = document.getElementById("treeTitle").clientHeight;
	document.getElementById("tree").style.height = dhxLayout2U.cells('a').getHeight()-treeTitleHeight;
	dhxLayout2UATree = new C("tree","100%","100%",0);
	dhxLayout2UATree.setImagePath("/console/shared/plugins/dhtmlx/tree/imgs/csh_vista/");
	dhxLayout2UATree.ck(treeOnClick);
	dhxLayout2UATree.Fa(setMenu);
	dhtmlxAjax.get("_menu.vpage?" + "t=" + new Date(), menuhandle);
	
	//将左侧导航树所属div添加到布局中
	dhxLayout2U.cells('a').attachObject('treeDiv');

	menu = new dhtmlXMenuObject();
	menu.renderAsContextMenu();
	menu.attachEvent("onClick", jq);
	menu.setIconsPath("/console/shared/plugins/dhtmlx/menu/imgs/");
	menu.loadXML("/console/security/js/dhxmenu.xml");
	dhxLayout2UATree.enableContextMenu(menu);
	
	setTimeout("dhxLayout2UATree.Jo(0);", 600);
	
	dhxLayout2U.cells('b').attachObject('securityContent');
	dhxLayout2U.cells('b').hideHeader();
	
	//计算右侧主体布局的高度
	positionHeight = document.getElementById("position").clientHeight;
	document.getElementById("contentMain").style.height=dhxLayout2U.cells('b').getHeight()-positionHeight;
	dhxLayout2UcellsB2E = new dhtmlXLayoutObject("contentMain", "2E");
	
	dhxLayout2UcellsB2E.cells("a").tI("\u7528\u6237\u5217\u8868");
	dhxLayout2UcellsB2E.cells("a").setHeight("200");
	dhxLayout2UcellsB2E.cells("b").tI("\u5b89\u5168\u4e2d\u5fc3");
	
	usersTools = dhxLayout2UcellsB2E.cells("a").attachToolbar();
	usersTools.setIconsPath("/console/security/images/");
	usersTools.loadXML("/console/shared/plugins/dhtmlx/toolbar/common/securityUsersTools.xml");
	param = param + 1;
	dhtmlxAjax.get("doSelectUsers.vpage?id=all&param=" + param, doUsersGridData);
	usersTools.attachEvent("onClick", function (id) {
		flg = "";
		var rowNo = usersList.bI();
		usersEvent = id;
		if (id == "listUser") 
		{
			param = param + 1;
			dhtmlxAjax.get("doSelectUsers.vpage?id=all&param=" + param, doUsersGridData);
		}
		if (id == "usersAddUser") {
			usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
			usersEntity.cells("a").tI("\u6dfb\u52a0\u7528\u6237");
			usersEntity.cells("a").attachURL("addUser.vpage");
		}
		if (id == "usersSetUser") {
			if (rowNo == null) {
				alert("\u8bf7\u9009\u62e9\u7528\u6237!");
			} else {
				usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
				usersEntity.cells("a").tI("\u8bbe\u7f6e\u5f53\u524d\u7528\u6237");
				usersEntity.cells("a").attachURL("updateUserGroup.vpage?uid=" + encodeURI(encodeURI(rowNo)) + "&t=" + new Date());
			}
		}
		if (id == "usersUpdate") {
			if (rowNo == null) {
				alert("\u8bf7\u9009\u62e9\u7528\u6237!");
			}
			else if(rowNo=="Anonymous")
			{
				alert("匿名用户不能修改密码！");
			} 
			else {
				usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
				usersEntity.cells("a").tI("\u4fee\u6539\u767b\u5f55\u5bc6\u7801");
				usersEntity.cells("a").attachURL("updatePassword.vpage?uid=" + encodeURI(encodeURI(rowNo)) + "&t=" + new Date());
			}
		}
		if (id == "usersDelete") {
			if (rowNo == null) {
				alert("\u8bf7\u9009\u62e9\u7528\u6237!");
			}else if(rowNo=="Anonymous")
			{
				alert("匿名用户不能被删除！");
			} 
			else {
				if (confirm("\u60a8\u786e\u5b9a\u8981\u5220\u9664\u8be5\u7528\u6237\u5417\uff1f")) {
					usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
					usersEntity.cells("a").tI("\u5220\u9664\u7528\u6237");
					usersEntity.cells("a").attachURL("deleteUsers.vpage?uid=" + encodeURI(encodeURI(rowNo)) + "&t=" + new Date());
				}
			}
		}
		if (id == "usersAuthorization") {
			if (rowNo == null) {
				alert("\u8bf7\u9009\u62e9\u7528\u6237!");
			} else {
				usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
				usersEntity.cells("a").tI("\u6388\u6743\u5f53\u524d\u7528\u6237");
				usersEntity.cells("a").attachURL("grantUser.vpage?uid=" + encodeURI(encodeURI(rowNo)) + "&t=" + new Date());
			}
		}
		if (id == "usersRecord") {
			if (rowNo == null) {
				alert("\u8bf7\u9009\u62e9\u7528\u6237!");
			} else {
				userId = rowNo;
				dhtmlxAjax.get("doTabbarGridName.vpage?" + "t=" + new Date(), usersRecord);
			}
		}
	});
	
	dhxLayout2U.attachEvent("onPanelResizeFinish", function() {
		dhxLayout2U.setSizes();
		dhxLayout2UcellsB2E.setSizes();
    });
}
function menuhandle(loader) {
	var str = loader.ai.responseText;
	str = Trim(str);
	if (str.indexOf("j_spring_security_check") >= 0) {
		document.location.href = document.location.href;
		return;
	}
	dhxLayout2UATree.loadXMLString(Trim(loader.ai.responseText));
}
function Trim(str) {
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function treeOnClick(id) {
	groupsOnClick(id);
	usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
	usersEntity.cells("a").tI("授权当前用户组");
	usersEntity.cells("a").attachURL("grantGroup.vpage?gid=" + id + "&t=" + new Date());
}
function groupsOnClick(id) {
	dhxLayout2UATree.hE(id);
	dhxLayout2UATree.Jz(id);
	dhtmlxAjax.get("doSelectUsers.vpage?id=" + id + "&t=" + new Date(), doUsersGridData);
}
function doUsersGridData(loader) {
	var str = loader.ai.responseText;
	str = Trim(str);
	if (str.indexOf("j_spring_security_check") >= 0) {
		document.location.href = document.location.href;
		return;
	}
	var usersValue = loader.ai.responseText;
	usersList = dhxLayout2UcellsB2E.cells("a").attachGrid();
	usersList.setImagePath("/console/shared/plugins/dhtmlx/grid/imgs/");
	usersList.setEditable(false);
	usersList.setInitWidths("50,250,300");
	usersList.setHeader("\u5e8f \u53f7,\u7528\u6237\u5217\u8868,\u7528\u6237\u7ec4");
	usersList.setColAlign("left,left,left");
	usersList.setColTypes("txt,txt,txt");
	usersList.setColSorting("str,str,str");
	usersList.init();
	usersList.parse(usersValue);
	usersList.attachEvent("onRowSelect", doOnUsersRowSelect);
}
function doOnUsersRowSelect(id) {
	usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
	usersEntity.cells("a").tI("授权当前用户");
	usersEntity.cells("a").attachURL("grantUser.vpage?uid=" + encodeURI(encodeURI(id)) + "&t=" + new Date());
}
function refreshUsers() {
	dhtmlxAjax.get("doSelectUsers.vpage" + "?t=" + new Date(), doUsersGridData);
}
function usersRecord(loader) {
	var str = loader.ai.responseText;
	str = Trim(str);
	if (str.indexOf("j_spring_security_check") >= 0) {
		document.location.href = document.location.href;
		return;
	}
	var tabbarName = loader.ai.responseText;
	var strArr = tabbarName.split("*");
	size = strArr.length;
	strName = tabbarName.split("*");
	strId = tabbarName.split("*");
	for (var i = 0; i < size; i++) {
		if (strArr[i] == "") {
			strName[i] = "\u767b\u5f55\u5386\u53f2";
			strId[i] = "userLogin";
		} else {
			var str = strArr[i].indexOf("&");
			strName[i] = strArr[i].substring(0, str);
			strId[i] = strArr[i].substring(str + 1);
		}
	}
	usersEntity = new dhtmlXLayoutObject(dhxLayout2UcellsB2E.cells("b"), "1C");
	tabbarGrid = usersEntity.cells("a").attachTabbar();
	tabbarGrid.setSkin("dhx_skyblue");
	tabbarGrid.setImagePath("/console/shared/plugins/dhtmlx/tabbar/imgs/");
	for (var i = 0; i < size; i++) {
		tabbarGrid.hG(strId[i], strName[i], "100px");
	}
	tabbarGrid.attachEvent("onSelect", doOnTabbarGrid);
	tabbarGrid.fP("userLogin");
}
function doOnTabbarGrid(id) {
	visitId = id;
	if (id == "userLogin") {
		flg = "doUserLog";
		doUserLog1C = new dhtmlXLayoutObject(tabbarGrid.cells(visitId), "1C");
		doUserLog1C.cells("a").tI("Loading...");
		dhtmlxAjax.get("doUserLoginLog.vpage?user=" + userId + "&t=" + new Date(), doUserLog);
	} else {
		flg = "doVisitLog";
		doVisitLog1C = new dhtmlXLayoutObject(tabbarGrid.cells(visitId), "1C");
		doVisitLog1C.cells("a").tI("Loading...");
		dhtmlxAjax.get("doVisitLog.vpage?username=" + userId + "&datasetId=" + id + "&t=" + new Date(), doVisitLog);
	}
	return true;
}
function doUserLog(loader) {
	var str = loader.ai.responseText;
	str = Trim(str);
	if (str.indexOf("j_spring_security_check") >= 0) {
		document.location.href = document.location.href;
		return;
	}
	dhxLayout2UcellsB2E.cells("b").progressOn();
	var doUserLogData = loader.ai.responseText;
	doUserLogData = Trim(doUserLogData);
	doUserLogGrid = doUserLog1C.cells("a").attachGrid();
	doUserLogGrid.setEditable(false);
	doUserLogGrid.setInitWidths("50,550");
	doUserLogGrid.setHeader("\u5e8f\u53f7,\u660e\u7ec6");
	doUserLogGrid.setColAlign("left,left");
	doUserLogGrid.setColTypes("txt,txt");
	doUserLogGrid.setColSorting("str,str");
	doUserLogGrid.init();
	if (doUserLogData != "") {
		doUserLogGrid.parse(doUserLogData);
		dhxLayout2UcellsB2E.cells("b").progressOff();
	}
	dhxLayout2UcellsB2E.cells("b").progressOff();
	doUserLog1C.cells("a").tI(userId + " \u5df2\u767b\u5f55\u6b21 " + doUserLogGrid.iD() + " \u6b21");
}
function doVisitLog(loader) {
	var str = loader.ai.responseText;
	str = Trim(str);
	if (str.indexOf("j_spring_security_check") >= 0) {
		document.location.href = document.location.href;
		return;
	}
	var doVisitLogData = loader.ai.responseText;
	doVisitLogData = Trim(doVisitLogData);
	doVisitLogGrid = doVisitLog1C.cells("a").attachGrid();
	doVisitLogGrid.setEditable(false);
	doVisitLogGrid.setInitWidths("50,550");
	doVisitLogGrid.setHeader("\u5e8f\u53f7,\u660e\u7ec6");
	doVisitLogGrid.setColAlign("left,left");
	doVisitLogGrid.setColTypes("txt,txt");
	doVisitLogGrid.setColSorting("str,str");
	doVisitLogGrid.init();
	if (doVisitLogData != "") {
		doVisitLogGrid.parse(doVisitLogData);
	}
	doVisitLog1C.cells("a").tI(userId + " \u8bbf\u95ee\u4e86 " + doVisitLogGrid.iD() + " \u6b21\u8be5\u6570\u636e\u96c6");
}

