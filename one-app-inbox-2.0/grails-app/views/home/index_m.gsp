<%@ page import="com.oneapp.cloud.core.FormTagLib" %>
<html>
<head>
    <title>Form Builder Inbox </title>
    <meta name="layout" content="mobile"/>
</head>
<body>
	<script>
		function logoutRedirectContol(){
				$.post("${grailsApplication.config.grails.serverURL}/logout/index",function(data){
					 if(data.status == false)
					 {
						 
					 }
					 else
					 {
						 $.mobile.changePage( "${grailsApplication.config.grails.serverURL}/login/auth", {
								transition: "slide",
								reverse: true,
								changeHash: true
							});
					 }
				});
			}
		function showAllMobileFormList(urL){
			$.mobile.showPageLoadingMsg();
			$.ajax({ 
				url: urL,
		        type: "GET",
		        dataType: "json",
		        success: function(data) {
		        	var panelList=$("#mobileFormList");
		        	panelList.children().remove();
		        	var l=$('<li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-corner-top ui-btn-up-undefined" ></li>').appendTo(panelList)
		        	 l.html("Forms")
	        	    $.each(data, function(key, val) {
	        	    	$.mobile.hidePageLoadingMsg();
			        	 var id=val.id
			        	 var name=val.name
			        	 var count
			        	 if(val.count<=100){
			        	   count=val.count
			        	 }else{
			        		count='100+'
				         }
			        	 var li = $('<li name="formList"  data-theme="c" class="ui-btn ui-btn-icon-right ui-li ui-li-has-alt ui-btn-up-c"></li>').appendTo(panelList)
						 var div_1=$('<div class="ui-btn-inner ui-li ui-li-has-alt"></div>').appendTo(li)
						 var div_2=$('<div  class="ui-btn-text"></div>').appendTo(div_1)
						 var aTag_1=$('<a class="ui-link-inherit" rel="external"></a>').attr('href','/form-builder/formViewer/mobileList?formId='+id).appendTo(div_2)
						  aTag_1.html(name)
						 var span_1=$('<span class="ui-li-count ui-btn-up-c ui-btn-corner-all"></span>').appendTo(li)
						  span_1.html(count)
						 var aTag_2=$('<a title="" class="ui-li-link-alt ui-btn ui-btn-up-c" data-theme="c" rel="external"></a>').attr('href','/form-builder/formViewer/create?formId='+id).appendTo(li)
						 var span_2=$('<span class="ui-btn-inner"></span>').appendTo(aTag_2)
						 var span_3=$('<span class="ui-btn-text"></span>').appendTo(span_2)
						 var span_4=$('<span title="" data-theme="b" class="ui-btn ui-btn-up-b ui-btn-icon-notext ui-btn-corner-all ui-shadow"></span>').appendTo(span_2)
						 var span_5=$('<span class="ui-btn-inner ui-btn-corner-all"></span>').appendTo(span_4)
						 var span_6=$('<span class="ui-btn-text"></span>').appendTo(span_5)
						 var span_5=$('<span class="ui-icon ui-icon-plus ui-icon-shadow"></span>').appendTo(span_4)
	        	 	});
		        	var lq=$('<li data-role="list-divider" class="formMoreMenu ui-li ui-li-divider ui-btn ui-bar-b ui-corner-bottom ui-btn-up-undefined" role="heading"></li>').appendTo(panelList)
		        }
		  });
		}
		function moblieTagList(urL){
			$.mobile.showPageLoadingMsg();
			var $node = $("#idMoblieTagList");
			$("#divleftpanelTagList").show();
			$.ajax({ 
				url: urL,
		        type: "GET",
		        dataType: "html",
		        success: function (data, textStatus, xhr) {
		        	$.mobile.hidePageLoadingMsg();
		        	$node.children().remove();
		        	$node.html(data);
		        },
				error: function(){
					$.mobile.hidePageLoadingMsg();
					$("#divleftpanelTagList").hide();
					window.alert('Error in transmission..! </br> please check your connection..! ');
				}
		  });
		}
		
	</script>
	<div data-role="header" data-position="inline">
		<h1>Form Builder Inbox </h1>
		<a href="javascript:;" onclick="logoutRedirectContol()" class="ui-btn-right" data-icon="check" data-theme="b"><g:message code="menu.logout" default="Logout"/></a>
	</div>
	<div class="content-primary">
		<nav>
			<div data-role="content" >
				
				<ul data-role="listview" data-inset="true" >
					<li data-icon="false">
						<a href="javascript:;">
							<user:userMobileProfileInfo></user:userMobileProfileInfo> 
				         </a>
			         </li>
			      </ul>
				<ul data-role="listview" data-split-icon="plus" data-inset="true">
					<li data-role="list-divider">
						Inbox
					</li>
					<g:mobileInboxFeedCount/>
				</ul>
				<ul id="mobileFormList" data-role="listview" data-split-icon="plus" data-inset="true">
					<li data-role="list-divider">Forms</li>
					<form:mobileFormList/>
				</ul>
				
				<ul data-role="listview" id="idMoblieTagList"data-split-icon="plus" data-inset="true">
					<li data-role="list-divider" onclick="moblieTagList('${request.getContextPath()}/activityFeed/leftPanelMobileTagList')">Show tags</li>
				</ul>
			</div>
		</nav>
	</div>
</body>
</html>
