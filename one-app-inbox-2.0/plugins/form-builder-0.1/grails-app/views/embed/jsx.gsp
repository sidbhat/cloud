<%response.setContentType("text/javascript") %>
oneAppFormHeight = null
var setHeight = function(){
	if(oneAppFormHeight!=null){
		var iFrame = document.getElementById("formOneApp${params.id }")
		iFrame.style.height = oneAppFormHeight;
		iFrame.src = "${grailsApplication.config.grails.serverURL }/embed/a?formId=${params.id }";
	}else{
		setTimeout("setHeight()",200);
	}
}

function addEvent( obj, type, fn ) {
    if ( obj.attachEvent ) {
        obj["e"+type+fn] = fn;
        obj[type+fn] = function() { obj["e"+type+fn]( window.event ); };
        obj.attachEvent( "on"+type, obj[type+fn] );
    }
    else{
        obj.addEventListener( type, fn, false );   
    }
}

var formDiv = document.getElementById("oneappcloud${params.id }");
var iFrame = document.createElement("iframe");
formDiv.appendChild(iFrame);
iFrame.setAttribute("id","formOneApp${params.id }");
iFrame.setAttribute("name","formOneApp${params.id }");
iFrame.style.width = "${params.width?:'100%'}";
if(navigator.platform.indexOf("iPod") != -1){
    iFrame.style.height = '100%';
}
iFrame.style.borderStyle = "none";
iFrame.style.borderWidth = "0";
//iFrame.style.borderColor = "initial";
//iFrame.style.borderImage = "initial";
iFrame.setAttribute("allowtransparancy","${params.allowtransparancy?:'true' }");
iFrame.setAttribute("scrolling","auto");
var addEvent = function(obj, type, fn){
	if (obj.attachEvent) {
		obj["e" + type + fn] = fn;
		obj[type + fn] = function () {
			obj["e" + type + fn](window.event)
		};
		obj.attachEvent("on" + type, obj[type + fn]);
	} else {
		obj.addEventListener('message', fn, false);
	}
}
bindMethod = function (method, scope) {
	return function () {
		method.apply(scope, arguments);
	}
}
resizeOneAppForm = function (event) {
	var data = event.data.split('|');
	var newFormHeight = new Number(data[0]); 
	var formEl = document.getElementById("formOneApp${params.id }");
	if (formEl) {
		formEl.height = newFormHeight; 
	}
	if(data[1]=='scroll'){
		window.scroll(0,findPos(document.getElementById("oneappcloud${params.id }")));
	}
}
addEvent(window, 'message', bindMethod(resizeOneAppForm, window))
iFrame.src = "${grailsApplication.config.grails.serverURL }/embed/a?formId=${params.id }";
function findPos(obj) {
	var curtop = 0;
	if (obj.offsetParent) {
		do {
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	return [curtop-100];
	}
}