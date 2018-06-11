
<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>
<%@ page import="com.oneapp.cloud.core.User" %>
<%@ page import="com.oneapp.cloud.core.UserProfile" %>
<%@ page import="org.grails.formbuilder.*" %>

<!DOCTYPE html>
<html>
<head>
<title>${viewName} &nbsp;(${total})</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="mobile">
</head>
<body>
	<div data-role="header" data-position="fixed">
			<a data-icon="arrow-l" class="ui-btn-left" href="${createLink(uri: '/dashboard/index')}"><g:message code="default.label" default="Back"/></a>
			<h1>Inbox</h1>
	</div>
	<div class="content-primary">
		<nav>
			<div data-role="content">
				<ul data-role="listview" style="padding-top:10px;">
					<li data-icon="false" style="padding-left:5px;padding-right:0px;background: #fff;border-bottom:1px solid #ddd;border-top-width:0px">
						<table>
							<tr>
								<td style="vertical-align:top;">
									<div class="userImage">
										<%
											User u = activityFeed?.createdBy
											def thisUserProfile = UserProfile.findByUser(u)
						                    def pictureURL
											if(thisUserProfile?.attachments){
											   pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${thisUserProfile.attachments[0].id}"
											} else if(u?.pictureURL && u.pictureURL?.length() > 0){
											   pictureURL = u.pictureURL
											}
											if (pictureURL) {%>
												   <img src="${pictureURL}" width="50px;"/>
											<%}else{%>
													 <img src="${resource(dir:'images',file:'user_32.png')}" style="margin:10px;"/>
										 <%}%>
									</div>
								</td>
							 <% 
								def max = 512 
								if (activityFeed.activityContent.size() > 512)
									max = 512
								else 
									max = activityFeed.activityContent.size()
							%>
								<td style="vertical-align:top;">
									<div style="color:#0066FF;">
										${activityFeed.createdBy.shortName?:(activityFeed.createdBy.firstName+" "+(activityFeed.createdBy.lastName?:''))}
									</div>
									<div class="feedContent" style="width:275px;">
										<pre style="font-family: Helvetica;white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;word-wrap: break-word;">${activityFeed?.activityContent?.substring(0,max)}</pre>
									</div>
									
									<% 	if (activityFeed?.config.configName != "content" ) { %>
											<g:render template="/activityFeed/mobileFormDetails" model="['activityFeedId':activityFeed?.id,'className':activityFeed?.config?.className,'shareId':activityFeed?.shareId,'shareType': activityFeed?.config?.shareType]"/>
									<%}%>
									
								</td>
							</tr>
						</table>
					</li>
					<li data-role="list-divider" data-icon="false" >
							Comments
							<span class="ui-li-count" id="commentLinkCount${activityFeed.id}">${activityFeed.comments.size()}</span>
					</li>
					<li data-icon="false" style="padding-left:0px;padding-right:0px;background: #fff;border-bottom:none;border-top-width:0px">
						<table  id="commentTable${activityFeed.id}" class="mytableclass${activityFeed.id}" width="100%" >
							<g:each in="${activityFeed?.comments.sort{ it.dateCreated }}" status="k" var="commentsInstance">
								<tr>
									<td>
										<feedComment:mobileRenderComment commentsInstance="${commentsInstance }" activityFeed="${activityFeed }" displayComment="true"></feedComment:mobileRenderComment>
									</td>
								</tr>
							</g:each>
						</table>
						<table width="100%">
							<tr>
								<td>
										<form name="addcomment${activityFeed.id}" method="post">
												<g:textArea id="commentText${activityFeed.id}"
												name="commentText${activityFeed.id}" style="width:95%;resize: vertical;" placeholder="Write comment...."></g:textArea>
												<input type="hidden" name="id" value="${activityFeed.id}" />
												<button class="button button-green fr loginButton" id="addCommentBtn${activityFeed.id}" onclick="javascript:addCommentTrTd${activityFeed.id}();" name="Comment" type="button">Comment</button>
										</form>
									<script>
										var commentCount${activityFeed.id} = ${activityFeed.comments.size()};
										
											
											function showMessageCommentDelete${activityFeed.id}(success,commentId){
												commentCount${activityFeed.id} = commentCount${activityFeed.id} - 1; 
												var newCommentCount = commentCount${activityFeed.id}
												$("#commentLinkCount${activityFeed.id}").html(newCommentCount);
												if(success)
													$('#'+commentId).remove();
											
											}
											function addCommentSuccess${activityFeed.id}(){
												//alert("comment successfully added")
												commentCount${activityFeed.id} = commentCount${activityFeed.id} + 1; 
												var newCommentCount = commentCount${activityFeed.id}
												$("#commentLinkCount${activityFeed.id}").html(newCommentCount);
												$('#commentText${activityFeed.id}').val('');
												$('#newComment').removeAttr('id');
												$('#newCommentTR').removeAttr('id');
											}
											function addCommentTrTd${activityFeed.id}(){
												$('#commentTable${activityFeed.id}').append($('<tr id="newCommentTR"><td id="newComment"></td></tr>'))
												var thisForm = $("[name='addcomment${activityFeed.id}']")[$("[name='addcomment${activityFeed.id}']").length-1];
												var previousForm = $("[name='addcomment${activityFeed.id}']")[$("[name='addcomment${activityFeed.id}']").length-2];
												($(previousForm)).remove();
												$.mobile.showPageLoadingMsg();
					   							$.post("${request.getContextPath()}/activityFeed/addComment", ($(thisForm)).serialize(), function(data){
					   								$.mobile.hidePageLoadingMsg();
					   						
					   								 if(data.status == false)
					   								 {
					   									 alert("Error occoured while saving contact")
					   								 }
					   								 else
					   								 {
					   									$('#newComment').append(data);
					   								 	addCommentSuccess${activityFeed.id}()
					   								 }
					   							});
																				
												
											}
											function addErrorMessage${activityFeed.id}(){
												$('#newCommentTR').remove();
												$('#messageShow').fadeIn('slow');
												$('#messageShow').html('Comment could not be added');
												setTimeout("$('#messageShow').fadeOut('slow',function(){$('#messageShow').hide();$('#messageShow').html('')});",2000);
											}

											function submitDeleteCommentForm(commentId){
												var commentName = "deletecomment"+commentId
												var thisForm = $("[name="+commentName+"]")[$("[name="+commentName+"]").length-1];
												var previousForm = $("[name="+commentName+"]")[$("[name="+commentName+"]").length-2];
												($(previousForm)).remove();
												$.mobile.showPageLoadingMsg();
					   							$.post("${request.getContextPath()}/activityFeed/deleteComment", ($(thisForm)).serialize(), function(data){
					   								$.mobile.hidePageLoadingMsg();
					   								 if(data.status == false)
					   								 {
					   									showMessageCommentDelete${activityFeed.id}(false)
					   								 }
					   								 else
					   								 {
					   									showMessageCommentDelete${activityFeed.id}(true,"commentDiv"+commentId)
					   								 }
					   							});
											}
											
										</script>
								</td>
							</tr>
						
						</table>
					</li>
					
				</ul>
			</div>
		</nav>
		
	</div>
</body>