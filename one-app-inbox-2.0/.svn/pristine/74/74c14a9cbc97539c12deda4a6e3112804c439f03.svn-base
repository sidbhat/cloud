%{----------------------------------------------------------------------------
  - [ NIKKISHI CONFIDENTIAL ]                                                -
  -                                                                          -
  -    Copyright (c) 2011.  Nikkishi LLC                                     -
  -    All Rights Reserved.                                                  -
  -                                                                          -
  -   NOTICE:  All information contained herein is, and remains              -
  -   the property of Nikkishi LLC and its suppliers,                        -
  -   if any.  The intellectual and technical concepts contained             -
  -   herein are proprietary to Nikkishi LLC and its                         -
  -   suppliers and may be covered by U.S. and Foreign Patents,              -
  -   patents in process, and are protected by trade secret or copyright law.
  -   Dissemination of this information or reproduction of this material     -
  -   is strictly forbidden unless prior written permission is obtained      -
  -   from Nikkishi LLC.                                                     -
  ----------------------------------------------------------------------------}%

<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="com.oneapp.cloud.core.social.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="user.list" default="User List"/></title>
</head>

<body>
<!-- Main Section -->
<section class="main-section grid_7">

    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>

    <div class="main-content grid_4 alpha">
        <header><h2><g:message code="user.list" default="User List"/> &nbsp;(${userInstanceTotal})
            <ul class="action-buttons clearfix fr">
                <sec:ifAnyGranted roles="ROLE_HR_MANAGER">
                <li><g:link action="list" params="['view':'list_a']" class="button button-gray no-text current"
                            title="Users"><span class="view-list"></span></g:link></li>
                </sec:ifAnyGranted> 
            </ul>
        </h2>

        </header>
        <section>
            <ul id="contacts" class="listing list-view clearfix stopPropagation">
                <g:each in="${userInstanceList}" status="i" var="userInstance">

                    <li class="contact clearfix">
                        <div class="avatar">
                        <% 
						def sessionUserProfile = UserProfile.findByUser(userInstance)
						def pictureURL=""
						if(sessionUserProfile?.attachments){
						   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
						} else if(userInstance?.pictureURL != null && userInstance?.pictureURL.length() != 0){
						   pictureURL = userInstance.pictureURL
						}
                        		
                        		if ( pictureURL ) {%>
					           		<img src="${pictureURL}" width="32" height="40"/>
						        <%}else{%>
						         	    
						         <%}%>
                 
                        </div>
                        <g:if test="${!isRoleUser}">
                        	<g:link class="more" action="show" id="${userInstance.id}">&raquo;</g:link>
                        </g:if>
                        <g:link action="edit" class="name" id="${userInstance.id}">${fieldValue(bean: userInstance, field: "username")}</g:link>
                        <div class="entry-meta">
                            ${UserRole.findByUser(userInstance)}
                        </div>

                        <div class="entry-meta">
                            ${fieldValue(bean: userInstance, field: "firstName")} &nbsp;
                            ${fieldValue(bean: userInstance, field: "lastName")}

                        </div>
                        <div class="entry-meta" style="font-style:italic;">Last Log In <%if(userInstance?.lastLogIn != null){ %><prettytime:display date="${userInstance?.lastLogIn}" /><%}else{%>Not Available<%}%> </div>
                    </li>
                </g:each>
            </ul>
            <ul class="pagination clearfix">
                <div class="paginateButtons">
                    <g:paginate total="${userInstanceTotal}" params="['view':'list']"/>
                </div>
            </ul>
			<sec:ifAnyGranted roles="ROLE_ADMIN">
            <div>
                <g:link action="create" class="button button-gray"><span
                        class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
            </div>
            </sec:ifAnyGranted>
        </section>
    </div>

    <div class="preview-pane grid_3 omega">
        <div class="content">
            <h3><g:message code="help.contact.right1"/></h3>

            <p><g:message code="help.contact.right2"/></p>

            <div class="message info">
                <h3><g:message code="help.main.helpful.tips"/></h3>
                <img src="${resource(dir: 'images', file: 'lightbulb_32.png')}" class="fl"/>

                <p><g:message code="help.contact.right3"/></p>

            </div>
        </div>
		<g:if test="${!isRoleUser}">
	        <div class="preview">
	        </div>
        </g:if>
    </div>

</section>
<!-- Main Section End -->

</body>
</html>
