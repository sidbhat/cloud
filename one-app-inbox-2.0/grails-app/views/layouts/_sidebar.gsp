<!-- Sidebar -->
<%@ page import="org.grails.taggable.*" %>
%{----------------------------------------------------------------------------
  - [ NIKKISHI CONFIDENTIAL ]                                                -
  -                                                                          -
  -    Copyright (c) 2011.  Nikkishi LLC                                     -
  -    All Rights Reserved.                                                  -
  -                                                                          -
  -   NOTICE:  All information contained herein is, and remains              -
  -   the property of Nikkishi LLC and its suppliers,                        -
  -   if any.  The intellectual and technical concepts contained             -
  -   herein are proprietary to Nikkishi LLC and its                         -
  -   suppliers and may be covered by U.S. and Foreign Patents,              -
  -   patents in process, and are protected by trade secret or copyright law.
  -   Dissemination of this information or reproduction of this material     -
  -   is strictly forbidden unless prior written permission is obtained      -
  -   from Nikkishi LLC.                                                     -
  ----------------------------------------------------------------------------}%

<%
	def user
	def client
	if (session?.SPRING_SECURITY_CONTEXT?.authentication?.principal ){
		 user = com.oneapp.cloud.core.User.findByUsername ( session?.SPRING_SECURITY_CONTEXT?.authentication?.principal?.username )
		 client = com.oneapp.cloud.core.Client.get(user?.userTenantId)
		 currentUserProfileInstance = com.oneapp.cloud.core.UserProfile.findByUser(user)
	}
%>
<sec:ifLoggedIn>

    <aside class="grid_1">
    	<nav class="recent">
            <ul class="clearfix">
            	<user:userProfileInfo></user:userProfileInfo> 
            </ul>
        </nav>

     
        <nav class="recent">
			
            <ul class="clearfix">
            	<sec:ifAnyGranted roles="ROLE_USER">
				<li id="feedLink" class="feedLink ${params.controller=='dashboard' && filterTag==null ?'active':''}">
	            	<a class="nav-icon icon-house" style="width:135px;" onclick="loadScreenBlock()" href="${request.getContextPath()}/dashboard/index">
	            	Inbox<span class="formInstCount"style="padding: 0 2px;" ><div id="totalFeedCount"></div> </span></a>
			     	 </li>
			     	  <li class="feedLink ${params.controller=='dashboard' && filterTag=='task' ?'active':''}"><a class="nav-icon icon-tick" href="${request.getContextPath()}/dashboard/index?filterTag=task" onclick="loadScreenBlock()" style="width:135px;" >Tasks<span style="padding: 0 2px;" class="formInstCount"><div id="totalTaskCount"></div></a></li>
			     	  <li class="feedLink ${params.controller=='dashboard' && filterTag=='recentTask' ?'active':''}"><a class="nav-icon icon-time" href="${request.getContextPath()}/dashboard/index?filterTag=recentTask" onclick="loadScreenBlock()" style="width:135px;" >Recent Tasks<span style="padding: 0 2px;" class="formInstCount"><div id="recentTaskCount"></div></a></li>
              	</sec:ifAnyGranted>
             </ul> 	
             <ul id="leftpanelformlist"class="clearfix">
				<sec:ifAnyGranted roles="ROLE_USER">
				  <form:leftPanelList />
				  <div id="divLeftPanelImg" style="background:#fff;opacity:0.2; display:none !important;text-align :center !important;width: '100%' !important"><img style="margin: auto; position: relative; top: 0pt; bottom: 0pt; " id="leftPanelImg" src="${request.getContextPath()}/images/ajax-loader.gif"></div>
				</sec:ifAnyGranted>
            </ul>
        </nav>
         <sec:ifAnyGranted roles="ROLE_USER">
	         <nav class="subnav recent formMenu">
			 	   <div class="formHeadingDiv">
						<a href="#" class="moreText" onclick="showOtherFeature('${request.getContextPath()}/activityFeed/leftPanelTagList')">More</a>
					</div>
	               
	          </nav>
          </sec:ifAnyGranted>
       
			<nav class="subnav">
              	<ul class="clearfix" style="overflow:hidden;display: none;" id="otherFeature">
					<li name="formListName"><g:link controller="ruleSet" class="nav-icon icon-wrench-orange" onclick="loadScreenBlock()"><g:message code="menu.subscription" default="Subscription"/></g:link></li>
					<li name="formListName"><g:link controller="report" action="index" class="nav-icon icon-charts" onclick="loadScreenBlock()"><g:message code="menu.reports" default="Reports"/></g:link></li>
				</ul>
         	  	<nav class="subnav" style="overflow:hidden;display: none;" id="tags2">
					 &nbsp;
				</nav>
        	</nav>
        	<nav class="subnav" style="overflow:hidden;display: none;" id="tags">
			 	<ul id="leftpanelTagList"class="clearfix" style="overflow:hidden;" >
			 	<div id="divleftpanelTagList" style=" background:#fff;opacity:0.2;display:none !important;text-align :center !important;width: '100%' !important"><img style="margin: auto; position: relative; top: 0pt; bottom: 0pt; " id="leftPanelImg" src="${request.getContextPath()}/images/ajax-loader.gif"></div>
					<%--<g:tagList></g:tagList>
				--%></ul>
			 </nav>
        	<nav class="" style="height:200px;" id="chat">
			 	<ul id="leftpanelUserList" class="clearfix" style="overflow:hidden;" >
			 		<icep:region group="loggedInUser" controller="alert" action="loggedInUsers" />
			    </ul>
			 </nav>
    </aside>
