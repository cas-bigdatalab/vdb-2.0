function getEnumSdef(div)
{
	var type = $(":input[name='style']", div)[0].value;
	var name = $(":input[name='fieldName']", div)[0].value;
	if(type=="EnumRadiosEdit")
	{
		var sdef = document.createElement("sdef");
		addChild(sdef, "value", $("input[name='" + name + "']:checked").val());
		div.ondataavailable(sdef.outerHTML);
	}
	else
	{
		return getInputSdef(div);
	}
}
