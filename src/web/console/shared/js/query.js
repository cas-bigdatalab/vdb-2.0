var searchFormId=0;
//查询对象
function Query(uriEntity)
{
	this.entity = uriEntity;
	this.whereFilter = null;
	this.pageIndex = 1;
	this.pageSize = 20;
	this.orderField = null;
	this.orderAsc = "desc";
	this.variables = {};
	
	this.setWhereFilter = function(expr)
	{
		this.whereFilter = expr;
	};
	
	this.setVariable = function(key, value)
	{
		this.variables[key] = value;
	};
}

function CollectionQuery(uriCollectionField, parentBeanId)
{
	this.newMethod = Query;
	this.newMethod(null);
	
	this.collectionField = uriCollectionField;
	this.parentBeanId = parentBeanId;
}
//表达式构造器
function ExprBuilder()
{
	this.expr = function(uriField, operator, value)
	{
		return new FieldExpr(uriField, operator, value);
	};
	
	this.and = function(a, b)
	{
		if(a === null){
			return b;
		}
		if(b === null){
			return a;
		}	
		return new ExprExpr(a, "and", b);
	};
	
	this.or = function(a, b)
	{
		if(a === null){
			return b;
		}
		if(b === null){
			return a;
		}	
		return new ExprExpr(a, "or", b);
	};
}

function DataGrid(query, htmlBinder)
{
	htmlBinder.jsoDataGrid = this;
	htmlBinder.jsoQuery = query;

	this.htmlBinder = htmlBinder;
	this.jsoQuery = query;
	this.urlDoQuery = "/!doQuery.vpage";
	this.queryFlag=false;//定义是否点击的前台查询按钮(默认不是)，当点击前台查询按钮时，将此值更改为true
	
	this.getCheckedRecords = function()
	{
		var checkedCount = 0;
		var ids = "";
		var rows = $("tr[@name=DATAGRID_ROW]",this.htmlBinder); 

		if(!rows.length)
		{
			rows = new Array(rows);
		}
		
		for(var i = 0; i < rows.length; i++)
		{
			var tr = rows[i];
			if("true" == tr.getAttribute("checked"))
			{
				if(checkedCount != 0)
					ids = ids + ";";
	
				checkedCount++;
				ids = ids + tr.getAttribute("rowID");				
			}
		}
	
		var o = new Object();
		o.count = checkedCount;
		o.ids = ids;
		
		return o;
	}
		
	this.refresh = function()
	{
		//如果点击的是前台查询按钮，则在URL中传入flag=true
		if(this.queryFlag)
		{
			this.queryFlag=false;//重置为false;
			loadHTML1(this.urlDoQuery, "query=" + escape(JSON.stringify(this.jsoQuery))+"&flag=true&timestamp=" + new Date().getTime(), this.htmlBinder);
		}
		else
		{
			loadHTML1(this.urlDoQuery, "query=" + escape(JSON.stringify(this.jsoQuery))+"&timestamp=" + new Date().getTime(), this.htmlBinder);
		}
		
	}
}

