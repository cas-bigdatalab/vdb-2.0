//-------- 菜单点击事件 -------
function c(srcelementid)
{
  var targetid,srcelement,targetelement;
  var strbuf;
  
  srcelement =document.getElementById(srcelementid);

  //-------- 如果点击了展开或收缩按钮---------
  targetid=srcelement.id+"d";
  targetelement=document.getElementById(targetid);

  if (targetelement.style.display=="none")
  {
     srcelement.className="active";
     targetelement.style.display='';
  }
  else
  {
     srcelement.className="";
     targetelement.style.display="none";
  }
}
function set_current(id)
{
   cur_link=document.getElementById(cur_id)
   if(cur_link)
      cur_link.className="";
   cur_link=document.getElementById(id);
   if(cur_link)
      cur_link.className="active";
   cur_id=id;
}
//-------- 打开网址 -------
function a(URL,id)
{
	var expires = new Date();
    expires.setTime(new Date().getTime() +1000*60*60);
	setCookie("selectedid", id, expires);
	set_current(id);
	window.location.href = URL;
}