	var dhxWins;
	function onLoad()
	{
		dhxWins = new dhtmlXWindows();
	    dhxWins.enableAutoViewport(true);
	    dhxWins.setImagePath("/console/shared/plugins/dhtmlx/windows/imgs/");
	}

	function getWidgetLib(wid)
	{
		document.location.href = "listWidgets.vpage?widgetlib="+wid;
	}
	
	function deleteWidget(id)
	{
		if(!confirm('组件删除将不能被恢复，是否继续？'))
		{
			return;
		}
		dhtmlxAjax.get("doDeleteWidget.vpage?widget="+id,function (loader)
		{
			alert(loader.ai.responseText);
			document.location.reload();
		});
	}
	function updateWidget(id)
	{
		var dhxWindow = dhxWins.createWindow("w1", 0, 20, 720, parseInt(document.body.clientHeight*0.8));
	    dhxWindow.setModal(true);
	    dhxWindow.tI("组件修改");
	    //dhxWindow.button("minmax1").hide();
	    //alert(dhxTabbar_attach.wX());
    	dhxWindow.attachURL("doUpdateWidget.vpage?widgetlib=" + id);
   		dhxWindow.centerOnScreen();
    	//dhxWindow.denyResize();
	}
	
	function copyWidget(id)
	{
		
		var dhxWindow = dhxWins.createWindow("w1", 0, 20, 520, 200);
	    dhxWindow.setModal(true);
	    dhxWindow.tI("组件复制");
	    //dhxWindow.button("minmax1").hide();
	    //alert(dhxTabbar_attach.wX());
    	dhxWindow.attachURL("doCopyDialog.vpage?widget="+id);
   		dhxWindow.centerOnScreen();
   		
		/*
		var str=prompt("组件名(由英文加数字组成)","");

		var reg = /^[a-zA-Z0-9]+$/g;
		if(str.match(reg)==null)
		{
			alert("输入的组件名包含中文或非法字符，组件名只能为英文字母加数字组成");
			return;
		}
		//return;
		dhtmlxAjax.get("doCopyWidget.vpage?widget="+id+"&newName="+str,function (loader)
		{
			alert(loader.ai.responseText);
			document.location.reload();
		});
		*/
	}
	