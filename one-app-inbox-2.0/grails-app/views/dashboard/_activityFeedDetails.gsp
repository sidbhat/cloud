<div class="Heading">
	<g:if test="${activityFeed.source=='facebook'}">
		<img src="${resource(dir:'images',file:'facbookIcon.png')}"
			alt="facebook" />
	</g:if>
	<g:if test="${activityFeed.source=='linkedin'}">
		<img src="${resource(dir:'images',file:'LinkdinIcon.png')}"
			alt="LinkedIn" />
	</g:if>
	<g:if test="${activityFeed.source=='twitter'}">
		<img src="${resource(dir:'images',file:'TwitterIcon.png')}"
			alt="Twitter" />
	</g:if>
	<h2>Activity from ${activityFeed.source}</h2>
	<span class="close">&nbsp;</span>
</div>

<div class="ActivityCommentMainP">
	<div class="ActivityCommentP">
	     	    <img src="${resource(dir:'images',file:'user_32.png')}"/>
    
	<p><b> ${activityFeed.fromUser} :</b> ${activityFeed.feedContent} .</p>
		<p>
			<span><prettytime:display date="${activityFeed.feedDate}" /></span></p><p>
			
			<% if ( activityFeed.source == "facebook") {%>
			<span><a href="#">comments (${activityFeed.commentsCnt}) </a>
			</span>
			<span><a href="#">likes (${activityFeed.likesCnt})</a>
			</span> 
			<span><a href="${activityFeed.link}" target="new">View</a></span>
			<%}%>
			
		</p>
	</div>
</div>
<div class="ActivityCommentMainP">

<comments:each bean="${activityFeed}">
<div class="ActivityCommentP">
	<img src="${resource(dir:'images',file:'user_32.png')}"/>
		<p>
			<a href="#"></a> ${comment.body}
		</p>
		<p>
			<span><prettytime:display date="${comment.dateCreated}" /></span><span><a href="#">Like</a>
			</span>
		</p>
	</div>
     
</comments:each>

	
</div>
 