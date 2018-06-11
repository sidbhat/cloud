<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<g:javascript library="prototype" />
<title>DashBoard</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="main">
<link rel="stylesheet" media="screen"
	href="${resource(dir:'css',file:'dashboard.css')}" />
<script type="text/javascript">
		function showlayer(san){document.getElementById(san).style.display="block";}
		function hidelayer(san){document.getElementById(san).style.display="none";}
		
	</script>
<g:javascript library="jquery" />

</head>
<body>
	<section>
		<div class="container_8 clearfix">

			<g:render template="/layouts/sidebar" />


			<!-- Main Section -->

			<section class="main-section grid_7">

				<div class="main-content grid_4 alpha">
					<header class="clearfix">
						<!--<span class="avatar"></span>-->
						<hgroup>
							<h2>
								<ul class="action-buttons clearfix fr">
									<li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp"
										class="button button-gray no-text help" rel="#overlay"
										style="height: 26px;">Help<span class="help"></span> </a></li>
								</ul>
								Welcome to streamlined
							</h2>
						</hgroup>
					</header>
								<section>
								<g:formRemote onSucess="alert('completed');" name="mainForm"
										url="[ controller: 'contacts', action: 'createGroup']"
										update="aresult" onSucess="alert('completed');">
									
									<ul class="listing list-view">
									<li>
									Please enter Group name: <input type="text" name ="groupname"><br/>
									</li>
										<g:if
											test="${contactList!=null && contactList.size()==0}">
											<h2>No contact available</h2>
										</g:if>
										<g:else>
											<g:each in="${contactList}" var="contact">
												<g:checkBox name="groupid" value="${contact.contactId}" checked='false'/><br/>
												<li>
												<div class="UpdateFrom">
														<g:if test="${contact.source=='facebook'}">
															<img
																src="${resource(dir:'images',file:'facbookIcon.png')}"
																alt="facebook" />
														</g:if>
														<g:if test="${contact.source=='linkedin'}">
															<img
																src="${resource(dir:'images',file:'LinkdinIcon.png')}"
																alt="LinkedIn" />
														</g:if>
														<g:if test="${contact.source=='twitter'}">
															<img
																src="${resource(dir:'images',file:'TwitterIcon.png')}"
																alt="Twitter" />
														</g:if>
														<span> ${contact.source} </span>
													</div>
													
													<div class="ActivityCtnt">
														<p>
															<b> ${contact.contactName}</b>
															<!--${contact.contactId}-->
														</p>
														
													</div>

													<div class="ActivityCommentMain" id="C1${contact.contactId}">

														<div class="ActivityComment">
															<img src="${resource(dir:'images',file:'UserImage.png')}"
																alt="" />

													</div>
												</li>
											</g:each>
										</g:else>

									</ul>

									<ul class="pagination clearfix">
										<li class="first"><a class="button-gray" href="#">First</a>
										</li>
										<li class="prev"><a class="button-gray" href="#">&laquo;</a>
										</li>
										<li class="page"><a class="button-gray" href="#">1</a></li>
										<li class="page"><a class="button-gray" href="#">2</a></li>
										<li class="page"><a class="button-gray" href="#">3</a></li>
										<li class="page"><a class="button-gray" href="#">4</a></li>
										<li class="page"><a class="button-gray" href="#">5</a></li>
										<li class="next"><a class="button-gray" href="#">&raquo;</a>
										</li>
										<li class="last"><a class="button-gray" href="#">Last</a>
										</li>
									</ul>
									<span>
										<g:submitButton name="Create Group"></g:submitButton>
									</span>
						</g:formRemote>
						<script type="text/javascript">
							function creategroup()
							{
								alert("entering creategroup");
								alert("document.mainForm.groupname.value" + document.mainForm.groupname.value);
								//alert("document.mainForm.groupid.value" + document.mainForm.groupid.value);
								//document.mainForm.groupname.value = "company";
								document.mainForm.submit();
							}
						</script>
					</section>
				</div>

				<div class="preview-pane grid_3 omega">
					<div class="content">
						<h3>
							<sec:loggedInUserInfo field="username" />
							's contact information
						</h3>
						<ul class="profile-info">
							<li class="email"><sec:loggedInUserInfo field="username" />@somecompany.com<span>email</span>
							</li>
							<li class="phone">(555) 555-HOME<span>home</span></li>
							<li class="mobile">(555) 555-MOBI<span>mobile</span></li>
							<li class="phone">(555) 555-WORK<span>work</span></li>
						</ul>
						<h3>
							Tasks About
							<sec:loggedInUserInfo field="username" />
						</h3>
						None so far. <a href="#">Add a task now</a>
						<h3>Additional info</h3>
						<ul class="profile-info">
							<li class="calendar-day">January 1, 1991<span>birthday</span>
							</li>
							<li class="calendar-day">December 21, 2010<span>hire
									date</span></li>
							<li class="house">123 Some Street, LA<span>home
									address</span></li>
							<li class="building">456 Some Street, LA<span>office
									address</span></li>
						</ul>
					</div>
					
			
					<div class="preview" id="sideexpansion"></div>
				</div>
			
			</section>

			<!-- Main Section End -->


		</div>
		<div id="push"></div>
	</section>
	</div>
</body>
</html>
