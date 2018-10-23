/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except 
using on sites which belongs to DHTMLX LTD.

Please contact sales@dhtmlx.com to obtain necessary 
license for usage of dhtmlx components.
===================================================================
*/D.prototype.enableUndoRedo=function(){var self=this;var aQ=function(){return self.wm.apply(self,arguments)};this.attachEvent("onEditCell",aQ);var func2=function(a,b,c){return self.wm.apply(self,[2,a,b,(c?1:0),(c?0:1)])};this.attachEvent("onCheckbox",func2);this.uv=true;this.pr=[];this.pE= -1};D.prototype.MA=function(){this.uv=false;this.pr=[];this.pE= -1};D.prototype.wm=function(stage,bO,qF,new_value,uM){if(this.uv&&stage==2&&uM!=new_value){if(this.pE!== -1&&this.pE!=(this.pr.length-1)){this.pr=this.pr.slice(0,this.pE+1)}else if(this.pE=== -1&&this.pr.length>0){this.pr=[]};var obj={uM:uM,new_value:new_value,bO:bO,qF:qF};this.pr.push(obj);this.pE++};return true};D.prototype.alN=function(){if(this.pE=== -1)return false;var obj=this.pr[this.pE--];var c=this.cells(obj.bO,obj.qF);if(this.abv(obj.qF)=="tree")c.setLabel(obj.uM);else c.setValue(obj.uM)};D.prototype.alO=function(){if(this.pE==this.pr.length-1)return false;var obj=this.pr[++this.pE];this.cells(obj.bO,obj.qF).setValue(obj.new_value)};D.prototype.ajj=function(){if(this.pE==this.pr.length-1)return[];return this.pr.slice(this.pE+1)};D.prototype.ajN=function(){if(this.pE== -1)return[];return this.pr.slice(0,this.pE+1)};