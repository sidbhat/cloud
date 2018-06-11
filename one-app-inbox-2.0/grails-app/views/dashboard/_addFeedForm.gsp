									<g:formRemote onSucess="alert('completed');" name="postForm"
										url="[ controller: 'companyActivity', action: 'ajaxcreate']"
										update="aresult" onSucess="alert('completed');">
										<div class="PostForm">
											<textarea id="postactivity" name="postactivity"
												style="height: 80px; width: 330px;"></textarea>
											<input type="hidden" name="source" value="">

											<button class="fr button button-gray">Attach</button>
										</div>
									</g:formRemote>

									<script type="text/javascript">
											function submitform1()
											{
												document.postForm.source.value = "company";
												document.postForm.submit();
											}
											function submitformfb()
											{
												document.postForm.source.value = "facebook";
												document.postForm.submit();

											}
											function submitformtw()
											{
												document.postForm.source.value = "twitter";
												document.postForm.submit();

											}
											function submitformli()
											{
												document.postForm.source.value = "linkedin";
												document.postForm.submit();

											}
									</script>

									<div class="PostShare">
										<ul>
											<li ><a href="#"
												class="ShareMainBtn">Share<span class="arrow-down"></span>
											</a>
												<ul >
												
													<li >Group ${groupList.size} <span></span>
													
														<ul>
														<g:each in="${groupList}" var="groupDetails">
															<li><a href="#">${groupDetails.groupName}</a></li>
														</g:each>
								
														</ul>
													</li>
													<li><a href="#">User</a></li>

													<li class="Nobdr"><g:remoteLink
															controller="companyActivity" action="ajaxcreate"
															update="aresult" onSucess="alert('completed');"
															id="company" onComplete="javascript:submitform1()">Company</g:remoteLink>
													</li>
												</ul>
											</li>
										</ul>
										 <div id="aresult"></div>
									</div>