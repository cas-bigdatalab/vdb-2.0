/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except 
using on sites which belongs to DHTMLX LTD.

Please contact sales@dhtmlx.com to obtain necessary 
license for usage of dhtmlx components.
===================================================================
*/function NP(){var z=document.getElementsByTagName("div");for(var i=0;i<z.length;i++)if(z[i].className.indexOf("dhtmlxTabBar")!= -1){var n=z[i];var id=n.id;n.className="";var k=new Array();for(var j=0;j<n.childNodes.length;j++)if(n.childNodes[j].tagName&&n.childNodes[j].tagName!="!")k[k.length]=n.childNodes[j];var w=new aj(id,n.getAttribute("mode")||"top",n.getAttribute("tabheight")||20);window[id]=w;Xn=n.getAttribute("onbeforeinit");if(Xn)eval(Xn);if(n.getAttribute("enableForceHiding"))w.enableForceHiding(true);w.setImagePath(n.getAttribute("imgpath"));var Xn=n.getAttribute("margin");if(Xn!=null)w.hS=Xn;Xn=n.getAttribute("align");if(Xn)w.js=Xn;Xn=n.getAttribute("hrefmode");if(Xn)w.setHrefMode(Xn);Xn=n.getAttribute("offset");if(Xn!=null)w.qd=Xn;Xn=n.getAttribute("tabstyle");if(Xn!=null)w.mp(Xn);Xn=n.getAttribute("select");var agj=n.getAttribute("skinColors");if(agj)w.eL(agj.split(",")[0],agj.split(",")[1]);for(var j=0;j<k.length;j++){var m=k[j];m.parentNode.removeChild(m);w.hG(m.id,m.getAttribute("name"),m.getAttribute("width"),null,m.getAttribute("row"));var href=m.getAttribute("href");if(href)w.ny(m.id,href);else w.setContent(m.id,m);if((!w.UF)&&(m.style.display=="none"))m.style.display=""};if(k.length)w.fP(Xn||k[0].id);Xn=n.getAttribute("oninit");if(Xn)eval(Xn)}};dE(window,"load",NP);