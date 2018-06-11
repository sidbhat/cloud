
<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>
<%@ page import="com.oneapp.cloud.core.User" %>
<script type="text/javascript">
		function showHidelayer(san){
			if ( document.getElementById(san).style.display == "none" || document.getElementById(san).style.display == "" )
				document.getElementById(san).style.display="block";
			else if ( document.getElementById(san).style.display == "block")
			document.getElementById(san).style.display="none";
		
		}

		function approveRejectFeed(feedState,responseId,formId,activityId,activityType){
			$.ajax({
				  url: '${grailsApplication.config.grails.serverURL}/dashboard/changeStatus?responseId='+responseId+'&formId='+formId+'&status='+feedState+'&activityId='+activityId+'&activityType='+activityType,
				  success: function( data ) {
					  window.location.reload();
				    }
				  
				});
			}
		$(document).ready(function(){
$("table.mytableclass").paginate({rows: 10, buttonClass: 'button1 small white'});
});

	</script>
	
<div class="Heading">
	
			<% 
																		def shareType=""
																		if ( activityFeed.visibility == ActivityFeed.COMPANY ) 
																			shareType = "company"
																		else if ( activityFeed.visibility == ActivityFeed.GROUP ) 
																			shareType = "group"
																		else if ( activityFeed.visibility == ActivityFeed.USER ) 
																			shareType = "user" %>
																	
														<% if ( shareType == "company" ) {%>			
															<img src='${resource(dir:"images",file:"company.png")}' alt="" />
														<%}else  if ( shareType == "group" )  {%>
															<img src='${resource(dir:"images",file:"group.png")}' alt="" />
														<%}else{%>
															<img src='${resource(dir:"images",file:"user.png")}' alt="" />
														<%}%>
	
	<h2> Message
	<%if ( activityFeed?.createdBy.username == session.user.username ) {%>
									<div class="PostShare">
										<ul>
											<li><a href="#"
												class="ShareMainBtn">Tag<span class="arrow-down"></span>
											</a>
												<ul >
														<ul>
														<g:each in="['urgent','event','info','question']" var="t">
															<li class="Nobdr"><g:link
															controller="companyActivity" action="addTag"
															id="${activityFeed.id}"
															params='[tag:"${t}"]'
															>${t}</g:link></li>
														</g:each>
														</ul>
												</ul>
											</li>
										</ul>
									</div>
	<%}%>
	<div class="PostShare">
	<%def ratingList =['*','**','***','****'] %>
									<ul>
											<li>
												<ul >
														<ul>
														<g:each in="${ratingList}" var="t">
															<li class="Nobdr"><g:link
															controller="companyActivity" action="addRating"
															id="${activityFeed.id}"
															params='[rating:"${t}"]'
															>${t}</g:link></li>
														</g:each>
														</ul>
												</ul>
											</li>
										</ul>
		</div>
	</h2>
	<span class="close">&nbsp;</span>
	
	
</div>



<div class="ActivityCommentMainP">
<div class="ActivityCommentP">
	 	<% 
	    	User u = User.findByUsername(activityFeed?.createdBy.username)
			def sessionUserProfile = UserProfile.findByUser(u)
			def pictureURL=""
			if(sessionUserProfile?.attachments){
			   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
			} else if(u?.pictureURL && u.pictureURL?.length() > 0){
			   pictureURL = u.pictureURL
			}
	    	if (pictureURL) {%>
           		<img src="${pictureURL}" width="32" height="32"/>
        <%}else{%>
         	    <img src="${resource(dir:'images',file:'user_32.png')}"/>
         <%}%>
			
			
				<g:form name="deleteFeed"
					  url="[ controller: 'activityFeed', action:'delete' ]"
					  update="msg${i}" style="position:absolute;right:10px;">
				<g:submitButton action="delete" class="gray-button deleteFeedButton" name="x"/>
				<input type="hidden" name="id" value="${companyActivity.id}" />
				</g:form>
			
		<p><b> ${activityFeed.createdBy.username}</b> : <g:if test="${activityFeed?.config?.shareType == 'Approval' || activityFeed?.config?.shareType == 'Poll'}">
																Click here to view ${activityFeed?.config?.shareType.toLowerCase()}<a href="#" onclick='openViewFrame(${activityFeed?.shareId },${activityFeed?.activityContent.decodeHTML()})'>Here</a><br/>											
															</g:if>
															<g:else>
																${activityFeed?.activityContent.decodeHTML()}<br/>
															</g:else>

