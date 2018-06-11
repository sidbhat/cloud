

<%@ page import="com.oneapp.cloud.time.TaskUpdate" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'taskUpdate.label', default: 'TaskUpdate')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${taskUpdateInstance}">
            <div class="errors">
                <g:renderErrors bean="${taskUpdateInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${taskUpdateInstance?.id}" />
                <g:hiddenField name="version" value="${taskUpdateInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="content"><g:message code="taskUpdate.content.label" default="Content" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskUpdateInstance, field: 'content', 'errors')}">
                                    <g:textField name="content" value="${taskUpdateInstance?.content}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="date"><g:message code="taskUpdate.date.label" default="Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskUpdateInstance, field: 'date', 'errors')}">
                                    <g:datePicker name="date" precision="day" value="${taskUpdateInstance?.date}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="from1"><g:message code="taskUpdate.from1.label" default="From1" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskUpdateInstance, field: 'from1', 'errors')}">
                                    <g:textField name="from1" value="${taskUpdateInstance?.from1}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="subject"><g:message code="taskUpdate.subject.label" default="Subject" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskUpdateInstance, field: 'subject', 'errors')}">
                                    <g:textField name="subject" value="${taskUpdateInstance?.subject}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="taskName"><g:message code="taskUpdate.taskName.label" default="Task Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskUpdateInstance, field: 'taskName', 'errors')}">
                                    <g:textField name="taskName" value="${taskUpdateInstance?.taskName}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
