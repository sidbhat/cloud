

<%@ page import="com.oneapp.cloud.core.AsynchronousEmailStorage" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'asynchronousEmailStorage.label', default: 'AsynchronousEmailStorage')}" />
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
            <g:hasErrors bean="${asynchronousEmailStorageInstance}">
            <div class="errors">
                <g:renderErrors bean="${asynchronousEmailStorageInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${asynchronousEmailStorageInstance?.id}" />
                <g:hiddenField name="version" value="${asynchronousEmailStorageInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="emailSubject"><g:message code="asynchronousEmailStorage.emailSubject.label" default="Email Subject" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: asynchronousEmailStorageInstance, field: 'emailSubject', 'errors')}">
                                    <g:textField name="emailSubject" value="${asynchronousEmailStorageInstance?.emailSubject}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="emailData"><g:message code="asynchronousEmailStorage.emailData.label" default="Email Data" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: asynchronousEmailStorageInstance, field: 'emailData', 'errors')}">
                                    <g:textField name="emailData" value="${asynchronousEmailStorageInstance?.emailData}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="emailFrom"><g:message code="asynchronousEmailStorage.emailFrom.label" default="Email From" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: asynchronousEmailStorageInstance, field: 'emailFrom', 'errors')}">
                                    <g:textField name="emailFrom" value="${asynchronousEmailStorageInstance?.emailFrom}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="emailSentStatus"><g:message code="asynchronousEmailStorage.emailSentStatus.label" default="Email Sent Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: asynchronousEmailStorageInstance, field: 'emailSentStatus', 'errors')}">
                                    <g:textField name="emailSentStatus" value="${fieldValue(bean: asynchronousEmailStorageInstance, field: 'emailSentStatus')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="emailTO"><g:message code="asynchronousEmailStorage.emailTO.label" default="Email TO" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: asynchronousEmailStorageInstance, field: 'emailTo', 'errors')}">
                                    <g:textField name="emailTo" value="${asynchronousEmailStorageInstance?.emailTo}" />
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