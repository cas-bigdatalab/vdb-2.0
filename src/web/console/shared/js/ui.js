var seqid = new Date().getMilliseconds();

function toggleRecord(tr)
{
	var checked = "true" == tr.getAttribute("checked");
	tr.attributes.getNamedItem("checked").nodeValue= checked ? "false" : "true";
	tr.attributes.getNamedItem("class").nodeValue = checked ? tr.getAttribute("originalClass"): "checked";
}

function hiliteRecord(tr, hilite)
{
	if(hilite)
		tr.attributes.getNamedItem("class").nodeValue = 'mouseover';
	else
		tr.attributes.getNamedItem("class").nodeValue = ('true' == this.getAttribute("checked") ? 'checked' : tr.getAttribute("originalClass"));
}

function hiliteRecord(tr, hilite)
{
	if(hilite)
	{
		tr.attributes.getNamedItem("class").nodeValue = 'mouseover';
	}
	else
	{
		tr.attributes.getNamedItem("class").nodeValue = ('true' == tr.getAttribute("checked") ? 'checked' : tr.getAttribute("originalClass"));
	}
}

function checkRecords(srcCheck)
{

	var grid = $(srcCheck).parents("div#[@class=DataGrid]")[0].jsoDataGrid;
	//var rows = grid.htmlBinder.all("DATAGRID_ROW");
	var rows = $("tr[@name=DATAGRID_ROW]",grid.htmlBinder); 

	if(rows)
	{
		if(!rows.length)
		{
			rows = new Array(rows);
		}
		
		for(var i = 0; i < rows.length; i++)
		{
			var tr = rows[i];
			tr.attributes.getNamedItem("checked").nodeValue = srcCheck.checked ? "true" : "false";
			tr.attributes.getNamedItem("class").nodeValue =srcCheck.checked ? "checked" : tr.getAttribute("originalClass");
		}
	}
}

function gotoPage1(a, gotoPageValue)
{
	var gridElement = $(a).parents("div.DataGrid")[0];
	var grid = gridElement.jsoDataGrid;
	var query = gridElement.jsoQuery;
	query.pageIndex = gotoPageValue;
	grid.refresh();
}

//显示、隐藏高级查询框
function toggleQueryForm(container)
{
	container.style.display = (container.style.display == 'none' ? 'block' : 'none');
}

function setOrderBy(td)
{
	var gridElement = $(td).parents("div.DataGrid")[0];
	var grid = gridElement.jsoDataGrid;
	var query = gridElement.jsoQuery;
	query.pageIndex = 1;
	query.orderField = td.getAttribute("fieldUri");
	query.orderAsc = (td.getAttribute("orderAsc") == "desc" ? "asc" : "desc");
	grid.refresh();
}

function onQueryFieldSelect(fields, operatorArea, editorArea)
{
 	loadHTML1("/console/shared/query/listQueryOperators.vpage", "selectedFieldId=" + fields.options[fields.selectedIndex].value + "&fieldsCtrlId=" + fields.id + "&jsoFormBinder=" + $(fields).parents("div")[0].id, operatorArea);
}

function initializeQueryField(fields, operatorArea, editorArea)
{
	if(typeof fields!="undefined")
	{
		fields.operatorArea = operatorArea;
		fields.editorArea = editorArea;
		
		onQueryFieldSelect(fields, operatorArea, editorArea);
	}
}

function initializeQueryOperators(oplistArea, editorArea, selectedFieldId, editorName)
{
	
	oplist=$("select", oplistArea)[0];
	if(typeof oplist!="undefined")
	{
		oplist.editorArea = editorArea;
		oplist.selectedFieldId = selectedFieldId;
		oplist.editorName = editorName;
		
		onQueryOperatorSelect(oplist);
	}
}

function onQueryOperatorSelect(oplist)
{
	var op = oplist.options[oplist.selectedIndex];

	if(op.value == '' || op.getAttribute("isUnaryExpr") == 'true')
	{
		oplist.editorArea.innerHTML = "";
	}
	else
	{
		loadHTML1("/console/shared/query/listQueryEditors.vpage", "selectedFieldId=" + oplist.selectedFieldId + "&editorName=" + oplist.editorName + "&jsoFormBinder=" + $(oplist).parents("div")[0].id, oplist.editorArea);
	}
}


function loadHTML(url0, parameters0, div)
{
	if(div)
		div.innerHTML = "<p style='font-size:10.5pt;color:#888888'><img src='/console/shared/images/waiting.gif'> 正在加载...</p>";

	seqid++;
	if(parameters0 == null)
		parameters0 = "seqid=" + seqid;
	else
		parameters0 += "&seqid=" + seqid;

	$.post(url0, parameters0, function(data)
	{
		if(div)
			div.innerHTML = data;
	});
}


function loadHTMLAsync(url0, parameters0, div,async)
{
	if(div)
		div.innerHTML = "<p style='font-size:10.5pt;color:#888888'><img src='/console/shared/images/waiting.gif'> 正在加载...</p>";

	seqid++;
	if(parameters0 == null)
		parameters0 = "seqid=" + seqid;
	else
		parameters0 += "&seqid=" + seqid;
	
	   $.ajax({
                url : url0,
                data:parameters0,
                cache : false, 
                async : async,
                type : "POST",
                success : function (data){
                    if(div)
						div.innerHTML = data;
                }
            });
	
}

String.prototype.replaceAll = stringReplaceAll;

function stringReplaceAll(AFindText,ARepText)
{
	raRegExp = new RegExp(AFindText,"g");
	return this.replace(raRegExp,ARepText)
}

function loadHTML1(url0, parameters0, div)
{
	if(div)
		div.innerHTML = "<p style='font-size:10.5pt;color:#888888'><img src='/console/shared/images/waiting.gif'> 正在加载...</p>";

	seqid++;
	if(parameters0 == null || parameters0 == "")
		parameters0 = "seqid=" + seqid;
	else
		{
			parameters0 += "&seqid=" + seqid;
		}
		parameters0=parameters0.replaceAll("=","\":\"");
		parameters0=parameters0.replaceAll("&","\",\"");
		parameters0="{\""+parameters0+"\"}";

		var jsonData = parameters0;   
		jsonData=eval('('+parameters0+')'); 
		$(div).load(url0,jsonData);

}

function httpPost(url0, parameters0, func)
{
	$.post(url0, parameters0, func);
}

String.prototype.trim = function()  
{  
    // 用正则表达式将前后空格  
    // 用空字符串替代。  
    return this.replace(/(^\s*)|(\s*$)/g, "");  
}

function openDialog(sURL, sTitle)
{
	return window.open(sURL, "", 'height=600,width=850,left='+(screen.AvailWidth-850)/2+',top='+(screen.AvailHeight-600)/2+',status=no,toolbar=no,scrollbars=yes,menubar=no,location=no,resizable=yes');
}

function openModalDialog(sURL, sTitle, arg)
{
	return showModalDialog("/console/editor/dialogFrame.jsp?title=" + escape(sTitle) + "&url=" + escape(sURL), arg,  'dialogWidth:850px;dialogHeight:600px;resizable:yes;status:no;');
}
