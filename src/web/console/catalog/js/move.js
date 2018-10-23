var lastMovedRow = null;

function moveUp(which,id)
{
	var tr = document.all.item("FIELDS_ROW_" + which);
	if(tr == null)
		return;
	
	var table = tr.parentNode;
	var i = getPosition(table, tr);
	
	if(i <= 1)
		return;
	
	swapRow(table, i, i - 1);
}

function setPosition(count){
	var trCount = $("tr").length -3;
	
	for(i=1;i<=trCount;i++){
		//var input = document.getElementById(i);
		var tr = document.getElementById("FIELDS_ROW_" + i);
		var table = tr.parentNode;
		
		var j = getPosition(table,tr);
		document.getElementById(i).value = j;
	}
	
	return true;
}

function moveToHead(which)
{
	var tr = document.all.item("FIELDS_ROW_" + which);
	if(tr == null)
		return;
	
	var table = tr.parentNode;
	var i = getPosition(table, tr);
	
	if(i <= 1)
		return;
		
	swapRow(table, i, 1);
}

function moveDown(which)
{
	var tr = document.all.item("FIELDS_ROW_" + which);
	if(tr == null)
		return;
	
	var table = tr.parentNode;
	var i = getPosition(table, tr);
	
	if(i >= table.rows.length)
		return;
		
	swapRow(table, i, i + 1);
}

function moveToTail(which)
{
	var tr = document.all.item("FIELDS_ROW_" + which);
	if(tr == null)
		return;
	
	var table = tr.parentNode;
	var i = getPosition(table, tr);
	
	if(i >= table.rows.length)
		return;
		
	swapRow(table, i, table.rows.length - 1);
}

function getPosition(table, tr)
{
	var i = 1;
	for(i = 1; i < table.rows.length; i++)
	{
		if(tr == table.rows[i])
			return i;
	}
	
	return -1;
}

function swapRow(table, i1, i2)
{
	var tr1 = table.rows[i1];
	var tr2 = table.rows[i2];
	
	if(tr1 == null || tr2 == null)
		return;
		
	tr1.style.backgroundColor = "#D4F2FD";
	if(lastMovedRow != null && lastMovedRow != tr1)
	{
		lastMovedRow.style.backgroundColor = "";
	}
	
	tr2.swapNode(tr1);
	changeOrder(table, i1);
	changeOrder(table, i2);
	lastMovedRow = tr1;
}

function changeOrder(table, i)
{
	var tr = table.rows[i];
	//²éÕÒinput
	var ies = tr.getElementsByTagName("INPUT");
	for(var m = 0; m < ies.length; m++)
	{
		var ie = ies[m];
		if(ie != null && ie.name.length > 8 && ie.name.substring(0, 9) == "SEQUENCE_")
		{
			ie.value = i;
			break;
		}
	}
}

