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
    <title><g:message code="account.search" default="Account Search"/></title>
</head>

<body>
<!-- Main Section -->
<section class="main-section grid_7">

    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>

    <div class="main-content grid_4 alpha">
        <header><h2>Results &nbsp;(${searchInstanceTotal})<br/>
        <g:form name="search"
					  url="[ controller: 'linkedinConnect', action:'searchLinkedIn' ]">
				Keywords&nbsp;<input type="query" name="query" value="" /><br/>
				Company&nbsp;&nbsp;<input type="company" name="company" value="" />&nbsp;&nbsp;
				<g:submitButton action="searchLinkedIn" class="button1 small green" name="Search"/>
		</g:form>
		
         </h2>

        </header>
        <section>
        <% if (searchInstanceList!=null) { %>
            <ul id="contacts" class="listing list-view clearfix">
                <g:each in="${searchInstanceList}" status="i" var="searchInstance">
				    <% if (searchInstance instanceof Account) {%>
                    <li class="contact clearfix">
                        <div class="avatar">
                      	  <% avatar =  "<img src='${searchInstance.logoURL}'/>"%>
                        	${avatar}
                        </div>
                        <g:link class="more" action="show" id="${searchInstance.accountId}">&raquo;</g:link>
                        <span class="timestamp">${fieldValue(bean: searchInstance, field: "lastUpdated")} </span>
                        <a href=" ${searchInstance.externalURL}">Employees : ${fieldValue(bean: searchInstance, field: "totalEmployees")}</a>
                        <div class="entry-meta">
                            <b>Name: ${searchInstance.name}</b><br/>
                            <b>Industry : ${fieldValue(bean: searchInstance, field: "industry")}</b><br/>
                            ${searchInstance.addDescription}
                        </div>
                    </li>
                    <%}else{%>
                     <li class="contact clearfix">
                        <div class="avatar">
                      	  <% avatar =  "<img src=' ${searchInstance.linkedinPictureURL}'/>"%>
                        	${avatar}
                        </div>
                        <g:link class="more" action="show" id="${searchInstance.id}">&raquo;</g:link>
                        <span class="timestamp">${fieldValue(bean: searchInstance, field: "lastUpdated")} </span>
                        <a href="${searchInstance.profileURL}">Name : ${fieldValue(bean: searchInstance, field: "contactName")}</a>
                        <div class="entry-meta">
                            <b>Title: ${searchInstance.title}</b><br/>
                        </div>
                        <div class="entry-meta">
                            Headlines : ${fieldValue(bean: searchInstance, field: "headlines")}
                        </div>
                        
                    </li>

                    <%}%>
                </g:each>
            </ul>
            <ul class="pagination clearfix">

                <div class="paginateButtons">
                    <g:paginate total="${searchInstanceTotal}" params="['view':'list']"/>
                </div>
            </ul>
			 <div>
                <g:link action="create" class="button button-gray"><span
                        class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
            </div>
            <%}else{%>
            	<p> No records found </p>
            <%}%>
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

        <div class="preview">
        </div>
    </div>

</section>
<!-- Main Section End -->

</body>
</html>
