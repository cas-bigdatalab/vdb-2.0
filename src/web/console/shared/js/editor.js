function JsoField(id, name, title, hint)
{
	this.id = id;
	this.name = name;
	this.title = title;
	this.hint = hint;
}

function addChild(node, childNodeName, childNodeValue)
{
	var childNode = document.createElement(childNodeName);
	if(childNodeValue != null)
	{
		childNode.innerText = childNodeValue;
	}
	
	node.appendChild(childNode);
	return childNode;
}

function getInputSdef(div)
{
	var sdef = document.createElement("sdef");
	addChild(sdef, "value", $(':input', div).val());
	div.ondataavailable(sdef.outerHTML);
}

/////////////////////////////////////////////
///////////////form
/////////////////////////////////////////////

function submitForm(form)
{
	thisBeanForm.bean = new Object();
	
	for (var i = 0; i < thisBeanForm.fields.length; i++)
	{
		var field = thisBeanForm.fields[i];
		var type = field.type;
		var ie = $("#E_" + field.id);
		if(ie)
		{
			ie.ondataavailable = function(sdef)
			{
				submitSdef(field.name, sdef);
			}
			var fun = "get" + type + "Sdef(ie)";
			eval(fun);
		}
	}
}

function submitSdef(fieldName, sdef)
{
	thisBeanForm.bean[fieldName] = sdef;

	var items1 = 0;
	for(i in thisBeanForm.bean)
	{
		items1++;
	}

	var items2 = thisBeanForm.fields.length;
	
	//omg...i am the last one
	if(items1 == items2)
	{
		doSubmitForm();
	}
}

function errorWindows(message)
{
	dhxWins = new dhtmlXWindows();
	dhxWins.enableAutoViewport(true);
	dhxWins.attachViewportTo("winVP");
	dhxWins.setImagePath("dhtmlx/windows/imgs/");
	document.body.scrollTop=0;
	win1 = dhxWins.createWindow("win1", 200, 200, 300, 200);
	win1.setModal(true);	
	win1.tI("\u7f16\u8f91\u7ed3\u679c");
	var htmlString = "<div style='height:100%;background-Color:#f7f9fa;font-size:10pt;padding:20px' ><img src='/console/shared/images/error.gif' border=0 align=absbottom>&nbsp;" + message+"</div>";
	win1.attachHTMLString(htmlString);
}

function doSubmitForm()
{
	var jsonq = JSON.stringify(thisBeanForm);
	httpPost("DoUpdateBean.action",{page:thisBeanForm.page,form:escape(jsonq)} , function (data)
	{
		var status = JSON.parse(data);
		if (status.code == 400)
		{
			var hasSource = false;
			if(status.source != null)
			{
				for (var i = 0; i < thisBeanForm.fields.length; i++)
				{
					var field = thisBeanForm.fields[i];
					if(field.id == status.source)
					{
						hasSource = true;
						errorWindows(status.message);
						break;
					}
				}
			}
						
			if(!hasSource)
			{		
				errorWindows(status.message);
			}
		}
		else
		{
			window.showUpdateResult(status.id);
		}
	});	
}