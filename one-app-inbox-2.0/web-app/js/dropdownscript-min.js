function showHideSetting(){$("ul.dropdown li").hover(function(){$(this).addClass("hover");$("ul:first",this).css("visibility","visible")},function(){$(this).removeClass("hover");$("ul:first",this).css("visibility","hidden")});$("ul.dropdown li ul li:has(ul)").find("a:first").append(" � ")}function loadPopup(){if(popupStatus==0){$("#backgroundPopup").css({opacity:"0.7"});$("#backgroundPopup").fadeIn("slow");$("#popupContact").fadeIn("slow");popupStatus=1}}function disablePopup(){if(popupStatus==1){$("#backgroundPopup").fadeOut("slow");$("#popupContact").fadeOut("slow");popupStatus=0}}function centerPopup(){var a=document.documentElement.clientWidth;var b=document.documentElement.clientHeight;var c=$("#popupContact").height();var d=$("#popupContact").width();$("#popupContact").css({position:"absolute",top:b/2-c/2,left:"210px"});$("#backgroundPopup").css({height:b})}var popupStatus=0;$(document).ready(function(){$("#popupContactClose").click(function(){disablePopup()});$("#backgroundPopup").click(function(){disablePopup()});$(document).keypress(function(a){if(a.keyCode==27&&popupStatus==1){disablePopup()}})})