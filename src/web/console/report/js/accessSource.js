var key = "";//表示选中的图形类型
var dsuri = "";//数据集的URI
var id="";//数据实体的ID
var graphicsText="";//数据集或数据实体今日访问的数据

function accessSourceChart() {
	setRequestParams();
	var xml="";
	var swf="";
	if (key =='todayHistogram' || key == 'sourceHistogram') {
		swf = "/console/shared/plugins/amcharts/FusionCharts/amcolumn.swf";
		xml = "/console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Histogram.xml";
	}
	if (key =='todayLine') {
		swf = "/console/shared/plugins/amcharts/FusionCharts/amline.swf";
		xml = "/console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Line.xml";
	}
	if (key == 'sourcePie') {
		swf = "/console/shared/plugins/amcharts/FusionCharts/ampie.swf";
		xml = "/console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Pie.xml";
	}
	
	// 判断数据graphicsText是否有值
	if (graphicsText == "") {
		var graphicsDiv = document.getElementById('graphicsDiv');		
		graphicsDiv.innerHTML = "";
		return;
	}
	
	// 打印数据
	var so = new SWFObject(swf, "chart1Id", "725", "280", "0", "0");
	so.addVariable("path","/console/shared/plugins/amcharts/FusionCharts/");
	so.addVariable("settings_file", encodeURIComponent(xml));			
	so.addVariable("chart_data", encodeURIComponent(graphicsText));
	so.write("graphicsDiv");
}
//数据集访问来源统计时，改变操作类型调用的方法
function operationCitySpec(operation){
	setRequestParams();
	var getUrl="";
	if (key == 'sourcePie') {
		getUrl='doAccessSourceOperation.vpage?dsuri='+dsuri+'&streamName=access_area_pie&operation='+operation+'&random='+Math.random();
		loadData(getUrl);
	}
	if (key == 'sourceHistogram') {
		getUrl='doAccessSourceOperation.vpage?dsuri='+dsuri+'&streamName=access_area&operation='+operation+'&random='+Math.random();
		loadData(getUrl);
	}
}
//数据实体访问来源统计时，改变操作类型调用的方法
function operationEntityCitySpec(operation){
	setRequestParams();
	var getUrl="";
	if (key == 'sourceHistogram') {
		getUrl='doAccessSourceOperation.vpage?dsuri='+dsuri+'&id='+id+'&streamName=access_entity_area&operation='+operation+'&random='+Math.random();
		loadData(getUrl);
	}
	if (key == 'sourcePie') {
		getUrl='doAccessSourceOperation.vpage?dsuri='+dsuri+'&id='+id+'&streamName=access_entity_area_pie&operation='+operation+'&random='+Math.random();
		loadData(getUrl);
	}
}

//加载数据的函数，jQuery
function loadData(getUrl){
	$.ajax({
		type: 'POST',
		cache: false,
		dataType: 'html',
		url: getUrl,
		success: function(r) {//r就是返回的数据，列表已经被序列化成了一个js中的array，存在属性d当中。
			handleData(r);
		},
		error: function(xhr) {
			alert('error: ' + xhr.statusText);//出错时ajax会返回XMLHttp对象。
		}
	});	
}

//数据处理函数，根据返回的数据调用相应的参数文件，输出相应的图形
function handleData(data) {
	if (key == "sourcePie") {
		var so = new SWFObject("/console/shared/plugins/amcharts/FusionCharts/ampie.swf", "chart1Id", "725", "280", "0", "0");
		so.addVariable("settings_file", encodeURIComponent("/console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Pie.xml"));
	}
	if (key == "sourceHistogram") {
		var so = new SWFObject("/console/shared/plugins/amcharts/FusionCharts/amcolumn.swf", "chart1Id", "725", "280", "0", "0");
		so.addVariable("settings_file", encodeURIComponent("/console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Histogram.xml"));
	}
	so.addVariable("path","/console/shared/plugins/amcharts/FusionCharts/");
	so.addVariable("chart_data", encodeURIComponent(data));
	
	so.write("graphicsDiv");
}
//取得request范围内的dsuri变量的值
function setRequestParams()
{
	dsuri = document.getElementById("dsuri").value;
	key = document.getElementById("key").value;
	if(document.getElementById("id")!=null){
		id = document.getElementById("id").value;
	}
	graphicsText = document.getElementById("graphicsText").value;
}