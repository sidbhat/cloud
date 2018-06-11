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
    <div class="main-content">
        <header>

            <h2>
                <g:message code="user.list" default="User List"/> &nbsp;(${userInstanceTotal})</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>

        <section class="container_6 clearfix">
            <div class="grid_6">
                <form method="post">
                    <table class="datatable tablesort selectable full">
                        <thead>
                        <tr>
                           
                            <th>Select</th>
                            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
                        	<th>Client</th>
                            </sec:ifAnyGranted>
                            <g:sortableColumn property="username" title="Username" titleKey="user.username"/>
                            <g:sortableColumn property="firstName" title="First Name" titleKey="user.firstName"/>
                            <th>Phone</th>
                            <g:sortableColumn property="reportsTo" title="Manager" titleKey="user.reportsTo"/>
                            <g:sortableColumn property="lastLogIn" title="Last Login" titleKey="user.lastLogIn"/>
                         <sec:ifAnyGranted roles="ROLE_HR_MANAGER">
                            <th>Copy</th>
                            <g:if test="${grails.util.Environment.current != grails.util.Environment.PRODUCTION}">
							    <th>Login As</th>
							</g:if>


                            </sec:ifAnyGranted>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${userInstanceList}" status="i" var="userInstance">
                        
                        <%
							com.oneapp.cloud.core.Client client = com.oneapp.cloud.core.Client.get(session?.user?.userTenantId)
						%>
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                               
                                <td><g:checkBox name="r_${userInstance.id}"/></td>
                                 <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
                        	 		<td>${Client.get(userInstance?.userTenantId)}</td>
                                </sec:ifAnyGranted>
                                <td><g:link action="edit"
                                            id="${userInstance.id}"><b>${fieldValue(bean: userInstance, field: "username")}</g:link></b>
                                </td>
                                <td>${fieldValue(bean: userInstance, field: "firstName")}</td>
                                <td>${fieldValue(bean: userInstance, field: "mobilePhone")}</td>
                                <td>${fieldValue(bean: userInstance, field: "reportsTo")}</td>
                                <td> <div class="entry-meta" style="font-style:italic;"><%if(userInstance?.lastLogIn != null){ %><prettytime:display date="${userInstance?.lastLogIn}" /><%}else{%>Not Available<%}%> </div></td>
                                <sec:ifAnyGranted roles="ROLE_HR_MANAGER">
                                <td>
                                    <ul class="action-buttons">
                                        <li><g:link action="copy" id="${userInstance.id}"
                                                    class="button button-gray no-text"><span
                                                    class="copy"></span>Copy</g:link></li>
                                    </ul>
                                </td>
                                 <g:if test="${grails.util.Environment.current != grails.util.Environment.PRODUCTION}">
								    <td><g:link action="loginWith" params="[j_username:userInstance?.username]"
                                            >Login</g:link></td>
								</g:if>
                                </sec:ifAnyGranted>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>

                    <div class="paginateButtons">
                        <g:paginate total="${userInstanceTotal}" params="['view':'list_a']"/>
                    </div>
					<sec:ifAnyGranted roles="ROLE_ADMIN">
                    <div class="grid_6">
                        <g:actionSubmit action="create" class="button button-green" value="${message(code: 'create', 'default': 'Create')}"/>
                        <g:actionSubmit action="m_email" class="button button-blue" value="${message(code: 'email', 'default': 'Reset Password')}"/>
                    </div>
					</sec:ifAnyGranted>
                </form>
            </div>
        </section>
    </div>
</section>


<!-- Main Section End -->

</body>
</html>
