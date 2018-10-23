		//以下三个函数，利用初始化时获得的文本，进行三种参数的更新，需要三个函数是因为显示的div不同
		function updateSizeChart(chartSWF){
			//chartSWF为目标swf文件的路径
			var chart1 = new FusionCharts(chartSWF, "chart1Id", "500", "400", "0", "0");
			chart1.setDataXML(sizeText);
			chart1.render("chart1div");
		}
		function updateRecordNumChart(chartSWF){
			var chart2 = new FusionCharts(chartSWF, "chart1Id", "500", "400", "0", "0");
			chart2.setDataXML(recordNumText);
			chart2.render("chart2div");
		}
		function updateUsedRateChart(chartSWF){
			var chart3 = new FusionCharts(chartSWF, "chart1Id", "500", "400", "0", "0");
			chart3.setDataXML(usedRateText);
			chart3.render("chart3div");
		}
		//初始化页面时，调用下面三个函数发送Ajax请求

		// 09-12-16 追加修正
		// Chart2div 柱状图
		function updateRecordNumChartHistogram(chartSWF){
			var so = new SWFObject(chartSWF, "amcolumn", "580", "400", "8", "#FFFFFF");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Histogram.xml"));
			so.addVariable("chart_data", encodeURIComponent(recordNumText));
			so.addVariable("preloader_color", "#999999");
			so.write("chart2div");
		}
		// Chart2div 折线图
		function updateRecordNumChartLine(chartSWF){
			var so = new SWFObject(chartSWF, "amline", "580", "400", "8", "#FFFFFF");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Line.xml"));
			so.addVariable("chart_data", encodeURIComponent(recordNumText));
			so.addVariable("preloader_color", "#999999");
			so.write("chart2div");
		}
		// Chart2div 饼 图
		function updateRecordNumChartPie(chartSWF){
			var so = new SWFObject(chartSWF, "ampie", "580", "400", "0", "0");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Pie.xml"));
			so.addVariable("chart_data", encodeURIComponent(recordNumAmpieText));
			so.addVariable("preloader_color", "#ffffff");
			so.write("chart2div");
		}
		
		// Chart3div 柱状图
		function updateRecordNumChartChart3divHistogram(chartSWF){
			var so = new SWFObject(chartSWF, "amcolumn", "580", "400", "8", "#FFFFFF");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Histogram.xml"));
			so.addVariable("chart_data", encodeURIComponent(usedRateText));
			so.addVariable("preloader_color", "#999999");
			so.write("chart3div");
		}
		// Chart3div 折线图
		function updateRecordNumChartChart3divLine(chartSWF){
			var so = new SWFObject(chartSWF, "amline", "580", "400", "8", "#FFFFFF");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Line.xml"));
			so.addVariable("chart_data", encodeURIComponent(usedRateText));
			so.addVariable("preloader_color", "#999999");
			so.write("chart3div");
		}
		// Chart1div 饼 图
		function updateRecordNumChartChart1divPie(chartSWF){
			var so = new SWFObject(chartSWF, "ampie", "580", "400", "0", "0");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Pie.xml"));
			so.addVariable("chart_data", encodeURIComponent(sizeTextPie));
			so.addVariable("preloader_color", "#ffffff");
			so.write("chart1div");
		}
		
		// Chart3div 柱状图
		function updateRecordNumChartChart1divHistogram(chartSWF){
			var so = new SWFObject(chartSWF, "amcolumn", "580", "400", "8", "#FFFFFF");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Histogram.xml"));
			so.addVariable("chart_data", encodeURIComponent(sizeText));
			so.addVariable("preloader_color", "#999999");
			so.write("chart1div");
		}
		// Chart3div 折线图
		function updateRecordNumChartChart1divLine(chartSWF){
			var so = new SWFObject(chartSWF, "amline", "580", "400", "8", "#FFFFFF");
			so.addVariable("settings_file", encodeURIComponent("FusionChartsXML/amcolumn_Line.xml"));
			so.addVariable("chart_data", encodeURIComponent(sizeText));
			so.addVariable("preloader_color", "#999999");
			so.write("chart1div");
		}