/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except 
using on sites which belongs to DHTMLX LTD.

Please contact sales@dhtmlx.com to obtain necessary 
license for usage of dhtmlx components.
===================================================================
*/function eXcell_cntr(cell){this.cell=cell;this.grid=this.cell.parentNode.grid;if(!this.grid.RN&& !this.nh){this.grid.RN=true;if(this.grid._h2)this.grid.attachEvent("onOpenEn",function(id){this.JO(cell._cellIndex)});this.grid.attachEvent("onBeforeSorting",function(){var that=this;window.setTimeout(function(){if(that.aC&& !that.nh&&cell._cellIndex<that.aC.gA)that.aC.JO(cell._cellIndex);else that.JO(cell._cellIndex)},1);return true})};this.edit=function(){};this.getValue=function(){return this.cell.innerHTML};this.setValue=function(val){this.cell.style.paddingRight="2px";var cell=this.cell;window.setTimeout(function(){if(!cell.parentNode)return;var val=cell.parentNode.rowIndex;if(cell.parentNode.grid.ew||val<0||cell.parentNode.grid.ahK)val=cell.parentNode.grid.aD.bP(cell.parentNode)+1;cell.innerHTML=val;if(cell.parentNode.grid.aC&&cell._cellIndex<cell.parentNode.grid.aC.gA&&cell.parentNode.grid.aC.bj[cell.parentNode.idd])cell.parentNode.grid.aC.cells(cell.parentNode.idd,cell._cellIndex).dq(val);cell=null},100)}};D.prototype.JO=function(ind){if(this.aC&& !this.nh&&ind<this.aC.gA)this.aC.JO(ind,this.ew);var i=arguments[0]||0;if(this.ew)i=(this.ew-1)*this.cu;for(i=0;i<this.aD.length;i++)if(this.aD[i].tagName=="TR")this.bj[this.aD[i].idd].childNodes[ind].innerHTML=i+1};eXcell_cntr.prototype=new gD;