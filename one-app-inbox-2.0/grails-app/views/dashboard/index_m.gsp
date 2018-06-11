<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events"  %>
<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<% 				 
   				   def total
            	   if (viewName == "Activity Feed" )
            	   	total = totalCompFeeds
            	   else 
            	    total = totalFeeds
					
					
            	%>
<!DOCTYPE html>
<html>
<head>
<title>${viewName} &nbsp;(${total})</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="mobile">
</head>
<body>
<script type="text/javascript">
function ajaxCall(urL,obj){
	var $node = $(obj.parentNode.parentNode);
	$.ajax({ 
		url: urL,
        type: "GET",
        dataType: "html",
        success: function (data, textStatus, xhr) {
        	$node.html(data);
        }
  });
}
</script>
<%
def user
if (session?.SPRING_SECURITY_CONTEXT?.authentication?.principal ){
	user = com.oneapp.cloud.core.User.findByUsername ( session?.SPRING_SECURITY_CONTEXT?.authentication?.principal?.username )
}
 %>
 
 <g:if test="${SpringSecurityUtils.ifAnyGranted(Role.ROLE_ADMIN)}">
		<g:set var="isAdmin" value="${true }" />
	</g:if>
	<g:else>
		<g:set var="isAdmin" value="${false }" />
	</g:else>
	<div data-role="header" data-position="fixed">
			<a data-icon="home" class="ui-btn-left" href="${createLink(uri: '/home/index')}"><g:message code="default.home.label"/></a>
			<h1>Inbox</h1>
	</div>
	<div class="content-primary">
		<nav>
			<div data-role="content">
				<ul data-role="listview" >
					<li data-icon="false">
						<table width="100%">
								<tr>
									<td style="color:#333333;"><a href="${grailsApplication.config.grails.serverURL}/dashboard/mobileShare" id="postFeedIcon" class="postIcon icon-note">Write Post</a></td>
									<td><img style="margin: auto; position: absolute; top: 0pt; bottom: 0pt; left: 75px; right: 0pt;display:none;" id="loaderImg" src="${request.getContextPath()}/images/ajax-loader.gif"></td>
									<td style="color:#333333;float:right;"><a href="javascript:;" onclick="fetchEmailAction()"  name="fetchEmailBtn"  class="postIcon icon-note">Fetch Email</a>	</td>
								</tr>
						</table>
						
					</li>
					<g:if test="${activityFeedList!=null && activityFeedList.size()==0 && tagFilter == null}">
							<li>
							 	<div style="margin-left: 10px; ">
	                           		<h3><g:message code="company.no.feeds" default="No company feeds available"/></h3>
	                   				<p>
	               			        	<g:message code="company.feeds.help" default="Enter text above to share a feed."/>
	                 				 </p>
	                        	</div> 
	                        </li>
					</g:if>
					<g:elseif test="${activityFeedList!=null && activityFeedList.size()==0 && tagFilter != null}">
							<li>
							 	<div class="message warning" style="margin-left: 10px; ">
	                   				<h3><g:message code="company.no.filter.feeds" args="${[tagFilter]}" default="No feeds available for current filter"/></h3>
		                        </div> 
		                    </li>
					</g:elseif>
					<g:else>
						<g:each in="${activityFeedList}" var="activityFeed" status="i">
							<%
							def sharedDetails=""
							activityFeed.sharedGroups.eachWithIndex(){ shareGroup, sg ->
								if (sharedDetails != "")
									sharedDetails += ", "
								if(sg==0)
									sharedDetails += " Group: "
								sharedDetails += shareGroup.groupName
							}
							activityFeed.sharedUsers.eachWithIndex(){ sharedUsers, su ->
								if (sharedDetails != "")
									sharedDetails += ", "
								if(su==0)
									sharedDetails += " User: "
								sharedDetails += sharedUsers?.shortName?:sharedUsers?.firstName
							}
							activityFeed.sharedDepts.eachWithIndex(){ sharedDepts, sd ->
								if (sharedDetails != "")
									sharedDetails += ", "
								if(sd==0)
									sharedDetails += " Department: "
								sharedDetails += sharedDepts.name
							}
							activityFeed.sharedRoles.eachWithIndex(){ sharedRoles, sr ->
								if (sharedDetails != "")
									sharedDetails += ", "
								if(sr==0)
									sharedDetails += " Role: "
								sharedDetails += sharedRoles.description
							}
							if(sharedDetails == "" && activityFeed.visibility == ActivityFeed.COMPANY){
								sharedDetails = " Company "
							}
							
							%>
							<g:set var="starRatingVal" value="${(int)activityFeed?.averageRating}" />
							<g:ifNotFormExist config="${activityFeed?.config}" shareId="${activityFeed?.shareId}">
								<li data-icon="false" id="feed${activityFeed.id}" style="padding-left:5px;padding-right:0px;background: #fff;border-bottom:1px solid #ddd;border-top-width:0px">
									<table>
										<tr>
											<td style="vertical-align:top;">
												<div class="userImage">
												<g:set var="u" value="${activityFeed?.createdBy}" />
												<g:set var="thisUserProfile" value="${UserProfile.findByUser(u)}" />
						                        <%
												    def pictureURL 
													if(thisUserProfile?.attachments){
													   pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${thisUserProfile.attachments[0].id}"
													} else if(u?.pictureURL && u.pictureURL?.length() > 0){
													   pictureURL = u.pictureURL
													}
													if (pictureURL) {
													%><img src="${pictureURL}"  width="50px;" height="50px;"/>
													<%}else{%><img src="${resource(dir:'images',file:'user_32.png')}" style="margin:10px;"/>
												    <%} 
												%>
												</div>
											 </td>
											 <td style="vertical-align:top;">
												<g:if test="${isAdmin || ( (!isAdmin) && user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) )  }">
													 <form name="deleteFeed${activityFeed.id}" id="deleteFeed${activityFeed.id}" style="position:absolute;right:5px;top:0px;">
														<input type="hidden" name="id" value="${activityFeed.id}" />
													  	 <a data-role="button" data-icon="delete" onclick="deleteFeedAction${activityFeed.id}()" data-iconpos="notext">Delete</a>
												 	 </form>
													  <script>
													  function deleteFeedAction${activityFeed.id}(){
														  var thisForm = $("[name='deleteFeed${activityFeed.id}']")[$("[name='deleteFeed${activityFeed.id}']").length-1];
														  var previousForm = $("[name='deleteFeed${activityFeed.id}']")[$("[name='deleteFeed${activityFeed.id}']").length-2];
														  ($(previousForm)).remove();
														 
														  $.mobile.showPageLoadingMsg();
															 $.post("${request.getContextPath()}/activityFeed/delete", ($(thisForm)).serialize(), function(data){
																 $.mobile.hidePageLoadingMsg();
																  if(data.status == false)
																  {
																	 
																  }
																  else
																  {
																	 removeItemFormList('${activityFeed.id}');
																  }
															 });
													  }
													  
												  	</script>
												</g:if>
											 	<div style="color:#0066FF;">
													${activityFeed?.createdBy?.shortName?:(activityFeed.createdBy.firstName+" "+(activityFeed.createdBy.lastName?:""))}
												</div>
											 	<p class="feedContent">
									     			Feed unavilable as form for this feed was deleted.
												</p>
											 </td>
										 </tr>
									 </table>
								</li>
							</g:ifNotFormExist>
							<g:ifFormExist config="${activityFeed?.config}" shareId="${activityFeed?.shareId}">
								<li data-icon="false" id="feed${activityFeed.id}" style="padding-left:5px;padding-right:0px;background: #fff;border-bottom:1px solid #ddd;border-top-width:0px">
									<table>
										<tr>
											<td style="vertical-align:top;">
												<div class="userImage">
													<g:set var="u" value="${activityFeed?.createdBy}" />
													<g:if test="${u?.pictureURL != null && u?.pictureURL.length() != 0  }">
														<img src="${u?.pictureURL}" width="50px;" height="50px;"/>
													</g:if>
													<g:else>
														<img src="${resource(dir:'images',file:'user_32.png')}" style="margin:10px;"/>
													</g:else>
												</div>
											</td>
											<g:set var="max" value="${512}" />
											<g:if test="${activityFeed.activityContent.size() < max}">
												<g:set var="max" value="${activityFeed.activityContent.size()}" />
											</g:if>
											<td style="vertical-align:top;">
											<g:if test="${isAdmin || ( (!isAdmin) && user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) )  }">
												<form name="deleteFeed${activityFeed.id}" id="deleteFeed${activityFeed.id}" style="position:absolute;right:5px;top:0px;">
													<input type="hidden" name="id" value="${activityFeed.id}" />
												  	 <a data-role="button" data-icon="delete" onclick="deleteFeedAction${activityFeed.id}()" data-iconpos="notext">Delete</a>
											 	 </form>
												  <script>
												  function deleteFeedAction${activityFeed.id}(){
													  var deleteCheck = confirm("Are you sure?");
													  if(deleteCheck)
													  {
														  var thisForm = $("[name='deleteFeed${activityFeed.id}']")[$("[name='deleteFeed${activityFeed.id}']").length-1];
														  var previousForm = $("[name='deleteFeed${activityFeed.id}']")[$("[name='deleteFeed${activityFeed.id}']").length-2];
														  ($(previousForm)).remove();
														  $.mobile.showPageLoadingMsg();
														  $.post("${request.getContextPath()}/activityFeed/delete", ($(thisForm)).serialize(), function(data){
															 $.mobile.hidePageLoadingMsg();
															  if(data.status == false)
															  {
																 
															  }
															  else
															  {
																 removeItemFormList('${activityFeed.id}');
															  }
														 });
													  }else{
													  	return;
													  }
												  }
												  
											  </script>	
											</g:if>		
												<div style="color:#0066FF;">
													${activityFeed?.createdBy?.shortName?:(activityFeed.createdBy.firstName+" "+(activityFeed.createdBy.lastName?:""))}
												</div>
												<g:if test="${activityFeed?.config.shareType != 'Email'}">
														<div class="feedContent" style="width:275px;">
															<pre style="font-family: Helvetica;white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;word-wrap: break-word;">${activityFeed?.activityContent?.substring(0,max)}</pre>
														</div>
												</g:if>
												<g:if test="${activityFeed?.config.configName != 'content' && activityFeed?.config.shareType != 'Email'}">
													<g:render template="/activityFeed/mobileFormDetails" model="['activityFeedId':activityFeed?.id,'className':activityFeed?.config?.className,'shareId':activityFeed?.shareId,'shareType': activityFeed?.config?.shareType]"/>
												</g:if>
												<g:elseif test="${activityFeed?.config.configName != 'content' && activityFeed?.config.shareType == 'Email'}">
													<g:render template="/activityFeed/mobileEmailDetails" model="['activityFeedId':activityFeed?.id,'shareId':activityFeed?.shareId]"/>
												</g:elseif>
											</td>
										</tr>
										<tr>
											<td>
												
												<g:if test="${'Approval'.equalsIgnoreCase(activityFeed.config.shareType)}">
													<form:mobileFormStatusDropDown activityFeedInstance="${activityFeed}"></form:mobileFormStatusDropDown>
												</g:if>
											<td>
												<table>
													<tr>
														<td>
															<a href="${grailsApplication.config.grails.serverURL}/activityFeed/activityFeedDetails/${activityFeed.id}"  id="commentLinkCount${activityFeed.id}" style="font-weight:normal;font-size:11px;color:#0066FF;text-decoration:none;">Comments (${activityFeed.comments.size()})</a>
														</td>
														<td style="padding-left:5px;" class="startRating">
															<g:oneAppStar feedId="${activityFeed.id}" starAvgValue="${starRatingVal}"></g:oneAppStar>
														</td>
														<td style="padding-left:5px;">
															<p style="margin-bottom:0px;font-size:11px;"><prettytime:display date="${activityFeed.lastUpdated}" /></p>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</li>
							</g:ifFormExist>
						</g:each>
					</g:else>
				</ul>
				<script>
					function removeItemFormList(activityFeedId){
							var removeId = "feed"+activityFeedId;
							$("#"+removeId).remove();
							return false;
						}

					function fetchEmailAction(){
						lockButton();
						$.ajax({
							  url: '${request.getContextPath()}/dashboard/fetchEmail',
							  success: function(data) {
								  unLockButton(data)
							  },
						  	  error:function(data){
						  		unLockButton(data)
							  }
							});
						}
					
					function lockButton(){
						$("#loaderImg").show();
						$("[name='fetchEmailBtn']").hide();
					}

					function unLockButton(data){
							alert(data.message);
							$("#loaderImg").hide();
							$("[name='fetchEmailBtn']").show();
						}
				</script>
				<ul data-role="listview">
					<li>
						<div style="position: relative;text-align:center;" class="paginateButtons">
								<g:paginate total="${totalCompFeeds}" controller="dashboard" action="index" defaultPageSize="10" maxsteps="1" next="Next" prev="Previous"/>
						</div>
					</li>
				</ul>
			</div>
		</nav>
	</div>
</body>

</html>
