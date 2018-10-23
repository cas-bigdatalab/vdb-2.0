/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except 
using on sites which belongs to DHTMLX LTD.

Please contact sales@dhtmlx.com to obtain necessary 
license for usage of dhtmlx components.
===================================================================
 */D.prototype.enableBlockSelection = function(mode) {
	if (typeof this._bs_mode == "undefined") {
		var self = this;
		this.obj.onmousedown = function(e) {
			if (self._bs_mode)
				self.AV((e || event), this);
			return true
		};
		this.Cl = this.csv.row;
		this.attachEvent("onResize", function() {
			self.tV();
			return true
		});
		this.attachEvent("onFilterEnd", this.tV)
	}
	;
	if (mode === false) {
		this._bs_mode = false;
		return this.tV()
	} else
		this._bs_mode = true
};
D.prototype.Pd = function(mode) {
	this.aaV = K(mode)
};
D.prototype.ZD = function() {
	this.obj.onmousedown = null
};
D.prototype.AV = function(event, obj) {
	var self = this;
	//initGridBgcolor();
	if (event.button == 2)
		return;
	var src = event.srcElement || event.target;
	if (this.editor) {
		if (src.tagName
				&& (src.tagName == "INPUT" || src.tagName == "TEXTAREA"))
			return;
		this.editStop()
	}
	;
	if (!self.ft)
		self.setActive(true);
	var pos = this.fI(this.obj);
	var x = event.clientX - pos[0] + document.body.scrollLeft;
	var y = event.clientY - pos[1] + document.body.scrollTop;
	this.Ny(x - 4, y - 4);
	if (src == this.cg) {
		this.tV();
		this.om = null
	} else {
		while (src.tagName.toLowerCase() != 'td')
			src = src.parentNode;
		this.om = src
	}
	;
	this.obj.onmousedown = null;
	this.obj[_isIE ? "onmouseleave" : "onmouseout"] = function(e) {
		if (self.vZ)
			window.clearTimeout(self.vZ)
	};
	this.obj.aiU = this.obj.onmousemove;
	this._init_pos = [ x, y ];
	this.obj.onmousemove = function(e) {
		e = e || event;
		e.returnValue = false;
		self.Dm(e)
	};
	this.ail = document.body.onmouseup;
	document.body.onmouseup = function(e) {
		e = e || event;
		self.Nx(e, this);
		return true
	};
	document.body.onselectstart = function() {
		return false
	}
};
D.prototype._getCellByPos = function(x, y) {
	x = x;
	y = y;
	var _x = 0;
	for ( var i = 0; i < this.obj.rows.length; i++) {
		y -= this.obj.rows[i].offsetHeight;
		if (y <= 0) {
			_x = this.obj.rows[i];
			break
		}
	}
	;
	if (!_x || !_x.idd)
		return null;
	for ( var i = 0; i < this.gA; i++) {
		x -= this.obj.rows[0].childNodes[i].offsetWidth;
		if (x <= 0) {
			while (true) {
				if (_x.bq && _x.bq[i + 1] == _x.bq[i])
					_x = _x.previousSibling;
				else
					return this.cells(_x.idd, i).cell
			}
		}
	}
	;
	return null
};
D.prototype.Dm = function(event) {
	var self = this;
	this.Sm();
	var pos = this.fI(this.obj);
	var X = event.clientX - pos[0] + document.body.scrollLeft;
	var Y = event.clientY - pos[1] + document.body.scrollTop;
	if ((Math.abs(this._init_pos[0] - X) < 5)
			&& (Math.abs(this._init_pos[1] - Y) < 5))
		return this.tV();
	if (this.om == null)
		this.AX = this.om = this.bw(event.srcElement || event.target, "TD");
	else if (event.srcElement || event.target) {
		if ((event.srcElement || event.target).className == "dhtmlxGrid_selection")
			this.AX = (this._getCellByPos(X, Y) || this.AX);
		else {
			var t = this.bw(event.srcElement || event.target, "TD");
			if (t.parentNode.idd)
				this.AX = t
		}
	}
	;
	var Wu = this.HF.scrollLeft + this.HF.clientWidth;
	var WF = this.HF.scrollTop + this.HF.clientHeight;
	var afu = this.HF.scrollLeft;
	var agr = this.HF.scrollTop;
	var Di = false;
	if (this.vZ)
		window.clearTimeout(this.vZ);
	if (X + 20 >= Wu) {
		this.HF.scrollLeft = this.HF.scrollLeft + 20;
		Di = true
	} else if (X - 20 < afu) {
		this.HF.scrollLeft = this.HF.scrollLeft - 20;
		Di = true
	}
	;
	if (Y + 20 >= WF && !this.nh) {
		this.HF.scrollTop = this.HF.scrollTop + 20;
		Di = true
	} else if (Y - 20 < agr && !this.nh) {
		this.HF.scrollTop = this.HF.scrollTop - 20;
		Di = true
	}
	;
	this.iF = this.IM(this.om, this.AX);
	if (Di) {
		var a = event.clientX;
		var b = event.clientY;
		this.vZ = window.setTimeout( function() {
			self.Dm( {
				clientX : a,
				clientY : b
			})
		}, 100)
	}
};
D.prototype.Nx = function(event) {
	var self = this;
	if (this.vZ)
		window.clearTimeout(this.vZ);
	this.obj.onmousedown = function(e) {
		if (self._bs_mode)
			self.AV((e || event), this);
		return true
	};
	this.obj.onmousemove = this.obj.aiU || null;
	document.body.onmouseup = this.ail || null;
	if (parseInt(this.cg.style.width) < 2 && parseInt(this.cg.style.height) < 2) {
		this.tV()
	} else {
		var src = this.bw(event.srcElement || event.target, "TD");
		if ((!src) || (!src.parentNode.idd)) {
			src = this.AX
		}
		;
		if (!src)
			return this.tV();
		while (src.tagName.toLowerCase() != 'td')
			src = src.parentNode;
		this.Jc = src;
		this.iF = this.IM(this.om, this.Jc);
		this.callEvent("onBlockSelected", [])
	}
	;
	document.body.onselectstart = function() {
	}
};
D.prototype.IM = function(cP, iA) {
	var pos = {};
	pos.By = cP._cellIndex;
	pos.Bt = this.getRowIndex(cP.parentNode.idd);
	pos.yz = iA._cellIndex;
	pos.yA = this.getRowIndex(iA.parentNode.idd);
	var Ut = cP.offsetWidth;
	var Rh = cP.offsetHeight;
	cP = this.fI(cP, this.obj);
	var LZ = iA.offsetWidth;
	var JH = iA.offsetHeight;
	iA = this.fI(iA, this.obj);
	if (cP[0] < iA[0]) {
		var Left = cP[0];
		var Right = iA[0] + LZ
	} else {
		var akp = pos.yz;
		pos.yz = pos.By;
		pos.By = akp;
		var Left = iA[0];
		var Right = cP[0] + Ut
	}
	;
	if (cP[1] < iA[1]) {
		var Top = cP[1];
		var Bottom = iA[1] + JH
	} else {
		var akp = pos.yA;
		pos.yA = pos.Bt;
		pos.Bt = akp;
		var Top = iA[1];
		var Bottom = cP[1] + Rh
	}
	;
	var Width = Right - Left;
	var Height = Bottom - Top;
	this.cg.style.left = Left + 'px';
	this.cg.style.top = Top + 'px';
	this.cg.style.width = Width + 'px';
	this.cg.style.height = Height + 'px';
	return pos
};
D.prototype.Ny = function(x, y) {
	if (this.cg == null) {
		var div = document.createElement('div');
		div.style.position = 'absolute';
		div.style.display = 'none';
		div.className = 'dhtmlxGrid_selection';
		this.cg = div;
		this.cg.onmousedown = function(e) {
			e = e || event;
			if (e.button == 2 || (gP && e.ctrlKey))
				return this.parentNode.grid.callEvent("onBlockRightClick", [
						"BLOCK", e ])
		};
		this.cg.oncontextmenu = function() {
			return false
		};
		this.HF.appendChild(this.cg)
	}
	;
	this.cg.style.width = '0px';
	this.cg.style.height = '0px';
	this.cg.style.left = x + 'px';
	this.cg.style.top = y + 'px';
	this.cg.VH = x;
	this.cg.Vl = y
};
D.prototype.Sm = function() {
	if (this.cg)
		this.cg.style.display = ''
};
D.prototype.tV = function() {
	if (this.cg)
		this.cg.style.display = 'none';
	this.iF = null
};
D.prototype.wO = function() {
	if (this.iF != null) {
		var serialized = new Array();
		if (this.oX)
			this.zt = "getMathValue";
		else if (this.aaV)
			this.zt = "getTitle";
		else
			this.zt = "getValue";
		for ( var i = this.iF.Bt; i <= this.iF.yA; i++) {
			var data = this.lJ(this.P[i], null, this.iF.By, this.iF.yz + 1);
			if (!this.Sz)
				serialized[serialized.length] = data.substr(data
						.indexOf(this.csv.cell) + 1);
			else
				serialized[serialized.length] = data
		}
		;
		serialized = serialized.join(this.Cl);
		this.yP(serialized)
	}
};
D.prototype.ug = function() {
	var serialized = this.rD();
	if (this.iF != null) {
		var MO = this.iF.Bt;
		var MX = this.iF.By
	} else if (this.cell != null && !this.editor) {
		var MO = this.getRowIndex(this.cell.parentNode.idd);
		var MX = this.cell._cellIndex
	} else {
		return false
	}
	;
	serialized = serialized.split(this.Cl);
	if ((serialized.length > 1) && (serialized[serialized.length - 1] == ""))
		serialized.splice(serialized.length - 1, 1);
	for ( var i = 0; i < serialized.length; i++) {
		serialized[i] = serialized[i].split(this.csv.cell)
	}
	;
	var amd = MO + serialized.length;
	var Vi = MX + serialized[0].length;
	if (Vi > this.gA)
		Vi = this.gA;
	var k = 0;
	for ( var i = MO; i < amd; i++) {
		var row = this.render_row(i);
		if (row == -1)
			continue;
		var l = 0;
		for ( var j = MX; j < Vi; j++) {
			var ed = this.cells3(row, j);
			if (ed.isDisabled()) {
				l++;
				continue
			}
			;
			if (this.wm)
				this.wm(2, row.idd, j, serialized[k][l], ed.getValue());
			if (ed.combo) {
				var Yp = ed.combo.values;
				for ( var n = 0; n < Yp.length; n++)
					if (serialized[k][l] == Yp[n]) {
						ed.setValue(ed.combo.kk[n]);
						Yp = null;
						break
					}
				;
				if (Yp != null)
					ed.setValue(serialized[k][l++])
			} else
				ed[ed.hW ? "setLabel" : "setValue"](serialized[k][l++]);
			ed.cell.hy = true
		}
		;
		this.callEvent("onRowPaste", [ row.idd ]);
		k++
	}
};