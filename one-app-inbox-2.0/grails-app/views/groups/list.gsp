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

<%@ page import="com.oneapp.cloud.core.GroupDetails" %>
<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="groups.list" default="Groups List"/></title>
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
                <g:message code="groups.list" default="Groups List"/>  &nbsp;(${groupsInstanceTotal})</h2>
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

                        <g:sortableColumn property="groupName" title="Name" titleKey="groups.name"/>

                        <g:sortableColumn property="groupName" title="Type" titleKey="groups.type"/>

                        <g:sortableColumn property="groupDescription" title="Description" titleKey="groups.description"/>
					    <g:sortableColumn property="groupName" title="# People" titleKey="groups.people"/>

                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${groupsInstanceList}" status="i" var="groupsInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td>
                            	<g:if test="${SpringSecurityUtils.ifAnyGranted(com.oneapp.cloud.core.Role.ROLE_HR_MANAGER) }">
                            		<g:link action="edit" id="${groupsInstance.id}">${fieldValue(bean: groupsInstance, field: "groupName")}</g:link>
								</g:if>
								<g:else>
									${fieldValue(bean: groupsInstance, field: "groupName")}
								</g:else>
							</td>
						    <td>${fieldValue(bean: groupsInstance, field: "groupType")}</td>

                            <td>${fieldValue(bean: groupsInstance, field: "groupDescription")}</td>
                             <td>${groupsInstance.user?.size()+groupsInstance.contacts?.size()}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                    <g:paginate total="${groupsInstanceTotal}"/>
                </div>

                <div class="grid_6">
                	<g:if test="${SpringSecurityUtils.ifAnyGranted(com.oneapp.cloud.core.Role.ROLE_HR_MANAGER) }">
						<g:link action="create" class="button button-gray"><span
                            class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
					</g:if>
                </div>

            </div>
        </section>
    </div>
</section>

</body>
</html>
