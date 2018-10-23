
function getMultiSelectSdef(div) {
	var type = $(":input[name='style']", div)[0].value;
	var name = $(":input[name='fieldName']", div)[0].value;
	var check = $("input[name='" + name + "']:checked");  //得到所有被选中的checkbox
	var checkVal="";              //定义变量
	check.each(function (i) {        //循环拼装被选中项的值
		checkVal = checkVal + ";" + $(this).val();
	});
	if(checkVal.length>0)
		checkVal = checkVal.substring(1);
	var sdef = document.createElement("sdef");
	addChild(sdef, "value", checkVal);
	div.ondataavailable(sdef.outerHTML);
}

