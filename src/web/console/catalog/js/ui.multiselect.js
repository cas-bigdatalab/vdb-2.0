/*
 * jQuery UI Multiselect
 *
 * Authors:
 *  Michael Aufreiter (quasipartikel.at)
 *  Yanick Rochon (yanick.rochon[at]gmail[dot]com)
 * 
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 * 
 * http://www.quasipartikel.at/multiselect/
 *
 * 
 * Depends:
 *	ui.core.js
 *	ui.sortable.js
 *
 * Optional:
 * localization (http://plugins.jquery.com/project/localisation)
 * scrollTo (http://plugins.jquery.com/project/ScrollTo)
 * 
 * Todo:
 *  Make batch actions faster
 *  Implement dynamic insertion through remote calls
 */


(function($) {

$.widget("ui.multiselect", {
  options: {
		sortable: true,
		searchable: true,
		animated: 'fast',
		show: 'slideDown',
		hide: 'slideUp',
		dividerLocation: 0.6,
		nodeComparator: function(node1,node2) {
			var text1 = node1.text(),
			    text2 = node2.text();
			return text1 == text2 ? 0 : (text1 < text2 ? -1 : 1);
		}
	},
	_create: function() {
		this.element.hide();
		this.id = this.element.attr("id");
		this.container = $('<div class="ui-multiselect ui-helper-clearfix ui-widget"></div>').insertAfter(this.element);
		this.count = 0; // number of currently selected options
		this.selectedContainer = $('<div class="selected"></div>').appendTo(this.container);
		this.availableContainer = $('<div class="available"></div>').appendTo(this.container);
		this.selectedActions = $('<div class="actions ui-widget-header ui-helper-clearfix"><span class="count">0 '+$.ui.multiselect.locale.itemsCount+'</span><a href="#" class="remove-all" >'+$.ui.multiselect.locale.removeAll+'</a></div>').appendTo(this.selectedContainer);
		this.availableActions = $('<div class="actions ui-widget-header ui-helper-clearfix"><input type="text" class="search empty ui-widget-content ui-corner-all"/><a href="#" class="add-all" >'+$.ui.multiselect.locale.addAll+'</a></div>').appendTo(this.availableContainer);
		this.selectedList = $('<ul class="selected connected-list"><li class="ui-helper-hidden-accessible"></li></ul>').bind('selectstart', function(){return false;}).appendTo(this.selectedContainer);
		this.availableList = $('<ul class="available connected-list"><li class="ui-helper-hidden-accessible"></li></ul>').bind('selectstart', function(){return false;}).appendTo(this.availableContainer);
		
		var that = this;

		// set dimensions
		this.container.width(this.element.width()+1);
		this.selectedContainer.width(Math.floor(this.element.width()*this.options.dividerLocation));
		this.availableContainer.width(Math.floor(this.element.width()*(1-this.options.dividerLocation)));

		// fix list height to match <option> depending on their individual header's heights
		this.selectedList.height(Math.max(this.element.height()-this.selectedActions.height(),1));
		this.availableList.height(Math.max(this.element.height()-this.availableActions.height(),1));
		
		if ( !this.options.animated ) {
			this.options.show = 'show';
			this.options.hide = 'hide';
		}
		
		// init lists
		this._populateLists(this.element.find('option'));
		
		// make selection sortable
		if (this.options.sortable) {
			this.selectedList.sortable({
				placeholder: 'ui-state-highlight',
				axis: 'y',
				update: function(event, ui) {
					// apply the new sort order to the original selectbox
					that.selectedList.find('li').each(function() {
						if ($(this).data('optionLink'))
							$(this).data('optionLink').remove().appendTo(that.element);
					});
				},
				receive: function(event, ui) {
					ui.item.data('optionLink').attr('selected', true);
					// increment count
					that.count += 1;
					that._updateCount();
					// workaround, because there's no way to reference 
					// the new element, see http://dev.jqueryui.com/ticket/4303
					that.selectedList.children('.ui-draggable').each(function() {
						$(this).removeClass('ui-draggable');
						$(this).data('optionLink', ui.item.data('optionLink'));
						$(this).data('idx', ui.item.data('idx'));
						that._applyItemState($(this), true);
					});
			
					// workaround according to http://dev.jqueryui.com/ticket/4088
					setTimeout(function() { ui.item.remove(); }, 1);
				}
			});
		}
		
		// set up livesearch
		if (this.options.searchable) {
			this._registerSearchEvents(this.availableContainer.find('input.search'));
		} else {
			$('.search').hide();
		}
		
		// batch actions
		this.container.find(".remove-all").click(function() {
			var id = that.element.find('option').context.id;
			that._populateLists(that.element.find('option').removeAttr('selected'));
			
			if(id == "selectedfileds" && document.getElementById("selectedfiledsDiv")!=null)
			{
				removeAllSelectedFileds();
			}
			else if(id =="orderfileds" && document.getElementById("orderfiledsDiv")!=null)
			{
				removeAllOrderFileds();
			}
			
			return false;
		});
		
		this.container.find(".add-all").click(function() {
			var id = that.element.find('option').context.id;
			that._populateLists(that.element.find('option').attr('selected', 'selected'));
			
			//当页面具有相应的DIV组件时，才触发相应的方法
			if(id == "selectedfileds" && document.getElementById("selectedfiledsDiv")!=null)
			{
				addAllSelectedFileds();
			}
			else if(id =="orderfileds" && document.getElementById("orderfiledsDiv")!=null)
			{
				allAllOrderFileds();
			}
			return false;
		});
	},
	destroy: function() {
		this.element.show();
		this.container.remove();

		$.widget.prototype.destroy.apply(this, arguments);
	},
	_populateLists: function(options) {
		this.selectedList.children('.ui-element').remove();
		this.availableList.children('.ui-element').remove();
		this.count = 0;

		var that = this;
		var items = $(options.map(function(i) {
	      var item = that._getOptionNode(this).appendTo(this.selected ? that.selectedList : that.availableList).show();

			if (this.selected) that.count += 1;
			that._applyItemState(item, this.selected);
			item.data('idx', i);
			return item[0];
    }));
		
		// update count
		this._updateCount();
  },
	_updateCount: function() {
		this.selectedContainer.find('span.count').text(this.count+" "+$.ui.multiselect.locale.itemsCount);
	},
	_getOptionNode: function(option) {
		option = $(option);
		var node = $('<li class="ui-state-default ui-element" onclick="optionClick(this,\'' + option.val() + '\')" title="'+option.text()+'"><span class="ui-icon"/>'+option.text()+'<a href="#" class="action"><span class="ui-corner-all ui-icon"/></a></li>').hide();
		node.data('optionLink', option);
		return node;
	},
	// clones an item with associated data
	// didn't find a smarter away around this
	_cloneWithData: function(clonee) {
		var clone = clonee.clone();
		clone.data('optionLink', clonee.data('optionLink'));
		clone.data('idx', clonee.data('idx'));
		return clone;
	},
	_setSelected: function(item, selected) {
		item.data('optionLink').attr('selected', selected);
		var that = this;
		var id = that.element.find('option').context.id;
		
		if (selected) {
			var selectedItem = this._cloneWithData(item);
			item[this.options.hide](this.options.animated, function() { $(this).remove(); });
			selectedItem.appendTo(this.selectedList).hide()[this.options.show](this.options.animated);
			
			if(id == "selectedfileds" && document.getElementById("selectedfiledsDiv")!=null)
			{
				addSelectedDiv(selectedItem.data('optionLink')[0].value,selectedItem.text());
			}
			else if(id =="orderfileds" && document.getElementById("orderfiledsDiv")!=null)
			{
				addOrderDiv(selectedItem.data('optionLink')[0].value,selectedItem.text());
			}
			
			this._applyItemState(selectedItem, true);
			
			return selectedItem;
		} else {
			// look for successor based on initial option index
			var items = this.availableList.find('li'), comparator = this.options.nodeComparator;
			var succ = null, i = item.data('idx'), direction = comparator(item, $(items[i]));
			
			if(id == "selectedfileds" && document.getElementById("selectedfiledsDiv")!=null)
			{
				delSelectedDiv(item.data('optionLink')[0].value);
			}
			else if(id =="orderfileds" && document.getElementById("orderfiledsDiv")!=null)
			{
				delOrderDiv(item.data('optionLink')[0].value);
			}
			
			if ( direction ) {
				while (i>=0 && i<items.length) {
					direction > 0 ? i++ : i--;
					if ( direction != comparator(item, $(items[i])) ) {
						// going up, go back one item down, otherwise leave as is
						succ = items[direction > 0 ? i : i+1];
						break;
					}
				}
			} else {
				succ = items[i];
			}
			
			var availableItem = this._cloneWithData(item);
			succ ? availableItem.insertBefore($(succ)) : availableItem.appendTo(this.availableList);
			item[this.options.hide](this.options.animated, function() { $(this).remove(); });
			availableItem.hide()[this.options.show](this.options.animated);
			
			this._applyItemState(availableItem, false);
			return availableItem;
		}
	},
	_applyItemState: function(item, selected) {
	
		var that = this;
		var id = that.element.find('option').context.id;
		if (selected) {
		
			if(id == "selectedfileds" && document.getElementById("selectedfiledsDiv")!=null)
			{
				addSelectedDiv(item.data('optionLink')[0].value,item.text());
			}
			else if(id =="orderfileds" && document.getElementById("orderfiledsDiv")!=null)
			{
				addOrderDiv(item.data('optionLink')[0].value,item.text());
			}
			
			if (this.options.sortable)
				item.children('span').addClass('ui-icon-arrowthick-2-n-s').removeClass('ui-helper-hidden').addClass('ui-icon');
			else
				item.children('span').removeClass('ui-icon-arrowthick-2-n-s').addClass('ui-helper-hidden').removeClass('ui-icon');
			item.find('a.action span').addClass('ui-icon-minus').removeClass('ui-icon-plus');
			this._registerRemoveEvents(item.find('a.action'));
			
		} else {
			item.children('span').removeClass('ui-icon-arrowthick-2-n-s').addClass('ui-helper-hidden').removeClass('ui-icon');
			item.find('a.action span').addClass('ui-icon-plus').removeClass('ui-icon-minus');
			this._registerAddEvents(item.find('a.action'));
		}
		this._registerHoverEvents(item);
	},
	// taken from John Resig's liveUpdate script
	_filter: function(list) {
		var input = $(this);
		var rows = list.children('li'),
			cache = rows.map(function(){
				
				return $(this).text().toLowerCase();
			});
		
		var term = $.trim(input.val().toLowerCase()), scores = [];
		
		if (!term) {
			rows.show();
		} else {
			rows.hide();

			cache.each(function(i) {
				if (this.indexOf(term)>-1) { scores.push(i); }
			});

			$.each(scores, function() {
				$(rows[this]).show();
			});
		}
	},
	_registerHoverEvents: function(elements) {
		elements.removeClass('ui-state-hover');
		elements.mouseover(function() {
			$(this).addClass('ui-state-hover');
		});
		elements.mouseout(function() {
			$(this).removeClass('ui-state-hover');
		});
	},
	_registerAddEvents: function(elements) {
		var that = this;
		elements.click(function() {
			var item = that._setSelected($(this).parent(), true);
			that.count += 1;
			that._updateCount();
			return false;
		});
		
		// make draggable
		if (this.options.sortable) {
  		elements.each(function() {
  			$(this).parent().draggable({
  	      connectToSortable: that.selectedList,
  				helper: function() {
  					var selectedItem = that._cloneWithData($(this)).width($(this).width() - 50);
  					selectedItem.width($(this).width());
  					return selectedItem;
  				},
  				appendTo: that.container,
  				containment: that.container,
  				revert: 'invalid'
  	    });
  		});		  
		}
	},
	_registerRemoveEvents: function(elements) {
		var that = this;
		elements.click(function() {
			that._setSelected($(this).parent(), false);
			that.count -= 1;
			that._updateCount();
			return false;
		});
 	},
	_registerSearchEvents: function(input) {
		var that = this;

		input.focus(function() {
			$(this).addClass('ui-state-active');
		})
		.blur(function() {
			$(this).removeClass('ui-state-active');
		})
		.keypress(function(e) {
			if (e.keyCode == 13)
				return false;
		})
		.keyup(function() {
			that._filter.apply(this, [that.availableList]);
		});
	}
});
		
$.extend($.ui.multiselect, {
	locale: {
		addAll:'Add all',
		removeAll:'Remove all',
		itemsCount:'items selected'
	}
});


})(jQuery);


