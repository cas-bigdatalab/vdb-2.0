/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except
using on sites which belongs to DHTMLX LTD.

Please contact xcch@dhtmlx.com to obtain necessary
license for usage of dhtmlx components.
===================================================================
*/function eXcell_link(cell){this.cell=cell;this.grid=this.cell.parentNode.grid;
this.edit=function(){
this.val=this.getValue();
this.obj=document.createElement("DIV");

var text="";
var d = this.val.indexOf("^");
var title = this.val.substring(0,d)||"";
var link = this.val.substring(d+1,this.val.length)||"";
text+= "<table style='border:1px solid blue;background-color:#aed8dd'><tr align='center'><td>标题：</td><td><input type='text' value='"+title+"'/></td></tr><tr><td align='center'  >链接：</td><td><textarea >" +link+"</textarea></td></tr></table>";
this.obj.editor=this;
this.obj.innerHTML=text;

document.body.appendChild(this.obj);
this.obj.style.position="absolute";
this.obj.onclick=function(e){(e||event).cancelBubble=true;return true};

var kw=this.grid.fI(this.cell);
this.obj.style.left=kw[0]+"px";
this.obj.style.top=kw[1]+this.cell.offsetHeight+"px";
this.obj.getValue=function(){
	var text = this.childNodes[0].childNodes[0].value;
	return text;
}
};
this.getValue=function(){
if(this.cell.firstChild.getAttribute)return this.cell.firstChild.innerHTML+"^"+$(this.cell.firstChild).attr("href");else return ""};

this.detach=function(val){if(this.obj){this.setValue($(this.obj).find("input").val()+"^"+$(this.obj).find("textarea").text());this.obj.editor=null;this.obj.parentNode.removeChild(this.obj);this.obj=null};return this.val!=this.getValue()};

/*实际生成一个<a href=""></a>*/
this.setValue=function(val){
	var title="";
	var link="";
	if(!val||val.match(/^\s*$/))
		val = "^";
	else if(val.indexOf("^")<0)
	{
		val = "^" + val;
	}
	else
	{
		title = val.substring(0,val.indexOf("^"));
		link = val.substring(val.indexOf("^")+1,val.length);
	}
	if( (typeof(val)!="number")&&(!val||val.toString().PA()=="") )
	{this.dq("&nbsp;",qg);return(this.cell.mG=true)};

	var qg = new Array(2);
	qg[0] = title||link;
	qg[1] = link;
	
	if(qg.length>1)
	{
		qg[1]="href='"+qg[1]+"'";
		if(qg.length==3)
			qg[1]+=" target='"+qg[2]+"'";
		else qg[1]+=" target='_blank'"
	}
	this.dq("<a "+qg[1]+" onclick='(_isIE?event:arguments[0]).cancelBubble = true;' style='display:block;width:100px;color: #058CB6;overflow:hidden;text-overflow:ellipsis;'>"+qg[0]+"</a>",qg);

	}
};

eXcell_link.prototype=new gD;
/*鼠标hover时的小提示*/
eXcell_link.prototype.getTitle=function(){var z=this.cell.firstChild;return((z&&z.tagName)?z.getAttribute("href"):"")};
