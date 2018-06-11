<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events"  %>
<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<script type="text/javascript">
$(document).ready(function(){
	$('.attachmentImg').load(setImagesWidth);
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
		this.oncontextmenu = function(){
			return false;
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
	
	%>

	<g:if test="${SpringSecurityUtils.ifAnyGranted(Role.ROLE_ADMIN)}">
		<g:set var="isAdmin" value="${true }" />
	</g:if>
	<g:else>
		<g:set var="isAdmin" value="${false }" />
	</g:else>
	
				<g:set var="u" value="${activityFeed?.createdBy}" />
				<g:set var="currentUserProfileInstance" value="${com.oneapp.cloud.core.UserProfile.findByUser(u)}" />
					<li class="clearfix" <% if(user?.lastActivity < activityFeed?.dateCreated && user?.id != activityFeed?.createdBy?.id){%>style="background:#ffffcc;"<%} %> id="activityFeed${activityFeed?.id}">
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
							   pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
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
										<a href="${createLink(controller:'activityFeed',action:'index', params:[filterTag:tag])}" style="float:right;padding-right:5px;">
											<input type="button" class="button1 small green" style="font-size:10px;" value="${tag.size()<20?tag:tag.substring(0,19)+'..'}" onclick="loadScreenBlock()"/>
										</a>
								  	</g:each>
								 </span>
								 <p>
									<a href="#" title="Shared with  ${sharedDetails}" style="color:#0066FF;">
										<b>${((activityFeed.createdBy?.shortName)?:(activityFeed.createdBy.firstName+' '+(activityFeed.createdBy.lastName?:"")))} :</b>
									</a>
									<br/>
									<div>&nbsp;</div>
									<div style="margin-left: 10px; " class="message warning">
		                         		<h3>Feed unavilable as form for this feed was deleted.</h3>
		                      		</div>
									<g:form name="deleteFeed${activityFeed.id}" url="[ controller: 'activityFeed', action:'delete' ]" style="position:absolute;right:10px;top:25px;">
										  <input type="button" onclick="deleteFeedForm(${activityFeed.id})" class="gray-button deleteFeedButton" value="x" />
										  <input type="hidden" name="id" value="${activityFeed.id}" />
									</g:form>
									
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
								
									<g:form name="deleteFeed${activityFeed.id}" url="[ controller: 'activityFeed', action:'delete' ]" style="position:absolute;right:10px;top:25px; display:none;">
										  <input type="button" onclick="deleteFeedForm${activityFeed.id}()" class="gray-button" value="x" />
										  <input type="hidden" name="id" value="${activityFeed.id}" />
									</g:form>
									<div class="timestamp deleteFeedButton" style="display:inline; position:absolute;right:5px;top:25px;">																																										
										<ul class="dropdown">
											<li class="" onmouseover="showHideSetting()" >
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
													<li>
														<a onclick="loadScreenBlock();" id="feedDetails${activityFeed.id}" href="${request.getContextPath()}/activityFeed/edit/${activityFeed.id}">View Details</a>
													</li>
												</ul>
											</li>
										</ul>
									</div>
								
								<div style="padding-bottom:3px;" id="feedOwner${activityFeed.id}">
									<a href="#" title="Shared with  ${sharedDetails}" style="color:#0066FF;">
										<b> ${activityFeed.createdBy?.shortName?:(activityFeed.createdBy.firstName+" "+(activityFeed.createdBy.lastName?:""))} </b>
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
											<pre style="overflow-x:auto;overflow-y: hidden;line-height:16px;font-family: Helvetica;white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;word-wrap: break-word;padding-bottom:3px;">${activityFeed?.activityContent?.substring(0,max.toInteger())}</pre>
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
										<attachments:icon attachment="${attachment}" feed="${activityFeed.id }"/>
									</g:if>
									<g:else>
								    	<br/>
										<attachments:icon attachment="${attachment}" feed="${activityFeed.id }"/>
										<attachments:downloadLink attachment="${attachment}" feed="${activityFeed.id }"/>
										${attachment.niceLength}
									</g:else>
									<g:if test="${isAdmin || ( (!isAdmin) && ( user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) || attachment.poster.id == user.id ) ) }">
										<attachments:deleteLink attachment="${attachment}" label="${'[Delete]'}" feed="${activityFeed.id }" returnPageURI="${createLink(action: 'index')}"/>
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
											<a href="javascript:;" id="commentLinkCount${activityFeed.id}" onclick="showHideComments(${activityFeed.id})" style="font-weight:normal;font-size:11px;color:#0066FF;vertical-align:text-top;"> <img src="${resource(dir:'images',file:'comment_icon.png')}" style="vertical-align: top; padding-right: 3px;"/>comments (${activityFeed.comments.size()})</a>
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
								<tbody>
									<g:if test="${!nocomment }">
										<g:each in="${activityFeed?.comments.sort{ it.dateCreated }}" status="k" var="commentsInstance">
											<tr>
												<td>
													<g:if test="${activityFeed?.comments.size()-2 > k }">
														<feedComment:renderComment commentsInstance="${commentsInstance }" activityFeed="${activityFeed }" displayComment="false"></feedComment:renderComment>
													</g:if>
													<g:else>
														<feedComment:renderComment commentsInstance="${commentsInstance }" activityFeed="${activityFeed }" displayComment="true"></feedComment:renderComment>
													</g:else>
												</td>
											</tr>
										</g:each>
									</g:if>
								</tbody>
							</table>
							<div class="ActivityCommentP" id="C2${activityFeed.id}" >
								<g:formRemote name="addcomment${activityFeed.id}" update="newComment" before="addCommentTrTd('${activityFeed.id}')"
									url="[ controller: 'activityFeed', action:'addComment' ]" onSuccess="addCommentSuccess('${activityFeed.id}')" onFailure="addErrorMessage('${activityFeed.id}')">
										<g:textArea id="commentText${activityFeed.id}"
										name="commentText${activityFeed.id}" style="height: 20px; width: 88%;margin-right:30px;resize: vertical;" placeholder="Write comment...."></g:textArea>
										<input type="hidden" name="id" value="${activityFeed.id}" />
										<a href="javascript:;" class="commentAttachText" id="attachCommentFile${activityFeed.id}" style="font-weight: normal;" onclick="attachmentCommentFile('${activityFeed.id}')">Attach File</a>
										<g:submitButton id="addCommentBtn${activityFeed.id}" name="Comment" class="button1 small blue" style="display:none;"/>
								</g:formRemote>
								<div class="commentFileUpload" id="commentFileUpload${activityFeed.id}">
									<g:form class="attUploadForm noLoader" name="uploadForm" url="[ controller: 'activityFeed', action: 'uploadFileAttach']" enctype="multipart/form-data">
										<g:hiddenField name="id" id="attchActivityId" value="${activityFeed.id}"></g:hiddenField>
										<input type="file" name="file" id="attachFileToFeed" style="width: 220px;">
										<br><a href="javascript:;" onclick="attachMoreFile(this);" id="attachMoreFile${activityFeed.id}" style="font-weight: normal;">Attach more file</a>
										<input type="submit" id="Upload" value="Upload" class="button button-gray" name="Upload" style="padding:1px 10px;">
									</g:form>
								</div>
							</div>
					</div>
				</li>	
		
			<script>
						
						var commentCount${activityFeed.id} = ${activityFeed.comments.size()};
						
						
						
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
							
							
								
							
							
						</script>

									
