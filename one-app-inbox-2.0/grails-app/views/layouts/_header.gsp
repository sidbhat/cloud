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
<%@page import="javax.mail.Session"%>
<%@ page import="com.oneapp.cloud.core.SearchController" %>
<%
def user,client
if (session?.SPRING_SECURITY_CONTEXT?.authentication?.principal ){
	user = com.oneapp.cloud.core.User.findByUsername ( session?.SPRING_SECURITY_CONTEXT?.authentication?.principal?.username )
	client = com.oneapp.cloud.core.Client.get(user.userTenantId)
}
 %>
 <script type="text/javascript">
	 var loggedInUser = '${user?.firstName}';
	</script>
<header>
    <div class="container_8 clearfix">
       <h1 class='grid_1'><a href='${grailsApplication.config.grails.serverURL}/home/welcome'><g:message code="menu.oneapp" default="Form Builder"/></a>
       		
       </h1>
    	<nav class="grid_5">
    	<ul class="clearfix">
  				
	<sec:ifLoggedIn>
			<li class="action" style="border-style:none;margin-top:-5px;top:9px;">
                <a href="javascript:;" id="popupballoon123" onclick="updateAndGetData()">
                	<img src="${request.getContextPath()}/images/message_icon_32.png">
                	<span id="newFeedCountContainer" class="newFeedCountContainer" style="display:none;">&nbsp;<icep:region group="secondGroup" controller="alert" action="updates" />&nbsp;</span>
                </a>
            </li>
            <sec:ifAnyGranted roles="ROLE_TRIAL_USER">
              <sec:ifNotGranted roles=" ROLE_HR_MANAGER">
				<li class="action">
					<g:link controller="form" class="button button-blue" onclick="loadScreenBlock()"><span class="add"></span><g:message code="menu.form.builder" default="Form Builder"/></g:link>
	            </li>
	            </sec:ifNotGranted>
	            <sec:ifAnyGranted roles=" ROLE_ADMIN">
				<li class="action">
					<g:link controller="form" class="button button-blue" onclick="loadScreenBlock()"><span class="add"></span><g:message code="menu.form.builder" default="Form Builder"/></g:link>
	            </li>
	            </sec:ifAnyGranted>
         	 </sec:ifAnyGranted>
            <li class="fr">
                <user:userFirstLastName></user:userFirstLastName>
	               <ul>
	               		<sec:ifAnyGranted roles="ROLE_USER">
		               		<li class="openDropDown">
		                       	 	<a href="#"><g:message code="user.list" default="Users"/><span class="arrow-right"></span></a>
		                       	 	<ul class="secondDropDown" style="display: none;">
					               		<li><g:link controller="user"><g:message code="menu.people" default="People"/></g:link></li>
					               		<li><g:link controller="groups"><g:message code="menu.groups" default="Groups"/></g:link></li>
					               		<li><g:link controller="emailSettings"><g:message code="menu.emailAccount" default="Email Settings"/></g:link></li>
					               </ul>
		                     </li>
		                </sec:ifAnyGranted>
	                 	<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
	                       	 <li>
	                       	 	<g:link controller="client" action="list"><g:message code="menu.client" default="Client"/></g:link>
	                       	 </li>
	                       	  <li>
	                       	 	<g:link controller="blockedIp" action="list"><g:message code="menu.blockedIp" default="Blocked IP"/></g:link>
	                       	 </li>
	                       	 <li>
	                       	 	<g:link controller="applicationConf" action="edit"><g:message code="menu.applicationConf" default="App Config"/></g:link>
	                       	 </li>
	                       	  <li>
	                       	 	<g:link controller="tracker" action="list"><g:message code="menu.tracker" default="Tracker"/></g:link>
	                       	 </li>
	                    </sec:ifAnyGranted>
	                    <sec:ifAnyGranted roles="ROLE_ADMIN">
	                        <li><g:link controller="dropDown" action="list"><g:message code="menu.dropdowns" default="Dropdowns"/></g:link></li>
	                    </sec:ifAnyGranted>
	                     <sec:ifAnyGranted roles="ROLE_ADMIN">
							<li><g:link controller="dropDown" action="clientUsage"><g:message code="menu.clientUsage" default="Account Settings"/></g:link></li>
			         	 </sec:ifAnyGranted>
	                    <li>
	                  		<g:link controller="userProfile" action="edit"> Profile</g:link>
	                  	</li>
	                  	<g:if test="${user.password != 'password'}">
	                  		 <li><a href="${grailsApplication.config.grails.serverURL}/register/changePassword"><g:message code="menu.password" default="Password"/></a></li>
	                  	</g:if>
	                     <li>
							<a href="${grailsApplication.config.grails.serverURL}/documentation/universal_help.gsp" rel="#overlay" class="no-text help" id="openHelpHere">Help</a>	                
						 </li>
	           			<li><g:link controller="logout"><g:message code="menu.logout" default="Logout"/></g:link></li>
	               </ul>
            </li>
            </ul>
            </nav>
            <form class="grid_2">

                <div class="yui-skin-sam yui-ac">
                    <input type="text" style="display:none" id="myAC_id" name="myAC_id" hidden="true"/>
                    <input type="text" class="yui-ac-input" id="myAC" name="myAC" value="" placeholder="Search for users, forms and groups"/>
		            <div class="yui-ac-container" id="cgui_04dcd93a3c175ff76e685481b088001d">
                    </div>
                </div>

                <script type="text/javascript">
                    var newFeedCount = $("#newFeedCountContainer").children()[0].innerHTML;
                    var notificationAvail = false
                	$(".openDropDown").hover(
                		  function () {
                		    $(".secondDropDown").show();
                		  }, 
                		  function () {
                			  $(".secondDropDown").hide();
                		  }
                		);
                							
                
                    function init_myAC_ac() {

                        var dataSource = new YAHOO.widget.DS_XHR('${grailsApplication.config.grails.serverURL}/search/search', ['result','name','id']);
                        dataSource.scriptQueryAppend = "";
                        dataSource.connMethodPost = true;

                        GRAILSUI.myAC = new YAHOO.widget.AutoComplete('myAC', 'cgui_04dcd93a3c175ff76e685481b088001d', dataSource);
                        GRAILSUI.myAC.dataSource = dataSource;
                        GRAILSUI.myAC.prehighlightClassName = 'yui-ac-prehighlight';
                        GRAILSUI.myAC.minQueryLength = 0;
                        GRAILSUI.myAC.typeAhead = null;
                        GRAILSUI.myAC.typeAheadDelay = null;
                        GRAILSUI.myAC.textboxFocusEvent.subscribe(function() {
                            var sInputValue = YAHOO.util.Dom.get('myAC').value;
                            if (sInputValue.length === 0) {
                                var oSelf = this;
                                setTimeout(function() {
                                    oSelf.sendQuery(sInputValue);
                                }, 0);
                            }
                        });
                        // here, we are populating a hidden input with the selected id so it will be sent with the form
                        var itemSelectHandler = function(sType, aArgs) {
                            var oMyAcInstance = aArgs[0];
                            var elListItem = aArgs[1];
                            var aData = aArgs[2];
                            var id = aData[1];
                            document.getElementById('myAC_id').value = id;
                            var s = new String(aArgs[2])
                            if (s.indexOf("[User]") != -1)
                                document.location.href = '${grailsApplication.config.grails.serverURL}/user/edit/' + id;
                    	    else if (s.indexOf("[Form]") != -1)
                                document.location.href = '${grailsApplication.config.grails.serverURL}/formViewer/list?formId=' + id;
                    	    else if (s.indexOf("[Group]") != -1)
                                document.location.href = '${grailsApplication.config.grails.serverURL}/groups/edit/' + id;
                            else if(s.indexOf("[Feed]") != -1)
                            	document.location.href = '${grailsApplication.config.grails.serverURL}/activityFeed/edit/' + id;
                            else if(s.indexOf("[Comment]") != -1)
                            	document.location.href = '${grailsApplication.config.grails.serverURL}/activityFeed/edit/' + id;
                            else if(s.indexOf("[Task]") != -1)
                            	document.location.href = '${grailsApplication.config.grails.serverURL}/activityFeed/edit/' + id;
                        };
                        GRAILSUI.myAC.itemSelectEvent.subscribe(itemSelectHandler);
                        GRAILSUI.myAC.minQueryLength = 2;
                        GRAILSUI.myAC.queryDelay = 0.5;
                        GRAILSUI.myAC.type = 'text';
                        GRAILSUI.myAC.placeholder = 'Search...';
                        GRAILSUI.myAC.useShadow = false;
                        GRAILSUI.myAC.queryMatchContains = false;


                    }
                    YAHOO.util.Event.onDOMReady(init_myAC_ac);

                    if(newFeedCount > 0){
                    	$("#newFeedCountContainer").show()
                        }

                    function updateAndGetData(){
                    	 $("#newFeedContainer").html('')
	                    	$.ajax({ 
	                    		url: "${grailsApplication.config.grails.serverURL}/activityFeed/updateAndGetData",
	                            type: "GET",
	                            dataType: "json",
	                            success: function(data) {
		                            var $newFeedContainer = $("#newFeedContainer")
	                            	$("#feedloaderImg").hide()
	                            	$("#newFeedCountContainer").hide()
	                            	$("#newFeedCountContainer").children()[0].innerHTML = "0"
	                            		$newFeedContainer.html('')
	                                 var maxNotification = data.length
			            	        if(maxNotification > 5)
			            	        	maxNotification = 5
	                                for(var i=0; i<maxNotification; i++){
	                                    var feedString = "<div class='nfacDiv'><div onclick='openFeedDetails("+data[i].activityFeedId+")' class='nfacfDiv'>"+data[i].message+"</div><div class='nfactDiv'>"+data[i].dateCreated+"</div></div>"
	                                    $newFeedContainer.append(feedString)
	                                }
	                            	if($newFeedContainer.children().size()==0){
	                            		var feedString = "<div class='nfacDiv'><div class='nfacfDiv'>No notifications</div><div class='nfactDiv' style='height:0'>&nbsp;</div></div>"
	                            			$newFeedContainer.append(feedString)
		                            }
	                            	$("#groupNotiBallon").show()
	                            },error: function(){
	                				window.alert('Please re-login to view this section ');
	                			}
	                	  });
                    }

                    function openFeedDetails(feedId){
                    	var homeUrl = "${grailsApplication.config.grails.serverURL}/activityFeed/edit/"+feedId
                    	window.location = homeUrl
                       }
                </script>

            </form>
        </sec:ifLoggedIn>

    </div>
     <ul style="position:absolute;top:8px;right:25px;list-style: none;">
            	<sec:ifAnyGranted roles="ROLE_TRIAL_USER">
         	 	<sec:ifNotGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_HR_MANAGER">
         	 		<li class="action" style="float:right;">
						<g:link controller="dropDown" action="clientUsage" class="button button-red" onclick="loadScreenBlock()" style="color:#fff !important;"><g:message code="menu.clientUsage" default="Upgrade"/></g:link>
		            </li>
         	 	</sec:ifNotGranted>
         	 </sec:ifAnyGranted>
         	 <sec:ifAnyGranted roles="ROLE_ADMIN">
         	 	<sec:ifNotGranted roles="ROLE_SUPER_ADMIN">
         	 	<% if(!client.plan){ %>
         	 		<li class="action" style="float:right;">
						<g:link controller="dropDown" action="clientUsage" class="button button-red" onclick="loadScreenBlock()" style="color:#fff !important;"><g:message code="menu.clientUsage" default="Upgrade"/></g:link>
		            </li>
				<%}%>
				</sec:ifNotGranted>
         	 </sec:ifAnyGranted>
            </ul>
</header>
<style>
	header.noOpacity{
		opacity:1 !important;
		height:1px;
		margin:0;
	}
</style>
<header class="noOpacity">
	<div class="container_8 clearfix" style="height:1px;">
		<h1 class='grid_1' style="height:1px;">&nbsp;</h1>
		<nav class="grid_5">
			<ul class="clearfix">
				<li class="action" style="border-style:none;margin-top:-5px;top:9px;">
					<div class="popupballoon top" style="margin:1px 0 1em;background-color:white;top:42px;" id="groupNotiBallon">
						<div class="nfacDiv">
							<span class="nfachDiv" style="width:50px">Notification</span><a href="javascript:;" style="float:right;line-height:10px;color:#000 !important" class="close">x</a>
						</div>
						<div id="newFeedContainer">
							<img src="/form-builder/images/ajax-loader.gif" id="feedloaderImg" style="margin: auto; position: relative; top: 5px; bottom: 0pt; left: 75px; right: 0pt;">
						</div>     
					</div>
				</li>
			</ul>
		</nav>
	</div>
</header>