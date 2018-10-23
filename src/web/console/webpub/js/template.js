var layout;
var dhxTree;
//以下是几个高度全局变量
var topHeight;
var footTopHeight;
var footHeight;
var treeTitleHeight;

window.onresize = function(){ 
	//重新调整2U的高度和宽度
	document.getElementById("parentId").style.height = document.body.clientHeight- topHeight -footTopHeight- footHeight;
	layout.setSizes();
	
	//重新调整左侧树DIV的高度
	document.getElementById("tree").style.height=layout.cells('a').getHeight()-treeTitleHeight;
};
 
function init() 
{
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
    
    //左侧导航树
	treeTitleHeight = document.getElementById("treeTitle").clientHeight;
	document.getElementById("tree").style.height = layout.cells('a').getHeight()-treeTitleHeight;
	dhxTree = new C("tree","100%","100%",0);
	dhxTree.setImagePath("/console/shared/plugins/dhtmlx/tree/imgs/csh_vista/");
	dhxTree.ss("iconText.gif", "folderOpen.gif", "folderClosed.gif");
	dhxTree.ck(tondblclick);
	dhxTree.loadXML("js/menu.xml");
	
	//将左侧导航树所属div添加到布局中
	layout.cells('a').attachObject('treeDiv');
    window.setTimeout("dhxTree.Jo(0);", 10);
}

function tondblclick(id) {
	if(id.indexOf('directory@')>=0){
		return false;
	}
	layout.cells("b").attachURL(id);
}