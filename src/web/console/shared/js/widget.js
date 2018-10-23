function setInnerHTML(el, htmlCode) {
	var ua = navigator.userAgent.toLowerCase();
	if (ua.indexOf('msie') >= 0 && ua.indexOf('opera') < 0) {
		htmlCode = '<div style="display:none">for IE</div>' + htmlCode;
		htmlCode = htmlCode.replace(/<script([^>]*)>/gi, '<script$1 defer>');
		el.innerHTML = htmlCode;
		el.removeChild(el.firstChild);
	} else {
		var el_next = el.nextSibling;
		var el_parent = el.parentNode;
		el_parent.removeChild(el);
		el.innerHTML = htmlCode;
		if (el_next) {
			el_parent.insertBefore(el, el_next)
		} else {
			el_parent.appendChild(el);
		}
	}
}

function loadWidgetBody(widget,isViewMode) 
{
	dhtmlxAjax.get("/console/shared/asycWidget.vpage?widget=" + widget+"&isViewMode="+isViewMode,
			function(loader) {
				// $(document).ready(
			// function() {
			setInnerHTML(document.getElementById(widget + 'BodyInner'),
					loader.ai.responseText);
			// });
		});

}