function insertMeta(button,p)
{
	addItem($("#" + button.id + "gif").val(),$("#" + button.title + "mid").val(), button.title,p);
}

function insertMeta2(p,q,button){
	addItem($("#" + p + "_" + q + "gif").val(),button.getAttribute("mid"), button.title,p);
}

function submitItems(form,p)
{
	for(i=1;i<=p;i++){
		var id = 'metaList' + i;
		var ids = 'ids' + i;
		var b = document.getElementById(id).getElementsByTagName('button');		
		
		var value = '';
		if(b && b.length > 0)
		{
			for(j = 0; j < b.length; j++)
			{
				var a = b[j];
				value += a.getAttribute("mid") + ";"; 
			}
		}
		
		$('#' + ids).val(value);
	}
}

function removeAll(p)
{
	var metaList =	document.getElementById("metaList" + p);
	metaList.innerHTML = "";
}

function addItem(gif, mid, title,p)
{
	var metaList = document.getElementById("metaList" + p);
	metaList.innerHTML = metaList.innerHTML + "<button type='button' title='" + title + "' mid=" + mid + " contentEditable=false><img src='" + gif + "'> " + title + "</button><br>";
	//metaList.setActive();
	//document.selection.createRange().pasteHTML("<button title='" + title + "' mid=" + mid + " contentEditable=false><img src='" + gif + "'> " + title + "</button><br>");
}

function initItem(gif, mid, title,p)
{
 		var metaList = document.getElementById("metaList" + p );
	    metaList.innerHTML = metaList.innerHTML + "<button type='button' title='" + title + "' mid=" + mid + " contentEditable=false><img src='" + gif + "'> " + title + "</button><br>";
	    //metaList.setActive();
		//document.selection.createRange().pasteHTML("<button title='" + title + "' mid=" + mid + " contentEditable=false><img src='" + gif + "'> " + title + "</button><br>");
}

function addAll(p)
{
	removeAll(p);
	var b = options.getElementsByTagName('button');

	if(b && b.length > 0)
	{
		for(i = 0; i < b.length; i++)
		{
			var a = b[i];
			insertMeta2(p,i+1,a);		
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

function copy(p,q){
	if(q!=0&&p!=0)document.getElementById('metaList' + p ).innerHTML = document.getElementById("metaList" + q).innerHTML;
}