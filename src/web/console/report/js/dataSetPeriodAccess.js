var period="week";//选中的时间段，值可以为：week,thismonth,month,period，分别表示本周、本月、指定月份、指定日期
var keyId = "";//表示选中的图形类型，值可以为periodHistogram和periodLine，分别表示柱状图和曲线图
var dsuri = "";//数据集的URI

//点击时间段类型时首先调用的方法
function periodClick(obj){
	//切换时间段时，默认选中“所有操作”
	if(obj.id=="d0"){
		document.getElementById("allRadio").checked=true;
		periodWeekChart();
	}
	else if(obj.id=="d1"){
		document.getElementById("allRadio").checked=true;
		periodThisMonth();
	}
	else if(obj.id=="d2"){
		var value = document.getElementById("month").value;
		document.getElementById("allRadio").checked=true;
		periodMonth(value);
	}
	else if(obj.id=="d3"){
		//do nothing
	}
	var anyMonth = document.getElementById("d2");
	var anyDate = document.getElementById("d3");
	if(anyMonth.checked) document.getElementById("anyMonth").style.display="block";
	else document.getElementById("anyMonth").style.display="none";
	if(anyDate.checked) document.getElementById("dateExtent").style.display="block";
	else document.getElementById("dateExtent").style.display="none";
}
//点击本周访问统计时调用的方法
function periodWeekChart(){
	period="week"
	setRequestParams();
	var getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&streamName=access_period_week&random='+Math.random();
	
	loadData(getUrl);
}
//点击本月访问统计时，调用的方法
function periodThisMonth(){
	period="thismonth"
	setRequestParams();
	var getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&streamName=access_period_month&random='+Math.random();
	
	loadData(getUrl);
}
//点击指定月份统计时，调用的方法
function periodMonth(month){
	period="month"
	setRequestParams();
	var getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&streamName=access_period_month&month='+month+'&random='+Math.random();

	loadData(getUrl);
}
//点击指定日期统计时调用的方法
function periodBydate(){
	period="period"
	setRequestParams();
	var start_date=document.getElementById("Text1").value;
	var end_date=document.getElementById("Text2").value;
	if(start_date=="" || end_date==""){
		window.alert("请选择开始和结束日期！");
		return;
	}
	if(start_date>end_date){
		window.alert("开始日期大于结束日期，请正确填写");
		return;
	}
	
	var getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&streamName=access_period_date&startDate='+start_date+'&endDate='+end_date+'&random='+Math.random();
	document.getElementById("allRadio").checked=true;
	
	loadData(getUrl);
}
//点击操作类型时调用的方法
function periodAndoperation(operation){
	setRequestParams();
	var getUrl='';
	if(period=='week'){
		getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&operation='+operation+'&streamName=access_period_week&random='+Math.random();
	}
	else if(period=='period'){
		var start_date=document.getElementById("Text1").value;
		var end_date=document.getElementById("Text2").value;
		getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&operation='+operation+'&streamName=access_period_date&startDate='+start_date+'&endDate='+end_date+'&random='+Math.random();
	}
	else if(period=='month'){
		var select=document.getElementById('month');
		var month;
		for (i=0;i<select.length;i++){
			if (select.options[i].selected == true){
				month=i+1;
			}
		}
		getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&operation='+operation+'&streamName=access_period_month&month='+month+'&random='+Math.random();
	}
	else if(period=='thismonth'){
		getUrl='doPeriodAccess.vpage?dsuri='+dsuri+'&operation='+operation+'&streamName=access_period_month&random='+Math.random();
	}
	
	loadData(getUrl);
}
//加载数据的函数，jQuery
function loadData(getUrl){
	$.ajax({
		type: 'POST',
		async: false,
		cache: false,
		dataType: 'json',
		url: getUrl,
		success: function(r) {//r就是返回的数据，列表已经被序列化成了一个js中的array，存在属性d当中。
			handleData("\""+r[0]+"\"","\""+r[1]+"\"");
		},
		error: function(xhr) {
			alert('error: ' + xhr.statusText);//出错时ajax会返回XMLHttp对象。
		}
	});	
}

//数据处理函数，根据返回的数据调用相应的参数文件，输出相应的图形
function handleData(xml,data) {
	if (keyId == "periodLine") {
		var so = new SWFObject("../../console/shared/plugins/amcharts/FusionCharts/amline.swf", "chart1Id", "650", "280", "0", "0");
		so.addVariable("settings_file", encodeURIComponent("../../console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Line.xml"));
	}
	if (keyId == "periodHistogram") {
		var so = new SWFObject("../../console/shared/plugins/amcharts/FusionCharts/amcolumn.swf?random="+Math.random(), "chart1Id", "650", "280", "0", "0");
		if(xml==''){
			so.addVariable("settings_file", encodeURIComponent("../../console/shared/plugins/amcharts/FusionChartsXML/amcolumn_Histogram_edit.xml"));
		}else{
			so.addVariable("chart_settings", encodeURIComponent(xml));
		}
	}
	so.addVariable("path","/console/shared/plugins/amcharts/FusionCharts/");
	so.addVariable("chart_data", encodeURIComponent(data));
	so.addParam("wmode","transparent");
	so.write("perioddiv");
}
//取得request范围内的dsuri变量的值
function setRequestParams()
{
	dsuri = document.getElementById("dsuri").value;
	keyId = document.getElementById("key").value;
}