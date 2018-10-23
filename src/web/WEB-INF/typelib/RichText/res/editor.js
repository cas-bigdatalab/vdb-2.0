function getRichTextSdef(div)
{	


	var str = $(":input[name='editorName']", div)[0].value;
	var oEditor = FCKeditorAPI.GetInstance(str);
	var sdef = document.createElement("sdef");
	var value = oEditor.GetXHTML(true);
		
	addChild(sdef, "value", value);
	
	var stext = sdef.outerHTML;
	var re = new RegExp("<br>","gi");
	var re1 = new RegExp("<BR>","gi");
	var re2 = new RegExp("&nbsp;","gi");

	stext = stext.replace(re, "\r\n");
	stext = stext.replace(re1, "\r\n");
	stext = stext.replace(re2, "");
	
	div.ondataavailable(stext);
}