function optionClick(objLi,value)
{
	if(objLi.parentNode.className.indexOf("selected")>=0)
	{
		var divIdStr=objLi.parentNode.parentNode.parentNode.parentNode.innerHTML;
		if(divIdStr.indexOf("id=selectedfileds")>0 || divIdStr.indexOf("id=\"selectedfileds\"")>0){
			var divObj = document.getElementById("selectedfiledsDiv");
			for(var i=0;i<$("div",divObj).length;i++)
			{
				$("div",divObj)[i].style.display="none";
			}
			
			var id ="selectedfileds_"+value;
			document.getElementById(id).style.display ="block";
		}
		
		if(divIdStr.indexOf("id=orderfileds")>0 || divIdStr.indexOf("id=\"orderfileds\"")>0){
			var divObj = document.getElementById("orderfiledsDiv");
			for(var i=0;i<$("div",divObj).length;i++)
			{
				$("div",divObj)[i].style.display="none";
			}
			var id="orderfileds_"+value;
			document.getElementById(id).style.display="block";
		}
	}
}

function addOrderDiv(value,text){
	var id="orderfileds_"+value;
	if(document.getElementById(id)==null){
		var div=document.createElement("div");
		div.id=id
		div.style.display="none";
		var strTable="编辑排序字段：<br><br><table width='100%' border='0' cellspacing='0' cellpadding='0' style='border-top:#91c0fe 1px solid;border-left:#91c0fe 1px solid;background:#fff;filter: alpha(opacity=95); -moz-opacity:0.95;opacity:0.95;'><tr><td class='table01_bg' width='150px'>字段名称</td><td  class='table01_bg' >排序方式</td></tr><tr><td>"+text+"</td><td><input type='radio'  name='sort"+value+"' style='border:0px' value='asc' checked>升序(asc) &nbsp; <input type='radio' style='border:0px'  name='sort"+value+"' value='desc' >降序(desc)</td></tr></table>";
		div.innerHTML=strTable;
		document.getElementById("orderfiledsDiv").appendChild(div);
	}
}

