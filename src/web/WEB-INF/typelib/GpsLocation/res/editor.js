function getGpsLocationSdef(div)
{	
	isSetGpsLocationReady = false;
	var frameName = $(":input[name='frameName']", div)[0].value;
	var transferUrl = $(":input[name='transferUrl']", div)[0].value;
	var type = $(":input[name='type']", div)[0].value;
	window.frames[frameName].frames[0].transfer(transferUrl,type);
	var count = 0;
	var intervalChem = window.setInterval( 
							function(){ 
								count++;
                                if(isSetGpsLocationReady)
								{    
									window.clearInterval(intervalChem);
									var sdef = document.createElement("sdef");
									
									var val0 = $(':input', div)[1].value;
									if(isNaN(val0))
									{ 
										alert('经度必须是数值！'); 
										return false; 
									} 
									if(val0>180||val0<-180)
									{
										alert('经度值超出范围！'); 
									  	return false; 
									}
									addChild(sdef, "value", val0);
									
									var val1 = $(':input', div)[0].value;
									if(isNaN(val1))
									{ 
										alert('维度必须是数值！'); 
										return false; 
									} 
									if(val1>90||val1<-90)
									{
										alert('纬度值超出范围！'); 
									  	return false; 
									}
									addChild(sdef, "value", val1);
									
									var val2 = $(':input', div)[2].value;
									if(isNaN(val2))
									{ 
										alert('海拔必须是数值！'); 
										return false; 
									} 
									addChild(sdef, "value", val2);
									
									
									div.ondataavailable(sdef.outerHTML);
                                } 
                                if(count > 100)
                                {
                                	alert("GPS应用服务器连接超时，无法获取编辑数据，请稍后重试！");
                                	window.clearInterval(intervalChem);
									var sdef = document.createElement("sdef");
									for(i=0;i<3;i++)
									{
										var value = $(':input', div)[i].value;
										addChild(sdef, "value", value);
									}
									div.ondataavailable(sdef.outerHTML);
                                }
                              } 
                      ,100);
		
}

function setGpsLocationSdef(div,ddl)
{
	var ddls=ddl.split("~!!~");
	if(ddls.length > 0)
	$("input","#E_"+div)[0].value=ddls[0];
	if(ddls.length > 1)
	$("input","#E_"+div)[1].value=ddls[1];
	if(ddls.length > 2)
	$("input","#E_"+div)[2].value=ddls[2];
	
	isSetGpsLocationReady = true;
	//getGPSSdef($("#E_"+div)[0]);
}