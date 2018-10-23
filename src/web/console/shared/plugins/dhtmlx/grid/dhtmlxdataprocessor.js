/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except 
using on sites which belongs to DHTMLX LTD.

Please contact sales@dhtmlx.com to obtain necessary 
license for usage of dhtmlx components.
===================================================================
 */function Iq(dI) {
	this.cj = dI;
	this.action_param = "!nativeeditor_status";
	this.obj = null;
	this.T = [];
	this.aH = true;
	this.cJ = "cell";
	this.hR = "GET";
	this.post_delim = "_";
	this.ef = 0;
	this._in_progress = {};
	this._invalid = {};
	this.az = [];
	this.messages = [];
	this.akR = {
		updated : "font-weight:bold;",
		inserted : "font-weight:bold;",
		deleted : "text-decoration : line-through;",
		invalid : "background-color:FFE0E0;",
		invalid_cell : "border-bottom:2px solid red;",
		error : "color:red;",
		clear : "font-weight:normal;text-decoration:none;"
	};
	this.enableUTFencoding(true);
	dhtmlxEventable(this);
	return this
};
Iq.prototype = {
	JI : function(mode, total) {
		this.hR = mode;
		this.gW = total
	},
	escape : function(data) {
		if (this.ahf)
			return encodeURIComponent(data);
		else
			return escape(data)
	},
	enableUTFencoding : function(mode) {
		this.ahf = K(mode)
	},
	setDataColumns : function(val) {
		this._columns = (typeof val == "string") ? val.split(",") : val
	},
	iM : function() {
		return !this.T.length
	},
	ahP : function(mode) {
		this.iO = K(mode)
	},
	enablePartialDataSend : function(mode) {
		this._changed = K(mode)
	},
	mq : function(mode, dnd) {
		this.aH = (mode == "cell");
		this.cJ = mode;
		this.dnd = dnd
	},
	setUpdated : function(cq, state, mode) {
		var ind = this.iv(cq);
		mode = mode || "updated";
		var existing = this.obj.getUserData(cq, this.action_param);
		if (existing && mode == "updated")
			mode = existing;
		if (state) {
			this.set_invalid(cq, false);
			this.T[ind] = cq;
			this.obj.setUserData(cq, this.action_param, mode)
		} else {
			if (!this.is_invalid(cq)) {
				this.T.splice(ind, 1);
				this.obj.setUserData(cq, this.action_param, "")
			}
		}
		;
		if (!state)
			this._clearUpdateFlag(cq);
		this.markRow(cq, state, mode);
		if (state && this.aH)
			this.sendData(cq)
	},
	_clearUpdateFlag : function(cq) {
		if (this.obj.acE != "tree") {
			var row = this.obj.dz(cq);
			if (row)
				for ( var j = 0; j < this.obj.gA; j++)
					this.obj.cells(cq, j).cell.hy = false
		}
	},
	markRow : function(id, state, mode) {
		var str = "";
		var invalid = this.is_invalid(id);
		if (invalid) {
			str = this.akR[invalid];
			state = true
		}
		;
		if (this.callEvent("onRowMark", [ id, state, mode, invalid ])) {
			if (state)
			{
				//if(mygrid.cells(id,1).cell.style.fontWeight.indexOf("bold")<0
				//		||mygrid.cells(id,1).cell.style.color.indexOf("#FF0000")<0)
					str += this.akR[mode];
			}
			else
				str += this.akR.clear;
			this.obj[this._methods[0]](id, str);
			if (invalid && invalid.details) {
				str += this.akR[invalid + "_cell"];
				for ( var i = 0; i < invalid.details.length; i++)
					if (invalid.details[i])
						this.obj[this._methods[1]](id, i, str)
			}
		}
	},
	YA : function(id) {
		return this.obj.getUserData(id, this.action_param)
	},
	is_invalid : function(id) {
		return this._invalid[id]
	},
	set_invalid : function(id, mode, details) {
		if (details)
			mode = {
				value : mode,
				details : details,
				toString : function() {
					return this.value.toString()
				}
			};
		this._invalid[id] = mode
	},
	aZ : function(cq) {
		var valid = true;
		var c_invalid = [];
		for ( var i = 0; i < this.obj.gA; i++)
			if (this.az[i]) {
				var res = this.az[i].call(this.obj, this.obj.cells(cq, i)
						.getValue(), cq, i);
				if (typeof res == "string") {
					this.messages.push(res);
					valid = false
				} else {
					valid &= res;
					c_invalid[i] = !res
				}
			}
		;
		if (!valid) {
			this.set_invalid(cq, "invalid", c_invalid);
			this.setUpdated(cq, false)
		}
		;
		return valid
	},
	sendData : function(cq) {
		if (this.ef && (this.obj.acE == "tree" || this.obj._h2))
			return;
		if (this.obj.editStop)
			this.obj.editStop();
		if (this.obj.linked_form)
			this.obj.linked_form.update();
		if (typeof cq == "undefined" || this.gW)
			return this.sendAllData();
		if (this._in_progress[cq])
			return false;
		this.messages = [];
		if (!this.aZ(cq)
				&& this.callEvent("onValidatationError", [ cq, this.messages ]))
			return false;
		this._beforeSendData(this.fD(cq), cq)
	},
	_beforeSendData : function(data, cq) {
		if (!this.callEvent("onBeforeUpdate", [ cq, this.YA(cq) ]))
			return false;
		this._sendData(data, cq)
	},
	_sendData : function(a1, cq) {
		if (!a1)
			return;
		if (cq)
			this._in_progress[cq] = (new Date()).valueOf();
		if (!this.callEvent("onBeforeDataSending", cq ? [ cq, this.YA(cq) ]
				: []))
			return false;
		var a2 = new ag(this.afterUpdate, this, true);
		var a3 = this.cj;
		if (this.hR != "POST")
			a2.loadXML(a3 + ((a3.indexOf("?") != -1) ? "&" : "?") + a1);
		else
			a2.loadXML(a3, true, a1);
		this.ef++
	},
	sendAllData : function() {
		if (!this.T.length)
			return;
		this.messages = [];
		var valid = true;
		for ( var i = 0; i < this.T.length; i++)
			valid &= this.aZ(this.T[i]);
		if (!valid
				&& !this
						.callEvent("onValidatationError", [ "", this.messages ]))
			return false;
		if (this.gW)
			this._sendData(this.ahb());
		else
			for ( var i = 0; i < this.T.length; i++)
				if (!this._in_progress[this.T[i]]) {
					if (this.is_invalid(this.T[i]))
						continue;
					this._beforeSendData(this.fD(this.T[i]), this.T[i]);
					if (this.ef && (this.obj.acE == "tree" || this.obj._h2))
						return
				}
	},
	ahb : function(cq) {
		var out = new Array();
		var rs = new Array();
		for ( var i = 0; i < this.T.length; i++) {
			var id = this.T[i];
			if (this._in_progress[id] || this.is_invalid(id))
				continue;
			if (!this.callEvent("onBeforeUpdate", [ id, this.YA(id) ]))
				continue;
			out[out.length] = this.fD(id, id + this.post_delim);
			rs[rs.length] = id;
			this._in_progress[id] = (new Date()).valueOf()
		}
		;
		if (out.length)
			out[out.length] = "ids=" + rs.join(",");
		return out.join("&")
	},
	fD : function(cq, apG) {
		apG = (apG || "");
		if (this.obj.acE == "tree") {
			var z = this.obj.ak(cq);
			var z2 = z.parentObject;
			var i = 0;
			for (i = 0; i < z2.aE; i++)
				if (z2.childNodes[i] == z)
					break;
			var str = apG + "tr_id=" + this.escape(z.id);
			str += "&" + apG + "tr_pid=" + this.escape(z2.id);
			str += "&" + apG + "tr_order=" + i;
			str += "&" + apG + "tr_text=" + this.escape(z.span.innerHTML);
			z2 = (z.PJ || "").split(",");
			for (i = 0; i < z2.length; i++)
				str += "&" + apG + this.escape(z2[i]) + "="
						+ this.escape(z.userData["t_" + z2[i]])
		} else {
			var str = apG + "gr_id=" + this.escape(cq);
			if (this.obj.gY())
				str += "&" + apG + "gr_pid="
						+ this.escape(this.obj.getParentId(cq));
			var r = this.obj.dz(cq);
			for ( var i = 0; i < this.obj.gA; i++) {
				if (this.obj._c_order)
					var i_c = this.obj._c_order[i];
				else
					var i_c = i;
				var c = this.obj.cells(cq, i);
				if (this._changed && !c.hy())
					continue;
				if (this.iO)
					str += "&" + apG + this.obj.getColumnId(i) + "="
							+ this.escape(c.getValue());
				else
					str += "&" + apG + "c" + i_c + "="
							+ this.escape(c.getValue())
			}
			;
			var data = this.obj.da[cq];
			if (data) {
				for ( var j = 0; j < data.kk.length; j++)
					if (data.kk[j].indexOf("__") != 0)
						str += "&" + apG + data.kk[j] + "="
								+ this.escape(data.values[j])
			}
			;
			var data = this.obj.da["gridglobaluserdata"];
			if (data) {
				for ( var j = 0; j < data.kk.length; j++)
					str += "&" + apG + data.kk[j] + "="
							+ this.escape(data.values[j])
			}
		}
		;
		if (this.obj.linked_form)
			str += this.obj.linked_form.get_serialized(cq, apG);
		return str
	},
	xU : function(ind, dw) {
		this.az[ind] = dw || ( function(value) {
			return (value != "")
		})
	},
	hX : function(ind) {
		this.az[ind] = false
	},
	iv : function(aiw) {
		var i = 0;
		for (i = 0; i < this.T.length; i++)
			if (aiw == this.T[i])
				break;
		return i
	},
	alx : function(name, handler) {
		if (!this.aF)
			this.aF = [];
		this.aF[name] = handler
	},
	afterUpdateCallback : function(sid, tid, action, btag) {
		delete this._in_progress[sid];
		var correct = (action != "error" && action != "invalid");
		if (!correct)
			this.set_invalid(sid, action);
		if ((this.aF) && (this.aF[action]) && (!this.aF[action](btag)))
			return;
		this.setUpdated(sid, false);
		var jl = sid;
		switch (action) {
		case "inserted":
		case "insert":
			if (tid != sid) {
				this.obj[this._methods[2]](sid, tid);
				sid = tid
			}
			;
			break;
		case "delete":
		case "deleted":
			this.obj.setUserData(sid, this.action_param, "true_deleted");
			this.obj[this._methods[3]](sid);
			return this.callEvent("onAfterUpdate", [ sid, action, tid, btag ]);
			break
		}
		;
		if (correct)
			this.obj.setUserData(sid, this.action_param, '');
		this.callEvent("onAfterUpdate", [ sid, action, tid, btag ])
	},
	afterUpdate : function(that, b, c, d, xml) {
		xml.cR("data");
		if (!xml.ai.responseXML)
			return;
		var CZ = xml.et("//data/action");
		for ( var i = 0; i < CZ.length; i++) {
			var btag = CZ[i];
			var action = btag.getAttribute("type");
			var sid = btag.getAttribute("sid");
			var tid = btag.getAttribute("tid");
			that.afterUpdateCallback(sid, tid, action, btag)
		}
		;
		if (that.ef)
			that.ef--;
		if ((that.obj.acE == "tree" || that.obj._h2) && that.T.length)
			that.sendData();
		that.callEvent("onAfterUpdateFinish", []);
		if (!that.T.length)
			that.callEvent("onFullSync", [])
	},
	init : function(anObj) {
		this.obj = anObj;
		if (this.obj._dp_init)
			return this.obj._dp_init(this);
		var self = this;
		if (this.obj.acE == "tree") {
			this._methods = [ "setItemStyle", "", "changeItemId", "deleteItem" ];
			this.obj.attachEvent("onEdit", function(state, id) {
				if (state == 3)
					self.setUpdated(id, true);
				return true
			});
			this.obj.attachEvent("onDrop", function(id, id_2, id_3, tree_1,
					tree_2) {
				if (tree_1 == tree_2)
					self.setUpdated(id, true)
			});
			this.obj.ajc = function(cq) {
				var z = self.YA(cq);
				if (z == "inserted") {
					self.set_invalid(cq, false);
					self.setUpdated(cq, false);
					return true
				}
				;
				if (z == "true_deleted") {
					self.setUpdated(cq, false);
					return true
				}
				;
				self.setUpdated(cq, true, "deleted");
				return false
			};
			this.obj.BX = function(cq) {
				self.setUpdated(cq, true, "inserted")
			}
		} else {
			this._methods = [ "setRowTextStyle", "setCellTextStyle",
					"changeRowId", "deleteRow" ];
			this.obj.attachEvent("onEditCell", function(state, id, index) {
				if (self._columns && !self._columns[index])
					return true;
				var cell = self.obj.cells(id, index);
				if (state == 1) {
					if (cell.wI()) {
						self.setUpdated(id, true)
					}
				} else if (state == 2) {
					if (cell.hy()) {
						self.setUpdated(id, true)
					}
				}
				;
				return true
			});
			this.obj.attachEvent("onRowPaste", function(id) {
				self.setUpdated(id, true)
			});
			this.obj.attachEvent("onRowIdChange", function(id, newid) {
				var ind = self.iv(id);
				if (ind < self.T.length)
					self.T[ind] = newid
			});
			this.obj.attachEvent("onSelectStateChanged", function(cq) {
				if (self.cJ == "row")
					self.sendData();
				return true
			});
			this.obj.attachEvent("onEnter", function(cq, akG) {
				if (self.cJ == "row")
					self.sendData();
				return true
			});
			this.obj.attachEvent("onBeforeRowDeleted", function(cq) {
				if (this.fX && self.dnd) {
					window.setTimeout( function() {
						self.setUpdated(cq, true)
					}, 1);
					return true
				}
				;
				var z = self.YA(cq);
				if (this._h2) {
					this._h2.so(cq, function(el) {
						self.setUpdated(el.id, false);
						self.markRow(el.id, true, "deleted")
					}, this)
				}
				;
				if (z == "inserted") {
					self.set_invalid(cq, false);
					self.setUpdated(cq, false);
					return true
				}
				;
				if (z == "deleted")
					return false;
				if (z == "true_deleted") {
					self.setUpdated(cq, false);
					return true
				}
				;
				self.setUpdated(cq, true, "deleted");
				return false
			});
			this.obj.attachEvent("onRowAdded", function(cq) {
				if (this.fX && self.dnd)
					return true;
				self.setUpdated(cq, true, "inserted");
				return true
			});
			this.obj.on_form_update = function(id) {
				self.setUpdated(id, true);
				return true
			}
		}
	},
	link_form : function(obj) {
		obj.on_update = this.obj.on_form_update
	},
	afd : function(ev) {
		this.attachEvent("onAfterUpdate", ev)
	},
	iU : function(mode) {
	},
	gS : function(aQ) {
		this.attachEvent("onBeforeDataSending", aQ)
	}
};