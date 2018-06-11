<h4><b>${groupdetail.groupName}</b></h4>
<hr />
<p>${groupdetail.groupDescription}</p>
<hr />
<h4>Contacts<span>(${groupdetail.contacts.size()})</span></h4>
<div class="GroupMemberList">
  <ul>
  <g:if test="${groupdetail.contacts!=null && groupdetail.contacts.size()==0}">
											<h2>No contacts in this group</h2>
							</g:if>
							<g:else>
							<g:each in="${groupdetail.contacts}" var="contactDetails">
							
      <li> <a href="#"> <img src="${resource(dir:'images',file:'UserImage.png')}" alt="" />
      <p><span>${contactDetails.contactName}</span></p>
      </a> </li> 
      </g:each>
      </g:else>
   
  </ul>
  
</div>
