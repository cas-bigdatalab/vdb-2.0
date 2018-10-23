function getChemStructureSdef(div)
{
	isSetChemStructureReady = false;
	var frameName = $(":input[name='frameName']", div)[0].value;
	var transferUrl = $(":input[name='transferUrl']", div)[0].value;
	var type = $(":input[name='type']", div)[0].value;
	window.frames[frameName].frames[0].transfer(transferUrl,type);
	var count = 0;
	var intervalChem = window.setInterval( 
							function(){ 
								count++;
                                if(isSetChemStructureReady)
								{    
									window.clearInterval(intervalChem);
									return getInputSdef(div);
                                } 
                                if(count > 100)
                                {
                                	alert("化学结构应用服务器连接超时，无法获取编辑数据，请稍后重试！");
                                	window.clearInterval(intervalChem);
									return getInputSdef(div);
                                }
                              } 
                      ,100);
}

function setChemStructureSdef(div,ddl)
{
	$("input","#E_"+div)[0].value=ddl;
	isSetChemStructureReady = true;
	//getInputSdef($("#E_"+div)[0]);
}
