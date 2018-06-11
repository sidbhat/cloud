

<%@ page import="com.oneapp.cloud.core.CompanyActivity" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'companyActivity.label', default: 'CompanyActivity')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${companyActivityInstance}">
            <div class="errors">
                <g:renderErrors bean="${companyActivityInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="activityContent"><g:message code="companyActivity.activityContent.label" default="Activity Content" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: companyActivityInstance, field: 'activityContent', 'errors')}">
                                    <g:textField name="activityContent" value="${companyActivityInstance?.activityContent}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="activityDate"><g:message code="companyActivity.activityDate.label" default="Activity Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: companyActivityInstance, field: 'activityDate', 'errors')}">
                                    <g:datePicker name="activityDate" precision="day" value="${companyActivityInstance?.activityDate}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="username"><g:message code="companyActivity.username.label" default="Username" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: companyActivityInstance, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${companyActivityInstance?.username}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="visibility"><g:message code="companyActivity.visibility.label" default="Visibility" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: companyActivityInstance, field: 'visibility', 'errors')}">
                                    <g:textField name="visibility" value="${companyActivityInstance?.visibility}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
