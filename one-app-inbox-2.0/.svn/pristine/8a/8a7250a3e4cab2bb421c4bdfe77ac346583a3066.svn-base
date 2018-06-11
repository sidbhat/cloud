<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="main">
<link rel="stylesheet" media="screen"
	href="${resource(dir:'css',file:'dashboard.css')}" />
	<link rel="stylesheet" media="screen"
	href="${resource(dir:'css',file:'contact.css')}" />

</head>
<body>
    
  
    
        
        
        <section>
            <div class="container_8 clearfix">



                   <g:render template="/layouts/sidebar" />

                    

                <!-- Main Section -->

                <section class="main-section grid_7">

                    <div class="main-content grid_4 alpha">
                        <header>
                            <ul class="action-buttons clearfix fr">
                                <li><a href="#" class="button button-gray no-text current" style="height:25px;" title="View as a List" onClick="$(this).addClass('current').parent().siblings().find('a').removeClass('current');$('#contacts').removeClass('grid-view').addClass('list-view');return false;">List View<span class="view-list"></span></a></li>
                                <li><a href="#" class="button button-gray no-text" style="height:25px;" title="View as a Grid" onClick="$(this).addClass('current').parent().siblings().find('a').removeClass('current');$('#contacts').removeClass('list-view').addClass('grid-view');return false;">Grid View<span class="view-grid"></span></a></li>
                                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" style="height:25px;" class="button button-gray no-text help" rel="#overlay">Help<span class="help"></span></a></li>
                            </ul>
                      <div class="view-switcher">
                                <h2>All People &amp; Companies <!--<a href="#">&darr;</a>--></h2>
                                <!--<ul>
                                    <li><a href="#">All People</a></li>
                                    <li><a href="#">All Companies</a></li>
                                    <li><a href="#">All People &amp; Companies</a></li>
                                    <li class="separator"></li>
                                    <li>Recently viewed...</li>
                                    <li class="separator"></li>
                                    <li><a href="#">Contacts</a></li>
                                    <li><a href="#">Companies</a></li>
                                </ul>-->
                            </div>
                      </header>
                        <section>
               <ul id="contacts" class="listing list-view clearfix" style="padding:0px">
			    <g:if
				test="${contactList!=null && contactList.size()==0}">
			    <h2>No contact available</h2>
			    </g:if>
			    <g:else>
				    <g:each in="${contactList}" var="contact">
											
					<li class="contact clearfix" style="padding:5px 10px">
					<div class="avatar">
							<g:if test="${contact.source=='facebook'}">
								<img src="${resource(dir:'images',file:'facbook_16.png')}" alt="" class="SocialIcon" />
							</g:if>
							<g:if test="${contact.source=='linkedin'}">
								<img src="${resource(dir:'images',file:'Linkdin_16.png')}" alt="" class="SocialIcon" />
							</g:if>
							<g:if test="${contact.source=='twitter'}">
								<img src="${resource(dir:'images',file:'Twitter_16.png')}" alt="" class="SocialIcon" />
							</g:if>
					    </div>
					    <div class="avatar">
					    <% if (contact.picture == null || contact.picture.length() == 0 || contact.facebookPictureURL == null || contact.facebookPictureURL.length() == 0) { %>
					    	<img src="${resource(dir:'images',file:'user_32.png')}" />
					    <%}else{%>
							<img src="${contact.picture}" />
						<%}%>
					    </div>
					    <g:remoteLink controller="contacts"
														action="contactDetails" id="${contact.id}"
														update="sideexpansion" class="more">&raquo;</g:remoteLink>
					    <span class="timestamp">${contact.contactDate}</span>
					    <a href="#" class="name">${contact.contactName}</a>
					</li>
				  </g:each>
		 	    </g:else>
                            </ul>
                            <div class="paginateButtons">
				            <g:paginate total="${totalContacts}" controller="contacts"/>
			    </div>
                        </section>
                    </div>

                    <div class="preview-pane grid_3 omega">
                        <div class="content">
                            <h3>Preview Pane</h3>
                            <p>This is the preview pane. Click on the more button on an item to view more information.</p>
                            <div class="message info">
                                <h3>Helpful Tips</h3>
                                <img src="${resource(dir:'images',file:'lightbulb_32.png')}" class="fl" />
                                <p>Phasellus at sapien eget sapien mattis porttitor. Donec ultricies turpis pulvinar enim convallis egestas. Pellentesque egestas luctus mattis. Nulla eu risus massa, nec blandit lectus. Aliquam vel augue eget ante dapibus rhoncus ac quis risus.</p>
                            </div>
                        </div>
                        <div id="sideexpansion" class="preview">
                        </div>
                    </div>

                </section>

                <!-- Main Section End -->


            </div>
            <div id="push"></div>
        </section>
    
   
    
</body>
</html>