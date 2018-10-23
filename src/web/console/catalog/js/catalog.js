function insertMeta(button)
{
	addItem(button.gif, button.mid, button.title);
}

function submitItems(form)
{
	var b = metaList.getElementsByTagName('button');
	var ids = "";

	if(b && b.length > 0)
	{
		for(i = 0; i < b.length; i++)
		{
			var a = b[i];
			ids += a.mid + ";";
		}
	}
	
	form.ids.value = ids;
}

function removeAll()
{
	metaList.innerHTML = "";
}

function addItem(gif, mid, title)
{
	metaList.setActive();
	document.selection.createRange().pasteHTML("<button title='" + title + "' mid=" + mid + " contentEditable=false><img src='" + gif + "'> " + title + "</button><br>");
}

function addAll()
{
	removeAll();
	var b = options.getElementsByTagName('button');

	if(b && b.length > 0)
	{
		for(i = 0; i < b.length; i++)
		{
			var a = b[i];
			insertMeta(a);		
		}
	}
}

function checkRadioOptions(radios, value)
{
	if(radios)
	{
		if(!radios.length)
			radios = new Array(radios);
			
		for(i = 0; i < radios.length; i++)
		{
			var radio = radios[i];
			if(radio.value == value)
			{
				radio.checked = true;
				break;
			}
		}
	}
}

function checkSelectOptions(select, value)
{
	if(select)
	{
		for(i = 0; i < select.options.length; i++)
		{
			var option = select.options[i];
			if(option.value == value)
			{
				option.selected = true;
			}
		}
	}
}

//对于只读文本框，禁用Backspace键
document.documentElement.onkeydown = function(evt){ 
	if(window.event.keyCode==8){
		var type=window.event.srcElement.type;//获取触发事件的对象类型
		var reflag=window.event.srcElement.readOnly;//获取触发事件的对象是否只读
		var disflag=window.event.srcElement.disabled;//获取触发事件的对象是否可用
		if(type=="text"||type=="textarea")
		{
			if(reflag||disflag){
				window.event.returnValue=false;
			}
		}
		else
		{ 
			window.event.returnValue=true;
		}
	}
}