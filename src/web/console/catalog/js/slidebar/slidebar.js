/*
*  Slidebar v1.0
*  Written by misshjn
*  WebSite: http://www.happyshow.org
*  Mail: misshjn@163.com
*  Date: 2008-04-29
*/
function SlideBar(){
	this.maxValue=100;
	this.defaultValue = 0;
}
SlideBar.List = [];
SlideBar.getValue = function(id){
	return typeof id=="string" ? SlideBar.List[id].value : id.value;
}

SlideBar.setValue = function(id,evt){
	var v = typeof evt=="object"?(evt?evt:window.event):evt;
	id = typeof id!="object" ? SlideBar.List[id] : id;
	if(typeof v != "object"){
		id.style.left = (id.parentNode.clientWidth-id.clientWidth-id.getStyle("border-left-width").cint()*2)*v/id.handle.maxValue + "px";
		id.value = v;
	}else{
		var L = (v.layerX||v.x)-id.clientWidth/2;		
		id.style.left = L + "px";
		id.value = (L*id.handle.maxValue/(id.parentNode.clientWidth-id.clientWidth-id.getStyle("border-left-width").cint()*2)).toFixed(2);
	}

	if(id.handle.onmoving)id.handle.onmoving.apply(id);
	if(id.handle.onend)id.handle.onend.apply(id);
}
SlideBar.moveCursor = function(evt){
	evt = evt?evt:window.event;
	var me = $f(this);
	var detlaX = evt.clientX - me.offsetLeft; //parseInt(me.style.left);
	if(document.all){
		me.attachEvent("onmousemove",move);
		me.attachEvent("onmouseup",up);
		me.setCapture();
	}else{
		document.addEventListener("mousemove",move,true);
		document.addEventListener("mouseup",up,true);
		evt.stopPropagation();
		evt.preventDefault();
	}
	var meWidth = me.parentNode.clientWidth - me.clientWidth - me.getStyle("border-left-width").cint()*2;	
	var onmoving = me.handle.onmoving;
	var onend = me.handle.onend;

	function move(evt){
		if(!document.all){evt.stopPropagation();}
		var w = evt.clientX - detlaX;
		if(w<0 || w>meWidth){
			return;
		}		
		me.style.left = w + "px";
		me.value = ((w/meWidth)*me.handle.maxValue).toFixed(2);
		if(onmoving){onmoving.apply(me); }
	}
	function up(evt){	
		if(document.all){
			me.detachEvent("onmousemove",move);
			me.detachEvent("onmouseup",up);
			me.releaseCapture();
		}else{
			document.removeEventListener("mousemove",move,true);
			document.removeEventListener("mouseup",up,true);
			evt.stopPropagation();
		}
		if(onend){onend.apply(me);}
	}
}
SlideBar.prototype.write = function(){
	var id = this.id || "cursor"+parseInt(Math.random()*100000);
	var str = '<div class="slidebox">'
			+ '<div class="lineBox"><div class="line" onmousedown="SlideBar.setValue(\''+id+'\',event)"></div></div>'
			+ '<div class="cursor" id="'+id+'"></div>'
			+ '</div>';
	document.write(str);
	var t = this;
	setTimeout(function(){
		var e = $f(id);
		SlideBar.List.push(e);
		SlideBar.List[id] = e;
		e.onmousedown = SlideBar.moveCursor;
		e.style.left = (e.parentNode.clientWidth-(e.clientWidth+e.getStyle("border-left-width").cint()*2))*t.defaultValue/t.maxValue + "px";
		e.value = t.defaultValue;
		if(t.onmoving){t.onmoving.apply(e)}
		e.handle = t;
	},0);
}

document.execCommand("BackgroundImageCache", false, true); 