function delOrderDiv(value)
{
	var id="orderfileds_"+value;
	var obj = document.getElementById(id);
	document.getElementById("orderfiledsDiv").removeChild(obj);
	
}

function addSelectedDiv(value,text){
	var id="selectedfileds_"+value;
	if(document.getElementById(id)==null){
		var div=document.createElement("div");
		div.id=id
		div.style.display="none";
		var strTable="<table width='100%' border='0' cellspacing='0' cellpadding='0' style='border-top:#91c0fe 1px solid;border-left:#91c0fe 1px solid;background:#fff;filter: alpha(opacity=95); -moz-opacity:0.95;opacity:0.95;'><tr><td class='table01_bg'>字段名称</td><td class='table01_bg'>默认值</td><td class='table01_bg'>只读属性</td></tr><tr><td>"+text+"</td><td><input type='text'  id='defaultValue"+value+"' name='defaultValue"+value+"'></td><td><input type='checkbox' style='border:0px'  name='read"+value+"' value='readonly' >只读</td></tr></table>";
		div.innerHTML=strTable;
		document.getElementById("selectedfiledsDiv").appendChild(div);
	}
}

function delSelectedDiv(value)
{
	var id="selectedfileds_"+value;
	var obj = document.getElementById(id);
	document.getElementById("selectedfiledsDiv").removeChild(obj);
}

function addAllSelectedFileds()
{
	var fa=$('#selectedfileds').find("option:selected");
	for(var i=0; i<fa.length;i++){
		addSelectedDiv(fa[i].value,fa[i].text);
	}
}

function allAllOrderFileds()
{
	var fa=$('#orderfileds').find("option:selected");
	for(var i=0; i<fa.length;i++){
		addOrderDiv(fa[i].value,fa[i].text);
	}
}

function removeAllSelectedFileds()
{
	var fa=$('#selectedfileds').find("option:available");
	for(var i=0; i<fa.length;i++){
		delSelectedDiv(fa[i].value);
	}
}

function removeAllOrderFileds()
{
	var fa=$('#orderfileds').find("option:available");
	for(var i=0; i<fa.length;i++){
		delOrderDiv(fa[i].value);
	}
}