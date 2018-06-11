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
<%@ page import="com.oneapp.cloud.core.User" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>

    <script type="text/javascript">
        function showHide() {
//alert(document.getElementById("open").checked);
            if (document.getElementById("open").checked == true || document.getElementById("personal").checked == true) {
                document.getElementById("projectDiv1").style.visibility = 'hidden';
                document.getElementById("projectDiv2").style.visibility = 'hidden';
                document.getElementById("projectDiv3").style.visibility = 'hidden';
                document.getElementById("projectDiv7").style.visibility = 'hidden';
                if (document.getElementById("open").checked == true) {
                    document.getElementById("projectDiv4").style.visibility = 'hidden';
                    document.getElementById("projectDiv5").style.visibility = 'hidden';
                    document.getElementById("projectDiv6").style.visibility = 'hidden';
                    document.getElementById("personal").checked = false

                }
            }
            else {
                document.getElementById("projectDiv1").style.visibility = 'visible';
                document.getElementById("projectDiv2").style.visibility = 'visible';
                document.getElementById("projectDiv3").style.visibility = 'visible';
                document.getElementById("projectDiv7").style.visibility = 'visible';
                if (document.getElementById("open").checked == false) {
                    document.getElementById("projectDiv4").style.visibility = 'visible';
                    document.getElementById("projectDiv5").style.visibility = 'visible';
                    document.getElementById("projectDiv6").style.visibility = 'visible';

                }
            }

        }
    </script>
</head>

<body onload="showHide()">
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2><g:message code="default.create.label" args="[entityName]"/></h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            <g:hasErrors bean="${taskInstance}">
                <div class="errors">
                    <g:renderErrors bean="${taskInstance}" as="list"/>
                </div>
            </g:hasErrors>
        </header>
        <section class="container_6 clearfix">
            <div class="form grid_6">
                <g:form action="save" method="post">
                    <ul class="tabs">
                        <li><a href="#">Basic</a></li>
                    </ul>

                    <!-- tab "panes" -->
                    <div class="panes clearfix">
                        <section>
                            <table>
                                <tbody>

                                <tr class="prop">
                                    <td valign="top" class="name">
                                        <label for="name"><g:message code="task.name" default="Name"/>: <em>*</em>
                                        </label>
                                    </td>
                                    <td valign="top"
                                        class="value ${hasErrors(bean: taskInstance, field: 'name', 'errors')}">
                                        <g:textField name="name"
                                                     value="${fieldValue(bean: taskInstance, field: 'name')}"/>

                                    </td>
                                </tr>


                                <tr class="prop">
                                    <td valign="top" class="name">
                                        <label for="taskType"><g:message code="task.taskType"
                                                                         default="Task Type"/>:<em>*</em></label>
                                    </td>
                                    <td valign="top"
                                        class="value ${hasErrors(bean: taskInstance, field: 'taskType', 'errors')}">
                                        <g:select name="taskType.id"
                                                  from="${com.oneapp.cloud.core.DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.TASK_TYPE)}"
                                                  optionKey="id" value="${taskInstance?.taskType?.id}"/>

                                    </td>
                                </tr>

                                <tr class="prop">
                                    <td valign="top" class="description">
                                        <label for="description"><g:message code="task.description"
                                                                            default="Description"/>:</label>
                                    </td>
                                    <td valign="top"
                                        class="value ${hasErrors(bean: taskInstance, field: 'description', 'errors')}">
                                        <g:textArea name="description"
                                                     value="${fieldValue(bean: taskInstance, field: 'description')}"/>

                                    </td>
                                </tr>

                                <tr class="prop" id="projectDiv4">
                                    <td valign="top" class="name">
                                        <label for="status"><g:message code="task.status" default="Status"/>:</label>
                                    </td>
                                    <td valign="top"
                                        class="value ${hasErrors(bean: taskInstance, field: 'status', 'errors')}">
                                        <g:select name="status.id"
                                                  from="${com.oneapp.cloud.core.DropDown.findAllByTypeAndNameInList(com.oneapp.cloud.core.DropDownTypes.STATUS,['OPEN','CLOSE','IN_PROCESS'])}"
                                                  optionKey="id" value="${taskInstance?.status?.id}"
                                                  noSelection="['null': '']"/>

                                    </td>
                                </tr>

                                <tr class="prop" id="projectDiv6">
                                    <td valign="top" class="name">
                                        <label for="dueDate"><g:message code="task.dueDate"
                                                                        default="Due Date"/>:</label>
                                    </td>
                                    <td valign="top"
                                        class="value ${hasErrors(bean: taskInstance, field: 'dueDate', 'errors')}">
                                        <input type="date" name="dueDate" value="${taskInstance?.dueDate}"
                                               noSelection="['': '']"/>

                                    </td>
                                </tr>
                                     <tr class="prop" id="projectDiv3">
                                    <td valign="top" class="name">
                                        <label for="assignedTo"><g:message code="task.assignedTo"
                                                                           default="Assigned To"/>:</label>
                                    </td>
                                    <td valign="top"
                                        class="value ${hasErrors(bean: taskInstance, field: 'assignedTo', 'errors')}">
                                        <g:select name="assignedTo.id"
                                                  from="${User.findAllByUserTenantId(User.findByUsername(session.SPRING_SECURITY_CONTEXT.authentication?.principal?.username).userTenantId)}"
                                                  optionKey="id" value="${taskInstance?.assignedTo?.id}"
                                                   noSelection="['': '']"/>
                                    </td>
                                </tr>
                                
                                </tbody>
                            </table>
                        </section>

                
                    </div>

                    <div class="action">
                        <span class="button"><g:submitButton name="create" class="button button-green"
                                                             value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
                    </div>
                </g:form>
            </div>
        </section>
    </div>
</section>
</body>
</html>
