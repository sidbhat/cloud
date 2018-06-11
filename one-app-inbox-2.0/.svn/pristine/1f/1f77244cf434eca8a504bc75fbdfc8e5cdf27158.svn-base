
									<ul class="listing list-view clearfix">

										<g:if
											test="${activityFeedList!=null && activityFeedList.size()==0}">
											<br/>
											
											 <div class="message warning" style="margin-left: 10px; ">
                                    				<h3><g:message code="social.no.feeds" default="No social feeds available"/></h3>
                                    				<p>
                                			        <g:message code="social.help" default="Click on the social icons on the left hand menu."/>
                                  					 </p>
                                			</div>
                                			
											
										</g:if>
										<g:else>
											<g:each in="${activityFeedList}" var="activityFeed">
												<li class="clearfix" style="margin-left: 5px;"><g:remoteLink controller="dashboard"
														action="activityFeedDetails" id="${activityFeed?.id}"
														update="sideexpansion" class="more">&raquo;</g:remoteLink>
													<div class="UpdateFrom">
														<g:if test="${activityFeed?.source=='facebook'}">
															<img
																src="${resource(dir:'images',file:'facbookIcon.png')}"
																alt="facebook" />
														</g:if>
														<g:if test="${activityFeed?.source=='linkedin'}">
															<img
																src="${resource(dir:'images',file:'LinkdinIcon.png')}"
																alt="LinkedIn" />
														</g:if>
														<g:if test="${activityFeed?.source=='twitter'}">
															<img
																src="${resource(dir:'images',file:'TwitterIcon.png')}"
																alt="Twitter" />
														</g:if>
														<span> ${activityFeed?.source} </span>
													</div>
													<div class="ActivityCtnt" >
														<p>
															<b> ${activityFeed?.fromUser} :</b>
															  <!--feed:convertLinkToURL item="${activityFeed?.id}" type="social"/-->
															  ${activityFeed?.feedContent}
														</p>
														
														
														<% if (activityFeed?.url != null && activityFeed?.url.length() != 0  ) {%>
																<%if ( activityFeed?.feedType != "video") {%>
																	<p><img src="${activityFeed.url}"/></p>
																<%}else{%>
																	<p><iframe src="${activityFeed.url}" frameborder="0" ></iframe></p>
																<%}%>
														<%}%>
														<p>
															<span><prettytime:display
																	date="${activityFeed?.feedDate}" /> </span> 
															<% if ( activityFeed?.source == "facebook") {%>
															<span><a href="#"
																onClick="showlayer('C1${activityFeed.id}')">comments (${activityFeed?.commentsCnt})</a>
															</span> <span><a href="#">likes (${activityFeed?.likesCnt})</a> </span>
															<%}%>
															
															<span>
														</p>
													</div>

													<div class="ActivityCommentMain" id="C1${activityFeed?.id}">

														<div class="ActivityComment">
															<img src="${resource(dir:'images',file:'user_32.png')}"
																alt="" />

															<g:formRemote name="addcomment${activityFeed?.id}"
																url="[ controller: 'dashboard', action:'addcomment' ]"
																update="commentResult${activityFeed?.id}">

																<g:textArea id="commentText${activityFeed?.id}"
																	name="commentText${activityFeed?.id}" rows="5"
																	coloums="40"></g:textArea>
																<input type="hidden" name="activity_id"
																	value="${activityFeed?.id}" />
														</div>
														<p>
															<span><g:submitButton name="PostComment">
																</g:submitButton> </span>
															</g:formRemote>
														<div id="commentResult${activityFeed?.id}"></div>


													</div>
												</li>
											</g:each>
										</g:else>
									<div class="paginateButtons clearfix">
              							  <g:paginate total="${totalFeeds}" controller="dashboard" action="index"/>
            						</div>
									</ul>
								
								
