/*
===================================================================
Copyright DHTMLX LTD. http://www.dhtmlx.com
This code is obfuscated and not allowed for any purposes except 
using on sites which belongs to DHTMLX LTD.

Please contact sales@dhtmlx.com to obtain necessary 
license for usage of dhtmlx components.
===================================================================
 */D.prototype.enableDragAndDrop = function(mode) {
	if (mode == "temporary_disabled") {
		this.ko = false;
		mode = true
	} else
		this.ko = true;
	this.nz = K(mode);
	this._drag_validate = true
};
D.prototype.setDragBehavior = function(mode) {
	this.ll = this.mW = 0;
	switch (mode) {
	case "child":
		this.mj = 0;
		this.Fl = false;
		break;
	case "sibling":
		this.mj = 1;
		this.Fl = false;
		break;
	case "sibling-next":
		this.mj = 1;
		this.Fl = true;
		break;
	case "complex":
		this.mj = 2;
		this.Fl = false;
		break;
	case "complex-next":
		this.mj = 2;
		this.Fl = true;
		break
	}
};
D.prototype.enableDragOrder = function(mode) {
	this.acy = K(mode)
};
D.prototype.Jt = function(row, ids) {
	var z = this._h2.get[row.idd].parent;
	if (!z.parent)
		return;
	for ( var i = 0; i < ids.length; i++)
		if (ids[i] == z.id)
			return true;
	return this.Jt(this.bj[z.id], ids)
};
D.prototype.gL = function(aI, e) {
	this.editStop();
	if (window.F.al)
		return null;
	if (!this.ko)
		return null;
	aI.parentObject = new Object();
	aI.parentObject.treeNod = this;
	var text = this.callEvent("onBeforeDrag", [ aI.parentNode.idd,
			aI._cellIndex ]);
	if (!text)
		return null;
	var z = new Array();
	z = this.bI();
	z = (((z) && (z != "")) ? z.split(this.gX) : []);
	var exst = false;
	for ( var i = 0; i < z.length; i++)
		if (z[i] == aI.parentNode.idd)
			exst = true;
	if (!exst) {
		this.Th(this.bj[aI.parentNode.idd], false, e.ctrlKey, false);
		if (!e.ctrlKey) {
			z = []
		}
		;
		z[this.gN ? z.length : 0] = aI.parentNode.idd
	}
	;
	if (this.gY()) {
		for ( var i = z.length - 1; i >= 0; i--)
			if (this.Jt(this.bj[z[i]], z))
				z.splice(i, 1)
	}
	;
	var self = this;
	if (z.length && this.acy)
		z.sort( function(a, b) {
			return (self.bj[a].rowIndex > self.bj[b].rowIndex ? 1 : -1)
		});
	var el = this.bw(_isIE ? e.srcElement : e.target, "TD");
	if (el)
		this.acJ = el._cellIndex;
	this.jG = new Array();
	for ( var i = 0; i < z.length; i++)
		if (this.bj[z[i]]) {
			this.jG[this.jG.length] = this.bj[z[i]];
			this.bj[z[i]].treeNod = this
		}
	;
	aI.parentObject.parentNode = aI.parentNode;
	var fj = document.createElement('div');
	fj.innerHTML = (text !== true ? text : this.vD(aI.parentNode.idd));
	fj.style.position = "absolute";
	fj.className = "dragSpanDiv";
	return fj
};
D.prototype.WW = function() {
	this.Ac = document.createElement("DIV");
	this.Ac.innerHTML = "&nbsp;";
	this.Ac.className = "gridDragLine";
	this.HF.appendChild(this.Ac)
};
function fX(a, b, c, d, e, f, j, h, k, l) {
	this.source = a || "grid";
	this.target = b || "grid";
	this.mode = c || "move";
	this.dropmode = d || "child";
	this.sid = e || 0;
	this.tid = f;
	this.sobj = null;
	this.tobj = null;
	this.sExtra = k || null;
	this.tExtra = l || null;
	return this
};
fX.prototype.valid = function() {
	if (this.sobj != this.tobj)
		return true;
	if (this.sid == this.tid)
		return false;
	if (this.target == "treeGrid") {
		var z = this.tid;
		while (z = this.tobj.getParentId(z)) {
			if (this.sid == z)
				return false
		}
	}
	;
	return true
};
fX.prototype.close = function() {
	this.sobj = null;
	this.tobj = null
};
fX.prototype.copy = function() {
	return new fX(this.source, this.target, this.mode, this.dropmode, this.sid,
			this.tid, this.sobj, this.tobj, this.sExtra, this.tExtra)
};
fX.prototype.set = function(a, b) {
	this[a] = b;
	return this
};
fX.prototype.uid = function(a, b) {
	this.zX = this.sid;
	while (this.tobj.bj[this.zX])
		this.zX = this.zX + ((new Date()).valueOf());
	return this
};
fX.prototype.data = function() {
	if (this.sobj == this.tobj)
		return this.sobj.Vf(this.sobj.bj[this.sid]);
	if (this.source == "tree")
		return this.tobj.Bl(this.sobj, this.sid, this.tid);
	else
		return this.tobj.aaJ(this.sid, this.sobj, this.tobj)
};
fX.prototype.bu = function() {
	if (this.source == "treeGrid")
		return this.sobj._h2.get[this.sid].TP ? this.sobj._h2.get[this.sid].agN
				: null;
	return null
};
fX.prototype.qc = function() {
	if (!this.tid)
		return 0;
	if (!this.tobj._h2)
		return 0;
	if (this.target == "treeGrid")
		if (this.dropmode == "child")
			return this.tid;
		else {
			var z = this.tobj.bj[this.tid];
			var alP = this.tobj._h2.get[z.idd].parent.id;
			if ((this.alfa) && (this.tobj.Fl) && (z.nextSibling)) {
				var GG = this.tobj._h2.get[z.nextSibling.idd].parent.id;
				if (GG == this.tid)
					return this.tid;
				if (GG != alP)
					return GG
			}
			;
			return alP
		}
};
fX.prototype.ind = function() {
	if (this.tid == window.unknown)
		return 0;
	if (this.target == "treeGrid") {
		if (this.dropmode == "child")
			this.tobj.hx(this.tid);
		else
			this.tobj.hx(this.tobj.getParentId(this.tid))
	}
	;
	var ind = this.tobj.P.bP(this.tobj.bj[this.tid]);
	if ((this.alfa) && (this.tobj.Fl) && (this.dropmode == "sibling")) {
		var z = this.tobj.bj[this.tid];
		if ((z.nextSibling)
				&& (this._h2.get[z.nextSibling.idd].parent.id == this.tid))
			return ind + 1
	}
	;
	return (ind + 1 + ((this.target == "treeGrid" && ind >= 0 && this.tobj._h2.get[this.tobj.P[ind].idd].state == "minus") ? this.tobj
			.jS(this.tobj.P[ind].idd, 0)
			: 0))
};
fX.prototype.img = function() {
	if ((this.target != "grid") && (this.sobj._h2))
		return this.sobj.getItemImage(this.sid);
	else
		return null
};
fX.prototype.anJ = function() {
	var res = new Array();
	for ( var i = 0; i < this.sid.length; i++)
		res[res.length] = this.sid[i][(this.source == "tree") ? "id" : "idd"];
	return res.join(",")
};
D.prototype.BK = function(la, Ll, il, aG) {
	if (this.nh)
		return this.aC.BK();
	var z = (this.aG);
	if (this.hg)
		window.clearTimeout(this.hg);
	var Mi = il.parentNode;
	var IX = la.parentObject;
	if (!Mi.idd) {
		Mi.grid = this;
		this.mW = 0
	}
	;
	var c = new fX(0, 0, 0, (Mi.grid.ll ? "sibling" : "child"));
	if (IX && IX.childNodes)
		c.set("source", "tree").set("sobj", IX.treeNod).set("sid", c.sobj.jG);
	else {
		if (IX.treeNod.gY && IX.treeNod.gY())
			c.set("source", "treeGrid");
		c.set("sobj", IX.treeNod).set("sid", c.sobj.jG)
	}
	;
	if (Mi.grid.gY())
		c.set("target", "treeGrid");
	else
		c.set("dropmode", "sibling");
	c.set("tobj", Mi.grid).set("tid", Mi.idd);
	if (((c.tobj.mj == 2) && (c.tobj.ll == 1)) && (c.tobj.mW < 0))
		if (c.tobj.obj.rows[1].idd != c.tid)
			c.tid = Mi.previousSibling.idd;
		else
			c.tid = 0;
	var el = this.bw(aG, "TD");
	if (el)
		c.set("tExtra", el._cellIndex);
	if (el)
		c.set("sExtra", c.sobj.acJ);
	if (c.sobj.BM)
		c.set("mode", "copy");
	if (c.tobj.nh)
		c.tobj = c.tobj.aC;
	if (c.sobj.nh)
		c.sobj = c.sobj.aC;
	c.tobj.mH();
	if (IX && IX.treeNod && IX.treeNod._nonTrivialRow)
		IX.treeNod._nonTrivialRow(this, c.tid, c.dropmode, IX);
	else {
		c.tobj.fX = c;
		if (!c.tobj.callEvent("onDrag", [ c.anJ(), c.tid, c.sobj, c.tobj,
				c.sExtra, c.tExtra ]))
			return c.tobj.fX = null;
		var result = new Array();
		if (typeof (c.sid) == "object") {
			var nc = c.copy();
			for ( var i = 0; i < c.sid.length; i++) {
				if (!nc.set("alfa", (!i)).set("sid",
						c.sid[i][(c.source == "tree" ? "id" : "idd")]).valid())
					continue;
				//nc.tobj.sL(nc);
				if (nc.target == "treeGrid" && nc.dropmode == "child")
					nc.tobj.hx(nc.tid);
				result[result.length] = nc.zX;
				nc.set("dropmode", "sibling").set("tid", nc.zX)
			}
			;
			nc.close()
		} else
			c.tobj.sL(c);
		if (c.tobj.acO)
			c.tobj.acO();
		c.tobj.callEvent("onDrop", [ c.anJ(), c.tid, result.join(","), c.sobj,
				c.tobj, c.sExtra, c.tExtra ])
	}
	;
	c.tobj.fX = null;
	c.close()
};
D.prototype.sL = function(c) {
	if ((c.sobj == c.tobj) && (c.source == "grid") && (c.mode == "move")
			&& !this.aC) {
		if (c.sobj.WJ)
			return;
		var fr = c.sobj.bj[c.sid];
		var bind = c.sobj.P.bP(fr);
		c.sobj.P.hd(c.sobj.P.bP(fr));
		c.sobj.aD.hd(c.sobj.aD.bP(fr));
		c.sobj.aD.ml(c.ind(), fr);
		if (c.tobj.aC) {
			c.tobj.aC.P.hd(bind);
			var tr = c.tobj.aC.bj[c.sid];
			tr.parentNode.removeChild(tr)
		}
		;
		c.sobj._insertRowAt(fr, c.ind());
		c.zX = c.sid;
		c.sobj.callEvent("onGridReconstructed", []);
		return
	}
	;
	var aiK;
	if (this._h2 && typeof c.tid != "undefined" && c.dropmode == "sibling"
			&& (this.Fl || c.tid)) {
		if (c.alfa && this.Fl && this._h2.get[c.tid].bu.length) {
			this.hx(c.tid);
			aiK = c.uid().tobj.alo(c.zX, c.data(),
					this._h2.get[c.tid].bu[0].id, c.img(), c.bu())
		} else
			aiK = c.uid().tobj.amC(c.zX, c.data(), c.tid, c.img(), c.bu())
	} else
		aiK = c.uid().tobj.hY(c.zX, c.data(), c.ind(), c.qc(), c.img(), c.bu());
	if (c.source == "tree") {
		this.callEvent("onRowAdded", [ c.zX ]);
		var sn = c.sobj.ak(c.sid);
		if (sn.aE) {
			var nc = c.copy().set("tid", c.zX).set("dropmode",
					c.target == "grid" ? "sibling" : "child");
			for ( var j = 0; j < sn.aE; j++) {
				c.tobj.sL(nc.set("sid", sn.childNodes[j].id));
				if (c.mode == "move")
					j--
			}
			;
			nc.close()
		}
	} else {
		c.tobj.Ux(c);
		this.callEvent("onRowAdded", [ c.zX ]);
		if ((c.source == "treeGrid")) {
			if (c.sobj == c.tobj)
				aiK.Q = c.sobj.bj[c.sid].Q;
			var YY = c.sobj._h2.get[c.sid];
			if ((YY) && (YY.bu.length)) {
				var nc = c.copy().set("tid", c.zX);
				if (c.target == "grid")
					nc.set("dropmode", "sibling");
				else {
					nc.tobj.hx(c.tid);
					nc.set("dropmode", "child")
				}
				;
				var l = YY.bu.length;
				for ( var j = 0; j < l; j++) {
					c.sobj.render_row_tree(null, YY.bu[j].id);
					c.tobj.sL(nc.set("sid", YY.bu[j].id));
					if (l != YY.bu.length) {
						j--;
						l = YY.bu.length
					}
				}
				;
				nc.close()
			}
		}
	}
	;
	if (c.mode == "move") {
		c.sobj[(c.source == "tree") ? "deleteItem" : "deleteRow"](c.sid);
		if ((c.sobj == c.tobj) && (!c.tobj.bj[c.sid])) {
			c.tobj.changeRowId(c.zX, c.sid);
			c.zX = c.sid
		}
	}
};
D.prototype.aaJ = function(cq, sgrid, aha) {
	var z = new Array();
	for ( var i = 0; i < sgrid.hdr.rows[0].cells.length; i++)
		z[i] = sgrid.cells(cq, i).getValue();
	return z
};
D.prototype.El = function(node, id) {
	if ((!this._h2) || (!id) || (!node))
		return false;
	if (node.id == id)
		return true;
	else
		return this.El(node.parent, id)
};
D.prototype.tY = function(aI, nq, x, y) {
	if (!this.ko)
		return 0;
	var tree = this.gY();
	if (this._drag_validate) {
		if (aI.parentNode == nq.parentNode)
			return 0;
		if ((tree)
				&& ((this
						.El(this._h2.get[aI.parentNode.idd], nq.parentNode.idd))))
			return 0
	}
	;
	var obj = nq.parentNode.idd ? nq.parentNode : nq.parentObject;
	if (!this.callEvent("onDragIn", [ obj.idd || obj.id, aI.parentNode.idd,
			obj.grid || obj.treeNod, aI.parentNode.grid ]))
		return this.VN(aI, x, y, true);
	this.VN(aI, x, y);
	if ((tree) && (aI.parentNode.expand != "")) {
		this.hg = window.setTimeout(new jP(this.AN, this), 1000);
		this.EV = aI.parentNode.idd
	} else if (this.hg)
		window.clearTimeout(this.hg);
	return aI
};
D.prototype.AN = function(e, Qk) {
	Qk.hx(Qk.EV)
};
D.prototype.iX = function(aI) {
	this.mH();
	var obj = aI.parentNode.parentObject ? aI.parentObject.id
			: aI.parentNode.idd;
	this.callEvent("onDragOut", [ obj ]);
	if (this.hg)
		window.clearTimeout(this.hg)
};
D.prototype.VN = function(aI, x, y, Ch) {
	if (!aI.parentNode.idd)
		return;
	var a1 = dg(aI);
	var a2 = dg(this.HF);
	if ((a1 - a2 - parseInt(this.HF.scrollTop)) > (parseInt(this.HF.offsetHeight) - 50))
		this.HF.scrollTop = parseInt(this.HF.scrollTop) + 20;
	if ((a1 - a2) < (parseInt(this.HF.scrollTop) + 30))
		this.HF.scrollTop = parseInt(this.HF.scrollTop) - 20;
	if (Ch)
		return 0;
	if (this.mj == 2) {
		var z = y
				- a1
				+ this.HF.scrollTop
				+ (document.body.scrollTop || document.documentElement.scrollTop)
				- 2 - aI.offsetHeight / 2;
		if ((Math.abs(z) - aI.offsetHeight / 6) > 0) {
			this.ll = 1;
			if (z < 0)
				this.mW = -1;
			else
				this.mW = 1
		} else
			this.ll = 0
	} else
		this.ll = this.mj;
	if (this.ll) {
		if (!this.Ac)
			this.WW();
		this.Ac.style.display = "block";
		this.Ac.style.top = a1 - a2 + ((this.mW >= 0) ? aI.offsetHeight : 0)
				+ "px"
	} else {
		this.BU = aI;
		if (aI.parentNode.tagName == "TR")
			for ( var i = 0; i < aI.parentNode.childNodes.length; i++) {
				var z = aI.parentNode.childNodes[i];
				z.akr = z.style.backgroundColor;
				z.style.backgroundColor = ""
			}
	}
};
D.prototype.mH = function() {
	if (this.Ac)
		this.Ac.style.display = "none";
	if ((this.BU) && (this.BU.parentNode.tagName == "TR"))
		for ( var i = 0; i < this.BU.parentNode.childNodes.length; i++)
			this.BU.parentNode.childNodes[i].style.backgroundColor = this.BU.akr;
	this.BU = null
};
D.prototype.vD = function(jg) {
	var out = this.cells(jg, 0).getValue();
	return out
};
D.prototype.Ux = function(c) {
	if (!c.tobj.da[c.zX] || c.tobj != c.sobj)
		c.tobj.da[c.zX] = new qB();
	var z1 = c.sobj.da[c.sid];
	var z2 = c.tobj.da[c.zX];
	if (z1) {
		z2.kk = z2.kk.concat(z1.kk);
		z2.values = z2.values.concat(z1.values)
	}
};
D.prototype.moveRow = function(cq, mode, CN, Br) {
	switch (mode) {
	case "row_sibling":
		this.acz(cq, CN, "move", "sibling", this, Br);
		break;
	case "up":
		this.zs(cq);
		break;
	case "down":
		this.uQ(cq);
		break
	}
};
D.prototype.oy = function(tree, bn, bT, aa, z2) {
	if ((tree.callEvent) && (!z2))
		if (!tree.callEvent("onDrag", [ aa.idd, bn.id, (bT ? bT.id : null),
				this, tree ]))
			return false;
	var jg = aa.idd;
	var treeNodeId = jg;
	while (tree.gQ[treeNodeId])
		treeNodeId += (new Date()).getMilliseconds().toString();
	var img = (this.gY() ? this.getItemImage(jg) : "");
	if (bT) {
		for (i = 0; i < bn.aE; i++)
			if (bn.childNodes[i] == bT)
				break;
		if (i != 0)
			bT = bn.childNodes[i - 1];
		else {
			st = "TOP";
			bT = ""
		}
	}
	;
	var NF = tree.kV(bn, treeNodeId, this.gridToTreeElement(tree, treeNodeId,
			jg), "", img, img, img, "", "", bT);
	if (this._h2) {
		var Ji = this._h2.get[jg];
		if (Ji.bu.length)
			for ( var i = 0; i < Ji.bu.length; i++) {
				this.oy(tree, NF, 0, this.bj[Ji.bu[i].id], 1);
				if (!this.BM)
					i--
			}
	}
	;
	if (!this.BM)
		this.deleteRow(jg);
	if ((tree.callEvent) && (!z2))
		tree.callEvent("onDrop", [ treeNodeId, bn.id, (bT ? bT.id : null),
				this, tree ])
};
D.prototype.gridToTreeElement = function(treeObj, treeNodeId, jg) {
	return this.cells(jg, 0).getValue()
};
D.prototype.Bl = function(treeObj, treeNodeId, jg) {
	var w = new Array();
	var z = this.cellType.bP("tree");
	if (z == -1)
		z = 0;
	for ( var i = 0; i < this.So(); i++)
		w[w.length] = (i != z) ? (treeObj.getUserData(treeNodeId, this
				.getColumnId(i)) || "") : treeObj.getItemText(treeNodeId);
	return w
};
D.prototype.acz = function(NL, NZ, mode, dropmode, AL, Br) {
	var c = new fX((AL || this).gY() ? "treeGrid" : "grid",
			(Br || this).gY() ? "treeGrid" : "grid", mode, dropmode
					|| "sibling", NL, NZ, AL || this, Br || this);
	c.tobj.sL(c);
	c.close();
	return c.zX
};
D.prototype.enableMercyDrag = function(mode) {
	this.BM = K(mode)
};