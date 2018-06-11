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

<%@ page import="com.oneapp.cloud.time.Task" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="task.list" default="Task List"/></title>
</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>

            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <div class="view-switcher">
                <h2>${viewName} &nbsp;(${taskInstanceTotal}) <a href="#">&darr;</a></h2>
                <ul>
                    <li><g:link action="list" params="[view:'my_open']"><g:message
                            code="task.view.my_open"/></g:link></li>
                    <li><g:link action="list" params="[view:'my_all']"><g:message
                            code="task.view.my_all"/></g:link></li>
                    <sec:ifAnyGranted roles="ROLE_HR_MANAGER">
                        <li class="separator"></li>
                        <li><g:link action="list" params="[view:'all_open']"><g:message
                                code="task.view.all_open"/></g:link></li>
                        <li><g:link action="list" params="[view:'all']"><g:message code="task.view.all"/></g:link></li>
                    </sec:ifAnyGranted>
                </ul>
            </div>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                    <tr>

                        <g:sortableColumn property="name" title="Name" titleKey="task.name"/>

                        <th><g:message code="task.status" default="Status"/></th>
                        <g:sortableColumn property="taskType" title="Type" titleKey="task.task_type"/>

                       <g:sortableColumn property="assignedTo" title="Assigned To" titleKey="task.assignedTo"/>

                        <g:sortableColumn property="dueDate" title="Due" titleKey="task.dueDate"/>
                        <g:sortableColumn property="dueDate" title="Copy" titleKey="task.copy"/>
                        <g:sortableColumn property="dueDate" title="Share" titleKey="task.share"/>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${taskInstanceList}" status="i" var="taskInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="edit"
                                        id="${taskInstance.id}">${fieldValue(bean: taskInstance, field: "name")}</g:link></td>

                            <td><% if (taskInstance.status == "Close") { %>
                                <a href="#" class="button1 small red"><font
                                        color="white">${fieldValue(bean: taskInstance, field: "status")}</font></a>
                                <% } else { %>
                                <a href="#" class="button1 small green"><font
                                        color="white">${fieldValue(bean: taskInstance, field: "status")}</font></a>
                                <% } %>
                            </td>
                            <td>${fieldValue(bean: taskInstance, field: "taskType")}</td>

                            <td>${fieldValue(bean: taskInstance, field: "assignedTo")}</td>

                            <td>${fieldValue(bean: taskInstance, field: "dueDate")}</td>
                            <td>
                                <ul class="action-buttons">
                                    <li><g:link action="copy" id="${taskInstance.id}"
                                                class="button button-gray no-text"><span
                                                class="copy"></span>Copy</g:link></li>
                                </ul>
                            </td>
                            <td>
                               <g:render template="/dashboard/sharepopup" model="['id':taskInstance.id,'shareType':'TASK','className':taskInstance.class.name]"/>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                 <g:paginate total="${taskInstanceTotal}"/>
             </div>

             <div class="grid_6">
                 <g:link action="create" class="button button-gray"><span
                         class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
             </div>
        </section>
    </div>
</section>

</body>
</html>
