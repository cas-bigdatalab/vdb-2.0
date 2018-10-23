function getFilesSdef(div)
{
	var jsoCtrl = $("span", div)[0].jsoCtrl;
	var sdef = document.createElement("sdef");
	var fsn = addChild(sdef, "files");
	for(var k in jsoCtrl.files)
	{
		var file = jsoCtrl.files[k];
		var fn = addChild(fsn, "file");
		addChild(fn, "id", file.id);
	}
	
	div.ondataavailable(sdef.outerHTML);
}

function getLocalFileSdef(div)
{
	var jsoCtrl = $(':input', div).val();
	var sdef = document.createElement("sdef");
	var fsn = addChild(sdef, "files");
	var file = jsoCtrl;
	var fn = addChild(fsn, "file");
	addChild(fn, "id", "1f62837025d7eb020125d7ebb8cd0038");
	div.ondataavailable(sdef.outerHTML);
}