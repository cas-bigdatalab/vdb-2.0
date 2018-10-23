var layout;
var dhxTree;
//以下是几个高度全局变量
var topHeight;
var footTopHeight;
var footHeight;
var treeTitleHeight;

function init() 
{
	var xmlurl="getDynamicTree.vpage?date="+new Date();
	
	//得到头DIV和脚部DIV的高度
	topHeight = document.getElementById("topDiv").clientHeight;
	footTopHeight = document.getElementById("footTopDiv").clientHeight;
	footHeight = document.getElementById("footDiv").clientHeight;
	//设置主体DIV的高度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	layout = new dhtmlXLayoutObject("parentId", "2U");
	
    layout.cells("a").setWidth("220"); 
    layout.cells("a").hideHeader();
    
    layout.cells("b").hideHeader();
    layout.cells("b").attachURL("welcome.vpage");
	
	//左侧导航树
	treeTitleHeight = document.getElementById("treeTitle").clientHeight;
	document.getElementById("tree").style.height = layout.cells('a').getHeight()-treeTitleHeight;
	dhxTree = new C("tree","100%","100%",0);
	dhxTree.setImagePath("/console/shared/plugins/dhtmlx/tree/imgs/csh_vista/");
	dhxTree.ss("iconText.gif", "folderOpen.gif", "folderClosed.gif");
    dhxTree.ck(tondblclick);
    
	//将左侧导航树所属div添加到布局中
	layout.cells('a').attachObject('treeDiv');
	
    dhxTree.rx("id");//自动将ID添加到xmlurl的后面，作为变量传入后台
    dhxTree.Cn("child");//是否有子节点的识别标志
    
    dhxTree.eG(xmlurl);//设置目录树的动态加载
    dhxTree.enableSmartXMLParsing(true);
    dhxTree.loadXML(xmlurl);
}

//增加节点，根据节点类型的不同响应不同的增加操作 [直接刷新父节点即可]
//调用TREE相应的INSERT、DELETE操作也可以，但如果要显示图片，也要执行相应的刷新操作
//insertNewItem方法在执行时，需要判断此节点是否有子节点，如果有子节点则需要添加CHILD属性，如果没有，则不能添加CHILD属性
function addItem(fatherId, id, text)
{
	//dhxTree.insertNewItem(fatherId,id,text,0,0,0,0,'SELECT,CHILD');
	dhxTree.rq(fatherId);//刷新父节点，刷新父节点即执行一次父节点的点击操作，如果不刷新则无法显示对应的图片
}

//删除节点
function deleteItem(fatherId,id)
{
	//不执行刷新父节点的操作也是可以的，只是在图片、数字无法得到体现
	//删除方法必须要执行
	dhxTree.deleteItem(id,true);
	dhxTree.rq(fatherId);
}

//修改节点text内容
function setItemText(fatherId,id,text){
	if(fatherId==="-1"){
		dhxTree.setItemText(id,text);
	}else{
		dhxTree.rq(fatherId);//刷新父节点，刷新父节点即执行一次父节点的点击操作，如果不刷新则无法显示对应的图片
	}
}

//刷新父节点
function refreshItem(fatherId){
	dhxTree.rq(fatherId);
}

//响应节点的点击操作，根据节点不同展现不同的页面
function tondblclick(id) {
	var url="";
	var tempId="";
	var arr;
	if(id.indexOf('directory@')>=0){
		url="updateDomain.vpage";
	}else if(id.indexOf('repository')>=0){
		url="repository.vpage";
	}else if(id.indexOf('DataSetList')>=0){
		url="listDataSets.vpage";
	}else if(id.indexOf('DomainView')>=0){
		url="updateDomainView.vpage";
	}else if(id.indexOf('LogOut')>=0){
		if(window.confirm("您真的要注销当前用户吗？")){
			url="/console/shared/logout.jsp";
		}else{
			return ;
		}
	}else if(id.indexOf("DataSet@")>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateDataSet.vpage?dsid="+tempId;
	}else if(id.indexOf('Repository@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateRepository.vpage?dsid="+tempId;
	}else if(id.indexOf('EntityList@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="listEntities.vpage?dsid="+tempId;
	}else if(id.indexOf('QueryList@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="listQueries.vpage?dsid="+tempId;
	}else if(id.indexOf('RelationList@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="listRelations.vpage?dsid="+tempId;
	}else if(id.indexOf('Query@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateQuery.vpage?id="+tempId;
	}else if(id.indexOf('Relation@')>=0){
		arr = id.split("@");
		tempId=arr[2];
		if(arr[1]=="m2m"){ url="updateM2M.vpage?id="+tempId;}
		else if(arr[1]=="m2o") {url="updateO2M.vpage?id="+tempId;}
	}else if(id.indexOf('Entity@')>=0){
		arr = id.split("@");
		url="updateEntity.vpage?id="+arr[1]+"&dsid="+arr[2];
	}else if(id.indexOf('Identifier@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateIdentifier.vpage?id="+tempId;
	}else if(id.indexOf('FieldList@')>=0){
		arr = id.split("@");
		url="listFields.vpage?id="+arr[1]+"&dsid="+arr[2];
	}else if(id.indexOf('Field@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateField.vpage?id="+tempId;
	}else if(id.indexOf('PageView@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="manageEntityPage.vpage?entityId="+tempId;
	}else if(id.indexOf('EntityView@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateEntityView.vpage?entityId="+tempId;
	}else if(id.indexOf('DataSetView@')>=0){
		arr = id.split("@");
		tempId=arr[1];
		url="updateDataSetView.vpage?id="+tempId;
	}
	if(url.indexOf("?")>0)
	{
		url = url + "&t="+new Date();
	}
	else
	{
		url = url + "?t="+new Date();
	}
	layout.cells("b").attachURL(url);
}
window.onresize = function(){ 
	//重新调整2U的高度和宽度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	layout.setSizes();
	
	//重新调整左侧树DIV的高度
	document.getElementById("tree").style.height=layout.cells('a').getHeight()-treeTitleHeight;
}