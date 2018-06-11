<h4><b>${contactdetail.contactName}</b></h4>
<hr />
<p>Contact imported from ${contactdetail.source}</p>
<hr />
<div class="GroupMemberList">
  <ul>
  
    
        				<div class="avatar">
					    <% if (contactdetail.picture == null || contactdetail.picture.length() == 0 || contactdetail.facebookPictureURL == null || contactdetail.facebookPictureURL.length() == 0) { %>
					    	<img src="${resource(dir:'images',file:'user_32.png')}" />
					    <%}else{%>
							<img src="${contactdetail.picture}" />
						<%}%>
					    </div>
    	<p><span>${contactdetail.summary}</span></p>
   		<p><span>${contactdetail.headlines}</span></p>
   		<p><span>${contactdetail.currentStatus}</span></p>
   
  </ul>

</div>

<g:form action="addContactToGroup">
<g:if test="${groupList!=null && groupList.size()==0}">
							</g:if>
							<g:else>
							<input type="hidden" name="contactid" value="${contactdetail.id}"/>
							Add Contact to the Group :
							<g:select id="groups" name="groupid"
                 			 from="${groupList}"
                 			 value="groupName"
                  			 optionValue="groupName"
                  			 optionKey="id"
         					 />
         					 
							<input type="submit" value="Add to group">
							</g:else>
							
							

</g:form>