function QueryForm(query, entity, htmlBinder, eventHandler)
{
	this.entity = entity;
	this.htmlBinder = htmlBinder;
	this.query = query;
	this.flag=0;
	
	//original whereFilter
	this.originalWhereFilter = query.whereFilter;
	
	htmlBinder.jsoQueryForm = this;
	this.urlQueryEntity = '/!queryEntity.vpage?uri=' + this.entity;

	if(!$("div[@class=searchForm]", this.htmlBinder)[0])
	{
		searchFormId++;
		loadHTML1(this.urlQueryEntity, "turi=" + entity+"&seqid="+searchFormId, this.htmlBinder);
	}
	else
	{
		$("div[@class=searchForm]", this.htmlBinder).show();
	}
	
	this.toggle = function()
	{
		if(this.htmlBinder.style.display != "none")
		{
			$(this.htmlBinder).hide();
		}
		else
		{
			$(this.htmlBinder).show();
		}
	}
	
	this.onreset = function()
	{
		this.query.setWhereFilter(this.originalWhereFilter);
		//eventHandler.onsubmit();//重新设定条件时，就不要执行提交的方法了
	}
	
	this.onsubmit = function()
	{
		var query = this.query;
	
		var form = $("div", this.htmlBinder)[0];
		var exprBuilder = new ExprBuilder();
		var whereFilter = this.query.whereFilter;
		if(this.flag == 1)
		{
			whereFilter = this.query.whereFilter;
		}
		else
		{
			whereFilter = this.originalWhereFilter;
		}
		
		var fields = $("select.fieldList", form).get();
		var availableDataCounter = 0;
		for(var i = 0; i < fields.length; i++)
		{
			var field = fields[i];
			
			var operator = $("select", field.operatorArea)[0];
			var opop = operator.options[operator.selectedIndex];
			if(opop.value != "")
			{
				if(opop.getAttribute("isUnaryExpr") == 'true')
				{
					addFilter(exprBuilder.expr(field.options[field.selectedIndex].getAttribute("uri"), opop.value, ""));	
				}
				else
				{
					var editor = $("#E_" + field.value, field.editorArea)[0];
					editor.ondataavailable = function(sdef)
					{
						if(sdef != null && sdef != "")
						{
							addFilter(exprBuilder.expr(field.options[field.selectedIndex].getAttribute("uri"), opop.value, sdef));
						}
					}
					var type = $("input[name='type']",field.editorArea)[0].value;
					var fun = "getQuery" + type + "Sdef(editor)";
					eval(fun);
				}			
			}
			else
			{
				addFilter(null);
			}
		}
		function addFilter(thisFilter)
		{
			availableDataCounter++;

			if(thisFilter != null)
			{
				if(whereFilter == null)
					whereFilter = thisFilter;
				else
					whereFilter = exprBuilder.and(whereFilter, thisFilter);
			}

			if(availableDataCounter == fields.length)
			{
				query.setWhereFilter(whereFilter);
				eventHandler.onsubmit();
			}
		}
	}
}

//global functions
function doQuery1(a)
{
	var queryForm = $(a).parents("div #QueryForm")[0].jsoQueryForm;
	queryForm.onreset();
	
	return false;
}

function doQuery2(form,flag)
{	
	var queryForm;
	if($(form).parents("div").parents(".QueryForm")[0]==undefined)
	{
		//数据编辑模块调用的高级查询
		queryForm = $(form).parents("div").parents("div[@id=QueryForm]")[0].jsoQueryForm;
			queryForm.flag = flag;
		if(flag == 0)
		{
			queryForm.originalWhereFilter = new Query(queryForm.entity).whereFilter;
		}
		queryForm.onsubmit();
		queryFormWhereFilter = queryForm.entity + "*&^%$#@!!@#$%^&**" + encodeURI(encodeURI(JSON.stringify(queryForm.query)));
		dhtmlxAjax.get("getTableData.vpage?entity="+queryForm.entity+"&start="+1+"&size="+pageSize+"&whereFilter="+ encodeURI(encodeURI(JSON.stringify(queryForm.query)))+"&t="+new Date(), outputResponse);
	}
	else
	{
		//前台调用的高级查询
		queryForm = $(form).parents("div").parents("div[@class=QueryForm]")[0].jsoQueryForm;
		queryForm.flag=flag;
		queryForm.onsubmit();
	}
	
	return false;
}

function doReset(form)
{
	var queryForm;
	var queryForm;
	if($(form).parents("div").parents("div[@class=QueryForm]")[0]==undefined)
	{
		queryForm = $(form).parents("div").parents("div[@id=QueryForm]")[0].jsoQueryForm;
	}
	else
	{
		queryForm = $(form).parents("div").parents("div[@class=QueryForm]")[0].jsoQueryForm;
	}
	queryForm.onreset();
	
	return false;
}

function Expr()
{
}

//字段过滤条件构造器
function FieldExpr(uriField, operator, value)
{
	this.newMethod = Expr;
	
	this.field = uriField;
	this.operator = operator;
	this.value = value;
}

function ExprExpr(a, operator, b)
{
	this.newMethod = Expr;
	
	this.a = a;
	this.operator = operator;
	this.b = b;
}

function toggleQueryImg(id)
{
	if($("#"+id))
	{
		if($("#"+id)[0].src.indexOf("images/ico_jiaohao02.jpg")>-1)
		{
			$("#"+id)[0].src="images/ico_jianhao02.jpg";
		}
		else
		{
			$("#"+id)[0].src="images/ico_jiaohao02.jpg"
		}
	}
}