
<%@ page import="com.oneapp.cloud.core.CompanyActivity" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'companyActivity.label', default: 'CompanyActivity')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'companyActivity.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="activityContent" title="${message(code: 'companyActivity.activityContent.label', default: 'Activity Content')}" />
                        
                            <g:sortableColumn property="activityDate" title="${message(code: 'companyActivity.activityDate.label', default: 'Activity Date')}" />
                        
                            <g:sortableColumn property="username" title="${message(code: 'companyActivity.username.label', default: 'Username')}" />
                        
                            <g:sortableColumn property="visibility" title="${message(code: 'companyActivity.visibility.label', default: 'Visibility')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${companyActivityInstanceList}" status="i" var="companyActivityInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${companyActivityInstance.id}">${fieldValue(bean: companyActivityInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: companyActivityInstance, field: "activityContent")}</td>
                        
                            <td><g:formatDate date="${companyActivityInstance.activityDate}" formatName="format.date"/></td>
                        
                            <td>${fieldValue(bean: companyActivityInstance, field: "username")}</td>
                        
                            <td>${fieldValue(bean: companyActivityInstance, field: "visibility")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${companyActivityInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
