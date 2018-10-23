function getDateSdef(div)
{
	var sdef = document.createElement("sdef");
	var dateStr = $(':input', div).val();
	var ms = null;
	if(dateStr != "")
	{
		str1 = dateStr.split(" ");
		date = str1[0].split("-");
		
		if(str1.length == 2)
		{
			time = str1[1].split(":");
			ms = new Date(date[0], date[1] - 1, date[2], time[0], time[1], time[2]);
		}
		else
		{
			ms = new Date(date[0], date[1] - 1, date[2]);
		}
	}

	if(ms != null)
	{
		addChild(sdef, "value", ms.getTime());
		addChild(sdef, "title", ms.toLocaleDateString());
	}
	else
	{
		addChild(sdef, "value", "");
	}
	
	div.ondataavailable(sdef.outerHTML);
}
