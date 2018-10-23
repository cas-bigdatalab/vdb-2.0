function getQueryDateSdef(div)
{
	var sdef = document.createElement("sdef");
	var dateStr = $(':input', div).val();
	if(dateStr != "")
	{
		addChild(sdef, "value", dateStr);
		addChild(sdef, "title", dateStr);
	}
	else
	{
		addChild(sdef, "value", "");
	}
	
	div.ondataavailable(sdef.outerHTML);
}