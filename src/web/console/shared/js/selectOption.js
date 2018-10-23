function addAll(objA, objB) {
	var newOpt;
	var i = objA.options.length;
	objB.options.length = 0;
	while (i > 0) {
		newOpt = new Option(objA.options[objA.options.length - i].text, objA.options[objA.options.length - i].value);
		objB.options.add(newOpt);
		i--;
	}
}
function deleteAll(objA) {
	objA.options.length = 0;
}

function checkBeforeMove(objA, objB)
{

if(objB.options.length>1)
{
}
}
function moveAtoBsel(objA, objB) {
	for (i = 0; i < objA.options.length; i++) {
		if (objA.options[i].selected == true) {
			if (!existOption(objA.options[i].value, objB)) {
				newOpt = new Option(objA.options[i].text, objA.options[i].value);
				objB.options.add(newOpt);
			}
		}
	}
	return;
}
function deleteSel(objA) {

	var selectCount = 0;
	for (i = 0; i < objA.options.length; i++) 
	{
		if (objA.options[i].selected == true) 
		{
			selectCount++;
		}
	}
	
	if(selectCount==0)
	{
		alert("提示： 请选择要删除的用户组！");
		return false;
	}
		
	if(objA.options.length==0)
		alert("没有需要删除的项！");
	else
	{
		for (i = 0; i < objA.options.length; i++) {
			if (objA.options[i].selected == true) {
				objA.options[objA.selectedIndex] = null;
				i--;
			}
		}
		return true;
	}
}
function existOption(optionValue, objB) {
	for (j = 0; j < objB.options.length; j++) {
		if (objB.options[j].value == optionValue) {
			return true;
		}
	}
	return false;
}

function checkBeforeMove(objA, objB)
{
	if(objB.options.length > 0)
	{
		alert("提示： 一个用户同时只能属于一个用户组！");
	}
	else
	{
		var selectCount = 0;
		for (i = 0; i < objA.options.length; i++) 
		{
			if (objA.options[i].selected == true) 
			{
				selectCount++;
			}
		}
		 if(selectCount > 1)
			alert("提示： 一个用户同时只能属于一个用户组！");
		else if(selectCount==0)
			alert("提示： 请选择一个用户组！");
		else
		{
			moveAtoBsel(objA, objB);
		}
	}
}
