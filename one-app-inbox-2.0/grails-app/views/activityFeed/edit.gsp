<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events"  %>
<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<%@ page import="grails.converters.JSON" %>

<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'DasMain.css')}" />
 <!-- markitup! -->

<script type="text/javascript" src="${resource(dir:'markitup',file:'jquery.markitup.js')}" />
<!-- markItUp! toolbar settings -->
<script type="text/javascript" src="${resource(dir:'markitup/sets/default',file:'set.js')}" />
<!-- markItUp! skin -->
<link rel="stylesheet" media="screen" href="${resource(dir:'markitup/skins/simple',file:'style.css')}" />
<!--  markItUp! toolbar skin -->
<link rel="stylesheet" media="screen" href="${resource(dir:'markitup/sets/default',file:'style.css')}" />

<script type="text/javascript">
$(document).ready(function(){
	//$('.markItUpTextarea').markItUp(mySettings, { root:"${resource(dir:'markitup/skins/simple')}"});
});


</script>


<style>
.markItUp {
    width: 390px;
}
</style>

<title>Activity Feed</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="mainFeed">
</head>
<body>
<section class="main-section grid_7" style="padding-top:10px;">
    	<div class="main-content grid_4 alpha">
<script type="text/javascript">
$(document).ready(function(){
	$('.attachmentImg').load(setImagesWidth);
	   showOverLayPopup();
	   showShareOptionPopup();
       showHideSetting();
       scrollPaginationControl()
});
var setImagesWidth = function(){
	$('.attachmentImg').each(function (){
		var w = this.clientWidth/90;
		var h = this.clientHeight/100;
		if(w >= h){
			this.style.height=100+"px";
			this.style.width="auto";
			this.style.marginLeft = -(this.clientWidth - 90)/2 +"px";
			this.style.marginTop = "0";
		}else{
			this.style.width=90+"px";
			this.style.height="auto";
			this.style.marginTop = -(this.clientHeight - 100)/2 +"px";
			this.style.marginLeft = "0";
		}
	});  
}
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
	def colourList =['green','gray','red','orange', 'purple', 'blue'] 
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
	<ul class="listing list-view" style="padding:15px 0 10px;"  id="activityFeedEditContainer">
				<g:set var="u" value="${activityFeed?.createdBy}" />
				<g:set var="currentUserProfileInstance" value="${com.oneapp.cloud.core.UserProfile.findByUser(u)}" />
					<li class="clearfix" style="border-top:none;background:#fff;" id="activityFeed${activityFeed?.id}">
						<div class="UpdateFrom">
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
								sharedDetails += sharedUsers.shortName?:sharedUsers.firstName
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
							if(activityFeed.visibility == ActivityFeed.COMPANY){
								if (sharedDetails != "")
									sharedDetails += " &"
								sharedDetails += " Company "
							}
							if(activityFeed?.config?.shareType?.equalsIgnoreCase("Email") && sharedDetails == ""){
								sharedDetails = " me "
							}
							def sessionUserProfile = UserProfile.findByUser(u)
							def pictureURL=""
							if(sessionUserProfile?.attachments){
							   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
							} else if(u?.pictureURL && u.pictureURL?.length() > 0){
							   pictureURL = u.pictureURL
							}
							%>
							<g:if test="${pictureURL}">
								<img src="${pictureURL}" width="50" <g:if test="${currentUserProfileInstance?.currentStatus}" >title="${currentUserProfileInstance?.currentStatus}" </g:if>/>
							</g:if>
							<g:else>
								<img src="${resource(dir:'images',file:'user_32.png')}" class="feedProfileImage"<g:if test="${currentUserProfileInstance?.currentStatus}" >title="${currentUserProfileInstance?.currentStatus}" </g:if>/>
							</g:else>
						</div>
						<div class="ActivityCtnt">
							<g:ifNotFormExist config="${activityFeed?.config}" shareId="${activityFeed?.shareId}">
								<span style="width:700px;position:absolute;right:0px;top:0px;">
									<g:each in="${activityFeed?.tags.sort()}" var="tag" status="tg">
										<a href="${createLink(controller:'activityFeed', action:'index', id:tag)}" style="float:right;padding-right:5px;">
											<input type="button" class="button1 small green" style="font-size:10px;" value="${tag.size()<20?tag:tag.substring(0,19)+'..'}" onclick="loadScreenBlock()"/>
										</a>
								  	</g:each>
								  	<g:if test="${activityFeed?.isTask}">
										<a style="float: right; position: relative; margin-right: 5px; font-size: 17px; margin-top: 2px;" href="javascript:;">${activityFeed?.referenceId}</a>
									</g:if>
								 </span>
								 <p>
									<a href="#" title="Shared with  ${sharedDetails}" style="color:#0066FF;">
										<b>${(activityFeed.createdBy.shortName?:(activityFeed.createdBy.firstName+' '+(activityFeed.createdBy.lastName?:"")))} :</b>
									</a>
									<br/>
									<div>&nbsp;</div>
									<div style="margin-left: 10px; " class="message warning">
		                         		<h3>Feed unavilable as form for this feed was deleted.</h3>
		                      		</div>
									<g:form name="deleteFeed${activityFeed.id}" url="[ controller: 'activityFeed', action:'delete' ]" style="position:absolute;right:10px;top:25px;">
										  <input type="button" onclick="deleteFeedForm${activityFeed.id}()" class="gray-button deleteFeedButton" value="x" />
										  <input type="hidden" name="id" value="${activityFeed.id}" />
									</g:form>
									<script>
										function deleteFeedForm${activityFeed.id}(){
											var deleteFeedCheck = confirm("Are you sure?");
											if(deleteFeedCheck){
												loadScreenBlock()
												document.forms["deleteFeed${activityFeed.id}"].submit();
											}else{
												return;
											}
										}
									</script>
								</p>
							</g:ifNotFormExist>
							<g:ifFormExist config="${activityFeed?.config}" shareId="${activityFeed?.shareId}">
								<span style="width:700px;position:absolute;right:0px;top:0px;">
									<g:each in="${activityFeed?.tags.sort()}" var="tag" status="tg">
										<a href="${createLink(controller:'dashboard', action:'index', params:[filterTag:tag])}" style="float:right;padding-right:5px;">
											<input type="button" class="button1 small green" style="font-size:10px;" value="${tag.size()<20?tag:tag.substring(0,19)+'..'}" onclick="loadScreenBlock()"/>
										</a>
								  </g:each>
								  <g:if test="${activityFeed?.isTask}">
										<a style="float: right; position: relative; margin-right: 5px; font-size: 17px; margin-top: 2px;" href="javascript:;">${activityFeed?.referenceId}</a>
									</g:if>
									<g:if test="${activityFeed?.taskStatus}">
										<a style="float: right; position: relative; margin-right: 5px; font-size: 17px; margin-top: 4px;" href="javascript:;"><img src="${request.getContextPath()}/images/icons/tick.png"/></a>
									</g:if>
								 </span>
								<p>
								<g:if test="${isAdmin || ( (!isAdmin) && user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) )  }">
										<g:form name="deleteFeed${activityFeed.id}" url="[ controller: 'activityFeed', action:'delete' ]" style="position:absolute;right:10px;top:25px; display:none;">
											  <input type="button" onclick="deleteFeedForm${activityFeed.id}()" class="gray-button" value="x" />
											  <input type="hidden" name="id" value="${activityFeed.id}" />
										</g:form>
										
										<span class="timestamp deleteFeedButton" style="position:absolute;right:5px;top:25px;">																																										
											<ul class="dropdown">
												<li class="" onmouseover="showHideSetting()">
													<a href="#" style="padding:1px 2px 0px;">
														<img alt="" src="${request.getContextPath()}/images/gear.png">
													</a>
													<ul style="padding-left: 0px; visibility: hidden;width:80px;" class="sub_menu">
														<g:if test="${isAdmin || ( (!isAdmin) && user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) )  }">
															<li class="">
																<a onclick="deleteFeedForm(${activityFeed.id})" href="javascript:;">Delete Post</a>
															</li>
															<li>
																<a onclick="showShareUnsharePopUp(${activityFeed.id})" href="javascript:;" class="question">Share/Unshare</a>
															</li>
														</g:if>
														<li>
															<a onclick="setFeedId(${activityFeed.id},'${g.formatDate(format:'MM/dd/yyyy',date:activityFeed?.dueDate)}')" href="javascript:;" class="leave">Mark as Task</a>
														</li>
													</ul>
												</li>
											</ul>
										</span>
										
								</g:if>
								<div style="padding-bottom:3px;" id="feedOwner${activityFeed.id}">
									<a href="#" title="Shared with  ${sharedDetails}" style="color:#0066FF;">
										<b> ${activityFeed.createdBy.shortName?:(activityFeed.createdBy.firstName+" "+(activityFeed.createdBy.lastName?:""))} </b>
									</a>
								</div>
								<div id="container${activityFeed.id}">
									<g:set var="max" value="512" />
									<g:if test="${activityFeed.activityContent.size() > 512}">
										<g:set var="max" value="512" />
									</g:if>
									<g:else>
										<g:set var="max" value="${activityFeed.activityContent.size()}" />
									</g:else>
									<g:if test="${activityFeed?.config.shareType != 'Email'}">
										<div>
											<pre style="overflow-x:auto;line-height:16px;overflow-y: hidden;font-family: Helvetica;white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;word-wrap: break-word;padding-bottom:3px;">${activityFeed?.activityContent?.substring(0,max.toInteger())}</pre>
										</div>
									</g:if>
									<g:if test="${activityFeed?.config.configName != 'content' && activityFeed?.config.shareType != 'Email'}">
										<g:render template="/activityFeed/formDetails" model="['activityFeedId':activityFeed?.id,'className':activityFeed?.config?.className,'shareId':activityFeed?.shareId,'shareType': activityFeed?.config?.shareType]"/>
									</g:if>
									<g:elseif test="${activityFeed?.config.configName != 'content' && activityFeed?.config.shareType == 'Email'}">
										<g:render template="/activityFeed/emailDetails" model="['activityFeedId':activityFeed?.id,'shareId':activityFeed?.shareId]"/>
									</g:elseif>
								</div>
								<g:each in="${activityFeed.attachments.sort{if(grailsApplication.config.attachment.image.ext.contains(it.ext?.toLowerCase())){return -1}else{return 0}}}" var="attachment">
									<g:if test="${grailsApplication.config.attachment.image.ext.contains(attachment.ext?.toLowerCase())}">
										<div style="border: 1px solid #CCCCCC;display: inline-block;margin: 2px;padding: 5px;width: 92px;">
										<attachments:icon attachment="${attachment}"/>
									</g:if>
									<g:else>
								    	<br/>
										<attachments:icon attachment="${attachment}"/>
										<attachments:downloadLink attachment="${attachment}"/>
										${attachment.niceLength}
									</g:else>
									<g:if test="${isAdmin || ( (!isAdmin) && ( user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) || attachment.poster.id == user.id ) ) }">
										<attachments:deleteLink attachment="${attachment}" label="${'[Delete]'}" returnPageURI="${createLink(action: 'index')}"/>
									</g:if>
									<g:else>
										&nbsp;
									</g:else>
									<g:if test="${grailsApplication.config.attachment.image.ext.contains(attachment.ext?.toLowerCase())}">
										</div>
									</g:if>
								</g:each>
								<g:set var="starRatingVal" value="${(int)activityFeed?.averageRating}" />
							</p>
							<table>
								<tr>
									<td>
										<g:if test="${'Approval'.equalsIgnoreCase(activityFeed.config.shareType)}">
											<span class= "timestamp">
												<form:formStatusDropDown activityFeedInstance="${activityFeed}"></form:formStatusDropDown>
											</span>
										</g:if>
										<g:if test="${activityFeed.isTask}">
											<span class= "timestamp">
												<form:taskStatusDropDown activityFeedInstance="${activityFeed}"></form:taskStatusDropDown>
											</span>
										</g:if>
									</td>
									<td>
										<span>
											<a href="javascript:;" id="commentLinkCount${activityFeed.id}" style="font-weight:normal;font-size:11px;color:#0066FF;vertical-align:text-top;"> <img src="${resource(dir:'images',file:'comment_icon.png')}" style="vertical-align: top; padding-right: 3px;"/>comments (${activityFeed.comments.size()})</a>
										</span>
									</td>
									<td style="padding-left:10px;">
										<span style="font-weight:normal;font-size:11px;color:#666;vertical-align:top;">
											<g:oneAppStar feedId="${activityFeed.id}" starAvgValue="${starRatingVal}"></g:oneAppStar>
											<span style="font-weight: normal; font-size: 11px; color: rgb(102, 102, 102); vertical-align: top; position: relative; top: 2px;">
												(${activityFeed?.totalRatings} votes)
											</span>
										</span>
										
									</td>
									<td style="padding-left:10px;">
										<span style="font-weight:normal;font-size:11px;color:#666;vertical-align:top;">
											<prettytime:display date="${activityFeed?.lastUpdated?activityFeed?.lastUpdated:activityFeed.dateCreated}" />
										</span>
									</td>
									<g:if test="${activityFeed?.isTask}">
										<td style="padding-left:10px;">
											<span style="font-weight:normal;font-size:11px;color:#666;vertical-align:top;">
												Task Due <prettytime:display date="${activityFeed?.dueDate}" />
											</span>
										</td>
									</g:if>
								</tr>
							</table>
						</g:ifFormExist>
						</div>
						<div id="commentContainer${activityFeed.id}" >
							<br/>
							<table  id="commentTable${activityFeed.id}" class="mytableclass${activityFeed.id}" style="border-spacing:0px;">
								<g:each in="${activityFeed?.comments.sort{ it.dateCreated }}" status="k" var="commentsInstance">
									<tr>
										<td>
											<feedComment:renderComment commentsInstance="${commentsInstance }" activityFeed="${activityFeed }" displayComment="true"></feedComment:renderComment>
										</td>
									</tr>
								</g:each>
							</table>
							<div class="ActivityCommentP" id="C2${activityFeed.id}" >
								<g:formRemote name="addcomment${activityFeed.id}" update="newComment" before="addCommentTrTd('${activityFeed.id}')"
									url="[ controller: 'activityFeed', action:'addComment' ]" onSuccess="addCommentSuccess('${activityFeed.id}')" onFailure="addErrorMessage('${activityFeed.id}')">
										<g:textArea id="commentText${activityFeed.id}"
										name="commentText${activityFeed.id}" style="height: 20px; width: 88%;margin-right:30px;resize: vertical;" placeholder="Write comment...."></g:textArea>
										<input type="hidden" name="id" value="${activityFeed.id}" />
										<a href="javascript:;" class="commentAttachText" id="attachCommentFile${activityFeed.id}" style="font-weight: normal;">Attach File</a>
										<g:submitButton id="addCommentBtn${activityFeed.id}" name="Comment" class="button1 small blue" style="display:none;"/>
								</g:formRemote>
								<div class="commentFileUpload" id="commentFileUpload${activityFeed.id}">
									<g:form class="attUploadForm noLoader" name="uploadForm" url="[ controller: 'activityFeed', action: 'uploadFileAttach']" enctype="multipart/form-data">
										<g:hiddenField name="id" id="attchActivityId" value="${activityFeed.id}"></g:hiddenField>
										<g:hiddenField name="redirectPage" id="redirectPage" value="feedEdit"></g:hiddenField>
										<input type="file" name="file" id="attachFileToFeed" style="width: 220px;">
										<br><a href="javascript:;" onclick="attachMoreFile(this);" id="attachMoreFile${activityFeed.id}" style="font-weight: normal;">Attach more file</a>
										<input type="submit" id="Upload" value="Upload" class="button button-gray" name="Upload" style="padding:1px 10px;">
									</g:form>
								</div>
							</div>
						<script>
						
						var commentCount${activityFeed.id} = ${activityFeed.comments.size()};
						function attachMoreFile(obj){
							$("<input type='file' name='file' id='attachFileToFeed' style='width: 220px;'><br />").insertBefore(obj);
							}
						$("#attachCommentFile${activityFeed.id}").click(function () {
							$("#commentFileUpload${activityFeed.id}").slideToggle('slow');
							$("#attachCommentFile${activityFeed.id}").hide();
						});
						
							$("#commentText${activityFeed.id}").bind('keypress', function(e) {
								if(e.keyCode==13 && !e.ctrlKey){
									$("#commentText${activityFeed.id}").blur();
									var commentText = $("#commentText${activityFeed.id}").val()
									if($.trim(commentText) == ""){
										alert("Please enter a comment")
										$("#commentText${activityFeed.id}").focus();
										return false;
									}else{
										$("#addCommentBtn${activityFeed.id}").trigger('click');
										return false;
									}
								}else if((e.keyCode==13 && e.ctrlKey) || e.keyCode==10){
									$(this).val($(this).val() + "\n");
								}
							});
	
							function submitDeleteCommentForm${activityFeed.id}(commentId){
								var commentName = "deletecomment"+commentId
								var thisForm = $("[name="+commentName+"]")[$("[name="+commentName+"]").length-1];
	   							$.ajax({
		   							  type:"post",
		   							  url: "${request.getContextPath()}/activityFeed/deleteComment",
		   							  data: ($(thisForm)).serialize(),
		   							  success: function(){
		   								showMessageCommentDelete${activityFeed.id}(true,"commentDiv"+commentId)
		   							  },
	   							      failure : function(){
	   							    	showMessageCommentDelete${activityFeed.id}(false)
		   							   }
		   							});
							}
							function showMessageCommentDelete${activityFeed.id}(success,commentId){
								commentCount${activityFeed.id} = commentCount${activityFeed.id} - 1; 
								var newCommentCount = "<img style='vertical-align: bottom; padding-right: 3px;' src='${request.getContextPath()}/images/comment_icon.png'>comments ("+commentCount${activityFeed.id}+")"
								$("#commentLinkCount${activityFeed.id}").html(newCommentCount);
								if(success)
									$('#'+commentId).remove();
							
							}

							function addCommentTrTd(activityFeedId){
								$('#commentTable'+activityFeedId).append($('<tr id="newCommentTR"><td id="newComment"></td></tr>'))
							}
							
							function addCommentSuccess(activityFeedId){
								window['commentCount'+activityFeedId] = window['commentCount'+activityFeedId] + 1; 
								var newCommentCount = "<img style='vertical-align: bottom; padding-right: 3px;' src='${request.getContextPath()}/images/comment_icon.png'>comments ("+window['commentCount'+activityFeedId]+")"
								$("#commentLinkCount"+activityFeedId).html(newCommentCount);
								$('#commentText'+activityFeedId).val('');
								$('#newComment').removeAttr('id');
								$('#newCommentTR').removeAttr('id');
								$("#commentText${activityFeed.id}").focus();
							}	
							
							function addErrorMessage(activityFeedId){
								$('#newCommentTR').remove();
								$('#messageShow').fadeIn('slow');
								$('#messageShow').html('Comment could not be added');
								setTimeout("$('#messageShow').fadeOut('slow',function(){$('#messageShow').hide();$('#messageShow').html('')});",2000);
							}
							
							$("#commentLinkCount${activityFeed.id}").click(function () {
										if ($("[name='commentDiv${activityFeed.id}']").is(':hidden')) 
											$("[name='commentDiv${activityFeed.id}']").show();
										else
											$("[name='commentDiv${activityFeed.id}']").hide();	
									});
						</script>
					</div>
				</li>
				<li style="background: #fff;" id="showHistorybutton">
					<div style="padding: 5px; background: #eee; width: 110px; margin-left: 315px; margin-top: -16px;cursor: pointer;border-radius:0 0 10px 10px;" onclick="showHistoryTab()">
						<div style="text-align:center;" id="showHideHistoryBtn">Show Feed history</div>
					</div>
				</li>
				<li style="display:none;background: #fff;border-top:none;padding-top:0px;overflow-y:hidden;height:450px;" id="historytab">
					<table style="width:640px;">
						<% 
							def counter = 0
							com.oneapp.cloud.core.log.Tracker.filterByControllerAndId("activityFeed", "${activityFeed?.id}", "${session.user.userTenantId}")?.list().reverse().each {
							def historyMessage = ""
								if(it.action == "edit"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Viewed feed."
								}
								else if(it.action == "deleteComment"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Deleted a comment from feed."
								}else if(it.action == "addComment"){
									def commentMessage = ""
									try{
										def paramsList = JSON.parse(it.params)
										boolean found = false
										paramsList?.each{k,v->
											if(!found && k.indexOf("commentText")==0){
												commentMessage = v
												found = true
											}
										}
										if(commentMessage.length() > 15){
											commentMessage = commentMessage.substring(0,15)+"..."
										}
									}catch(Exception e){}
									commentMessage = commentMessage?("\""+commentMessage+"...\""):""
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Commented ${commentMessage} on feed."
								}else if(it.action == "addRating"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Rated feed."
								}else if(it.action == "feedShareUnShare"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Re-shared feed."
								}else if(it.action == "uploadFileAttach"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Uploaded a file."
								}else if(it.action == "download"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Downloaded a file."
								}else if(it.action == "delete"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Deleted a file."
								}else if(it.action == "updateFeedAsTask"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Added a task."
								}else if(it.action == "updateFeedAsTask"){
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Added a task."
								}else if(it.action == "changeStatus"){
									def newStatus = ""
									try{
										def paramsList = JSON.parse(it.params)
										boolean found = false
										paramsList?.each{k,v->
											if(!found && k.indexOf("status")==0){
												newStatus = v
												found = true
											}
										}
									}catch(Exception e){}
									historyMessage	= "<b>"+(it.user.shortName?:(it.user.firstName+' '+(it.user.lastName?:'')))+"</b> Updated feed status to ${newStatus}."
								}
							    %>
								<tr><td>
									<div class="ActivityCommentP" style="top:0px;">
									<%
									   sessionUserProfile = UserProfile.findByUser(it.user)
										pictureURL=""
										if(sessionUserProfile?.attachments){
										   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
										} else if(it.user?.pictureURL && it.user.pictureURL?.length() > 0){
										   pictureURL = it.user.pictureURL
										}
									
									%>
										<g:if test="${pictureURL }">
											<img src="${pictureURL}" width="32" height="32"/>
										</g:if>
										<g:else>
											<img src="${resource(dir:'images',file:'user_32.png')}"/>
										</g:else>
										<div style="width: 85%;color:#000;font-size:11px;padding-left:40px;margin-top:5px;line-height:10px;">
											${historyMessage}
										</div>
										<p style="padding:5px 5px 0 0;"><span>${prettytime.display(date:it.dateCreated)}</span></p>
									</div>
								</td></tr>
							<%
							counter++;	
							
						}
						%>
					</table>
				</li>
		</ul>
		<div id="question-modal-content">
				<div id="osx-modal-title" class="questionTitle">Share/Unshare Feed</div>
					<div class="close"><a href="javascript:;" class="simplemodal-close">x</a></div>
					<div id="osx-modal-data">
						<g:form name="shareUnshareForm" url="[ controller: 'activityFeed', action: 'feedShareUnShare']" >
							<label for="shareUnshareWit"style="margin-left:5px;color:#0066FF;font-weight:bold;">Shared With :</label>
							<input type="text" id="demo-input-populate" name="toMessage" style="box-shadow:none;width: 350px;"/>
							<input type="hidden" name="id" id="activityFeedId" value="">
							<input type="hidden" name="shareUnshareWith" id="shareUnshareWith" value=""><br/>
							<div id="feedContent" style="margin-left:5px;overflow:auto;max-width:570px;max-height:450px;"></div>
							<div style="margin-left:5px;color:#0066FF;font-weight:bold;"><span style="color:#0066FF;">Shared By :</span><span id="feedSharedBy"></span></div>
							<br/>
							<p><button class="button button-blue simplemodal-close" id="questionButton" onclick="submitShareUnshareForm()">Save Changes</button></p>
						</g:form>
					</div>
				</div>		
			</div>	
			<div id="approval-modal-content">
				<div id="osx-modal-title" class="approvalTitle">Create Task</div>
				<div class="close"><a href="#" class="simplemodal-close">x</a></div>
				<div id="osx-modal-data">
					<label for="question">&nbsp;</label>
					<table style="margin:2px 55px;">
						<tr>
							<td colspan="3">
								<span>This feed will be tagged as task with following details</span>
							</td>
						</tr>
						<tr>
							<td>
								<span>Due Date</span>
							</td>
							<td>
								:&nbsp; &nbsp;
							</td>
							<td>
								<input type="text" id="option1" name="option1" onclick="$(this).dateinput({format:'mm/dd/yyyy',trigger:false}).trigger('focus')" placeholder="mm/dd/yyyy"/>
							</td>
						</tr>
						<tr>
							<td>
								<span>Reference Id</span>
							</td>
							<td>
							:&nbsp; &nbsp;
							</td>
							<td>
								<input type="text" name="referenceId" id="referenceId" placeholder="" readonly="readonly"/>
							</td>
						</tr>
					</table>
					<p><button class="button button-blue simplemodal-close" onclick="updateFeedAsTask()" id="approvalButton">Create</button></p>
				</div>
			</div>
			<script>
			var shareUnshareList;
			function showShareUnsharePopUp(feedId){
				shareUnshareList = new Array()	
				$("#activityFeedId").val(feedId);
				$("#feedSharedBy").html($("#feedOwner"+feedId).html());
				 $.ajax({
                             type : "POST",
                             url : '${request.getContextPath()}/activityFeed/feedShareDetails?activityFeedId='+feedId,
                             success: function(data) {
                             	 $("#demo-input-populate").tokenInput( data[0],
						      { theme:"facebook",
							    propertyToSearch: "name",
						 	    resultsFormatter: function(item){ 
							 	    	return "<li><div style='display: inline-block; padding-left: 10px;'><b>" + item.type + " : </b><span class='full_name'>" + item.name + "</span></div></li>" 
						   				},
                               	 onAdd: function (item) {
                               		 	shareUnshareList[shareUnshareList.length] = {"visibility":item.type,'sharedWith':item.id}
                               		 },
                               	 onDelete: function (item) {
                                	 var deleteIndex;
                               		 	for(var i=0;i<shareUnshareList.length;i++){
                                		 		var currentObj = shareUnshareList[i]
                                		 		if(currentObj.visibility == item.type && currentObj.sharedWith == item.id)
                                		 			deleteIndex = i
                                		 	}
                               		 	shareUnshareList.splice(deleteIndex,1);
                               		 },
                               		 prePopulate:data[1],
                               	preventDuplicates: true
							});
							for(var j=0;j<data[1].length; j++){
								shareUnshareList[shareUnshareList.length] = {"visibility":data[1][j].type,'sharedWith':data[1][j].id}
							}
                             },
                             error : function() {
                                     alert("Sorry, The requested property could not be found.");
                             }
                     	});
				}
				
				function submitShareUnshareForm(){
							$("#shareUnshareWith").val(JSON.stringify(shareUnshareList));
							if($("#shareUnshareWith").val()=="[]"||shareUnshareList.length<=0){
								$(".success p").html("Please specify at least one to share")
								showMessage();
								return;
							}else{
								loadScreenBlock();
								document.shareUnshareForm.submit();
							}
					}

				function showHistoryTab(){
					if ($("#historytab").is(':hidden')) {
						$("#historytab").show();
						$("#showHideHistoryBtn").html("Hide Feed history")
					}else{
						$("#historytab").hide();	
						$("#showHideHistoryBtn").html("Show Feed history")
					}
				} 

				function setFeedId(activityFeedId,dueDate){
					operationFeedId = activityFeedId
					$("#option1").val(dueDate)
					$("#referenceId").val("#"+activityFeedId)
				}

				function updateFeedAsTask(){
					shareUnshareList = new Array()	
					var dueDate = $("[name='option1']").val();
					var referenceId = $("#referenceId").val()
					 $.ajax({
                             type : "POST",
                             url : '${request.getContextPath()}/activityFeed/updateFeedAsTask',
                             data: {referenceId:referenceId,dueDate:dueDate,activityFeedId:operationFeedId},
                             success: function(data) {
                            	 if(data != "Error"){
                                	$("#activityFeed"+operationFeedId).html(data)
                                	$("#activityFeed"+operationFeedId).css('border-top','none')
                                	$("#activityFeed"+operationFeedId).css('background','#fff')
                                	$("#feedDetails"+operationFeedId).remove()
                                	$(".ActivityCommentP").show()
                                }
                            	showShareOptionPopup();
                     	        showHideSetting();
                     	        showOverLayPopup();
                             },
                             error : function() {
                                     alert("Sorry, The requested property could not be found.");
                             }
                     	});
				}

				function updateFeedStatus(feedType,status,feedId){
					 $.ajax({
                            type : "POST",
                            url : '${request.getContextPath()}/activityFeed/changeStatus',
                            data: {status:status,id:feedId,feedType:feedType},
                            success: function(data) {
                           	 if(data != "Error"){
                               	  $("#activityFeed"+feedId).html(data)
                               	  $("#activityFeed"+feedId).css('border-top','none')
                               	  $("#activityFeed"+feedId).css('background','#fff')
                               	  $("#feedDetails"+feedId).remove()
                               	  $(".ActivityCommentP").show()
                               }
                           	 showShareOptionPopup();
                    	        showHideSetting();
                    	        showOverLayPopup();
                            },
                            error : function() {
                                    alert("Sorry, The requested property could not be found.");
                            }
                    	});
				}
				
				$("#historytab").hover(
				  function () {
				    $(this).css('overflow-y','auto')
				  }, 
				  function () {
				    $(this).css('overflow-y','hidden')
				  }
				);
				
				function attachmentCommentFile(activityFeedId){
					$("#commentFileUpload"+activityFeedId).slideToggle('slow');
					$("#attachCommentFile"+activityFeedId).hide();
				}

				function deleteFeedForm(activityFeedId){
					var deleteFeedCheck = confirm("Are you sure?");
					if(deleteFeedCheck){
						var feedFormName = "deleteFeed"+activityFeedId
						var thisForm = $("[name="+feedFormName+"]")[$("[name="+feedFormName+"]").length-1];
						$.ajax({
							  type:"post",
							  url: "${request.getContextPath()}/activityFeed/deleteFeed",
							  data: ($(thisForm)).serialize(),
							  success: function(){
								$(".success p").html("Feed sucessfully deleted")
								showMessage();
								setTimeout("loc()",1000)
							  },
						      failure : function(){
						    	alert("Error occured")
							   }
							});
					}else{
						return;
					}
				}
				function loc(){
					window.location="${grailsApplication.config.serverURL}/form-builder/dashboard/index"
				}
				</script>
		</div>
	</div>	 		
	</section>
	
</body>
</html>


									
