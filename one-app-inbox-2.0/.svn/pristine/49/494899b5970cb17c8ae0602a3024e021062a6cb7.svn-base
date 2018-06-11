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
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="client.list" default="Client List"/></title>
</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2>
                <g:message code="client.list" default="Client List"/> &nbsp;(${clientInstanceTotal})</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                    <tr>
                        <g:sortableColumn property="name" title="Name" titleKey="client.name"/>
    					<g:sortableColumn property="description" title="Description" titleKey="client.description"/>
    					<g:sortableColumn property="maxUsers" title="Allowed Users" titleKey="client.maxUsers"/>
						<g:sortableColumn property="maxUsers" title="Active Users" titleKey="client.totalUsers"/>
						<g:sortableColumn property="validTo" title="Valid To" titleKey="client.validTo"/>
						<th>Plan</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${clientInstanceList}" status="i" var="clientInstance">
                    	<%	
                    		def activeUsers=0
							def inActiveUsers=0
                    		def activeUsersList = User.findAllByUserTenantIdAndEnabled(clientInstance.id,true)
							def inactiveUsersList = User.findAllByUserTenantIdAndEnabled(clientInstance.id,false)
                    		if ( activeUsersList )
                    			activeUsers = activeUsersList.size()
							if ( inactiveUsersList )
								inActiveUsers = inactiveUsersList.size()
                    	%>
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="edit"
                                        id="${clientInstance.id}">${fieldValue(bean: clientInstance, field: "name")}</g:link></td>
                            <td>${fieldValue(bean: clientInstance, field: "description")}</td>
							<td>${fieldValue(bean: clientInstance, field: "maxUsers")}</td>
							<td>${activeUsers}</td>
							<td><g:formatDate format="MM/dd/yyyy" date="${clientInstance?.validTo}"/></td>
							<td>${clientInstance?.plan?.description}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                    <g:paginate total="${clientInstanceTotal}"/>
                </div>

                <div class="grid_6">
                    <g:link action="create" class="button button-gray"><span
                            class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
                </div>

            </div>
        </section>
    </div>
</section>

</body>
</html>
