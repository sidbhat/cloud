
<%@ page import="com.oneapp.cloud.core.BillingHistory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="billingHistory.list" default="BillingHistory List" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="billingHistory.new" default="New BillingHistory" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="billingHistory.list" default="BillingHistory List" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	    <g:sortableColumn property="id" title="Id" titleKey="billingHistory.id" />
                        
                   	    <th><g:message code="billingHistory.client" default="Client" /></th>
                   	    
                   	    <g:sortableColumn property="description" title="Description" titleKey="billingHistory.description" />
                        
                   	    <g:sortableColumn property="amount" title="Amount" titleKey="billingHistory.amount" />
                        
                   	    <g:sortableColumn property="currency" title="Currency" titleKey="billingHistory.currency" />
                        
                   	    <g:sortableColumn property="billDate" title="Bill Date" titleKey="billingHistory.billDate" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${billingHistoryInstanceList}" status="i" var="billingHistoryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${billingHistoryInstance.id}">${fieldValue(bean: billingHistoryInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: billingHistoryInstance, field: "client")}</td>
                        
                            <td>${fieldValue(bean: billingHistoryInstance, field: "description")}</td>
                        
                            <td><g:formatNumber number="${billingHistoryInstance.amount}" /></td>
                        
                            <td>${fieldValue(bean: billingHistoryInstance, field: "currency")}</td>
                        
                            <td><g:formatDate date="${billingHistoryInstance.billDate}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${billingHistoryInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
