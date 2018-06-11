  <%@ page import="com.oneapp.cloud.core.social.*" %>
 <%@ page import="com.oneapp.cloud.core.*" %>
 <%
 
 		def groupList = GroupDetails.list()
		def user1 = session["user"]
		def currentUserRoles = user1?.authorities
		def userList
		if(currentUserRoles*.authority.contains(Role.ROLE_TRIAL_USER)){
			userList = [user1]
		}else{
			userList = User.findAllByUserTenantIdAndEnabled(user1?.userTenantId,true)
		}
		def roleList = Role.list()
		def deptList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
 
 %>
 
 <link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'DasMain.css')}" />
					 <a href="#" class="has-popupballoon button1 small blue"><font color="white">Share</font></a>
                             <div class="popupballoon top">
               					<g:formRemote name="postForm${id}"
										url="[ controller: 'activityFeed', action: 'ajaxcreate']"
										 >
									<div class="results${id}"></div>
									<div class="PostForm">
											&nbsp;<input type="text" id="postactivity" name="postactivity"/>
											<input type="hidden" name="source" value="">
											<input type="hidden" name="visibility" value="">
											<input type="hidden" name="share" value="">
											<input type="hidden" name="className" value="${className}">
											<input type="hidden" name="shareType" value="${shareType}">
											<input type="hidden" name="ajax" value="true">
											<input type="hidden" name="sharedWith" value="">
											<input type="hidden" name="formId" value="${formId}">
									</div>
									
								

									<div class="PostShare">
										<ul>
											<li ><a href="#" class="ShareMainBtn"><g:message code="share.label" default="Share"/><span class="arrow-down"></span></a>
												<ul>
													<li ><a href="#" ><g:message code="share.user" default="User"/></a><span></span>
														<ul>
															<g:each in="${userList}" var="user">
																<li>
																<a href="#"
																onclick="document.postForm${id}.source.value='company';document.postForm${id}.visibility.value='USER';document.postForm${id}.share.value='${id}';document.postForm${id}.sharedWith.value='${user.id}';document.postForm${id}.submit();return false;">
																
																<!--g:remoteLink
																controller="activityFeed" action="ajaxcreate"
																update="aresult"
																before="javascript:submitform3(postForm${id},${user.id})"-->${user.username.substring(0,user.username.indexOf("@"))}</a></li>
															</g:each>
														</ul>
													</li>
													
													<li ><a href="#" ><g:message code="share.group" default="Group"/></a><span></span>
														<ul>
														<g:each in="${groupList}" var="groupDetails">
															<li class="Nobdr">
																<a href="#"
																onclick="document.postForm${id}.source.value='company';document.postForm${id}.visibility.value='GROUP';document.postForm${id}.share.value='${id}';document.postForm${id}.sharedWith.value='${groupDetails?.id}';document.postForm${id}.submit(); return false;">
														
																${groupDetails.groupName}</a></li>
														</g:each>
														</ul>
													</li>
													<li ><a href="#" ><g:message code="share.role" default="Role"/></a><span></span>
														<ul>
														<g:each in="${roleList}" var="roleDetails">
															<g:if test="${roleDetails.authority != 'ROLE_SUPER_ADMIN'}">
																<li class="Nobdr">
																	<a href="#"
																	onclick="document.postForm${id}.source.value='company';document.postForm${id}.visibility.value='ROLE';document.postForm${id}.share.value='${id}';document.postForm${id}.sharedWith.value='${roleDetails?.id}';document.postForm${id}.submit(); return false;">
															
																	${roleDetails.description}</a>
																</li>
															</g:if>
														</g:each>
														</ul>
													</li>
													<li ><a href="#" ><g:message code="share.department" default="Department"/></a><span></span>
														<ul>
														<g:each in="${deptList}" var="deptDetails">
															<li class="Nobdr">
																<a href="#"
																onclick="document.postForm${id}.source.value='company';document.postForm${id}.visibility.value='DEPARTMENT';document.postForm${id}.share.value='${id}';document.postForm${id}.sharedWith.value='${deptDetails.id}';document.postForm${id}.submit(); return false;">
														
																${deptDetails.description}</a></li>
														</g:each>
														</ul>
													</li>
												</ul>
											</li>
										</ul>
									</div>
									
                             	<hr />
                                <a href="javascript:;" class="button button-gray close" style="float:left;font-weight:normal;" >Cancel</a>
                                
                                </g:formRemote>
                            </div>
    