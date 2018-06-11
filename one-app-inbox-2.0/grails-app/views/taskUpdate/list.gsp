
<%@ page import="com.oneapp.cloud.time.TaskUpdate" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'taskUpdate.label', default: 'TaskUpdate')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'taskUpdate.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="content" title="${message(code: 'taskUpdate.content.label', default: 'Content')}" />
                        
                            <g:sortableColumn property="date" title="${message(code: 'taskUpdate.date.label', default: 'Date')}" />
                        
                            <g:sortableColumn property="from1" title="${message(code: 'taskUpdate.from1.label', default: 'From1')}" />
                        
                            <g:sortableColumn property="subject" title="${message(code: 'taskUpdate.subject.label', default: 'Subject')}" />
                        
                            <g:sortableColumn property="taskName" title="${message(code: 'taskUpdate.taskName.label', default: 'Task Name')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${taskUpdateInstanceList}" status="i" var="taskUpdateInstance">

                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${taskUpdateInstance.id}">${fieldValue(bean: taskUpdateInstance, field: "id")}</g:link></td>
                        
                            <td>${taskUpdateInstance?.content?.encodeAsHTML()?.toString()}</td>
                        
                            <td><g:formatDate date="${taskUpdateInstance.date}" formatName="format.date"/></td>
                        
                            <td>${fieldValue(bean: taskUpdateInstance, field: "from1")}</td>
                        
                            <td>${fieldValue(bean: taskUpdateInstance, field: "subject")}</td>
                        
                            <td>${fieldValue(bean: taskUpdateInstance, field: "taskName")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${taskUpdateInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