</p>
 <p>
      
      	<p>
															<span><prettytime:display
																	date="${activityFeed.dateCreated}" /> </span> 
																	<span><a href="#">comments (${activityFeed.comments.size()})</a> </span><span>

      <br/>
      <p>
      <g:each in="${activityFeed.tags.sort()}">
      	<g:if test="${it == 'approval'}">
      		<sec:ifAnyGranted roles="ROLE_HR_MANAGER">
				<input type="button" class="button1 small green" value="Approve" id="approved" onclick="approveRejectFeed('approved',${companyActivity.activityContent.decodeHTML()},${companyActivity.shareLink },${companyActivity.id},'${companyActivity.shareType.toLowerCase()}')"/>&nbsp;<input type="button" class="button1 small red" value="Reject" id="rejected" onclick="approveRejectFeed('rejected',${companyActivity.activityContent.decodeHTML()},${companyActivity.shareLink },${companyActivity.id},'${companyActivity.shareType.toLowerCase()}')"/>
			</sec:ifAnyGranted>       	
        </g:if>
        <g:else>
        	<input type="button" class="button1 small green" value="${it}"/>
        </g:else>
      </g:each>
          <% 
        	if ( activityFeed?.averageRating != null) {
        %>
    	<input type="button" class="button1 small purple" value="${ratingList[(int)activityFeed?.averageRating]} (${activityFeed?.totalRatings})"/>	
		<%
			}
		%>
      </p>
	  </span>
	  </b>
		</p>
														
	 													
	</div>
	
	
<% 
	int count=0, offset=10 
	boolean added=false
%>
						<div class="ActivityCommentP">
						 <div id="message" class="button1 small white"></div>
						 </div>
						 
						<div class="ActivityCommentP" id="C2${activityFeed.id}">
															<g:formRemote name="addcomment${activityFeed.id}" update="message"
																url="[ controller: 'companyActivity', action:'addcomment' ]">
															<img src="${resource(dir:'images',file:'user_32.png')}"/>	&nbsp;&nbsp;&nbsp;
																<g:textArea id="commentText${activityFeed.id}"
																	name="commentText${activityFeed.id}" style="height: 20px; width: 80%;"
																	></g:textArea>
																	<p align="right">
																	<% if (activityFeed?.config.shareType=="workflow") {%>
																	<g:render template="/dashboard/approveReject"/>
																	<%}else{%>
																	<g:submitButton 
																	name="Reply" class="button1 small blue"/>
																	<%}%>
																	</p>
																<input type="hidden" name="activity_id"
																	value="${activityFeed.id}" />
															</g:formRemote>
														<div id="commentResult${activityFeed.id}"></div>
														</div>	
														
<table class="mytableclass">

<g:each in="${comments}" status="i" var="commentsInstance">
<tr><td>
<div class="ActivityCommentP">
<%  
if ( i < count+offset ) {%>

	    <% 
	    	u = User.findByUsername(commentsInstance.poster.username)
			 sessionUserProfile = UserProfile.findByUser(u)
			 pictureURL=""
			if(sessionUserProfile?.attachments){
			   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
			} else if(u?.pictureURL && u.pictureURL?.length() > 0){
			   pictureURL = u.pictureURL
			}
	    	if ( pictureURL ) {%>
           		<img src="${pictureURL}" width="32" height="32"/>
        <%}else{%>
         	    <img src="${resource(dir:'images',file:'user_32.png')}"/>
         <%}%>
		<p>
			<a href="#">${commentsInstance.poster.username}</a> ${commentsInstance.body}
		</p>
		<p>
			<span><prettytime:display date="${commentsInstance.dateCreated}" />
		<g:formRemote name="deletecomment${i}"
					  url="[ controller: 'companyActivity', action:'deleteComment' ]"
					  update="commentResult${activityFeed.id}">
				<g:submitButton action="deleteComment" class="gray-button" name="x"/>
				<input type="hidden" name="activity_id" value="${activityFeed.id}" />
				<input type="hidden" name="comment_id" value="${commentsInstance.id}" />
		</g:formRemote>
				
		</span>
	</p>
		
<%}else{%>

<%if ( !added ) { %><div id="more" style="display:none" class="ActivityCommentP"> 
	<a href="#" onClick="showHidelayer('more')">More</a><% added=true}%>
	<img src="${resource(dir:'images',file:'UserImage.png')}"/>
		<p>
			<a href="#">${commentsInstance?.poster?.username}</a> ${commentsInstance?.body}
		</p>
		<p>
			<span><prettytime:display date="${commentsInstance.dateCreated}" /></span>
		</p>
	<%if ( i == comments.size()-1) { %> </div><%}%>
<%}%>
	</div>
</tr></td>
</g:each>
</table>
	
</div>
 