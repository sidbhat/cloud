
<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>
<%@ page import="com.oneapp.cloud.core.ActionTagLib" %>
<%@ page import="com.oneapp.cloud.core.User" %>
<%def ratingList =['*','**','***','****'] %>
	

									<ul class="listing list-view">
										<g:if test="${activityFeedList!=null && activityFeedList.size()==0}">
											<br/>
												
										 <div class="message warning" style="margin-left: 10px; ">
                                   				<h3><g:message code="company.no.feeds" default="No company feeds available"/></h3>
                                   				<p>
                               			        <g:message code="company.feeds.help" default="Enter text below to share a feed."/>
                                 					 </p>
                                			</div>                              			
										</g:if>
										<g:else>
											<g:each in="${activityFeedList}" var="activityFeed" status="i">
												<li class="clearfix ${(i % 2) == 0 ? 'odd' : 'even'}" style="margin-left: 5px;"><g:remoteLink controller="activityFeed"
														action="activityFeedDetails" id="${activityFeed.id}"
														update="sideexpansion" class="more">&raquo;</g:remoteLink>
													<input type="checkbox" style="float:left;margin-top:10px;width:20px;">
													<div class="UpdateFrom">
														
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
													</div>
													<div class="ActivityCtnt">
													<g:oneappDropDown id="${activityFeed.id}"></g:oneappDropDown>
														
														<p>
        													<b> ${activityFeed.createdBy.username} :</b>
															<% 
																def max = 512 
																if (activityFeed.activityContent.size() > 512)
																	max = 512
																else 
																	max = activityFeed.activityContent.size()
															%>
															<g:if test="${activityFeed?.config?.shareType == 'Approval' || activityFeed?.config?.shareType == 'Poll'}">
																Click to view ${activityFeed.config.shareType.toLowerCase()}<a href="#" onclick='openViewFrame(${activityFeed.shareId },${activityFeed.activityContent.decodeHTML()})'> Here</a><br/>										
															</g:if>
															<g:else>
																${activityFeed.activityContent.decodeHTML().substring(0,max)}<br/>
															</g:else>
														
														 <attachments:each bean="${activityFeedInstance}">
    <attachments:icon attachment="${attachment}"/>
    <attachments:deleteLink
                         attachment="${attachment}"
                         label="${'[X]'}"
                         returnPageURI="${createLink(action: 'list')}"/>
    <attachments:downloadLink
                         attachment="${attachment}"/>
    ${attachment.niceLength}
</attachments:each>
													<% 
														def starRatingVal = (int)activityFeed?.averageRating;
														%>
														
														</p>
													
														<p>
															<g:oneAppStar feedId="${activityFeed.id}" starAvgValue="${starRatingVal}"></g:oneAppStar>
														
															<span><prettytime:display
																	date="${activityFeed.dateCreated}" /> </span> 
																
																	<span><a href="#"
																onClick="showHidelayer('C2${activityFeed.id}')">comments (${activityFeed.comments.size()})</a> 
																</span>
																<span><a href="#"
																onClick="share('${activityFeed?.activityContent?.encodeAsHTML()}')">share</a> </span>
																<span>
														</p>

      <br/>
      <p>
      <g:each in="${activityFeed.tags.sort()}">
        <a href="${createLink(controller:'dashboard', action:'index', id:it)}"><input type="button" class="button1 small green" value="${it}"/></a>
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
															<!--span><a href="#">Like</a> </span-->
														</p>
													</div>
												</li>
											</g:each>
										</g:else>
										
									<div class="paginateButtons clearfix">
										<g:paginate total="${totalCompFeeds}" controller="dashboard" action="index"/>
									</div>	
									</ul>
									
							

									