</sec:ifLoggedIn>
<script>
	function showOtherFeature(urL){
		$(".formMenu").hide();
		$("#otherFeature").slideDown('slow');
		$("#tags").show()
		tagList(urL)
		if($("[name='tagList']").length > 0)
			$("#tags2").show()
	}
	function showAllForms(urL){
		$("#idformMoreMenu").hide();
		$("#divLeftPanelImg").show();
		$.ajax({ 
			url: urL,
	        type: "GET",
	        dataType: "json",
	        success: function(data) {
	        	var panelList=$("#leftpanelformlist");
	        	panelList.children().remove();
        	    $.each(data, function(key, val) {
		        	 var id=val.id
		        	 var name=val.name
		        	 var count
		        	 if(val.count<=100){
		        	   count=val.count
		        	 }else{
		        		count='100+'
			         }
		        	 paramscontroller='${params.controller}'
			         var fm='${formId}';
			         var cn= (paramscontroller=='ddc'&& fm==id)?'active':'notactive'
		        	 var li = $('<li name="formList"class="'+cn+'" ></li>').appendTo(panelList)
		        	 var tab=$('<table style=" border-collapse:collapse;border-spacing:0;"></table>').appendTo(li)
					 var row=$('<tr></tr>').appendTo(tab)
					 var cell_1=$('<td></td>').appendTo(row)
					 var cell_2=$('<td class="showToolTip"></td>').attr('title',name).appendTo(row) 
					 var aTag_1=$('<a class="icon-toggle-plus createFormInstance" onclick="loadScreenBlock()" ></a>').attr('href','/form-builder/formViewer/create?formId='+id).appendTo(cell_1)
					 var aTag_2=$('<a style="padding:5px 0 5px 9px;width:135px;"onclick="loadScreenBlock()"></a>').attr('href','/form-builder/formViewer/list?formId='+id).appendTo(cell_2)
					 var div_1=$('<div class="formInstCount" style="padding: 0 2px;"></div>').appendTo(aTag_2)
						 div_1.html(count);
					 var div_2=$('<div class="menuFormName"></div>').appendTo(aTag_2)
					 	div_2.html(name);
        	 	});
	        },
			error: function(){
				$("#divLeftPanelImg").hide();
				$("#idformMoreMenu").show();
			}
	  });
	}

	function tagList(urL){
		var $node = $("#leftpanelTagList");
		$("#divleftpanelTagList").show();
		$.ajax({ 
			url: urL,
	        type: "GET",
	        dataType: "html",
	        success: function (data, textStatus, xhr) {
	        	$node.children().remove();
	        	$node.html(data);
	        },
			error: function(){
				$("#divleftpanelTagList").hide();
			}
	  });
	}

	function updateNewuser(){
		$.ajax({ 
  	  		url: "${grailsApplication.config.grails.serverURL}/alert/loggedInUsers",
  	          type: "GET",
  	          dataType: "json",
  	          success: function(data) {
  	  	        $("[name='onlineStatus']").html('')
				for(var i=0; i<data.length; i++){
	        			$("#onlineStatus"+data[i]).html("<img src='${grailsApplication.config.grails.serverURL}/images/bullet_green.png'/>")
  	       		}
  	          },error: function(){
  				}
  		  });
		}

	$("#chat").hover(
			  function () {
			    $(this).css('overflow-y','auto')
			  }, 
			  function () {
			    $(this).css('overflow-y','hidden')
			  }
			);
		
	$("#leftpanelUserList li").hover(
			  function () {
			    $(this).css('background','#f4f4f4')
			  }, 
			  function () {
			    $(this).css('background','#ffffff')
			  }
			);	

</script>
<!-- Sidebar End -->