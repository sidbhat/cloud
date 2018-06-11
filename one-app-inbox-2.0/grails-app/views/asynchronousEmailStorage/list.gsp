
<%@ page import="com.oneapp.cloud.core.AsynchronousEmailStorage" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'asynchronousEmailStorage.label', default: 'AsynchronousEmailStorage')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'asynchronousEmailStorage.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="emailSubject" title="${message(code: 'asynchronousEmailStorage.emailSubject.label', default: 'Email Subject')}" />
                        
                            <g:sortableColumn property="emailData" title="${message(code: 'asynchronousEmailStorage.emailData.label', default: 'Email Data')}" />
                        
                            <g:sortableColumn property="emailFrom" title="${message(code: 'asynchronousEmailStorage.emailFrom.label', default: 'Email From')}" />
                        
                            <g:sortableColumn property="emailSentStatus" title="${message(code: 'asynchronousEmailStorage.emailSentStatus.label', default: 'Email Sent Status')}" />
                        
                            <g:sortableColumn property="emailTO" title="${message(code: 'asynchronousEmailStorage.emailTO.label', default: 'Email TO')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${asynchronousEmailStorageInstanceList}" status="i" var="asynchronousEmailStorageInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${asynchronousEmailStorageInstance.id}">${fieldValue(bean: asynchronousEmailStorageInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: asynchronousEmailStorageInstance, field: "emailSubject")}</td>
                        
                            <td>${fieldValue(bean: asynchronousEmailStorageInstance, field: "emailData")}</td>
                        
                            <td>${fieldValue(bean: asynchronousEmailStorageInstance, field: "emailFrom")}</td>
                        
                            <td>${fieldValue(bean: asynchronousEmailStorageInstance, field: "emailSentStatus")}</td>
                        
                            <td>${fieldValue(bean: asynchronousEmailStorageInstance, field: "emailTo")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${asynchronousEmailStorageInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
